#!/usr/bin python
# -*- coding: utf-8 -*-

import os
import subprocess
import sendMail
from datetime import date
import fileinput

PATH = "/var/www/thomaskoscheck.de/public_html/projekte/wg-verwaltung/server"
PIDFILE = PATH + "/pid.txt"

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
        proc = subprocess.Popen(['python', PATH + '/development/server.py', '9998'], close_fds=True)
        pid = proc.pid # access `pid` attribute to get the pid of the child process.
        return pid

    except Exception as e:
        print(str(e))
        sendMail.sendError("Error starting server on development branch", str(e) + "\nTime: "  + str(date.today()))
        return -1

def killOldServer():  
    try:
        # read pid in file
        # I do nor reuse readFile(), because I dont want exception handling 
        file = open(PIDFILE, "r") 
        oldPid = file.read() 
        file.close() 

        # kill running server
        killStatement = 'kill '.join(oldPid)
        os.system(killStatement)

    # always breaks on first run, because no old devServer exists
    except Exception:
        pass


def cloneRepo():
    try:
        # clone current branch
        os.system('git clone --branch server-development https://github.com/ThomasKoscheck/WG-Verwaltung.git')

        # move and cleanup code
        os.system('mkdir -p ' + PATH + '/development')
        os.system('mv ' + PATH + '/WG-Verwaltung/Server/* ' + PATH +'/development/')
        os.system('rm -rf ' + PATH + '/WG-Verwaltung')

    except Exception as e:
        print(e)

def manipulateFiles():
    try:
        # exchange path to config.ini in credentials.py
        filedata_credentials = readFile(PATH + '/development/credentials.py')
        filedata_credentials = filedata_credentials.replace('/path-to-config-ini-file', '/var/www/thomaskoscheck.de/credentials/config.ini')
        writeFile(PATH +"/development/credentials.py", filedata_credentials)

        # exchange database calling no.1
        filedate_sqlHandler = readFile(PATH + '/development/sqlHandler.py')
        filedate_sqlHandler = filedate_sqlHandler.replace('credentials.getDBTABLE()', 'credentials.getDBTABLEDEV()')
        writeFile(PATH + '/development/sqlHandler.py', filedate_sqlHandler)  
        # exchange database calling no.2
        filedate_sqlHandler = readFile(PATH + '/development/jsonHandler.py')
        filedate_sqlHandler = filedate_sqlHandler.replace('credentials.getDBTABLE()', 'credentials.getDBTABLEDEV()')
        writeFile(PATH + '/development/jsonHandler.py', filedate_sqlHandler)  

    except Exception as e:
        print(e)
          


# get newest code
cloneRepo()

# kill old Server
killOldServer()

# change values to dev values
manipulateFiles()

# try to start new server
newPid = startServer()

# write pid in file for later use
writeFile(PIDFILE, newPid)


    

