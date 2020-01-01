#!/bin/bash
SCRIPT_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
cd $SCRIPT_HOME/..
java -cp ./build/libs/minihadoop-1.0-SNAPSHOT-all.jar comnet.minihadoop.master.Application