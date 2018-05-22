#!/usr/bin python
# -*- coding: utf-8 -*-

import os
import subprocess
import sendMail
from datetime import date
import fileinput

PIDFILE = "pid.txt"

def readFile(file):
    try:
        file = open(file, "r") 
        data = file.read() 
        file.close() 
        return data
    except Exception as e:
        print(e)

def writeFile(file, content):
    try:
        file = open(file, "w")
        file.write(str(content))
        file.close()
    except Exception as e:
        print(e)

def startServer():
    # write shell script with later gets executes
    # dirty but efficient
    try:
        proc = subprocess.Popen(['python', 'development/server.py', '9998'], close_fds=True)
        pid = proc.pid # access `pid` attribute to get the pid of the child process.
        return pid

    except Exception as e:
        print(str(e))
        sendMail.sendError("Error starting server on development branch", str(e) + "\nTime: "  + str(date.today()))
        return -1


def cloneRepo():
    try:
        # clone current branch
        os.system('git clone --branch server-development https://github.com/ThomasKoscheck/WG-Verwaltung.git')

        # move and cleanup code
        os.system('mkdir -p development')
        os.system('mv WG-Verwaltung/Server/ development/')
        os.system('rm -rf WG-Verwaltung')

    except Exception as e:
        print(e)


# get newest code
cloneRepo()

# read pid in file 
oldPid = readFile(PIDFILE)

if oldPid is not None: # is None on first start
   try:
        # kill running server
        killStatement = 'kill '.join(oldPid)
        os.system(killStatement)
   except Exceptio as e:
        print(e)

# exchange path to config.ini in credentials.py
filedata = readFile("development/credentials.py")

# Tmanipulate file and exchange path
# Replace the target string
filedata = filedata.replace('/path-to-config-ini-file', '/var/www/thomaskoscheck.de/credentials/config.ini')
writeFile("development/credentials.py", filedata)

# try to start new server
newPid = startServer()

# write pid in file for later use
writeFile(PIDFILE, newPid)


    

