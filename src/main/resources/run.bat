@echo off
IF "%JAVA_HOME%" == "" (
    echo JAVA_HOME has not been set. Please follow the installation steps on the GNTool guide.
    echo https://github.com/NicholasMoser/GNTool/blob/master/README.md
    echo.
    pause
    exit(1)
)

"%JAVA_HOME%/bin/java" --module-path=modules --module com.github.nicholasmoser