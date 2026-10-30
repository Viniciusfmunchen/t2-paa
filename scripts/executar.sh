#!/bin/bash
cd "$(dirname "$0")/.." || exit

JAVA_BIN="java"
if ! command -v java &> /dev/null || ! java -version &> /dev/null; then
    if [ -x "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/java" ]; then
        JAVA_BIN="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/java"
    elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
        JAVA_BIN="$JAVA_HOME/bin/java"
    fi
fi

if [ ! -d "out" ] || [ -z "$(ls -A out)" ]; then
    ./scripts/compilar.sh
fi
echo "🚀 Iniciando GraphLab PAA com $JAVA_BIN..."
"$JAVA_BIN" -cp "out:lib/jgraphx.jar" Main

