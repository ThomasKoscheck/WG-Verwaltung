#!/usr/bin python
# -*- coding: utf-8 -*-

import os
import subprocess
import sendMail
from datetime import date

FILE = "pid.txt"

# get newest code
cloneRepo()

# read pid in file 
oldPid = readFile(FILE)

# kill running server
killStatement = 'kill '.join(oldPid)
os.system(killStatement)

# exchange path to config.ini in credentials.py
data = readFile("development/credentials.py")

# TO-DO manipulate file and exchange path


# try to start new server
newPid = startServer()

# write pid in file for later use
writeFile(FILE, newPid)

def readFile(file):
    file = open(file, "w") 
    data = file.read() 
    file.close() 
    return data

def writeFile(file, content):
    file = open(file, "r")
    file.write(content)
    file.close()

def startServer():
    try:
        proc = subprocess.Popen(['python', 'development/server.py', '&', '9998'], shell=True)
        pid = proc.pid # access `pid` attribute to get the pid of the child process.
        return pid

    except Exception as e:
        print(str(e))
        sendMail.sendError("Error starting server on development branch", str(e) + "\nTime: "  + str(date.today()))
        return -1


def cloneRepo():
    # clone current branch
    os.system('git clone --branch server-development https://github.com/ThomasKoscheck/WG-Verwaltung.git')

    # move and cleanup code
    os.system('mkdir development')
    os.system('mv WG-Verwaltung/Server/* development/')
    os.system('rm -rf WG-Verwaltung')
    

