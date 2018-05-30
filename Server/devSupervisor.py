#!/usr/bin python
# -*- coding: utf-8 -*-

import os
from subprocess import Popen
import sendMail
from datetime import date

PATH = '/path/to/your/server/folder'
PIDFILE = PATH + "/pid.txt"

def readFile(filePath):
    try:
        file = open(filePath, "r") 
        data = file.read() 
        file.close() 
        return data
    except Exception as e:
        print(e)

def writeFile(filePath, content):
    try:
        file = open(filePath, "w")
        file.write(str(content))
        file.close()
    except Exception as e:
        print(e)

def startServer():
    try:
        pathToFile = PATH + '/development/server.py'
        proc = Popen(['python', pathToFile, '9998 &'], close_fds=True)
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
        pidFile = open(PIDFILE, "r") 
        oldPid = pidFile.read() 
        pidFile.close() 

        # kill runnng server
        killStatement = 'kill ' +  str(oldPid)
        os.system(killStatement)

    # always breaks on first run, because no old devServer exists
    except Exception as e:
	    print(e)
        pass


def cloneRepo():
    try:
        os.system('rm -rf ' + PATH + '/development/')
        
        # clone current branch
        git_command = 'git clone --quiet --branch server-development https://github.com/ThomasKoscheck/WG-Verwaltung.git ' + PATH + '/WG-Verwaltung/'
        os.system(git_command)
        
        # move and cleanup code
        os.system('mkdir -p ' + PATH + '/development/')
        mv_command = 'mv ' + PATH + '/WG-Verwaltung/Server/* ' + PATH +'/development/'
        os.system(mv_command)
        os.system('rm -rf ' + PATH + '/WG-Verwaltung/')

    except Exception as e:
        print(e)

def manipulateFiles():
    try:
        # exchange path to config.ini in credentials.py
        filedata_credentials = readFile(PATH + '/development/credentials.py')
        filedata_credentials = filedata_credentials.replace('/path-to-config-ini-file', '/path/to/your/config/file')
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
