@echo off
chcp 65001 > nul
echo ===================================================
echo    Smart City Scheduling - Final Compilation
echo ===================================================
echo.

REM Clean and create directories
if exist "target" rmdir /s /q "target"
mkdir "target\classes"

echo Step 1: Compiling common classes...
javac -d target\classes src\main\java\graph\common\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 2: Compiling model classes...
javac -cp target\classes -d target\classes src\main\java\graph\model\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 3: Compiling SCC classes...
javac -cp target\classes -d target\classes src\main\java\graph\scc\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 4: Compiling topological sort classes...
javac -cp target\classes -d target\classes src\main\java\graph\topo\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 5: Compiling DAG shortest path classes...
javac -cp target\classes -d target\classes src\main\java\graph\dagsp\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 6: Compiling utility classes...
javac -cp target\classes -d target\classes src\main\java\graph\util\*.java
if %errorlevel% neq 0 exit /b 1

echo Step 7: Compiling main class...
javac -cp target\classes -d target\classes src\main\java\graph\Main.java
if %errorlevel% neq 0 exit /b 1

echo.
echo ===================================================
echo ✓ COMPILATION SUCCESSFUL!
echo ===================================================
echo.
echo Running the application...
echo.
java -cp target\classes graph.Main
echo.
pause