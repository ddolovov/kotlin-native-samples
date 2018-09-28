@echo off

setlocal

set DIR=.

if defined KONAN_HOME (
    set "PATH=%KONAN_HOME%\bin;%PATH%"
) else (
    set "PATH=..\..\dist\bin;..\..\bin;%PATH%"
)

if "%TARGET%" == "" set TARGET=mingw

set OUTPUT_DIR="%DIR%\build\bin\csvParser\main\release\executable\"
mkdir "%OUTPUT_DIR%"

call konanc -target "%TARGET%" -entry sample.csvparser.main "%DIR%\src\" -o "%OUTPUT_DIR%\csvparser.exe"
exit /b %ERRORLEVEL%
