import java.net.*;
import java.io.*;

/**
 * Die Klasse ChatClientThread erbt von thread und ist sommit auch einer. Sie implementiert die üblichen Thread Methoden angepasst and den Server-Client Chat.
 *
 * created by Maximilian Seidl on 12-14-2016
 */
public class ChatClientThread extends Thread{
    private Socket           socket   = null;
    private ChatClient       client   = null;
    private DataInputStream  streamIn = null;

    /*
    Der Konstruktor von ChatClientThread über nimmt ein Client- und Socket- Objekt und ruft die Methoden auf.
     */
    public ChatClientThread(ChatClient _client, Socket _socket){
        client   = _client;
        socket   = _socket;
        open();
        start();
    }

    /*
    Die open-Methode startet einen neuen InputStream und handled sauber die Exceptions
     */
    public void open(){
        try{
            streamIn  = new DataInputStream(socket.getInputStream());
        }catch(IOException ioe){
            System.out.println("Error getting input stream: " + ioe);
            client.stop();
        }
    }

    /*
    Die close-Methode schließt alle offenen Streams (InputStream)
     */
    public void close(){
        try{
            if (streamIn != null) streamIn.close();
        }catch(IOException ioe){
            System.out.println("Error closing input stream: " + ioe);
        }
    }

    /*
    Die run-Methode läuft in einer Endlosschleife, in der permanent auf den InputStream gelesen wird.7
     */
    public void run(){
        while (true){
            try{
                client.handle(streamIn.readUTF());
            }catch(IOException ioe){
                System.out.println("Listening error: " + ioe.getMessage());
                client.stop();
            }
        }
    }
}
