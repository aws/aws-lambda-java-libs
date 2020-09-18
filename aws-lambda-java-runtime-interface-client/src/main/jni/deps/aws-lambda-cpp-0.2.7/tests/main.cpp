#include <aws/core/Aws.h>
#include <aws/core/utils/logging/ConsoleLogSystem.h>
#include "gtest/gtest.h"

std::function<std::shared_ptr<Aws::Utils::Logging::LogSystemInterface>()> get_console_logger_factory()
{
    return [] {
        return Aws::MakeShared<Aws::Utils::Logging::ConsoleLogSystem>(
            "console_logger", Aws::Utils::Logging::LogLevel::Warn);
    };
}

std::string aws_prefix;

void parse_args(int argc, char** argv)
{
    const std::string resource_prefix_option = "--aws_prefix=";
    for (int i = 1; i < argc; i++) {
        std::string arg = argv[i];
        if (arg.find(resource_prefix_option) == 0) {
            aws_prefix = arg.substr(resource_prefix_option.length()); // get whatever value after the '='
            break;
        }
    }
}

int main(int argc, char** argv)
{
    parse_args(argc, argv);
    Aws::SDKOptions options;
    options.loggingOptions.logLevel = Aws::Utils::Logging::LogLevel::Warn;
    options.loggingOptions.logger_create_fn = get_console_logger_factory();
    Aws::InitAPI(options);
    ::testing::InitGoogleTest(&argc, argv);
    int exit_code = RUN_ALL_TESTS();
    Aws::ShutdownAPI(options);
    return exit_code;
}
