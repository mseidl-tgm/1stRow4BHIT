# -*- coding: utf-8 -*-

# Form implementation generated from reading ui file 'GUI\window.ui'
#
# Created: Mon Nov 14 13:18:50 2016
#      by: pyside-uic 0.2.15 running on PySide 1.2.2
#
# WARNING! All changes made in this file will be lost!

from PySide import QtCore, QtGui

class Ui_MainWindow(object):
    def setupUi(self, MainWindow):
        MainWindow.setObjectName("MainWindow")
        MainWindow.resize(1076, 600)
        self.centralwidget = QtGui.QWidget(MainWindow)
        self.centralwidget.setObjectName("centralwidget")
        self.gridLayout_2 = QtGui.QGridLayout(self.centralwidget)
        self.gridLayout_2.setObjectName("gridLayout_2")
        self.verticalLayout = QtGui.QVBoxLayout()
        self.verticalLayout.setObjectName("verticalLayout")
        self.navigation = QtGui.QLabel(self.centralwidget)
        self.navigation.setObjectName("navigation")
        self.verticalLayout.addWidget(self.navigation)
        self.formLayout = QtGui.QFormLayout()
        self.formLayout.setFieldGrowthPolicy(QtGui.QFormLayout.AllNonFixedFieldsGrow)
        self.formLayout.setObjectName("formLayout")
        self.start = QtGui.QLabel(self.centralwidget)
        self.start.setObjectName("start")
        self.formLayout.setWidget(0, QtGui.QFormLayout.LabelRole, self.start)
        self.startinput = QtGui.QLineEdit(self.centralwidget)
        self.startinput.setObjectName("startinput")
        self.formLayout.setWidget(0, QtGui.QFormLayout.FieldRole, self.startinput)
        self.ziel = QtGui.QLabel(self.centralwidget)
        self.ziel.setObjectName("ziel")
        self.formLayout.setWidget(1, QtGui.QFormLayout.LabelRole, self.ziel)
        self.zielinput = QtGui.QLineEdit(self.centralwidget)
        self.zielinput.setObjectName("zielinput")
        self.formLayout.setWidget(1, QtGui.QFormLayout.FieldRole, self.zielinput)
        self.verticalLayout.addLayout(self.formLayout)
        self.outputlabel = QtGui.QTextBrowser(self.centralwidget)
        self.outputlabel.setObjectName("outputlabel")
        self.verticalLayout.addWidget(self.outputlabel)
        self.gridLayout = QtGui.QGridLayout()
        self.gridLayout.setObjectName("gridLayout")
        self.reset = QtGui.QPushButton(self.centralwidget)
        self.reset.setObjectName("reset")
        self.gridLayout.addWidget(self.reset, 0, 1, 1, 1)
        self.close = QtGui.QPushButton(self.centralwidget)
        self.close.setObjectName("close")
        self.gridLayout.addWidget(self.close, 0, 2, 1, 1)
        self.submit = QtGui.QPushButton(self.centralwidget)
        self.submit.setMinimumSize(QtCore.QSize(600, 0))
        self.submit.setObjectName("submit")
        self.gridLayout.addWidget(self.submit, 0, 0, 1, 1)
        self.verticalLayout.addLayout(self.gridLayout)
        self.gridLayout_2.addLayout(self.verticalLayout, 0, 0, 1, 1)
        MainWindow.setCentralWidget(self.centralwidget)
        self.menubar = QtGui.QMenuBar(MainWindow)
        self.menubar.setGeometry(QtCore.QRect(0, 0, 1076, 31))
        self.menubar.setObjectName("menubar")
        MainWindow.setMenuBar(self.menubar)
        self.statusbar = QtGui.QStatusBar(MainWindow)
        self.statusbar.setObjectName("statusbar")
        MainWindow.setStatusBar(self.statusbar)

        self.retranslateUi(MainWindow)
        QtCore.QObject.connect(self.reset, QtCore.SIGNAL("clicked()"), self.zielinput.clear)
        QtCore.QObject.connect(self.reset, QtCore.SIGNAL("clicked()"), self.startinput.clear)
        QtCore.QObject.connect(self.reset, QtCore.SIGNAL("clicked()"), self.outputlabel.clear)
        QtCore.QObject.connect(self.close, QtCore.SIGNAL("clicked()"), MainWindow.close)
        QtCore.QObject.connect(self.reset, QtCore.SIGNAL("clicked()"), self.statusbar.clearMessage)
        QtCore.QMetaObject.connectSlotsByName(MainWindow)

    def retranslateUi(self, MainWindow):
        MainWindow.setWindowTitle(QtGui.QApplication.translate("MainWindow", "MainWindow", None, QtGui.QApplication.UnicodeUTF8))
        self.navigation.setText(QtGui.QApplication.translate("MainWindow", "Google Navigation", None, QtGui.QApplication.UnicodeUTF8))
        self.start.setText(QtGui.QApplication.translate("MainWindow", "Start:", None, QtGui.QApplication.UnicodeUTF8))
        self.ziel.setText(QtGui.QApplication.translate("MainWindow", "Ziel:", None, QtGui.QApplication.UnicodeUTF8))
        self.reset.setText(QtGui.QApplication.translate("MainWindow", "reset", None, QtGui.QApplication.UnicodeUTF8))
        self.close.setText(QtGui.QApplication.translate("MainWindow", "close", None, QtGui.QApplication.UnicodeUTF8))
        self.submit.setText(QtGui.QApplication.translate("MainWindow", "submit", None, QtGui.QApplication.UnicodeUTF8))

