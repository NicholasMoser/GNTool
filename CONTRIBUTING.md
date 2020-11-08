# Contributing

Thank you for wanting to contribute!

## Log an Issue

Open an Issue on the Github page under Issues. If it is a defect, please include the logs and an explanation of what you were trying to do. Logs can be found in your home directory and will start with fpk (e.g. `fpkjava0.log.0`). Five logs for separate instances can exist at one time, but just include them all with any defect request.

## Contribute Code

If you would like to contribute code, please open a pull request and I will try and review it in a timely manner.

Here are some recommendations for creating a local development environment for GNTool:

- Use the latest version of [Intellij](https://www.jetbrains.com/idea/)
- Use the [Google Java Style](https://github.com/google/styleguide/blob/gh-pages/intellij-java-google-style.xml)
  - File → Settings → Editor → Code Style → Select the small gear icon next to "Scheme", select Import Scheme → IntelliJ IDEA code style XML.

Here are some useful commands for working with GNTool:

- `gradlew run` will run GNTool
- `gradlew build` will build GNTool
- `gradlew dist` will package a distribution zip of GNTool

## Release Process

My release process for GNTool currently is:

- Make sure that `version` in the build.gradle file matches the version in the gradle.properties file. Update it, commit, and push if necessary.
- Run `gradlew release` to run the release plugin.
- Checkout the newly generated Git tag. For example, if releasing 2.7 you will want to do `git checkout 2.7`.
- Run `gradlew dist` to generate the distribution zip.
- Create a release and upload to Github.
- Profit!
