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
	test/integration/codebuild-local/docker-retry.sh docker build --load -t codebuild-agent \
	 --build-arg ARCHITECTURE=$(ARCHITECTURE_ALIAS) \
	 -f test/integration/codebuild-local/Dockerfile.agent \
	 test/integration/codebuild-local

# Smoke tests are split per-architecture so CI can run each set on a native
# runner. Running the linux/arm64/v8 combos under QEMU on an x86_64 host makes
# `mvn install` recompile curl for aarch64 emulated, which takes ~30 minutes.
.PHONY: test-smoke
test-smoke: test-smoke-x86_64 test-smoke-aarch64

.PHONY: test-smoke-x86_64
test-smoke-x86_64: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/amd64
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/amd64

.PHONY: test-smoke-aarch64
test-smoke-aarch64: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.15 corretto11 linux/arm64/v8
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11 linux/arm64/v8

# BUILDSPEC can point to the buildspec directory (default, runs every OS) or to a
# single buildspec file, which is how CI parallelizes the run across OSes.
BUILDSPEC ?= test/integration/codebuild

.PHONY: test-integ
test-integ: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_all.sh $(BUILDSPEC)

# Command to run everytime you make changes to verify everything works
.PHONY: dev
dev: test

# Verifications to run before sending a pull request
.PHONY: pr
pr: test test-smoke

# Per-architecture PR checks so CI can run each on a native runner (no QEMU).
.PHONY: pr-x86_64
pr-x86_64: test test-smoke-x86_64

.PHONY: pr-aarch64
pr-aarch64: test test-smoke-aarch64

.PHONY: build
build: build-x86_64 build-aarch64

.PHONY: build-x86_64
build-x86_64:
	mvn clean install -DmultiArch=false $(EXTRA_LOAD_ARG)
	mvn install -P linux-x86_64 $(EXTRA_LOAD_ARG)
	mvn install -P linux_musl-x86_64 $(EXTRA_LOAD_ARG)

.PHONY: build-aarch64
build-aarch64:
	mvn clean install -DmultiArch=false $(EXTRA_LOAD_ARG)
	mvn install -P linux-aarch64 $(EXTRA_LOAD_ARG)
	mvn install -P linux_musl-aarch64 $(EXTRA_LOAD_ARG)

.PHONY: publish
publish:
	./ric-dev-environment/publish_snapshot.sh

.PHONY: publish
test-publish:
	./ric-dev-environment/test-platform-specific-jar-snapshot.sh

.PHONY: test-rie
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
