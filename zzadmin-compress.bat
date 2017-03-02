title  fwpro
@echo off


set PROJECT=fwpro
set PREFIX=fw
set CURRENT_PATH=%cd%
set COMMON_PATH=%CURRENT_PATH%\%PREFIX%-common
set DEVICE_PATH=%CURRENT_PATH%\%PREFIX%-dao
set SERVICE_PATH=%CURRENT_PATH%\%PREFIX%-service
set ADMIN_PATH=%CURRENT_PATH%\\%PREFIX%-admin
echo ++++++++++++++++current path %CURRENT_PATH% ++++++++++++++++++


echo ++++++++++++++++mvn install start++++++++++++++++
cd %CURRENT_PATH%
call mvn clean install -e
echo ++++++++++++++++mvn install end++++++++++++++++

echo ++++++++++++++++copy war file start++++++++++++++++++
xcopy /y "%ADMIN_PATH%\target\fw-admin.war" "%CURRENT_PATH%"
xcopy /y "%DEVICE_PATH%\target\fw-dao-1.0.jar" "%CURRENT_PATH%"
xcopy /y "%SERVICE_PATH%\target\fw-service-1.0.jar" "%CURRENT_PATH%"
xcopy /y "%COMMON_PATH%\target\fw-common-1.0.jar" "%CURRENT_PATH%"
echo ++++++++++++++++copy war file end++++++++++++++++++++

echo ++++++++++++++++delete target start++++++++++++++++

rd /s /q "%COMMON_PATH%\target"
rd /s /q "%DEVICE_PATH%\target"
rd /s /q "%SERVICE_PATH%\target"
rd /s /q "%ADMIN_PATH%\target"

echo ++++++++++++++++delete target end++++++++++++++++

echo ok!
pause