class Seidl_Game_View_Control(object):


    stats = None
    playButtons = None
    newGame = None

    def __init__(self, game):
        self.game = game

        """
        Verwendung von structs -> Christoph Miko
        """
        self.stats = {
            "open": [self.game.open, self.game.open_n],
            "correct": [self.game.correct, self.game.correct_n],
            "wrong": [self.game.wrong, self.game.wrong_n],
            "sum": [self.game.sum, self.game.sum_n],
            "games": [self.game.games, self.game.games_n]
        }

        self.playButtons = [
            self.game.button_n_1,
            self.game.button_n_2,
            self.game.button_n_3,
            self.game.button_n_4,
            self.game.button_n_5,
            self.game.button_n_6,
            self.game.button_n_7,
            self.game.button_n_8,
            self.game.button_n_9,
            self.game.button_n_10,
            self.game.button_n_11,
            self.game.button_n_12,
            self.game.button_n_13,
            self.game.button_n_14,
            self.game.button_n_15
        ]

        self.newButton = self.game.button_new


    def defineButtonStatus(self, sender, status):
        if sender.isEnabled() != status:
            sender.setEnabled(status)

    def updateStats(self, stats):
        """
        Verwendung von structs -> Christoph Miko
        """
        self.stats["open"][1].setText(str(stats["open"]))
        self.stats["correct"][1].setText(str(stats["correct"]))
        self.stats["wrong"][1].setText(str(stats["wrong"]))
        self.stats["sum"][1].setText(str(stats["sum"]))
        self.stats["games"][1].setText(str(stats["games"]))

    def newGame(self,numbers):
        self.initField(numbers)
        for button in self.playButtons:
            button.setEnabled(True)

    def initField(self, numbers):
        for i in range(15):
            self.playButtons[i].setText(str(numbers[i]))