#!/usr/bin python
# -*- coding: utf-8 -*-

import smtplib
from email.MIMEMultipart import MIMEMultipart
from email.MIMEText import MIMEText
import bcolors 
 
def send(credit, product, requester, price, dates, done): 
    try:
        fromaddr = "mailer@domain.com"
        toaddr = 'recipient@domain.com, recipient2@domain.com'
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
    
        server = smtplib.SMTP('smtp.domain.com', 587)
        server.starttls()
        server.login(fromaddr, "mailpassword")
        text = msg.as_string()
        server.sendmail(fromaddr, toaddr.split(','), text)
        server.quit()

        print(bcolors.color.OKGREEN + "--- Sent mail succesfully --- " + bcolors.color.ENDC + "\n")

    except Exception as e:
        print(bcolors.color.FAIL + str(e) + bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error sending mail" + bcolors.color.ENDC)
