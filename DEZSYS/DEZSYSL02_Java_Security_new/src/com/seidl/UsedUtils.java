package com.seidl;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Maximilian Seidl on 18.10.2016.
 */
public class UsedUtils {

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }

    public static Object getPublicKey( Attributes attrs ) {

        if (attrs == null) {
            return null;
        } else {
        /* Print each attribute */
            try {
                for (NamingEnumeration ae = attrs.getAll(); ae.hasMore(); ) {
                    Attribute attr = (Attribute) ae.next();
                    System.out.println("attribute: " + attr.getID());

        		/* print each value */
                    for (NamingEnumeration e = attr.getAll(); e.hasMore(); ) {
                        if (attr.getID().equals("description"))
                            return e.next();
                        e.next();
                    }
                }
            } catch (NamingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static byte[] readFromSocket(Socket socket){
        byte[] encodedMessage = null;
        String received = "";
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                received = in.readLine();
                encodedMessage = UsedUtils.toByteArray(received);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return encodedMessage;
    }

    public static void writeToSocket(Socket socket, byte[] toSend){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(toHexString(toSend));
            out.flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
