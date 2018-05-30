#!/usr/bin python
# -*- coding: utf-8 -*-

import json
from datetime import date
import MySQLdb
import sys

import bcolors
import credentials
import sqlHandler

def parseJSON(data):
    ''' Parsing important informations out of the given json string '''
    try:
        jsonData = json.loads(data)
        sqlId = jsonData['id']
        product = jsonData['product']
        requester = jsonData['requester']
        price = jsonData['price']
        dates = str(date.today())
        done = jsonData['done']

        return sqlId, product, requester, price, dates, done

    except Exception as e:
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error parsing json" + bcolors.color.ENDC)

def buildJSON():
    reload(sys)
    sys.setdefaultencoding("utf-8")

    # get login credentials from outside of webroot
    dbhost = credentials.getDBHOST()
    dbuser = credentials.getDBUSER()
    dbpass = credentials.getDBPASS()
    dbname = credentials.getDBNAME()
    dbtable = credentials.getDBTABLE()

    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname, use_unicode=True, charset="utf8")

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    sql = "SELECT id, product, requester, price, date FROM %s \
       WHERE done=0" % (dbtable)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Fetch all the rows in a list of lists.
        results = cursor.fetchall()
        
        jsonstring = ""
        json = ""

        for row in results:
            jsonstring += '{"id":"' + str(row[0]).encode('utf-8') + '",' + \
                    '"product":"' + str(row[1]).encode('utf-8') + '",' + \
                    '"requester":"' + str(row[2]).encode('utf-8') + '",' + \
                    '"price":"' + str(row[3]).encode('utf-8') + '",' +\
                    '"date":"' + str(row[4]).encode('utf-8') + '"' + \
                    "},"

        jsonstring = jsonstring[:-1]

        json = "{" + \
            '"credit":"' + str(sqlHandler.getCreditSQL()).encode('utf-8') + '",'  + \
            '"expenses":[' + jsonstring + \
            "]}"

        
        # Now print fetched result
        print(bcolors.color.OKBLUE + "--- Builded this json --- \n" + bcolors.color.ENDC + json + "\n")

        return json

    except Exception as e:
        print(bcolors.color.FAIL+ str(e) +bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error: unable to fetch data or build json" + bcolors.color.ENDC)
        return -1
        
    # disconnect from server
    db.close()   