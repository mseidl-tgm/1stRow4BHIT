from PySide.QtCore import *
from PySide.QtGui import *
from PySide import QtCore

import sys
import view.Seidl_Game_View, model.Seidl_Model, view.Seidl_Game_View_Control

class Seidl_Controller(QMainWindow):

    """
    Beispiel PDF
    """

    def connectButtons(self):

        for pButton in self.Seidl_Game_View_Control.pButtons:
            QtCore.QObject.connect(pButton, QtCore.SIGNAL('clicked()'), self.onClicked),
        
        QtCore.QObject.connect(self.Seidl_Game_View_Control.newButton, QtCore.SIGNAL('clicked()'), self.onClicked)

    def start(self):
        self.Seidl_Game_View_Control.restartGame(self.Seidl_Model.generateRandomField())

    def onClicked(self):

        sender = self.sender()

        if sender.text() == "Neu":
            self.Seidl_Game_View_Control.restartGame(self.Seidl_Model.generateRandomField())
            self.Seidl_Model.restartGame()

        else:
            self.Seidl_Game_View_Control.defineButtonStatus(sender, self.Seidl_Model.getUserAction(sender))
            self.Seidl_Game_View_Control.updateStats(self.stats.getStats())

    def __init__(self, parent=None):
        """
        Dies ist der Controller, welcher die GUI mit der Logik verknüpft und somit das Spiel zum Laufen bringt
        """
        super().__init__(parent)
        self.game = view.Seidl_Game_View.Ui_MainWindow()
        self.game.setupUi(self)

        self.Seidl_Game_View_Control = view.Seidl_Game_View_Control.Seidl_Game_View_Control(self.game)

        self.stats = model.Seidl_Model.Stats()
        self.Seidl_Model = model.Seidl_Model.Seidl_Model(self.stats)
        self.Seidl_Model.generateRandomField()


        # connect the buttons with the clicked signal
        self.connectButtons()
        # start a new game
        self.start()

if __name__ == "__main__":
    app = QApplication(sys.argv)
    c = Seidl_Controller()
    c.show()
    sys.exit(app.exec_())