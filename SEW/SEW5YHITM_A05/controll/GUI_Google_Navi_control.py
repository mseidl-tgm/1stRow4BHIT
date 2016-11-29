from PySide.QtCore import *
from PySide.QtGui import *
import sys
import model.GUI_Google_Navi_model
import view.GUI_Google_Navi


class Controller(QMainWindow):
    """ MVC pattern: Creates a controller - mvc pattern.
    """
    def __init__(self, parent=None):
        """ Create a new controller with a object View and a object Model
        using the mvc pattern.
        :param parent:
        """
        super().__init__(parent)
        self.form = view.GUI_Google_Navi.Ui_MainWindow()
        self.form.setupUi(self)
        self.model = model.GUI_Google_Navi_model.Model
        self.form.submit.clicked.connect(self.handle_button)

    def handle_button(self):
        """
            handle Buttonclick, print route to outputlables
        """
        if self.form.startinput.text() == "" or self.form.zielinput.text() == "":
            self.form.statusbar.showMessage("Bitte geben sie eine Adresse ein!")
            self.form.outputlabel.setText("Bitte geben sie eine Adresse ein!")
        else:
            tmp = self.model.get_request(self.form.startinput.text(), self.form.zielinput.text())
            if tmp == "NOT OK":
                self.form.statusbar.showMessage("Adresse nicht gefunden")
                self.form.outputlabel.setText("Adresse nicht gefunden! Bitte geben Sie eine gültige Adresse ein!")
            else:
                self.form.outputlabel.setText(tmp[0])
                self.form.startinput.setText(tmp[1])
                self.form.zielinput.setText(tmp[2])
                self.form.statusbar.showMessage("Route wurde berechnet")

if __name__ == "__main__":
    app = QApplication(sys.argv)
    c = Controller()
    c.show()
    sys.exit(app.exec_())
