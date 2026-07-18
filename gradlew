#!/bin/sh
# Gradle wrapper launcher. Run `gradle wrapper` once with Gradle installed
# (or open the project in Android Studio) to regenerate gradle-wrapper.jar,
# then this script will work as usual: ./gradlew assembleDebug
DIR="$(cd "$(dirname "$0")" && pwd)"
exec java -jar "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
