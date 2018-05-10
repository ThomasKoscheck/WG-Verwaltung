#!/usr/bin python
# -*- coding: utf-8 -*-

import socket
import json
from datetime import date
from time import sleep
import MySQLdb
from six.moves import configparser
from Crypto import Random
from Crypto.Cipher import AES
from base64 import b64encode, b64decode
from os import urandom

BLOCK_SIZE=16

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

def listen():
    connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    connection.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    connection.bind(('0.0.0.0', 9999))
    connection.listen(10)
    while True:
        current_connection, address = connection.accept()
        while True:
            datalength = current_connection.recv(6)
            print("recieving: " + datalength)
            
            # generate IV for AES encryption
            random_bytes = urandom(16)
            IV = b64encode(random_bytes).decode('utf-8')[:17] # because of some strange things it only things
            current_connection.send(IV)

            IV = IV[:16]
                     
            print(bcolors.HEADER + "--- Generated IV for AES ---\n" + bcolors.ENDC + IV) 


            try:   
                # getting value of the stream that will be send from client           
                data = current_connection.recv(int(datalength))
                print(data)

                # decrypting the data
                dbhost, dbuser, dbpass, dbname, dbtable, appVersion, auth = getLoginCredentials()
                print(data + "\n")
                data = decrypt(data, auth, IV)

                print(bcolors.OKBLUE + "--- Got this data ---\n" + bcolors.ENDC  + data + "\n")

                if data.endswith("getServerData"):
                    
                    json = buildJSON()
                    current_connection.send(json)
                    print(bcolors.OKGREEN + "--- Sent data to server ---\n" + bcolors.ENDC)

                    current_connection.shutdown(1)
                    current_connection.close() 
                    print(bcolors.WARNING + "--- Closed connection ---\n" + bcolors.ENDC)
                    break 


                else:
                    print(bcolors.OKBLUE + "--- Client sent this data ---\n" + bcolors.ENDC + data + "\n")

                    product, requester, price, dates, done = parseJSON(data) # getting parsed data from json send from app
                    credit = getCreditSQL() # get the actual credit
                    credit -= price # new credit
                    insertIntoSQL(credit, product, requester, price, dates, done)   # inserting date into SQL database

                    current_connection.shutdown(1)
                    current_connection.close()
                    print(bcolors.WARNING + "--- Closed connection ---\n" + bcolors.ENDC)
                    break

            except Exception as e:    
                print(bcolors.FAIL + str(e) + bcolors.ENDC)
                print(bcolors.FAIL + "Either the data is empty or an other error occured getting data" + bcolors.ENDC)
                current_connection.shutdown(1)
                current_connection.close()
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
        print(bcolors.FAIL + str(e) + bcolors.ENDC)
        print(bcolors.FAIL + "Error parsing json" + bcolors.ENDC)
    
def insertIntoSQL(credit, product, requester, price, dates, done):  
    # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion, auth = getLoginCredentials()

    # Connect to MariaDB  
    # Open database connection
    db = MySQLdb.connect('%s' % dbhost, '%s' % dbuser, '%s' %dbpass, '%s' %dbname)

    # prepare a cursor object using cursor() method
    cursor = db.cursor()

    # Prepare SQL query to INSERT a record into the database.
    sql = "INSERT INTO %s (credit, product, requester, price, date, done) \
       VALUES ('%f', '%s', '%s', '%f', '%s', '%i' )" % \
       (dbtable, credit, product, requester, price, dates, done)

    try:
        # Execute the SQL command
        cursor.execute(sql)
        # Commit your changes in the database
        db.commit()

        print(bcolors.OKGREEN + "--- Inserted new entry in database ---\n" + bcolors.ENDC)

    except Exception as e:
        print(bcolors.FAIL+ str(e) +bcolors.ENDC)
        # Rollback in case there is any error
        db.rollback()

    # disconnect from server
    db.close()

def getCreditSQL():
    # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion, auth= getLoginCredentials()
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
        print(bcolors.FAIL + str(e)  + bcolors.ENDC)
        print("bcolors.FAIL + Error: unable to fetch current credit "+ bcolors.ENDC)

    # disconnect from server
    db.close()

    return credit

def buildJSON():
     # get login credentials from outside of webroot
    dbhost, dbuser, dbpass, dbname, dbtable, appVersion, auth = getLoginCredentials()

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
        print(bcolors.OKBLUE + "--- Builded this json --- \n" + bcolors.ENDC + jsonstring + "\n")

        json = "{" + \
            '"credit":"' + str(getCreditSQL()) + '",'  + \
            '"newestAppVersion":"' + str(appVersion) + '",' + \
            '"expenses":[' + jsonstring + \
            "]}"

        return json

    except Exception as e:
        print(bcolors.FAIL+ str(e) +bcolors.ENDC)
        print(bcolors.FAIL + "Error: unable to fetch data or build json" + bcolors.ENDC)

    # disconnect from server
    db.close()

def encrypt(message, passphrase, IV):
    # passphrase MUST be 16, 24 or 32 bytes long, how can I do that ?  
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    print(bcolors.HEADER + "--- Enrypted the data succesfully ---\n" + bcolors.ENDC)
    return b64encode(aes.encrypt(message))

def decrypt(encrypted, passphrase, IV):
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    print(bcolors.HEADER + "--- Decrypted the data succesfully ---\n" + bcolors.ENDC)
    print(b64decode(encrypted))
    print("aes mit dcode" + aes.decrypt(b64decode(encrypted)))
    return aes.decrypt(encrypted)
    
def getLoginCredentials():
    try:
        # get login credentials from outside of webroot
        config = configparser.ConfigParser()
        config.read('/path/to/config/file')
        dbpass = config.get('project-wg-verwaltung','wg_pw')
        dbuser = config.get('project-wg-verwaltung','wg_user')
        dbname = config.get('project-wg-verwaltung','wg_dbname')
        dbtable = config.get('project-wg-verwaltung','wg_dbtable')
        appVersion = config.get('project-wg-verwaltung','wg_app_version')
        auth = config.get('project-wg-verwaltung','wg_auth')
        dbhost = "localhost"

    except Exception as e:
        print(bcolors.FAIL + str(e)  + bcolors.ENDC)
        print(bcolors.FAIL + "Error getting values from config file" + bcolors.ENDC)   

    return dbhost, dbuser, dbpass, dbname, dbtable, appVersion, auth


if __name__ == "__main__":
    try:
        print(bcolors.OKGREEN + "--- Starting Server ---\n" + bcolors.ENDC)
        listen()
    except KeyboardInterrupt:
        pass