title  fwpro
@echo off


set PROJECT=fwpro
set CURRENT_PATH=%cd%
set COMMON_PATH=%CURRENT_PATH%\fw-common
set DAO_PATH=%CURRENT_PATH%\fw-dao
set SERVICE_PATH=%CURRENT_PATH%\fw-service
set ADMIN_PATH=%CURRENT_PATH%\fw-admin
set WEB_PATH=%CURRENT_PATH%\fw-web
s
echo ++++++++++++++++current path %CURRENT_PATH% ++++++++++++++++++


echo ++++++++++++++++mvn install start++++++++++++++++
cd %CURRENT_PATH%
call mvn clean install
echo ++++++++++++++++mvn install end++++++++++++++++

echo ++++++++++++++++delete target start++++++++++++++++
rd /s /q "%COMMON_PATH%\target"
rd /s /q "%DAO_PATH%\target"
rd /s /q "%SERVICE_PATH%\target"
rd /s /q "%ADMIN_PATH%\target"
rd /s /q "%WEB_PATH%\target"



echo ++++++++++++++++delete target end++++++++++++++++

echo ok!
pause