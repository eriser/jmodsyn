set PATH=C:\MinGW\bin\;%PATH%
gcc -v -o abnormal32.dll -shared -I"C:\Program Files (x86)\Java\jdk1.7.0_65\include" -I"C:\Program Files (x86)\Java\jdk1.7.0_65\include\win32" org_modsyn_abnormal_WinAbnormalSupport.c -Wl,--kill-at
pause