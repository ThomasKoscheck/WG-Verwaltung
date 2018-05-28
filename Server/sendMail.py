#!/usr/bin python
# -*- coding: utf-8 -*-

import smtplib
from email.MIMEMultipart import MIMEMultipart
from email.MIMEText import MIMEText
import bcolors 
import credentials
 
def send(credit, product, requester, price, dates, done): 
    try:
        fromaddr = credentials.getFROMADDR()
        toaddr = credentials.getTOADDR()
        msg = MIMEMultipart()
        msg['From'] = fromaddr
        msg['To'] = toaddr
        msg['Subject'] = "New entry on WG-App"
    
        body = "New entry: " + str(product) + "\n" + \
        "Name: " + str(requester) + "\n" + \
        "Price: " + str(price) + "\n" + \
        "Date: " + str(dates) + "\n" + \
        "New limit: " + str(credit)

        msg.attach(MIMEText(body, 'plain'))
    
        server = smtplib.SMTP(credentials.getSMTP(), credentials.getSMTPPORT())
        server.starttls()
        server.login(fromaddr, credentials.getMAILPASSWORD())
        text = msg.as_string()
        server.sendmail(fromaddr, toaddr.split(','), text)
        server.quit()

        print(bcolors.color.OKGREEN + "--- Sent mail succesfully --- " + bcolors.color.ENDC + "\n")

    except Exception as e:
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error sending mail" + bcolors.color.ENDC)

def sendError(data):
    try:
        fromaddr = credentials.getFROMADDR()
        toaddr = credentials.getTOADDR()
        msg = MIMEMultipart()
        msg['From'] = fromaddr
        msg['To'] = toaddr
        msg['Subject'] = "New error on WG-App"
    
        body = data

        msg.attach(MIMEText(body, 'plain'))
    
        server = smtplib.SMTP(credentials.getSMTP(), credentials.getSMTPPORT())
        server.starttls()
        server.login(fromaddr, credentials.getMAILPASSWORD())
        text = msg.as_string()
        server.sendmail(fromaddr, toaddr.split(','), text)
        server.quit()

        print(bcolors.color.OKGREEN + "--- Sent mail succesfully --- " + bcolors.color.ENDC + "\n")

    except Exception as e:
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error sending mail" + bcolors.color.ENDC)
