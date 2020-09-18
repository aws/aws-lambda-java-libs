#include <aws/lambda-runtime/version.h>
#include "gtest/gtest.h"

using namespace aws::lambda_runtime;

TEST(VersionTests, get_version_major)
{
    auto version = get_version_major();
    ASSERT_EQ(0, version);
}

TEST(VersionTests, get_version_minor)
{
    auto version = get_version_minor();
    ASSERT_GE(version, 1);
}

TEST(VersionTests, get_version_patch)
{
    auto version = get_version_patch();
    ASSERT_GE(version, 0);
}
