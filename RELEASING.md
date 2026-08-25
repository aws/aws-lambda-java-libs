# Releasing to Maven Central

How maintainers publish a module using the
[`Release to Maven Central`](.github/workflows/release.yml) workflow.

Releasable modules: `aws-lambda-java-core`, `aws-lambda-java-events`,
`aws-lambda-java-events-sdk-transformer`, `aws-lambda-java-log4j2`,
`aws-lambda-java-serialization`, `aws-lambda-java-tests`.

> `aws-lambda-java-runtime-interface-client` has its own pipeline,
> [`release-runtime-interface-client.yml`](.github/workflows/release-runtime-interface-client.yml).

## Cutting a release

1. **Actions → Release to Maven Central → Run workflow**, with the branch set to
   **`main`** (releases only run from `main`).
2. Fill in the inputs:

   | Input | Required | Notes |
   |-------|----------|-------|
   | `module` | yes | Module directory to release. |
   | `changelogEntry` | yes | Markdown bullets, e.g. `- Fix X`. Added to the module `RELEASE.CHANGELOG.md` and used as the GitHub Release notes. |
   | `releaseVersion` | no | Defaults to the POM version without `-SNAPSHOT`. |
   | `developmentVersion` | no | Next dev version; must end with `-SNAPSHOT`. |
   | `skip_publish` | no | Dry run: build and validate, publish nothing. |

3. Run it and approve the `Release` environment when prompted.

## What it does

1. Validates the branch and resolves the release version from the POM (or your override).
2. Builds and tests the module.
3. Publishes to Maven Central and pushes the tag `<module>-<version>`.
4. Opens a **version-bump PR** into `main` with the next `-SNAPSHOT`, the updated
   `lastPublished` comment, the parent POM's version-map entry (for modules other
   modules depend on: core, events, serialization), and your changelog entry.
5. Creates a GitHub Release on the tag from your changelog entry.

Merge the version-bump PR to return `main` to a clean `-SNAPSHOT` state.

## Dry runs

Set `skip_publish` to build, test, and `release:prepare -DdryRun=true` without
pushing anything. A `changelogEntry` is still required by the form but unused.

## If a release fails

- **Before publish:** nothing was pushed. Fix and re-run.
- **After publish:** the Central version is immutable and the tag exists. Don't
  re-run for the same version (it fails at `release:prepare`); finish the
  remaining steps by hand (merge the bump PR, or `gh release create`).
