@echo off
rem create by qinjp(3373616885@qq.com)
rem crazy coder
  
rem ����д��Ĳֿ�·��
set REPOSITORY_PATH=D:\Maven\repository
rem ��������...
for /f "delims=" %%i in ('dir /b /s "%REPOSITORY_PATH%\*lastUpdated*"') do (
    del /s /q %%i
)
rem �������
pause