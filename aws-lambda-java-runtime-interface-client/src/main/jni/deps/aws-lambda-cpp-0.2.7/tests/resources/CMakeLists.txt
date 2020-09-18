project(lambda-test-fun LANGUAGES CXX)

# resources are the actual lambda functions deployed and exercised by the tests
add_executable(${PROJECT_NAME} lambda_function.cpp)
target_include_directories(${PROJECT_NAME} PRIVATE ${CMAKE_SOURCE_DIR}/include)
target_link_libraries(${PROJECT_NAME} PRIVATE aws-lambda-runtime)
# package the lambda function into lambda.zip
add_custom_target(aws-lambda-package-lambda-test-fun
    COMMAND "${CMAKE_SOURCE_DIR}/packaging/packager" "${CMAKE_CURRENT_BINARY_DIR}/lambda-test-fun"
    DEPENDS ${PROJECT_NAME})

add_custom_target(aws-lambda-package-lambda-test-fun-no-glibc
    COMMAND "${CMAKE_SOURCE_DIR}/packaging/packager" -d "${CMAKE_CURRENT_BINARY_DIR}/lambda-test-fun"
    DEPENDS ${PROJECT_NAME})
