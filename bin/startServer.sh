#!/bin/sh
#
#
#

OWNDIR=`dirname $0`

#set -x

JAVA_ARGS=--server.port=8019
JAVA_ARGS="$JAVA_ARGS --logging.level.de.yard=DEBUG"
JAVA_OPTS=

java $JAVA_OPTS -jar target/webdav-photo-album-0.0.1-SNAPSHOT.jar $JAVA_ARGS

