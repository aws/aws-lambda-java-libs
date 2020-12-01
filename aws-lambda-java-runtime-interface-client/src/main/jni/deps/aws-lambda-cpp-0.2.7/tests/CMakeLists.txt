project(aws-lambda-runtime-tests LANGUAGES CXX)
find_package(AWSSDK COMPONENTS lambda iam)

add_executable(${PROJECT_NAME}
    main.cpp
    runtime_tests.cpp
    version_tests.cpp
    gtest/gtest-all.cc)

target_link_libraries(${PROJECT_NAME} PRIVATE ${AWSSDK_LINK_LIBRARIES} aws-lambda-runtime)

include(GoogleTest)
gtest_discover_tests(${PROJECT_NAME} EXTRA_ARGS "--aws_prefix=${TEST_RESOURCE_PREFIX}") # requires CMake 3.10 or later

add_subdirectory(resources)

