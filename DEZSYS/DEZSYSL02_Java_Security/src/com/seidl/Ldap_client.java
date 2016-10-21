package com.seidl;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.Data;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.ECField;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static com.seidl.ASymCrypto.getPublicKey;

/**
 * Created by Maximilian Seidl on 21.10.2016.
 */
public class Ldap_client {

    private String host = "";
    private String pubKey = "";
    private SecretKey secretKey  = null;
    private PublicKey pubKeyasKey = null;
    private byte[] cryptedSecret = null;
    private Socket s = null;
    private byte[] msg = "Das funktioniert nicht!".getBytes();


    public Ldap_client(){

    }
    public byte[] getCryptedSecret(){
        return this.cryptedSecret;
    }
    public String getPubKey(){
        return this.pubKey;
    }
    public String getHost(){
        return this.host;
    }
    public void setHost(String host){
        this.host = host;
    }
    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
    public void getPublicKeyFromLdap(){
        LDAPConnector ldapConnector = new LDAPConnector();
        try {
            NamingEnumeration listName = ldapConnector.search("dc=nodomain,dc=com", "(&(objectclass=PosixGroup)(cn=group.Service1))");
            while (listName.hasMore()) {
                SearchResult sr = (SearchResult) listName.next();
                this.pubKey = getPublicKey(sr.getAttributes()).toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createSecretKey(){

        try {
            secretKey = KeyGenerator.getInstance("AES").generateKey();
            //secretKey = Base64.getEncoder().encodeToString(key.getEncoded());
            System.out.println(secretKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void pubKeyStringtoKey(){
        byte[] pubKeyByte = Ldap_client.toByteArray(pubKey);
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec( pubKeyByte );
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            pubKeyasKey = keyFactory.generatePublic(pubKeySpec);
            System.out.print(pubKeyasKey.toString());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void encryptSecret(){
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, pubKeyasKey);
            cryptedSecret = cipher.wrap(secretKey);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void createClientSocket(){
        try (
                Socket s = new Socket(host, 9999);
        )
        {
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeEncryptedSecretKeytoInputStream(){
        try {
            OutputStream out = s.getOutputStream();
            DataOutputStream dos = new DataOutputStream(out);
            dos.write(cryptedSecret);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){
        Ldap_client lc = new Ldap_client();
        lc.getPublicKeyFromLdap();
        System.out.println(lc.getPubKey());
        lc.createSecretKey();
        lc.pubKeyStringtoKey();
        lc.encryptSecret();
        lc.createClientSocket();
        lc.writeEncryptedSecretKeytoInputStream();



    }

}
