from math import *
"""
Das Module Bruch implemntier ganz normale Bruchrechnung mit überladen von Operatoren
"""
class Bruch(object):


    def __init__(self, zaehler, nenner=None):
        """
        Der Konstruktor welcher die Attribute richtig setzt
        und auf gegebenfalls einen Bruch annimmt
        :param zaehler:
        :param nenner:
        """
        if(isinstance(zaehler, (Bruch))):
            self.zaehler = zaehler.zaehler
            self.nenner = zaehler.nenner
        elif(nenner == None):
            if (isinstance(zaehler, float)):
                raise TypeError
            self.zaehler = zaehler
            self.nenner = 1

        elif(nenner == 0):
            raise ZeroDivisionError

        elif(isinstance(nenner, (float))):
            raise TypeError
        else:
            self.zaehler = zaehler
            self.nenner = nenner



    def __float__(self):
        """
        Die überschriebende Methode float gibt den Bruch als Float zurück
        :return:
        """
        return (self.zaehler / self.nenner)

    def __int__(self):
        """
        Die überschriebende Methode int gibt den Bruch als INT (ohne Kommastellen) zurück
        :return:
        """
        return round(float(self))

    def __eq__(self, other):
        """
        Der überschriebene Operator == oder equals  gibt an ob zwei Brüche gleich sind oder nicht
        :param other:
        :return:
        """
        if isinstance(self,(Bruch,int,float)) and isinstance(other,(Bruch,int,float)):
            return float(self) == float(other)

    def __invert__(self):
        """
        Der überschriebene Operator invert gibt den Nenner mit dem Zähler vertauscht zurück
        :return:
        """
        z = self.nenner
        n = self.zaehler
        b = Bruch(z,n)
        return b

    def __str__(self):
        """
        Die überschriebene toString-Methode gibt die Brüche in gewünschter Form aus
        :return:
        """
        if (self.zaehler < 0) and (self.nenner < 0):
            return "(" + str(self.zaehler * -1) + "/" + str(self.nenner * -1) + ")"
        elif self.nenner == 1:
            return "(" + str(self.zaehler) + ")"
        return "(" + str(self.zaehler) + "/" + str(self.nenner) + ")"

    def __pow__(self,power, modulo=None):
        """
        Der überladene Operator pow gibt nimmt eine Hochzahl entgegen und gibt den Bruch^dem Exponent zurück
        :param power:
        :param modulo:
        :return:
        """
        b = Bruch(self.zaehler ** power, self.nenner ** power)
        return b

    def _Bruch__makeBruch(value):
        """
        Die Methode makeBruch erstellt einen Bruch aus einem value und gibt diesen zurück
        :return:
        """
        if(isinstance(value,(float,str))):
            raise TypeError
        b = Bruch(value)
        return b

    def __abs__(self):
        """
        Die überschriebene Methode abs (Betrag) errechnet sich den Betrag und gibt diesen zurück
        :return:
        """
        self.zaehler = abs(self.zaehler)
        self.nenner = abs(self.nenner)
        return self

    def __neg__(self):
        """
        Der überladene Operator gibt negiert den Bruch und gibt ihn zurück
        :return:
        """
        self.zaehler = abs(self.zaehler)
        return self

    def __ge__(self, other):
        """
        Der überladene Operator "greater equal" gibt das Ergebnis zweier verglichener Brüche zurück
        :param other:
        :return:
        """
        return float(self) >= float(other)

    def __gt__(self, other):
        """
        Der überladene Operator "greater than" gibt das Ergebnis zweier verglichener Brüche zurück
        :param other:
        :return:
        """
        return float(self) > float(other)

    def __add__(self, other):
        """
        Der überladene Operator add + gibt das Ergebnis zweier zu addierenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float,str)):
            raise TypeError
        else:
            return float(self)+float(other)

    def __radd__(self, other):
        """
        Der überladene Operator add + gibt das Ergebnis zweier zu addierenden Brüche zurück
        :param other:
        :return:
        """

        return float(self) + self.zaehler

    def __iadd__(self, other):
        """
        Der überladene Operator add + gibt das Ergebnis zweier zu addierenden Brüche zurück
        :param other:
        :return:
        """
        return self + other

    def __mul__(self, other):
        """
        Der überladene Operator * Multiplikation gibt das Ergebnis zweier zu multiplizierenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float,str)):
            raise TypeError
        else:
            return float(self) * float(other)

    def __imul__(self, other):
        """
        Der überladene Operator * Multiplikation gibt das Ergebnis zweier zu multiplizierenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float,str)):
            raise TypeError
        else:
            return self * other

    def __rmul__(self, other):
        """
        Der überladene Operator * Multiplikation gibt das Ergebnis zweier zu multiplizierenden Brüche zurück
        :param other:
        :return:
        """
        return self * other

    def __truediv__(self, other):
        """
        Der überladene Operator / Division gibt das Ergebnis zweier zu durcheinander teilenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float, str)):
            raise TypeError
        else:
            return float(self) / float(other)


    def __itruediv__(self, other):
        """
         Der überladene Operator / Division gibt das Ergebnis zweier zu durcheinander teilenden Brüche zurück
         :param other:
         :return:
         """
        if isinstance(other, (float, str)):
            raise TypeError
        else:
            return float(self) / float(other)

    def __rtruediv__(self, other):
        """
         Der überladene Operator / Division gibt das Ergebnis zweier zu durcheinander teilenden Brüche zurück
         :param other:
         :return:
         """
        if isinstance(other, (float, str)):
            raise TypeError
        elif self.zaehler == 0:
            raise ZeroDivisionError
        else:
            return float(self) / float(other)


    def __sub__(self, other):
        """
        Der überladene Operator - Substraktion gibt das Ergebins zweier von einander abzuziehenden Brüche zurück
        :param other:
        :return:
        """
        return float(self)-float(other)

    def __isub__(self, other):
        """
        Der überladene Operator - Substraktion gibt das Ergebins zweier von einander abzuziehenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float,str)):
            raise TypeError
        else:
            return float(self)-float(other)

    def __rsub__(self, other):
        """
        Der überladene Operator - Substraktion gibt das Ergebins zweier von einander abzuziehenden Brüche zurück
        :param other:
        :return:
        """
        if isinstance(other, (float,str)):
            raise TypeError
        else:
            return self.zaehler - float(self)

    def __iter__(self):
        """
        Der überladene Iterator geht durch den Bruch und gibt Nenner und Zähler zurück
        :return:
        """
        return (self.zaehler, self.nenner).__iter__()