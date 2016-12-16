import java.net.*;
import java.io.*;

/**
 * Die Klasse ChatServerThread erbt von Thread und ist somit auch ein Thread. Er vergibt jedem neuen Objekt eine eigene ID,
 * welche sich aus dem Port zusammengesetzt.
 *
 * created by Maximilian Seidl on 12-14-2016
 */
public class ChatServerThread extends Thread{
    private ChatServer       server    = null;
    private Socket           socket    = null;
    private int              ID        = -1;
    private DataInputStream  streamIn  =  null;
    private DataOutputStream streamOut = null;


    /*
    Der Konstruktor ChatServerThread übernimmt ein ChatServer- und ein Socket- Objekt und setzt seine Attribute
     */
    public ChatServerThread(ChatServer _server, Socket _socket){
        super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
    }

    /*
    Die methode send schreibt auf den Outputstream die Message, welche im Parameter übergeben wird.
    @param String:msg
     */
    public void send(String msg){
        try{
            streamOut.writeUTF(msg);
            streamOut.flush();
        }catch(IOException ioe){
            System.out.println(ID + " ERROR sending: " + ioe.getMessage());
            server.remove(ID);
            stop();
        }
    }

    /*
    Die getID-Methode gibt die ID eines ChatServerthread Objekt zurück
     */
    public int getID(){
        return ID;
    }

    /*
    Die run-Methode läuft in einer Endlosschleife und nutzt die Methoden vom Server um die Clients zu verwalten.
     */
    public void run(){
        System.out.println("Server Thread " + ID + " running.");
        while (true){
            try{
                server.handle(ID, streamIn.readUTF());
            }catch(IOException ioe){
                System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }

    /*
    Die open-Methode öffnet einen neuen Outpustream/Inputstream für Ein- und Aus-gaben
     */
    public void open() throws IOException{
        streamIn = new DataInputStream(new
            BufferedInputStream(socket.getInputStream()));
        streamOut = new DataOutputStream(new
                BufferedOutputStream(socket.getOutputStream()));
    }

    /*
    Die close-Methode schließt alle offenen Streams, sowie den Socket
     */
    public void close() throws IOException{
        if (socket != null)
            socket.close();
        if (streamIn != null)
            streamIn.close();
        if (streamOut != null)
            streamOut.close();
    }
}