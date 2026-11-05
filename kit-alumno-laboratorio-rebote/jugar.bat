@echo off
call compilar.bat
if not exist out\juego\Main.class (
    echo.
    echo *** No compilo. Revisa los mensajes de error de arriba. ***
    pause
    exit /b 1
)
java -cp out juego.Main
