include(CMakeFindDependencyMacro)

find_dependency(CURL)

include(${CMAKE_CURRENT_LIST_DIR}/@CMAKE_PROJECT_NAME@-targets.cmake)

set(AWS_LAMBDA_PACKAGING_SCRIPT ${CMAKE_CURRENT_LIST_DIR}/packager)
function(aws_lambda_package_target target)
    set(options OPTIONAL NO_LIBC)
    cmake_parse_arguments(PACKAGER "${options}" "" "" ${ARGN})
    if (${PACKAGER_NO_LIBC})
        set (PACKAGER_NO_LIBC "-d")
    else()
        set (PACKAGER_NO_LIBC "")
    endif()
    add_custom_target(aws-lambda-package-${target}
        COMMAND ${AWS_LAMBDA_PACKAGING_SCRIPT} ${PACKAGER_NO_LIBC} $<TARGET_FILE:${target}>
        DEPENDS ${target})
endfunction()

