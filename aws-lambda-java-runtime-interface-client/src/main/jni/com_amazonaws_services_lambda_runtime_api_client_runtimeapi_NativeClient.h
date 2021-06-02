#include <jni.h>

#ifndef _Included_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient
#define _Included_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_initializeClient
  (JNIEnv *, jobject, jbyteArray);

JNIEXPORT jobject JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_next
  (JNIEnv *, jobject);

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_runtime_api_client_runtimeapi_NativeClient_postInvocationResponse
  (JNIEnv *, jobject, jbyteArray, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
