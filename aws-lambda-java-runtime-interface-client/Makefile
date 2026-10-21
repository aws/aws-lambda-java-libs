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

# OS integration tests (see test/integration/codebuild/scripts/run_os_test.sh).
RUN_OS_TEST := test/integration/codebuild/scripts/run_os_test.sh

# Smoke tests are split per-architecture so CI can run each set on a native
# runner. amazoncorretto 11 is left out of the integration matrix
# (test/integration/os-test-matrix.json) because smoke already covers it.
# Changing it here changes integration coverage too.
.PHONY: test-smoke
test-smoke: test-smoke-x86_64 test-smoke-aarch64

.PHONY: test-smoke-x86_64
test-smoke-x86_64:
	OS_DISTRIBUTION=alpine DISTRO_VERSION=3.15 RUNTIME_VERSION=corretto11 PLATFORM=linux/amd64 $(RUN_OS_TEST)
	OS_DISTRIBUTION=amazoncorretto DISTRO_VERSION=amazoncorretto RUNTIME_VERSION=11 PLATFORM=linux/amd64 $(RUN_OS_TEST)

.PHONY: test-smoke-aarch64
test-smoke-aarch64:
	OS_DISTRIBUTION=alpine DISTRO_VERSION=3.15 RUNTIME_VERSION=corretto11 PLATFORM=linux/arm64/v8 $(RUN_OS_TEST)
	OS_DISTRIBUTION=amazoncorretto DISTRO_VERSION=amazoncorretto RUNTIME_VERSION=11 PLATFORM=linux/arm64/v8 $(RUN_OS_TEST)

# Without arguments, runs every combination in test/integration/os-test-matrix.json
# (the CI matrix) for the host's platform; PLATFORMS=all runs every platform.
# With arguments, runs one combination, e.g.
#   make test-integ OS_DISTRIBUTION=debian DISTRO_VERSION=bookworm RUNTIME_VERSION=corretto11 PLATFORM=linux/amd64
.PHONY: test-integ
test-integ:
ifeq ($(OS_DISTRIBUTION),)
	test/integration/codebuild/scripts/run_all_os_tests.sh
else
	OS_DISTRIBUTION=$(OS_DISTRIBUTION) DISTRO_VERSION=$(DISTRO_VERSION) RUNTIME_VERSION=$(RUNTIME_VERSION) PLATFORM=$(PLATFORM) $(RUN_OS_TEST)
endif

# Command to run everytime you make changes to verify everything works
.PHONY: dev
dev: test

# Verifications to run before sending a pull request
.PHONY: pr
pr: test test-smoke

# Per-architecture PR checks so CI can run each on a native runner (no QEMU).
# No `test` here: the required `build` check already runs the unit tests
# (build-<arch>), so CI would only run them twice.
.PHONY: pr-x86_64
pr-x86_64: test-smoke-x86_64

.PHONY: pr-aarch64
pr-aarch64: test-smoke-aarch64

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
