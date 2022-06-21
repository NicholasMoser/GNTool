# Contributing

Thank you for wanting to contribute!

## Log an Issue

Open an Issue on the Github page under Issues. If it is a defect, please include the logs and an
explanation of what you were trying to do. Logs can be found in your home directory and will start
with fpk (e.g. `fpkjava0.log.0`). Five logs for separate instances can exist at one time, but just
include them all with any defect request.

## Contribute Code

If you would like to contribute code, please open a pull request and I will try and review it in a
timely manner.

Here are some recommendations for creating a local development environment for GNTool:

- Download the latest version of [Intellij](https://www.jetbrains.com/idea/).
- In IntelliJ, go to `File -> New -> Project from Version Control...`,
  put in https://github.com/NicholasMoser/GNTool for the URL and hit the **Clone** button.
- In IntelliJ, go to `File -> Project Structure...`. Select the combobox under **Project SDK** and
  choose `Add SDK -> Download JDK...`. Select **Version** 17 and **Vendor** Eclipse Temurin.
- Download the [Google Java Code Style](https://github.com/google/styleguide/raw/gh-pages/intellij-java-google-style.xml)
  and save it somewhere.
- In IntelliJ, go to `File -> Settings -> Editor -> Code Style`. Then select the small gear icon
  next to the **Scheme** combobox. Select `Import Scheme → IntelliJ IDEA code style XML`. Last, 
  choose the previously downloaded Google Java Code Style.

The right side of IntelliJ has a view titled **Gradle** with commands to build and run the
application.

- `gntool -> Tasks -> application -> run` will run the application. Right click and select
  **Debug GNTool [run]** to run with debug breakpoints.
- `gntool -> Tasks -> build -> build` will build GNTool.
- `gntool -> Tasks -> other -> dist` will package a distribution zip of GNTool

## Release Process

My release process for GNTool currently is:

- Make sure that `version` in the build.gradle file matches the version in the gradle.properties file. Update it, commit, and push if necessary.
- Run `gradlew release` to run the release plugin.
- Checkout the newly generated Git tag. For example, if releasing 2.7 you will want to do `git checkout 2.7`.
- Run `gradlew dist` to generate the distribution zip.
- Create a release and upload to Github.
- Profit!
