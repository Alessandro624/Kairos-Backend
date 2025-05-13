REM =====================================
REM kairos-start.bat - Windows script for Kairos environment
REM =====================================
REM 
REM This batch script launches the Kairos backend application with admin privileges
REM using PowerShell to execute the bash startup script.
REM
REM The script:
REM 1. Runs with elevated (admin) privileges
REM 2. Changes to current directory 
REM 3. Executes the kairos-start.sh bash script
REM
REM Requirements:
REM - Windows OS
REM - PowerShell
REM - WSL or Git Bash installed
REM =====================================

powershell -Command "Start-Process cmd -Verb RunAs -ArgumentList '/k cd %cd% && bash kairos-start.sh'"
pause
