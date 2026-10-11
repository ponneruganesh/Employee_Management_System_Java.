@echo off
cd /d "%~dp0"
echo Compiling Employee Management System...
javac src\EmployeeManagementSystem.java
if errorlevel 1 pause & exit /b 1
echo Starting project...
java -cp src EmployeeManagementSystem
pause
