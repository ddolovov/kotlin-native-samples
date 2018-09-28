#!/usr/bin/env bash

DIR=$(cd "$(dirname "${BASH_SOURCE[0]}" )" && pwd )
source "$DIR/../konan.sh"

if [ x$TARGET == x ]; then
case "$OSTYPE" in
    darwin*)  TARGET=macbook ;;
    linux*)   TARGET=linux ;;
    msys*)    TARGET=mingw ;;
    *)        echo "unknown: $OSTYPE" && exit 1;;
esac
fi

var=CFLAGS_${TARGET}
CFLAGS=${!var}
var=LINKER_ARGS_${TARGET}
LINKER_ARGS=${!var}
var=COMPILER_ARGS_${TARGET}
COMPILER_ARGS=${!var} # add -opt for an optimized build.

OUTPUT_DIR=$DIR/build/bin/csvParser/main/release/executable/
mkdir -p $OUTPUT_DIR

kotlinc-native \
    $COMPILER_ARGS \
    -target $TARGET \
    -entry sample.csvparser.main \
    -o $OUTPUT_DIR/csvparser.kexe \
    $DIR/src/ || exit 1

echo "Artifact path is $OUTPUT_DIR/csvparser.kexe"
