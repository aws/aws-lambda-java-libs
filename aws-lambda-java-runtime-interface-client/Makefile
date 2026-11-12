# Java 8 does not support passing some args (such add --add-opens) so we need to clear them
ifeq ($(IS_JAVA_8),true)
	EXTRA_LOAD_ARG := -DargLineForReflectionTestOnly=""
else
	EXTRA_LOAD_ARG :=
endif

.PHONY: target
target:
	$(info ${HELP_MESSAGE})
	@exit 0

.PHONY: test
test:
	mvn test $(EXTRA_LOAD_ARG)

# One OS integration test (see test/integration/codebuild/scripts/run_os_test.sh;
# the combinations CI runs are in .github/workflows/runtime-interface-client_pr.yml), e.g.
#   make test-integ OS_DISTRIBUTION=debian DISTRO_VERSION=bookworm RUNTIME_VERSION=corretto11 PLATFORM=linux/amd64
# A PLATFORM other than the host's needs emulation: built into Docker Desktop; on
# Linux, run once: docker run --rm --privileged public.ecr.aws/eks-distro-build-tooling/binfmt-misc:qemu-v6.1.0 --install all
.PHONY: test-integ
test-integ:
	OS_DISTRIBUTION=$(OS_DISTRIBUTION) DISTRO_VERSION=$(DISTRO_VERSION) RUNTIME_VERSION=$(RUNTIME_VERSION) PLATFORM=$(PLATFORM) test/integration/codebuild/scripts/run_os_test.sh

# Command to run everytime you make changes to verify everything works
.PHONY: dev
dev: test

# Verifications to run before sending a pull request
.PHONY: pr
pr: test

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
