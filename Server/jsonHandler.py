#!/usr/bin python
# -*- coding: utf-8 -*-

import json
from datetime import date
import MySQLdb

import bcolors
import credentials
import sqlHandler

def parseJSON(data):
    ''' Parsing important informations out of the given json string '''
    try:
        jsonData = json.loads(data)
        product = jsonData['product']
        requester = jsonData['requester']
        price = jsonData['price']
        dates = str(date.today())
        done = 0

        return product, requester, price, dates, done
         
    except Exception as e:
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error parsing json" + bcolors.color.ENDC)

def buildJSON():
    # get login credentials from outside of webroot
    dbhost = credentials.getDBHOST()
    dbuser = credentials.getDBUSER()
    dbpass = credentials.getDBPASS()
    dbname = credentials.getDBNAME()
    dbtable = credentials.getDBTABLE()
    appVersion = credentials.getAPPVERSION()

    # Open database connection
    db = MySQLdb.connect(dbhost, dbuser, dbpass, dbname)

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    sql = "SELECT product, requester, price, date FROM %s \
       WHERE done=0" % (dbtable)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Fetch all the rows in a list of lists.
        results = cursor.fetchall()
        
        jsonstring = ""
        json = ""

        for row in results:
            jsonstring += '{ "requester":"' + str(row[0]) + '",' + \
                    '"product":"' + str(row[1]) + '",' + \
                    '"price":"' + str(row[2]) + '",' +\
                    '"date":"' + str(row[3]) + '"' + \
                    "},"

        jsonstring = jsonstring[:-1]

        # Now print fetched result
        print(bcolors.color.OKBLUE + "--- Builded this json --- \n" + bcolors.color.ENDC + jsonstring + "\n")

        json = "{" + \
            '"credit":"' + str(sqlHandler.getCreditSQL()) + '",'  + \
            '"newestAppVersion":"' + str(appVersion) + '",' + \
            '"expenses":[' + jsonstring + \
            "]}"

        return json

    except Exception as e:
        print(bcolors.color.FAIL+ str(e) +bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error: unable to fetch data or build json" + bcolors.color.ENDC)

    # disconnect from server
    db.close()
