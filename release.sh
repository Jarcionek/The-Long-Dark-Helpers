#!/usr/bin/env bash

#set -e # stop script at any error - needs fixing git commit
#set -x # print commands

# GET USER INPUT
echo 'ENTER RELEASE VERSION:'
read version

# UPDATE VERSIONS
sed -i "s|^    <version>.*</version>|    <version>${version}</version>|" pom.xml
sed -i "1s|^.*$|##### ${version} (`date +%d/%m/%Y`)|" CHANGELOG.md
sed -i "s|^    public static final String VERSION = ".*";|    public static final String VERSION = \"${version}\";|" src/main/java/uk/co/jpawlak/thelongdark/mapnotes/Main.java
git add pom.xml CHANGELOG.md src/main/java/uk/co/jpawlak/thelongdark/mapnotes/Main.java
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
sed -i "s|^    public static final String VERSION = ".*";|    public static final String VERSION = \"${nextVersion}-SNAPSHOT\";|" src/main/java/uk/co/jpawlak/thelongdark/mapnotes/Main.java
git add pom.xml CHANGELOG.md src/main/java/uk/co/jpawlak/thelongdark/mapnotes/Main.java
git commit -m "prepare for next development iteration"

# GIT PUSH
echo "MANUAL STEP REQUIRED!!!!!"
echo "git push && git push --tags"
echo "upload file to https://drive.google.com/open?id=1a-Tr_NBwtsMtFzHucmmitY7k2Cus0NJt"
