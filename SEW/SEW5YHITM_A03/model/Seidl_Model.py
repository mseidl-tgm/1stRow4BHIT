import random
from _operator import neg


class Seidl_Model(object):

    next = 0
    wonAt = 15

    #stats class
    stats = None

    def __init__(self, stats):
        self.stats = stats

    def restartGame(self):
        self.next = 0
        self.stats.defineGames(1)
        self.stats.resetValues()


    def getUserAction(self, button):
        if not button.text() == "Neu":
            if int(button.text()) == self.next:
                self.next = self.next + 1
                self.stats.defineOpen(-1)
                self.stats.defineCorrect(1)

                return False
        else:
            self.stats.defineWrong(1)
            return True

        """keine Ahnung"""
        return False

    def getIfWon(self):
        if self.next == self.wonAt:
            return True
        else:
            return False


    def generateRandomField(self):
        return random.sample(range(0, 15), 15)


class Stats(object):

    stats = {
        "open": 15,
        "correct": 0,
        "wrong": 0,
        "sum": 0,
        "games": 0
    }

    def getStats(self):
        return self.stats

    """
    define Values in Lbels
    """
    def defineOpen(self, value):
        self.stats["open"] += value

    def defineCorrect(self, value):
        self.stats["correct"] += value
        self.updateSum()

    def defineWrong(self, value):
        self.stats["wrong"] += value
        self.updateSum()

    def updateSum(self):
        self.stats["sum"] = self.stats["correct"] + self.stats["wrong"]

    def defineGames(self, value):
        self.stats["games"] += value

    def resetValues(self):
        self.defineOpen((neg(int(self.stats["open"]))) + 15)
        self.defineCorrect(neg(int(self.stats["correct"])))
        self.defineWrong(neg(int(self.stats["wrong"])))
        self.updateSum()