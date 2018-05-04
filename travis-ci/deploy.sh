#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] || [ -n "$TRAVIS_TAG" ]; then
    if [ -z "$TRAVIS_TAG" ]; then
        mvn deploy --settings travis-ci/maven_settings.xml -Dchangelist='-SNAPSHOT'
    else
        mvn versions:set -DnewVersion=$TRAVIS_TAG
        mvn deploy --settings travis-ci/maven_settings.xml
    fi
fi