#!/usr/bin python
# -*- coding: utf-8 -*-

import socket
import json
from datetime import date
from time import sleep
import MySQLdb
from six.moves import configparser

def listen():
    connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connection.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    connection.bind(('0.0.0.0', 9999))
    connection.listen(10)
    while True:
        current_connection, address = connection.accept()
        while True:
            data = current_connection.recv(2048)

            if data == "getServerData":
                json = buildJSON()
                current_connection.send(json)
                print("--- Send Data to Server ---")

            if data == "quit”":
                current_connection.shutdown(1)
                current_connection.close()
                print("--- Closing Connection because got 'quit' ---")

            else:
                print("Client send:\n" + data)

                product, requester, price, dates, done = parseJSON(data) # getting parsed data from json send from app
                credit = getCreditSQL() # get the actual credit
                credit -= price #new credit
                insertIntoSQL(credit, product, requester, price, dates, done)   # inserting date into SQL database

                current_connection.shutdown(1)
                current_connection.close()
                print("--- Closing Connection ---")
                break
          
            sleep(1)

def parseJSON(data):
    try:
        jsonData = json.loads(data)
        product = jsonData['product']
        requester = jsonData['requester']
        price = jsonData['price']
        dates = str(date.today())
        done = 0

        return product, requester, price, dates, done
         
    except Exception as e:
        print(e)
        print("Error parsing json")
    
def insertIntoSQL(credit, product, requester, price, dates, done):  
    # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion = getLoginCredentials()

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname)

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    # Prepare SQL query to INSERT a record into the database.
    sql = "INSERT INTO %s (credit, product, requester, price, date, done) \
       VALUES ('%d', '%s', '%s', '%d', '%s', '%i' )" % \
       (dbtable, credit, product, requester, price, dates, done)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Commit your changes in the database
        db.commit()
    except Exception as e:
        print(e)
        # Rollback in case there is any error
        db.rollback()

    # disconnect from server
    db.close()

def getCreditSQL():
    # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion = getLoginCredentials()
    credit = -9999

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname)

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    sql = "SELECT credit FROM %s \
        ORDER BY id DESC LIMIT 1" % (dbtable)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Fetch all the rows in a list of lists.
        results = cursor.fetchall()
        for row in results:
            credit = row[0]


    except Exception as e:
        print(e)
        print "Error: unable to fetch data"

    # disconnect from server
    db.close()

    return credit

def buildJSON():
     # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion = getLoginCredentials()

    # Open database connection
    db = MySQLdb.connect(dbhost, dbuser, dbpass, dbname)

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    sql = "SELECT product, requester, price, date FROM '%s' \
       WHERE done=0" % (dbtable)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Fetch all the rows in a list of lists.
        results = cursor.fetchall()
        jsonstring = ""
        for row in results:
            jsonstring += '{ "requester":"' + row[0] + '",' + \
                    '"product":"' + row[1] + '",' + \
                    '"price":"' + row[2] + '",' +\
                    '"date":"' + row[3] + '"' + \
                    "},"

        jsonstring = jsonstring[:-1]

        # Now print fetched result
        print(jsonstring)

        json = "{" + \
            '"credit":"' + getCreditSQL() + '",'  + \
            '"newestAppVersion":"' . appVersion + '",' + \
            '"expenses":[' + jsonstring + \
            "]}"

    except:
        print "Error: unable to fetch data"

    # disconnect from server
    db.close()

    return json
    
def getLoginCredentials():
    # get login credentials from outside of webroot
    config = configparser.ConfigParser()
    config.read('/path-to-config.ini')
    dbpass = config.get('project-wg-verwaltung','wg_pw')
    dbuser = config.get('project-wg-verwaltung','wg_user')
    dbname = config.get('project-wg-verwaltung','wg_dbname')
    dbtable = config.get('project-wg-verwaltung','wg_dbtable')
    appVersion = config.get('project-wg-verwaltung','wg_app_version')
    dbhost = "localhost"

    return dbhost, dbuser, dbpass, dbname, dbtable, appVersion


if __name__ == "__main__":
    try:
        print("--- Starting Server ---")
        listen()
    except KeyboardInterrupt:
        pass