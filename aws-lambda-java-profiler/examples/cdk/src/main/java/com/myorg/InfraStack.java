package com.myorg;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.LayerVersion;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static software.amazon.awscdk.services.lambda.Architecture.*;
import static software.amazon.awscdk.services.lambda.Runtime.*;

public class InfraStack extends Stack {
    public InfraStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfraStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        var resultsBucketName = UUID.randomUUID().toString();
        var resultsBucket = Bucket.Builder.create(this, "profiler-results-bucket")
                .bucketName(resultsBucketName)
                .build();

        var layerVersion = LayerVersion.Builder.create(this, "async-profiler-layer")
                .compatibleArchitectures(List.of(ARM_64, X86_64))
                .compatibleRuntimes(List.of(JAVA_11, JAVA_17, JAVA_21))
                .code(Code.fromAsset("../../target/extension.zip"))
                .build();

        var environmentVariables = Map.of("JAVA_TOOL_OPTIONS", "-XX:+UnlockDiagnosticVMOptions -XX:+DebugNonSafepoints -javaagent:/opt/profiler.jar",
                "PROFILER_RESULTS_BUCKET_NAME", resultsBucketName);

        var function = Function.Builder.create(this, "example-profiler-function")
                .runtime(JAVA_21)
                .handler("helloworld.App")
                .code(Code.fromAsset("../function/profiling-example/target/Helloworld-1.0.jar"))
                .memorySize(2048)
                .layers(List.of(layerVersion))
                .environment(environmentVariables)
                .timeout(Duration.seconds(30))
                .build();

        resultsBucket.grantPut(function);
    }
}
