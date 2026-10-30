@echo off
cd /d "%~dp0\.."
if not exist "out" mkdir out
echo Compilando o projeto...
javac -cp "lib\jgraphx.jar" -d out src\structures\*.java src\model\*.java src\io\*.java src\algorithms\*.java src\ui\*.java src\Main.java
if %ERRORLEVEL% equ 0 (
    echo Compilacao concluida com sucesso!
) else (
    echo Erro na compilacao.
)

