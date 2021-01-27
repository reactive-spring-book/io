#!/usr/bin/env bash

cd $GITHUB_WORKSPACE 

echo "Starting build"
mkdir -p $HOME/.m2/
cp .ci.settings.xml $HOME/.m2/settings.xml
mvn -e -f $GITHUB_WORKSPACE/pom.xml  verify deploy
echo "Stopping build"