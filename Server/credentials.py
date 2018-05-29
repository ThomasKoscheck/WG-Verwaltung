#!/usr/bin python
# -*- coding: utf-8 -*-

from six.moves import configparser
import bcolors

def configHandler():
    try:
        # get login credentials from outside of webroot
        config = configparser.ConfigParser()
        config.read('/path-to-config-ini-file')
        return config

    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading from config file" + bcolors.color.ENDC)   
        return -1
        
def getDBPASS():
    try:
        handler = configHandler()
        dbpass = handler.get('project-wg-verwaltung','wg_pw')
        return dbpass
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dbpass from config file" + bcolors.color.ENDC)   
        return -1
    
def getDBUSER():
    try:
        handler = configHandler()
        dbuser = handler.get('project-wg-verwaltung','wg_user')
        return dbuser
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dbuser from config file" + bcolors.color.ENDC)   
        return -1

def getDBNAME():
    try:
        handler = configHandler()
        dbname = handler.get('project-wg-verwaltung','wg_dbname')
        return dbname
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dbname from config file" + bcolors.color.ENDC)   
        return -1

def getDBTABLE():
    try:
        handler = configHandler()
        dbtable = handler.get('project-wg-verwaltung','wg_dbtable')
        return dbtable
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dbtable from config file" + bcolors.color.ENDC)   
        return -1
    
def getDBTABLEDEV():
    try:
        handler = configHandler()
        dbtable = handler.get('project-wg-verwaltung','wg_dbtable_dev')
        return dbtable
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dev dbtable from config file" + bcolors.color.ENDC)   
        return -1

def getAPPVERSION():
    try:
        handler = configHandler()
        appVersion = handler.get('project-wg-verwaltung','wg_app_version')
        return appVersion
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading appVersion from config file" + bcolors.color.ENDC)   
        return -1
     
def getAUTH():
    try:
        handler = configHandler()
        auth = handler.get('project-wg-verwaltung','wg_auth')
        return auth
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading auth from config file" + bcolors.color.ENDC)   
        return -1    

def getDBHOST():
    try:
        handler = configHandler()
        dbhost = handler.get('project-wg-verwaltung','wg_host')
        return dbhost
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading dbhost from config file" + bcolors.color.ENDC)   
        return -1   

def getFROMADDR():
    try:
        handler = configHandler()
        fromaddr = handler.get('project-wg-verwaltung','from_addr')
        return fromaddr
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading from_addr from config file" + bcolors.color.ENDC)   
        return -1  

def getTOADDR():
    try:
        handler = configHandler()
        toaddr = handler.get('project-wg-verwaltung','to_addr')
        return toaddr
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading toaddr from config file" + bcolors.color.ENDC)   
        return -1   

def getSMTP():
    try:
        handler = configHandler()
        smtp = handler.get('project-wg-verwaltung','smtp')
        return smtp
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading smtp from config file" + bcolors.color.ENDC)   
        return -1  

def getSMTPPORT():
    try:
        handler = configHandler()
        smtp_port = handler.get('project-wg-verwaltung','smtp_port')
        return smtp_port
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading smtpport from config file" + bcolors.color.ENDC)   
        return -1  

def getMAILPASSWORD():
    try:
        handler = configHandler()
        mail_pw = handler.get('project-wg-verwaltung','mail_pw')
        return mail_pw
        
    except Exception as e:
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error reading mail password from config file" + bcolors.color.ENDC)   
        return -1  
 

          
        
      
