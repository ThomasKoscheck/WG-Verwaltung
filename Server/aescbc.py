#!/usr/bin python
# -*- coding: utf-8 -*-

from Crypto import Random
from Crypto.Cipher import AES
from base64 import b64encode, b64decode
from os import urandom
import bcolors

def generatePadding(data):
    while len(data)%16 != 0:
        data += "?"  
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
    print(bcolors.color.HEADER + "--- Encrypted the data succesfully ---\n" + bcolors.color.ENDC)
    encrypted = b64encode(aes.encrypt(message))
    return encrypted

def decrypt(encrypted, passphrase, IV):
    print("Key: " + passphrase)
    print(bcolors.color.HEADER + "--- Decrypted the data succesfully ---\n" + bcolors.color.ENDC)
    decrypted = aes.decrypt(b64decode(encrypted))
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
