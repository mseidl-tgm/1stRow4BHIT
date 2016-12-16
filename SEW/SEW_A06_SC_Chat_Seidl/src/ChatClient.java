import java.net.*;
import java.io.*;

/**
 * Die Klasse ChatClient ist der Client im Chat und implementiert das Runnable Interface um gleichzeitig zu lesen und zu schreiben
 *
 * created by Maximilian Seidl on 12-14-2016
 */
public class ChatClient implements Runnable{
    private Socket socket              = null;
    private Thread thread              = null;
    private DataInputStream  console   = null;
    private DataOutputStream streamOut = null;
    private ChatClientThread client    = null;

    /*
    Der Konstruktor der Klasse ChatClient übernimmt die üblichen Parameter für einen ClientSocket und
    übernimmt das Exception-handling bei fehlerhaften Verbindungen.
     */
    public ChatClient(String serverName, int serverPort){
        System.out.println("Trying to connect. Please wait ...");
        try{
            socket = new Socket(serverName, serverPort);
            System.out.println("Connected: " + socket);
            start();
        }catch(UnknownHostException uhe){
            System.out.println("Host unknown: " + uhe.getMessage());
        }
        catch(IOException ioe){
            System.out.println("Unexpected exception: " + ioe.getMessage());
        }
    }

    /*
    Die zu implementierende run-Methode schreibt auf den Outputstream an den Server.
     */
    public void run(){
        while (thread != null){
            try{
                streamOut.writeUTF(console.readLine());
                streamOut.flush();
            }catch(IOException ioe){
                System.out.println("Sending error: " + ioe.getMessage());
                stop();
            }
        }
    }

    /*
    Die handle-Methode übernimmt das saubere Beenden der Verbindung
     */
    public void handle(String msg){
        if (msg.equals(".exit")){
            System.out.println("Press RETURN to exit ...");
            stop();
        }else {
            System.out.println(msg);
        }
    }

    /*
    Die start-Methode startet neue Streams und auch einen Thread.
     */
    public void start() throws IOException{
        console   = new DataInputStream(System.in);
        streamOut = new DataOutputStream(socket.getOutputStream());
        if (thread == null){
            client = new ChatClientThread(this, socket);
            thread = new Thread(this);
            thread.start();
        }
    }

    /*
    Die stop-Methode übernimmt das saubere Beeenden der Streams und der Threads.
     */
    public void stop(){
        if (thread != null){
            thread.stop();
            thread = null;
        }
        try{
            if (console   != null)  console.close();
            if (streamOut != null)  streamOut.close();
            if (socket    != null)  socket.close();
        }catch(IOException ioe){
            System.out.println("Error closing ...");
        }
        client.close();
        client.stop();
    }
    /*
    Die main-Methode startet ein neues ChatClient-Objekt mit den angegebenen Kommmandozeilenparameter.
     */
    public static void main(String args[]){
        ChatClient client = null;
        if (args.length != 2)
            System.out.println("Usage: java ChatClient host port");
        else
            client = new ChatClient(args[0], Integer.parseInt(args[1]));
    }
}