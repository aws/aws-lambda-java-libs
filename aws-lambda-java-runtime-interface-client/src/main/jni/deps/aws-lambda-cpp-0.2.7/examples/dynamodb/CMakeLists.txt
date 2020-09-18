cmake_minimum_required(VERSION 3.5)
set(CMAKE_CXX_STANDARD 11)
project(ddb-demo LANGUAGES CXX)

find_package(aws-lambda-runtime)
find_package(AWSSDK COMPONENTS dynamodb)

add_executable(${PROJECT_NAME} "main.cpp")

target_link_libraries(${PROJECT_NAME} PUBLIC AWS::aws-lambda-runtime ${AWSSDK_LINK_LIBRARIES})

target_compile_options(${PROJECT_NAME} PRIVATE
    "-fno-exceptions"
    "-fno-rtti"
    "-Wall"
    "-Wextra"
    "-Werror"
    "-Wconversion"
    "-Wno-sign-conversion")

target_compile_features(${PROJECT_NAME} PRIVATE "cxx_std_11")

aws_lambda_package_target(${PROJECT_NAME})

