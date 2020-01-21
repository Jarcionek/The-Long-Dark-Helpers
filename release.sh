#!/usr/bin/env bash

#set -e # stop script at any error - needs fixing git commit
#set -x # print commands

# GET USER INPUT
echo 'ENTER RELEASE VERSION:'
read version

# UPDATE VERSIONS
sed -i "s|^    <version>.*</version>|    <version>${version}</version>|" pom.xml
git add pom.xml
git commit -m "prepare release ${version}"
git tag "${version}"

# BUILD THE ARTIFACTS
rm -rf target/
mvn package

# PREPARE FOR NEXT ITERATION
nextVersion=$((${version%%.*} + 1)).0
sed -i "s|^    <version>.*</version>|    <version>${nextVersion}-SNAPSHOT</version>|" pom.xml
git add pom.xml
git commit -m "prepare for next development iteration"

# GIT PUSH
echo "git push && git push --tags"