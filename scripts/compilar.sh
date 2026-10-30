#!/bin/bash
cd "$(dirname "$0")/.." || exit

JAVAC_BIN="javac"
if ! command -v javac &> /dev/null || ! javac -version &> /dev/null; then
    if [ -x "/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/javac" ]; then
        JAVAC_BIN="/Library/Java/JavaVirtualMachines/jdk-21.jdk/Contents/Home/bin/javac"
    elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/javac" ]; then
        JAVAC_BIN="$JAVA_HOME/bin/javac"
    fi
fi

mkdir -p out
echo "🔨 Compilando o projeto com $JAVAC_BIN..."
"$JAVAC_BIN" -cp "lib/jgraphx.jar" -d out $(find src -name "*.java")
if [ $? -eq 0 ]; then
    echo "✅ Compilação concluída com sucesso!"
else
    echo "❌ Erro na compilação."
    exit 1
fi

