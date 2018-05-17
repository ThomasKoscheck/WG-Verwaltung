#!/usr/bin python
# -*- coding: utf-8 -*-

import os
import subprocess
import sendMail

# get newest code
cloneRepo()

# read pid in file to kill actual server
file = open(“pid.txt”,”r”) 
oldPid = file.read() 
file.close() 
killStatement = 'kill '.join(oldPid)
os.system(killStatement)

# try to start new server
startServer()

# write pid in file for later use
file = open(“pid.txt”,”w”) 
file.write(pid) 
file.close() 

def startServer():
    try:
        proc = subprocess.Popen(['python', 'development/server.py', '&', '9998'], shell=True)
        pid = proc.pid # access `pid` attribute to get the pid of the child process.

    except Exception as e:
        print(str(e))


def cloneRepo():
    # clone current branch
    os.system('git clone --branch server-development https://github.com/ThomasKoscheck/WG-Verwaltung.git')

    # move and cleanup code
    os.system('mkdir development')
    os.system('mv WG-Verwaltung/Server/* development/')
    os.system('rm -rf WG-Verwaltung')
    

