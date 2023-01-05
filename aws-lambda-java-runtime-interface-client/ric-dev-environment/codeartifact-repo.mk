
ifneq ("$(wildcard ric-dev-environment/codeartifact-properties.mk)","")

    include ric-dev-environment/codeartifact-properties.mk
    $(info Found codeartifact-properties.mk module)

    export MAVEN_REPO_URL:=$(shell aws codeartifact get-repository-endpoint \
                            --domain ${CODE_ARTIFACT_DOMAIN} \
                            --repository ${CODE_ARTIFACT_REPO_NAME} \
                            --format maven \
                            --output text \
                            --region ${CODE_ARTIFACT_REPO_REGION})

    export MAVEN_REPO_PASSWORD:=$(shell aws codeartifact get-authorization-token \
                                  --domain ${CODE_ARTIFACT_DOMAIN} \
                                  --domain-owner ${CODE_ARTIFACT_REPO_ACCOUNT} \
                                  --query authorizationToken \
                                  --output text \
                                  --region ${CODE_ARTIFACT_REPO_REGION})

    export MAVEN_REPO_USERNAME:=aws

    $(info MAVEN_REPO_URL: $(MAVEN_REPO_URL))
    # $(info MAVEN_REPO_PASSWORD: $(MAVEN_REPO_PASSWORD))
    $(info MAVEN_REPO_USERNAME: $(MAVEN_REPO_USERNAME))
    $(info CODE_ARTIFACT_REPO_NAME: $(CODE_ARTIFACT_REPO_NAME))
endif
