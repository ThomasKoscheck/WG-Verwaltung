#!/usr/bin python
# -*- coding: utf-8 -*-

import json
from datetime import date

import bcolors
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
    try:
        jsonstring = ""
        json = ""

        jsonstring += '{ "requester":"TestL",' + \
                '"product":"Salz",' + \
                '"price":"0.5",' +\
                '"date":"25.03.1998"' + \
                "},"

        jsonstring = jsonstring[:-1]

        # Now print fetched result
        print(bcolors.color.OKBLUE + "--- Builded this json --- \n" + bcolors.color.ENDC + jsonstring + "\n")

        json = "{" + \
            '"credit":"' + str(sqlHandler.getCreditSQL()) + '",'  + \
            '"newestAppVersion":"' + str(7) + '",' + \
            '"expenses":[' + jsonstring + \
            "]}"

        return json

    except Exception as e:
        print(bcolors.color.FAIL+ str(e) +bcolors.color.ENDC)
        print(bcolors.color.FAIL + "Error: unable to fetch data or build json" + bcolors.color.ENDC)
