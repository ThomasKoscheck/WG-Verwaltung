#!/usr/bin python
# -*- coding: utf-8 -*-

import MySQLdb

import bcolors
import credentials

def insertIntoSQL(credit, product, requester, price, dates, done):  
    # get login credentials from outside of webroot
    dbhost = credentials.getDBHOST()
    dbuser = credentials.getDBUSER()
    dbpass = credentials.getDBPASS()
    dbname = credentials.getDBNAME()
    dbtable = credentials.getDBTABLE()

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname, use_unicode=True, charset="utf8")

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    # Prepare SQL query to INSERT a record into the database.
    sql = 'INSERT INTO %s (credit, product, requester, price, date, done)' % (dbtable)
    sql = sql + ' VALUES (%s, %s, %s, %s, %s, %s)'
    
    try:
        # Execute the SQL command
        cursor.execute(sql, (credit, product, requester, price, dates, done))
        # Commit your changes in the database
        db.commit()

        print(bcolors.color.OKGREEN + "--- Inserted new entry in database ---\n" + bcolors.color.ENDC)
        return 1
        

    except Exception as e:
        print(bcolors.color.FAIL+ str(e) +bcolors.color.ENDC)
        # Rollback in case there is any error
        db.rollback()
        return -1

    # disconnect from server
    db.close()

def updateSQL(sqlId):  
    # get login credentials from outside of webroot
    dbhost = credentials.getDBHOST()
    dbuser = credentials.getDBUSER()
    dbpass = credentials.getDBPASS()
    dbname = credentials.getDBNAME()
    dbtable = credentials.getDBTABLE()

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname, use_unicode=True, charset="utf8")

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    # Prepare SQL query to INSERT a record into the database.
    sql = 'UPDATE %s SET done = 1' % (dbtable)
    sql = sql + ' WHERE (id = %s)'
    
    try:
        # Execute the SQL command
        cursor.execute(sql, (sqlId,))
        # Commit your changes in the database
        db.commit()

        print(bcolors.color.OKGREEN + "--- Updated entry in database ---\n" + bcolors.color.ENDC)

    except Exception as e:
        print(bcolors.color.FAIL+ str(e) +bcolors.color.ENDC)
        # Rollback in case there is any error
        db.rollback()

    # disconnect from server
    db.close()

def getCreditSQL():
    # get login credentials from outside of webroot
    dbhost = credentials.getDBHOST()
    dbuser = credentials.getDBUSER()
    dbpass = credentials.getDBPASS()
    dbname = credentials.getDBNAME()
    dbtable = credentials.getDBTABLE()
    credit = -9999

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname, use_unicode=True, charset="utf8")

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
        print(bcolors.color.FAIL + str(e)  + bcolors.color.ENDC)
        print("bcolors.color.FAIL + Error: unable to fetch current credit "+ bcolors.color.ENDC)

    # disconnect from server
    db.close()

    return credit