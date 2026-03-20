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


target:
	$(info ${HELP_MESSAGE})
	@exit 0


test:
	mvn test $(EXTRA_LOAD_ARG)


setup-codebuild-agent:
	docker build -t codebuild-agent \
	 --build-arg ARCHITECTURE=$(ARCHITECTURE_ALIAS) \
	  - < test/integration/codebuild-local/Dockerfile.agent


test-smoke: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/amd64
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/arm64/v8
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/amd64
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/arm64/v8


test-integ: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_all.sh test/integration/codebuild

# Command to run everytime you make changes to verify everything works

dev: test

# Verifications to run before sending a pull request

pr: test test-smoke


# The default `mvn clean install` builds all 4 native .so variants (x86_64/aarch64 × glibc/musl)
# and bundles them into a single jar (see build-jni-lib.sh), runs the tests for the host
# environment, and updates coverage data. This is sufficient for what we want to do here.
#
# Running profile-specific builds (e.g., `mvn install -P linux-aarch64`) does NOT test the
# cross-compiled .so — JniHelper.load() tries each .so in order and always loads the first
# one matching the host platform (linux-x86_64 on GitHub Actions runners), so the other
# variants are never exercised. 
#
# Platform-specific tests are run through test-smoke via codebuild containers.

build: 
	mvn clean install $(EXTRA_LOAD_ARG) 

publish:
	./ric-dev-environment/publish_snapshot.sh


test-publish:
	./ric-dev-environment/test-platform-specific-jar-snapshot.sh


test-rie:
	./scripts/test-rie.sh "EchoHandler::handleRequest" 

define HELP_MESSAGE

Usage: $ make [TARGETS]

TARGETS
	build       Builds the package.
	dev         Run all development tests after a change.
	pr          Perform all checks before submitting a Pull Request.
	test        Run the Unit tests.
	test-rie    Build and test RIC locally with Lambda Runtime Interface Emulator. (Requires building the project first)
endef

.PHONY: target test setup-codebuild-agent test-smoke test-integ dev pr build publish publish test-rie