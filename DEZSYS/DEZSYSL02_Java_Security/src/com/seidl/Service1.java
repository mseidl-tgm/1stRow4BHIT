package com.seidl;

import javax.crypto.Cipher;
import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.Base64;

import static com.seidl.ASymCrypto.toHexString;

/**
 * Created by Maximilian Seidl on 21.10.2016.
 */
public class Service1 {

    private byte[] encryptedSecret;
    private String encryptedSecretString;
    private int port = 999;
    private ServerSocket ss = null;
    private Socket csocket= null;
    private byte[] pubKey = null;
    private PrivateKey privKey = null;
    private Key secretKey = null;
    private String msg = "test Message!";
    private byte[] cryptedMsgByte = null;
    private String cryptedMsgString = "";

    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader stdIn;

    public Service1(){

    }
    public void setEncryptedSecret(String encryptedSecretString){
        this.encryptedSecretString = encryptedSecretString;

    }
    public byte[] getCryptedMsgByte(){
        return this.cryptedMsgByte;
    }
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
    public void genKeyPair(){
        KeyPair keyPair = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            generator.initialize(1024, random);
            keyPair = generator.generateKeyPair();

        } catch (Exception e) {
            e.printStackTrace();
        }
            pubKey = keyPair.getPublic().getEncoded();
            privKey = keyPair.getPrivate();

    }
    public void storePubKeyinLDAP(){
        LDAPConnector ldapConnector = new LDAPConnector();
        try {
            ldapConnector.updateAttribute("cn=group.Service1,dc=nodomain,dc=com", "description", toHexString(pubKey));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void readWriteFromServerSocket(Boolean rw){
        if(rw = true) {

            try {
                this.in = new BufferedReader(new InputStreamReader(csocket.getInputStream()));
                this.stdIn = new BufferedReader(new InputStreamReader(System.in));
                encryptedSecretString = in.readLine();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(rw = false){
            try {
                PrintWriter out = new PrintWriter(csocket.getOutputStream(), true);
                System.out.println(toHexString(cryptedMsgByte));
                out.println(toHexString(cryptedMsgByte));
                out.flush();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void decryptSecretKey(){
        encryptedSecret = toByteArray(encryptedSecretString);

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.UNWRAP_MODE, privKey);
            secretKey = cipher.unwrap(encryptedSecret, "AES", Cipher.SECRET_KEY);
            System.out.println(secretKey.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void encryptMessage(){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            cryptedMsgByte = cipher.doFinal();
        }catch (Exception e){

        }
    }
    public static void main(String[] args){

        Service1 s1 = new Service1();
        s1.genKeyPair();
        s1.storePubKeyinLDAP();
        System.out.println("Public Key stored in LDAP");
        try {
            s1.ss = new ServerSocket(s1.port);
            System.out.println("Wating for client to connect ...");
            s1.csocket = s1.ss.accept();
            System.out.println("Client connected successfully.");
            s1.in = new BufferedReader(new InputStreamReader(s1.csocket.getInputStream()));
            s1.stdIn = new BufferedReader(new InputStreamReader(System.in));
            String tmp = s1.in.readLine();
            s1.setEncryptedSecret(tmp);
            s1.decryptSecretKey();
            s1.encryptMessage();
            PrintWriter out = new PrintWriter(s1.csocket.getOutputStream(), true);
            System.out.println(toHexString(s1.getCryptedMsgByte()));
            out.println(toHexString(s1.getCryptedMsgByte()));
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
