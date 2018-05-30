#!/usr/bin python
# -*- coding: utf-8 -*-

import socket
import sys

from time import sleep

import aescbc
import bcolors 
import credentials
import jsonHandler
import sqlHandler
import sendMail

AES_BLOCK_SIZE = 16

# pass port as argument
try:
    sys.argv[1]

except Exception as e:
    print(bcolors.color.FAIL + "--- You have to pass a port as argument (python server.py 9999) ---\n" + bcolors.color.ENDC + "\n")
    sys.exit() 

# uncomment print lines for better debugging    
def listen():
    try:  
        connection = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        connection.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        connection.bind(('0.0.0.0', int(sys.argv[1])))
        connection.listen(10)

        while True:
            current_connection, address = connection.accept()
            while True:            
                # generate IV for AES encryption
            
                IV = aescbc.generateIV(AES_BLOCK_SIZE) 
                current_connection.send(IV)

                IV = IV[:16] # because of some strange things 
                        
                #print(bcolors.color.HEADER + "--- Generated IV for AES ---\n" + bcolors.color.ENDC + IV + "\n") 

                datalength = current_connection.recv(6)
                # print(bcolors.color.HEADER + "--- Now receiving json of this datalength ---\n" + bcolors.color.ENDC + datalength + "\n") 
                  
                # getting value of the stream that will be send from client           
                data = current_connection.recv(int(datalength))

                #print(bcolors.color.OKBLUE + "--- Encryted string ---\n" + bcolors.color.ENDC  + data + "\n")                
                
                # decrypting the data
                auth = credentials.getAUTH()
                data = aescbc.decrypt(data, auth, IV)

                #print(bcolors.color.OKBLUE + "--- Got this data ---\n" + bcolors.color.ENDC  + data + "\n")

                if data.startswith("getServerData"):
                    json = jsonHandler.buildJSON()
                    jsonpad = aescbc.generatePadding(json)
                    jsonencrypt = aescbc.encrypt(jsonpad, auth, IV)
                    current_connection.send(jsonencrypt)
                    #print(bcolors.color.OKGREEN + "--- Sent this encrypted data to server ---\n" + bcolors.color.ENDC)

                    current_connection.shutdown(1)
                    current_connection.close() 
                    print(bcolors.color.WARNING + "--- Closed connection ---\n" + bcolors.color.ENDC)
                    break 


                else:
                    print(bcolors.color.OKBLUE + "--- Client sent this data ---\n" + bcolors.color.ENDC + data + "\n")

                    sqlId, product, requester, price, dates, done = jsonHandler.parseJSON(data) # getting parsed data from json send from app
                    credit = sqlHandler.getCreditSQL() # get the actual credit
                    credit -= price # new credit

                    if done is 0:
                        resultState = sqlHandler.insertIntoSQL(credit, product, requester, price, dates, done)   # inserting date into SQL database
                        if resultState == 1:   # send Email about entry only in case of success 
                            sendMail.send(credit, product, requester, price, dates, done) 

                    elif done is 1:
                        sqlHandler.updateSQL(sqlId)   # updating entry to done in SQL database

                    current_connection.shutdown(1)
                    current_connection.close()
                    print(bcolors.color.WARNING + "--- Closed connection ---\n" + bcolors.color.ENDC)
                    break
        sleep(1)


    except Exception as e:    
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Either the data is empty or an other error occured getting data" + bcolors.color.ENDC)
        current_connection.shutdown(1)
        current_connection.close()
          
    
if __name__ == "__main__":
    try:
        print(bcolors.color.OKGREEN + "--- Starting Server ---\n" + bcolors.color.ENDC)
        listen()
    except KeyboardInterrupt:
        pass
