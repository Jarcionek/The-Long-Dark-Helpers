@echo off
for /f "tokens=2 delims==" %%a in ('wmic OS Get localdatetime /value') do set "dt=%%a"
set "YYYY=%dt:~0,4%"
set "MM=%dt:~4,2%"
set "DD=%dt:~6,2%"
set "HH=%dt:~8,2%"
set "Min=%dt:~10,2%"
set "Sec=%dt:~12,2%"

set "timestamp=%YYYY%%MM%%DD%-%HH%%Min%%Sec%"
echo timestamp: "%timestamp%"

mkdir %timestamp%
mkdir "%timestamp%/TheLongDark"
mkdir "%timestamp%/TheLongDarkNoSync"

xcopy "C:\Users\%USERNAME%\AppData\Local\Hinterland\TheLongDark\user*" "%timestamp%/TheLongDark"
xcopy "C:\Users\%USERNAME%\AppData\Local\Hinterland\TheLongDarkNoSync\user*" "%timestamp%/TheLongDarkNoSync"

explorer %cd%
::pause
