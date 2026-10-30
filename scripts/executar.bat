@echo off
cd /d "%~dp0\.."
if not exist "out" call scripts\compilar.bat
echo Iniciando GraphLab PAA...
java -cp "out;lib\jgraphx.jar" Main

