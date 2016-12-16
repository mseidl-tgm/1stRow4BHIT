import java.net.*;
import java.io.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Das ist die ServerKlasse, welche das Runnable Interface implementiert und sommit die run-Methode implementieren muss.
 * Zunächst besitzt der Server nur eine ArrayListe mit ServerThreads (Zu jedem ServerThread gehört ein Socket).
 * Sobald sich ein neuer Client verbindet bekommt dieser einen Eintrag in der ArrayListe und gehört sommit zum Chatroom.
 *
 * created by Maximilian Seidl on 12-14-2016
 */

public class ChatServer implements Runnable{
    //ArrayListe mit ChatServerThreads
    private CopyOnWriteArrayList<ChatServerThread> clients = new CopyOnWriteArrayList<>();
    //ServerSocket
    private ServerSocket server = null;
    //ein Thread-Objekt
    private Thread       thread = null;
    //ein Client-counter, damit die verbundenen Clients gezählt werden können.
    private int clientCount = 0;

    /*
    Der Konstruktor übernimmt das binden vom Server auf einen Port und handled im Notfall die Exception
    @param int:port
     */
    public ChatServer(int port) {
        try{
            System.out.println("Binding to port " + port + ", please wait  ...");
            server = new ServerSocket(port);
            System.out.println("Server started: " + server);
            start();
        }catch(IOException ioe){
            System.out.println("Can not bind to port " + port + ": " + ioe.getMessage()); }
        }

    /*
    Die run-Methode wird folgendermaßen implementiert: sobald sich ein Client verbindet,
    wird die addThread aufgerufen, welche dann einen neuen Thread zuweist mit einer eigenen ID
     */
    public void run() {
        while (thread != null){
            try{
                System.out.println("Waiting for a client ...");
                addThread(server.accept());
            }catch(IOException ioe){
                System.out.println("Server accept error: " + ioe); stop(); }
            }
    }

    /*
    Die Methoden start und stop müssen auch implementiert werden um als "Runnable" zu gelten.
    Hierbei wird jediglich ein neue Thread gestartet, oder ein Thread gestopt. (Verwendung der Methoden von Klasse Thread)
     */
    public void start(){
        if (thread == null){
            thread = new Thread(this);
            thread.start();
        }
    }

    public void stop(){
        if (thread != null){
            thread.stop();
            thread = null;
        }
    }

    /*
    Die Methode findClient sucht nach einem Client mit der CustomID,
    hierbei wird durch die Liste itteriert und nach dem passenden Client gesucht.
    @param int:ID
     */
    private int findClient(int ID){
        for (int i = 0; i < clientCount; i++)
            if (clients.get(i).getID() == ID)
                return i;
            return -1;
    }
    /*
    Die Methode handle MUSS synchronisiert werden, da hier Änderungen an der ArrayListe vorgenommen werden. Wäre die Synchronisation (geregelter Zugriff) nicht vorhanden,
    könnten Race Conditions oder andere Probleme auftreten und es würde zur Inkonsitenz führen.
    @param int:ID , String:input
     */
    public synchronized void handle(int ID, String input){
        if (input.equals(".exit")){
            clients.get(findClient(ID)).send(".exit");
            //hier findet der kongurierende Zugriff statt
            remove(ID); }
        else
            for (int i = 0; i < clientCount; i++)
                clients.get(i).send(ID + ": " + input);
    }

    /*
    Die Methode remove, sucht nach einem Thread in der Liste und versucht diesen zu terminieren.
    Wird ebenfalls synchronized, weil wieder ein Zugriff von mehreren Threads möglich ist.
    Zusätzlich wird der Counter wieder verkleinert.
     */
    public synchronized void remove(int ID){
        int pos = findClient(ID);
        if (pos >= 0){
            ChatServerThread toTerminate = clients.get(pos);
            System.out.println("Removing client thread " + ID + " at " + pos);
            if (pos < clientCount-1)
                for (int i = pos+1; i < clientCount; i++)
                    clients.set(i, clients.get(i-1));
                    clientCount--;
            try{
                toTerminate.close();
            }catch(IOException ioe){
                System.out.println("Error closing thread: " + ioe); }
                toTerminate.stop();
        }
    }

    /*
    Die Methode addThread startet einen neuen ClientSocket und einen neuen Thread und erhöht den Counter.
    @param Socket:Socket
     */
    private void addThread(Socket socket){
        if (clientCount < clients.size()+1){
            System.out.println("Client accepted: " + socket);
            clients.add(clientCount, new ChatServerThread(this, socket));
            try{
                clients.get(clientCount).open();
                clients.get(clientCount).start();
                clientCount++;
            }catch(IOException ioe){  System.out.println("Error opening thread: " + ioe); } }
        else
            System.out.println("Client refused: maximum " + clients.size() + " reached.");
    }
    /*
    Die main Methode startet einen neuen ChatServer und horcht auf den Port.
     */
    public static void main(String args[]){
        ChatServer server = null;
        if (args.length != 1)
            System.out.println("Usage: java ChatServer port");
        else
            server = new ChatServer(Integer.parseInt(args[0]));
    }
}