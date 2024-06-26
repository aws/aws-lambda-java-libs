x86_64_ALIAS := amd64
aarch64_ALIAS := arm64
ARCHITECTURE := $(shell arch)
ARCHITECTURE_ALIAS := $($(shell echo "$(ARCHITECTURE)_ALIAS"))
ARCHITECTURE_ALIAS := $(or $(ARCHITECTURE_ALIAS),amd64) # on any other archs defaulting to amd64

# Java 8 does not support passing some args (such add --add-opens) so we need to clear them
ifeq ($(IS_JAVA_8),true)
	EXTRA_LOAD_ARG := -DargLineForReflectionTestOnly=""
else
	EXTRA_LOAD_ARG :=
endif

# This optional module exports MAVEN_REPO_URL, MAVEN_REPO_USERNAME and MAVEN_REPO_PASSWORD environment variables
# making it possible to publish resulting artifacts to a codeartifact maven repository
-include ric-dev-environment/codeartifact-repo.mk

.PHONY: target
target:
	$(info ${HELP_MESSAGE})
	@exit 0

.PHONY: test
test:
	mvn test $(EXTRA_LOAD_ARG)

.PHONY: setup-codebuild-agent
setup-codebuild-agent:
	docker build -t codebuild-agent \
	 --build-arg ARCHITECTURE=$(ARCHITECTURE_ALIAS) \
	  - < test/integration/codebuild-local/Dockerfile.agent

.PHONY: test-smoke
test-smoke: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/amd64
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/arm64/v8
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/amd64
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/arm64/v8

.PHONY: test-integ
test-integ: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_all.sh test/integration/codebuild

# Command to run everytime you make changes to verify everything works
.PHONY: dev
dev: test

# Verifications to run before sending a pull request
.PHONY: pr
pr: test test-smoke

.PHONY: build
build: 
	mvn clean install $(EXTRA_LOAD_ARG)
	mvn install -P linux-x86_64 $(EXTRA_LOAD_ARG)
	mvn install -P linux_musl-x86_64 $(EXTRA_LOAD_ARG)
	mvn install -P linux-aarch64 $(EXTRA_LOAD_ARG)
	mvn install -P linux_musl-aarch64 $(EXTRA_LOAD_ARG)

.PHONY: publish
publish:
	./ric-dev-environment/publish_snapshot.sh

.PHONY: publish
test-publish:
	./ric-dev-environment/test-platform-specific-jar-snapshot.sh

define HELP_MESSAGE

Usage: $ make [TARGETS]

TARGETS
	build       Builds the package.
	dev         Run all development tests after a change.
	pr          Perform all checks before submitting a Pull Request.
	test        Run the Unit tests.

endef
