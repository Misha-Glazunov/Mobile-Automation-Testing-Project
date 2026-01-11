@echo off
echo Компиляция тестов...
cd C:\Users\mishg\IdeaProjects\Testing
call mvn compile

echo Запуск WorkingTest...
call mvn test -Dtest=WorkingTest
pause
