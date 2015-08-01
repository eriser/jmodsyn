cd C:\dev\vs\Common7\Tools\
CALL vsvars32.bat
cd C:\dev\workspaces\modsyn\Abnormal\src-native

cl -I"C:\Program Files\Java\jdk1.8.0_05\include" -I"C:\Program Files\Java\jdk1.8.0_05\include\win32" -LD "org_modsyn_abnormal_WinAbnormalSupport.c" -Fe"abnormal32.dll" 

cd C:\dev\vs\VC\bin\x86_amd64
CALL vcvarsx86_amd64.bat
cd C:\dev\workspaces\modsyn\Abnormal\src-native

cl -I"C:\Program Files\Java\jdk1.8.0_05\include" -I"C:\Program Files\Java\jdk1.8.0_05\include\win32" -LD "org_modsyn_abnormal_WinAbnormalSupport.c" -Fe"abnormal64.dll" 

pause