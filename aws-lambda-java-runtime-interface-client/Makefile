.PHONY: target
target:
	$(info ${HELP_MESSAGE})
	@exit 0

.PHONY: test
test:
	mvn test

.PHONY: setup-codebuild-agent
setup-codebuild-agent:
	docker build -t codebuild-agent - < test/integration/codebuild-local/Dockerfile.agent

.PHONY: test-smoke
test-smoke: setup-codebuild-agent
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.alpine.yml alpine 3.12 corretto11
	CODEBUILD_IMAGE_TAG=codebuild-agent test/integration/codebuild-local/test_one.sh test/integration/codebuild/buildspec.os.amazoncorretto.yml amazoncorretto amazoncorretto 11

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
	mvn clean install

define HELP_MESSAGE

Usage: $ make [TARGETS]

TARGETS
	build       Builds the package.
	dev         Run all development tests after a change.
	pr          Perform all checks before submitting a Pull Request.
	test        Run the Unit tests.

endef
