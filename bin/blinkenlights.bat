@echo off
setlocal

set CLASSPATH=../classes;.

set JAVA_HOME=c:\devenv\jdk1.3.1_01
%JAVA_HOME%\bin\java ajmas74.experiments.BlinkenLightsPlayer %1

