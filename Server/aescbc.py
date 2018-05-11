#!/usr/bin python
# -*- coding: utf-8 -*-

from Crypto import Random
from Crypto.Cipher import AES
from base64 import b64encode, b64decode
from os import urandom

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
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    print("Key: " +passphrase)
    print(bcolors.HEADER + "--- Encrypted the data succesfully ---\n" + bcolors.ENDC)
    encrypted = b64encode(aes.encrypt(message))
    return encrypted

def decrypt(encrypted, passphrase, IV):
    aes = AES.new(passphrase, AES.MODE_CBC, IV)
    print("Key: " +passphrase)
    print(bcolors.HEADER + "--- Decrypted the data succesfully ---\n" + bcolors.ENDC)
    decrypted = aes.decrypt(b64decode(encrypted))
    return decrypted