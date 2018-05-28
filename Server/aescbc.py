#!/usr/bin python
# -*- coding: utf-8 -*-

from Crypto.Cipher import AES
from base64 import b64encode, b64decode
from os import urandom
import bcolors

def generatePadding(data):
    while len(data)%16 != 0:
        data += "?"  
    return data

def removePadding(data):
    while data.endswith('?'):
        data = data[:-1]
    return data

def generateIV(length):
    # generate IV for AES encryption
    random_bytes = urandom(length)
    IV = b64encode(random_bytes).decode('utf-8')[:17] # because of some strange things
    return IV     

def encrypt(message, passphrase, IV):
    # passphrase MUST be 16, 24 or 32 bytes long, how can I do that ?  
    passphrase = padPassphrase(passphrase)
    print("Key: " + passphrase)
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    encrypted = b64encode(aes.encrypt(message))
    print(bcolors.color.HEADER + "--- Encrypted the data succesfully ---\n" + bcolors.color.ENDC)
    return encrypted

def decrypt(encrypted, passphrase, IV):
    print("Key: " + passphrase)  
    passphrase = padPassphrase(passphrase)
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    decrypted = aes.decrypt(b64decode(encrypted))
    decrypted = removePadding(decrypted)
    print(bcolors.color.HEADER + "--- Decrypted the data succesfully ---\n" + bcolors.color.ENDC)
    return decrypted

def padPassphrase(passphrase):
    if len(passphrase) < 16:
        while(len(passphrase)!=16):
            passphrase += "?"
    elif len(passphrase) < 24:
        while(len(passphrase)!=24):
            passphrase += "?"
    elif len(passphrase) < 32:
        while(len(passphrase)!=32):
            passphrase += "?"
    else:
        print("passphrase too long")
    return passphrase
