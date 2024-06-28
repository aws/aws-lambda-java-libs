/*
Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
SPDX-License-Identifier: Apache-2.0
*/
#include <jni.h>

#ifndef _Included_com_amazonaws_services_lambda_crac_DNSManager
#define _Included_com_amazonaws_services_lambda_crac_DNSManager
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_amazonaws_services_lambda_crac_DNSManager_clearCache
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif
