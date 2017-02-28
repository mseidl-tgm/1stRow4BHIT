import socket
from enum import Enum
import sys
import time

fielddata = 0
bomb = 0
castle_own = 0
castle_other = 0

def rec_fields(clientsocket):
    data = clientsocket.recv(1024).decode()
    global fielddata
    fielddata = data
    if not data:
        print("Connection closed")
        return True
    if len(data) == 50:
        print(data[0:10])
        print(data[10:20])
        print(data[20:30])
        print(data[30:40])
        print(data[40:50])
    elif len(data) == 18:
        print(data[0:6])
        print(data[6:12])
        print(data[12:18])
    elif len(data) == 98:
        print(data[0:14])
        print(data[14:28])
        print(data[28:42])
        print(data[42:56])
        print(data[56:70])
        print(data[70:84])
        print(data[84:98])
    else:
        # Lose / Win
        print(data)
        return True
    return False

class CommandType(Enum):
    UP = "up"
    RIGHT = "right"
    DOWN = "down"
    LEFT = "left"

def goRight():
    clientsocket.send(CommandType.RIGHT.value.encode())
    pass

def goLeft():
    clientsocket.send(CommandType.LEFT.value.encode())
    pass

def goUp():
    clientsocket.send(CommandType.UP.value.encode())
    pass

def goDown():
    clientsocket.send(CommandType.DOWN.value.encode())
    pass

"""

Die folgenden Methoden waren für die das Erkennen der Spielelemente, welche aber leider nicht funktionstüchtig sind.
def bombInRange():
    for i, val in enumerate(data):
        if (val=="B"):
            return i
    return False

def castleInRange():
    #bug with own castle
    for i, val in enumerate(data):
        if (val=="C"):
            return i
    return False

"""


def doEverythingForMeSimple():
    i = 0
    j = 0
    fortfahren = True
    while(fortfahren):
        time.sleep(0.25)
        #ueberpruefen ob Lake am naechsten Feld
        if (len(fielddata)==18):
            print(fielddata[10])
            if (fielddata[10]=='L' and j!=4):
                    goUp()
                    j += 1
                    i -= 1
            else:
                # Jeden Durchlauf eins nach unten
                if (i >= 9):
                    if (fielddata[14] == 'L'):
                        goRight()
                        #i=-1
                    else:
                        goDown()
                        i = 0
                elif (j >= 4):
                    # wenn ausgewichen nach oben, dann wieder nach unten gehen
                    goDown()
                    i -= 1
                    j = 0
                else:
                    goRight()
        elif (len(fielddata)==50):
            print(fielddata[26])
            if (fielddata[26] == 'L' and j!=4):
                    goUp()
                    j += 1
                    i -= 1
            else:
                # Jeden Durchlauf eins nach unten
                if (i >= 9):
                    if (fielddata[34] == 'L'):
                        goRight()
                        #i = -1
                    else:
                        goDown()
                        i = 0
                elif (j >= 4):
                    # wenn ausgewichen nach oben, dann wieder nach unten gehen
                    goDown()
                    i -= 1
                    j = 0
                else:
                    goRight()
        elif (len(fielddata)==98):
            print(fielddata[50])
            if (fielddata[50] == 'L' and j!=4):
                    goUp()
                    j += 1
                    i -= 1
            else:
                # Jeden Durchlauf eins nach unten
                if (i >= 9):
                    if (fielddata[62] == 'L'):
                        goRight()
                        #i = -1
                    else:
                        goDown()
                        i = 0
                elif (j >= 4):
                    # wenn ausgewichen nach oben, dann wieder nach unten gehen
                    goDown()
                    i -= 1
                    j = 0
                else:
                    goRight()
        else:
            pass
        i += 1
        if (j>0):
            j += 1
        print("i: " + str(i))
        print("j: " + str(j))
        rec_fields(clientsocket)
        if not fielddata:
            fortfahren = False
            clientsocket.close()


with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as clientsocket:
    try:
        # Verbindung herstellen (Gegenpart: accept() )
        clientsocket.connect((sys.argv[1], int(sys.argv[2])))
        msg = input("Name?")
        # Nachricht schicken
        clientsocket.send(msg.encode())
        # Antwort empfangen
        data = clientsocket.recv(1024).decode()
        if not data or not data=="OK":
            # Schließen, falls Verbindung geschlossen wurde
            clientsocket.close()
        else:
            while True:
                if rec_fields(clientsocket):
                    break
                doEverythingForMeSimple()
    except socket.error as serr:
        print("Socket error: " + serr.strerror)
