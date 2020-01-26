#!/usr/bin/env bash

#set -e # stop script at any error - needs fixing git commit
#set -x # print commands

# GET USER INPUT
echo 'ENTER RELEASE VERSION:'
read version

# UPDATE VERSIONS
sed -i "s|^    <version>.*</version>|    <version>${version}</version>|" pom.xml
sed -i "1s|^.*$|##### ${version} (`date +%d/%m/%Y`)|" CHANGELOG.md
git add pom.xml CHANGELOG.md
git commit -m "prepare release ${version}"
git tag "${version}"

# BUILD THE ARTIFACTS
rm -rf target/
mvn clean compile assembly:single
mv "target/TheLongDarkHelpers-${version}-jar-with-dependencies.jar" "target/TheLongDarkHelpers-${version}.jar"

# PREPARE FOR NEXT ITERATION
nextVersion=$((${version%%.*} + 1)).0
sed -i "s|^    <version>.*</version>|    <version>${nextVersion}-SNAPSHOT</version>|" pom.xml
sed -i "1s|^|##### ${nextVersion} (not yet released)\n\n|" CHANGELOG.md
git add pom.xml CHANGELOG.md
git commit -m "prepare for next development iteration"

# GIT PUSH
echo "MANUAL STEP REQUIRED!!!!!"
echo "git push && git push --tags"
