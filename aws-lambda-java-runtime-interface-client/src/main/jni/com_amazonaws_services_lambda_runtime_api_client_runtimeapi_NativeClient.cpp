/*
 * Copyright 2019-present Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */
#include <jni.h>
#include "com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient.h"
#include "aws/lambda-runtime/runtime.h"
#include "aws/lambda-runtime/version.h"

#define CHECK_EXCEPTION(env, expr) \
    expr; \
    if ((env)->ExceptionOccurred()) \
        goto ERROR;

static aws::lambda_runtime::runtime * CLIENT = nullptr;

static void throwLambdaRuntimeClientException(JNIEnv *env, std::string message, aws::http::response_code responseCode){
  jclass lambdaRuntimeExceptionClass = env->FindClass("com/amazonaws/services/lambda/runtime/api/client/runtimeapi/LambdaRuntimeClientException");
  jstring jMessage = env->NewStringUTF(message.c_str());
  jmethodID exInit = env->GetMethodID(lambdaRuntimeExceptionClass, "<init>", "(Ljava/lang/String;I)V");
  jthrowable lambdaRuntimeException = (jthrowable) env->NewObject(lambdaRuntimeExceptionClass, exInit, jMessage, static_cast<int>(responseCode));
  env->Throw(lambdaRuntimeException);
}

static std::string toNativeString(JNIEnv *env, jbyteArray jArray) {
  int length = env->GetArrayLength(jArray);
  jbyte* bytes = env->GetByteArrayElements(jArray, NULL);
  std::string nativeString = std::string((char *)bytes, length);
  env->ReleaseByteArrayElements(jArray, bytes, JNI_ABORT);
  env->DeleteLocalRef(jArray);
  return nativeString;
}

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_initializeClient(JNIEnv *env, jobject thisObject, jbyteArray userAgent) {
  std::string user_agent = toNativeString(env, userAgent);
  std::string endpoint(getenv("AWS_LAMBDA_RUNTIME_API"));
  CLIENT = new aws::lambda_runtime::runtime(endpoint, user_agent);
}

JNIEXPORT jobject JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_next
  (JNIEnv *env, jobject thisObject){
  auto outcome = CLIENT->get_next();
  if (!outcome.is_success()) {
    std::string errorMessage("Failed to get next.");
    throwLambdaRuntimeClientException(env, errorMessage, outcome.get_failure());
    return NULL;
  }

  jclass invocationRequestClass;
  jfieldID invokedFunctionArnField;
  jfieldID deadlineTimeInMsField;
  jfieldID xrayTraceIdField;
  jfieldID idField; 
  jobject invocationRequest;
  jfieldID clientContextField;
  jfieldID cognitoIdentityField;
  jbyteArray jArray;
  const jbyte* bytes;
  jfieldID contentField;
  auto response = outcome.get_result();

  CHECK_EXCEPTION(env, invocationRequestClass = env->FindClass("com/amazonaws/services/lambda/runtime/api/client/runtimeapi/InvocationRequest"));
  CHECK_EXCEPTION(env, invocationRequest = env->AllocObject(invocationRequestClass));

  CHECK_EXCEPTION(env, idField = env->GetFieldID(invocationRequestClass , "id", "Ljava/lang/String;"));
  CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, idField, env->NewStringUTF(response.request_id.c_str())));
  
  CHECK_EXCEPTION(env, invokedFunctionArnField = env->GetFieldID(invocationRequestClass , "invokedFunctionArn", "Ljava/lang/String;"));
  CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, invokedFunctionArnField, env->NewStringUTF(response.function_arn.c_str())));

  CHECK_EXCEPTION(env, deadlineTimeInMsField = env->GetFieldID(invocationRequestClass , "deadlineTimeInMs", "J"));
  CHECK_EXCEPTION(env, env->SetLongField(invocationRequest, deadlineTimeInMsField, std::chrono::duration_cast<std::chrono::milliseconds>(response.deadline.time_since_epoch()).count()));

  if(response.xray_trace_id != ""){
    CHECK_EXCEPTION(env, xrayTraceIdField = env->GetFieldID(invocationRequestClass , "xrayTraceId", "Ljava/lang/String;"));
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, xrayTraceIdField, env->NewStringUTF(response.xray_trace_id.c_str())));
  }

  if(response.client_context != ""){
    CHECK_EXCEPTION(env, clientContextField = env->GetFieldID(invocationRequestClass , "clientContext", "Ljava/lang/String;"));
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, clientContextField, env->NewStringUTF(response.client_context.c_str())));
  }

  if(response.cognito_identity != ""){
    CHECK_EXCEPTION(env, cognitoIdentityField = env->GetFieldID(invocationRequestClass , "cognitoIdentity", "Ljava/lang/String;"));
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, cognitoIdentityField, env->NewStringUTF(response.cognito_identity.c_str())));
  }

  bytes = reinterpret_cast<const jbyte*>(response.payload.c_str());
  CHECK_EXCEPTION(env, jArray = env->NewByteArray(response.payload.length()));
  CHECK_EXCEPTION(env, env->SetByteArrayRegion(jArray, 0, response.payload.length(), bytes));
  CHECK_EXCEPTION(env, contentField = env->GetFieldID(invocationRequestClass , "content", "[B"));
  CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, contentField, jArray));

  return invocationRequest;

  ERROR:
      return NULL;
}

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_postInvocationResponse
  (JNIEnv *env, jobject thisObject, jbyteArray jrequestId, jbyteArray jresponseArray) {
  std::string payload = toNativeString(env, jresponseArray);
  if ((env)->ExceptionOccurred()){
    return;
  }
  std::string requestId =  toNativeString(env, jrequestId);
  if ((env)->ExceptionOccurred()){
    return;
  }

  auto response = aws::lambda_runtime::invocation_response::success(payload, "application/json");
  auto outcome = CLIENT->post_success(requestId, response);
  if (!outcome.is_success()) {
    std::string errorMessage("Failed to post invocation response.");
    throwLambdaRuntimeClientException(env, errorMessage, outcome.get_failure());
  }
}
