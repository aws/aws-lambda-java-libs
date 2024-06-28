/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
#include <jni.h>
#include "macro.h"
#include "com_amazonaws_services_lambda_crac_DNSManager.h"

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_crac_DNSManager_clearCache
  (JNIEnv *env, jclass thisClass) {
    jclass iNetAddressClass;
    jclass concurrentMap;
    jfieldID cacheFieldID;
    jobject cacheObj;
    jmethodID clearMethodID;
    CHECK_EXCEPTION(env, iNetAddressClass = env->FindClass("java/net/InetAddress"));
    CHECK_EXCEPTION(env, concurrentMap = env->FindClass("java/util/concurrent/ConcurrentMap"));
    CHECK_EXCEPTION(env, cacheFieldID = env->GetStaticFieldID(iNetAddressClass, "cache", "Ljava/util/concurrent/ConcurrentMap;"));
    CHECK_EXCEPTION(env, cacheObj = (jobject) env->GetStaticObjectField(iNetAddressClass, cacheFieldID));
    CHECK_EXCEPTION(env, clearMethodID = env->GetMethodID(concurrentMap, "clear", "()V"));
    CHECK_EXCEPTION(env, env->CallVoidMethod(cacheObj, clearMethodID));
    return;

    ERROR:
      // we need to fail silently here
      env->ExceptionClear();
}
