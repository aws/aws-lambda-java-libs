/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
#include <jni.h>
#include "macro.h"
#include "com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient.h"
#include "aws/lambda-runtime/runtime.h"
#include "aws/lambda-runtime/version.h"

static aws::lambda_runtime::runtime * CLIENT = nullptr;

static jint JNI_VERSION = JNI_VERSION_1_8;

static jclass invocationRequestClass;
static jfieldID invokedFunctionArnField;
static jfieldID deadlineTimeInMsField;
static jfieldID idField;
static jfieldID contentField;
static jfieldID clientContextField;
static jfieldID cognitoIdentityField;
static jfieldID xrayTraceIdField;


jint JNI_OnLoad(JavaVM* vm, void* reserved) {

    JNIEnv* env;
    if (vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION) != JNI_OK) {
        return JNI_ERR;
    }

    jclass tempInvocationRequestClassRef;
    tempInvocationRequestClassRef = env->FindClass("com/amazonaws/services/lambda/runtime/api/client/runtimeapi/dto/InvocationRequest");
    invocationRequestClass = (jclass) env->NewGlobalRef(tempInvocationRequestClassRef);
    env->DeleteLocalRef(tempInvocationRequestClassRef);

    idField = env->GetFieldID(invocationRequestClass , "id", "Ljava/lang/String;");
    invokedFunctionArnField = env->GetFieldID(invocationRequestClass , "invokedFunctionArn", "Ljava/lang/String;");
    deadlineTimeInMsField = env->GetFieldID(invocationRequestClass , "deadlineTimeInMs", "J");
    contentField = env->GetFieldID(invocationRequestClass , "content", "[B");
    xrayTraceIdField = env->GetFieldID(invocationRequestClass , "xrayTraceId", "Ljava/lang/String;");
    clientContextField = env->GetFieldID(invocationRequestClass , "clientContext", "Ljava/lang/String;");
    cognitoIdentityField = env->GetFieldID(invocationRequestClass , "cognitoIdentity", "Ljava/lang/String;");

    return JNI_VERSION;
}

void JNI_OnUnload(JavaVM *vm, void *reserved) {
    JNIEnv* env;
    vm->GetEnv(reinterpret_cast<void**>(&env), JNI_VERSION);

    env->DeleteGlobalRef(invocationRequestClass);
}

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

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_initializeClient(JNIEnv *env, jobject thisObject, jbyteArray userAgent, jbyteArray awsLambdaRuntimeApi) {
  std::string user_agent = toNativeString(env, userAgent);
  std::string endpoint = toNativeString(env, awsLambdaRuntimeApi);
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

  jobject invocationRequest;
  jbyteArray jArray;
  const jbyte* bytes;
  auto response = outcome.get_result();

  CHECK_EXCEPTION(env, invocationRequest = env->AllocObject(invocationRequestClass));
  CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, idField, env->NewStringUTF(response.request_id.c_str())));
  CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, invokedFunctionArnField, env->NewStringUTF(response.function_arn.c_str())));
  CHECK_EXCEPTION(env, env->SetLongField(invocationRequest, deadlineTimeInMsField, std::chrono::duration_cast<std::chrono::milliseconds>(response.deadline.time_since_epoch()).count()));

  if(response.xray_trace_id != ""){
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, xrayTraceIdField, env->NewStringUTF(response.xray_trace_id.c_str())));
  }

  if(response.client_context != ""){
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, clientContextField, env->NewStringUTF(response.client_context.c_str())));
  }

  if(response.cognito_identity != ""){
    CHECK_EXCEPTION(env, env->SetObjectField(invocationRequest, cognitoIdentityField, env->NewStringUTF(response.cognito_identity.c_str())));
  }

  bytes = reinterpret_cast<const jbyte*>(response.payload.c_str());
  CHECK_EXCEPTION(env, jArray = env->NewByteArray(response.payload.length()));
  CHECK_EXCEPTION(env, env->SetByteArrayRegion(jArray, 0, response.payload.length(), bytes));
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
