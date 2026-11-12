#!/bin/bash
# Builds the browser version of the engine: Java -> JavaScript with TeaVM. No Gradle.
# Needs JDK 11+ and Node. Run from this folder: bash build.sh   then   bash pack.sh ../dist
set -e
cd "$(dirname "$0")"
SEP=":"; case "$(uname -s)" in MINGW*|MSYS*|CYGWIN*) SEP=";" ;; esac

# 1. download TeaVM, JOML and their dependencies from Maven Central into dl/
if [ ! -d dl ] || [ -z "$(ls dl/*.jar 2>/dev/null)" ]; then
  mkdir -p dl && (cd dl && node ../fetch-deps.js > /dev/null)
fi

# 2. compile the engine sources plus the WebGL / DOM shims
CP="dl/teavm-jso-0.12.3.jar${SEP}dl/teavm-jso-apis-0.12.3.jar${SEP}dl/teavm-interop-0.12.3.jar${SEP}dl/joml-1.9.23.jar${SEP}../libs/jbox2d-library.jar${SEP}dl/teavm-classlib-0.12.3.jar"
rm -rf out/classes out/js; mkdir -p out/classes out/js
find src -name '*.java' > out/srcs.txt
javac --release 11 -XDstringConcat=inline -nowarn -cp "$CP" -d out/classes @out/srcs.txt

# 3. compile and run the TeaVM driver
# the stock JOML jar must not be on the tool classpath: the patched copies in src/ have to win
TCP=$(ls dl/*.jar | grep -v joml | tr '\n' "$SEP")
javac -cp "$TCP" -d out Build.java
java -Xmx2g -cp "out${SEP}${TCP}" Build out/js out/classes dl/teavm-jso-0.12.3.jar dl/teavm-jso-apis-0.12.3.jar dl/teavm-interop-0.12.3.jar dl/joml-1.9.23.jar ../libs/jbox2d-library.jar dl/teavm-classlib-0.12.3.jar
echo "built out/js/engine.js"
