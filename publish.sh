#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
sudo "$DIR"/./gradlew clean uploadArchives
sudo chmod --recursive a+rwx "$DIR"/build
