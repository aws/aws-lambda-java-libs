cmake_minimum_required(VERSION 3.9)
set(CMAKE_CXX_STANDARD 11)
project(aws-lambda-runtime
    VERSION 0.2.7
    LANGUAGES CXX)

option(ENABLE_LTO "Enables link-time optimization, requires compiler support." ON)
option(ENABLE_TESTS "Enables building the test project, requires AWS C++ SDK." OFF)

add_library(${PROJECT_NAME}
    "src/logging.cpp"
    "src/runtime.cpp"
    "src/backward.cpp"
    "${CMAKE_CURRENT_BINARY_DIR}/version.cpp"
    )

set_target_properties(${PROJECT_NAME} PROPERTIES
    SOVERSION 0
    VERSION ${PROJECT_VERSION})

target_include_directories(${PROJECT_NAME} PUBLIC
    $<BUILD_INTERFACE:${CMAKE_CURRENT_SOURCE_DIR}/include>
    $<INSTALL_INTERFACE:include>)

if (ENABLE_LTO)
    include(CheckIPOSupported)
    check_ipo_supported(RESULT has_lto OUTPUT lto_check_output)
    if(has_lto)
        set_property(TARGET ${PROJECT_NAME} PROPERTY INTERPROCEDURAL_OPTIMIZATION TRUE)
    else()
        message(WARNING "Link-time optimization (LTO) is not supported: ${lto_check_output}")
    endif()
endif()

find_package(CURL REQUIRED)
if (CMAKE_VERSION VERSION_LESS 3.12)
    target_link_libraries(${PROJECT_NAME} PRIVATE ${CURL_LIBRARIES})
else()
    target_link_libraries(${PROJECT_NAME} PRIVATE CURL::libcurl)
endif()

target_include_directories(${PROJECT_NAME} PRIVATE ${CURL_INCLUDE_DIRS})

target_compile_options(${PROJECT_NAME} PRIVATE
    "-fno-exceptions"
    "-fno-rtti"
    "-fvisibility=hidden"
    "-fvisibility-inlines-hidden"
    "-Wall"
    "-Wextra"
    "-Werror"
    "-Wconversion"
    "-Wno-sign-conversion")

find_library(DW_LIB NAMES dw)
if (NOT DW_LIB STREQUAL DW_LIB-NOTFOUND)
    message("-- Enhanced stack-traces are enabled via libdw: ${DW_LIB}")
    target_compile_definitions(${PROJECT_NAME} PRIVATE "BACKWARD_HAS_DW=1")
    target_link_libraries(${PROJECT_NAME} PUBLIC "${DW_LIB}")
else()
    find_library(BFD_LIB NAMES bfd)
    if (NOT BFD_LIB STREQUAL BFD_LIB-NOTFOUND)
        message("-- Enhanced stack-traces are enabled via libbfd: ${BFD_LIB}")
        target_compile_definitions(${PROJECT_NAME} PRIVATE "BACKWARD_HAS_BFD=1")
        target_link_libraries(${PROJECT_NAME} PRIVATE "${BFD_LIB}")
    endif()
endif()

if (LOG_VERBOSITY)
    target_compile_definitions(${PROJECT_NAME} PRIVATE "AWS_LAMBDA_LOG=${LOG_VERBOSITY}")
elseif(CMAKE_BUILD_TYPE STREQUAL Debug)
    target_compile_definitions(${PROJECT_NAME} PRIVATE "AWS_LAMBDA_LOG=3")
else ()
    target_compile_definitions(${PROJECT_NAME} PRIVATE "AWS_LAMBDA_LOG=0")
endif()

#tests
if (ENABLE_TESTS)
    enable_testing()
    add_subdirectory(tests)
endif()

#versioning
configure_file(
    "${CMAKE_CURRENT_SOURCE_DIR}/src/version.cpp.in"
    "${CMAKE_CURRENT_BINARY_DIR}/version.cpp"
    NEWLINE_STYLE LF)

include (CMakePackageConfigHelpers)

write_basic_package_version_file("${PROJECT_NAME}-config-version.cmake"
    VERSION ${PROJECT_VERSION}
    COMPATIBILITY SameMajorVersion)

# installation
install(FILES "include/aws/http/response.h"
    DESTINATION "include/aws/http")

install(FILES 
    "include/aws/lambda-runtime/runtime.h"
    "include/aws/lambda-runtime/version.h"
    "include/aws/lambda-runtime/outcome.h"
    DESTINATION "include/aws/lambda-runtime")

install(FILES "include/aws/logging/logging.h"
    DESTINATION "include/aws/logging")

include(GNUInstallDirs)
install(TARGETS ${PROJECT_NAME}
    EXPORT ${PROJECT_NAME}-targets
    ARCHIVE DESTINATION ${CMAKE_INSTALL_LIBDIR}
    LIBRARY DESTINATION ${CMAKE_INSTALL_LIBDIR}
    RUNTIME DESTINATION ${CMAKE_INSTALL_BINDIR}
    )

configure_file("${CMAKE_CURRENT_SOURCE_DIR}/cmake/${PROJECT_NAME}-config.cmake"
    "${CMAKE_CURRENT_BINARY_DIR}/${PROJECT_NAME}-config.cmake"
    @ONLY)

export(EXPORT "${PROJECT_NAME}-targets" NAMESPACE AWS::)

install(EXPORT "${PROJECT_NAME}-targets"
    DESTINATION "${CMAKE_INSTALL_LIBDIR}/${PROJECT_NAME}/cmake/"
    NAMESPACE AWS::)

install(FILES "${CMAKE_CURRENT_BINARY_DIR}/${PROJECT_NAME}-config.cmake"
    "${CMAKE_CURRENT_BINARY_DIR}/${PROJECT_NAME}-config-version.cmake"
    DESTINATION "${CMAKE_INSTALL_LIBDIR}/${PROJECT_NAME}/cmake/")

install(PROGRAMS "${CMAKE_CURRENT_SOURCE_DIR}/packaging/packager"
    DESTINATION "${CMAKE_INSTALL_LIBDIR}/${PROJECT_NAME}/cmake/")

