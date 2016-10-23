package com.seidl;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.jws.soap.SOAPBinding;
import javax.naming.NamingEnumeration;
import javax.naming.directory.SearchResult;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by Maximilian Seidl on 18.10.2016.
 */
public class Ldap_Client {

    private PublicKey pubKey;

    private SecretKey secretKey;

    private String ldapHost;

    public void setLdapHost(String ldapHost){
        this.ldapHost=ldapHost;
    }


    public void getPublicKeyFromLdap(){
        LDAPConnector ldapConnector = new LDAPConnector(ldapHost);
        String tmp = "";
            try {
                NamingEnumeration listName = ldapConnector.search("dc=nodomain,dc=com", "(&(objectclass=PosixGroup)(cn=group.Service1))");
                while (listName.hasMore()) {
                    SearchResult sr = (SearchResult) listName.next();
                    tmp = UsedUtils.getPublicKey(sr.getAttributes()).toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            byte[] pubKeyByte = UsedUtils.toByteArray(tmp);
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(pubKeyByte);
            try {
                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                this.pubKey = keyFactory.generatePublic(pubKeySpec);
                System.out.println("public key from LDAP: "+ UsedUtils.toHexString(pubKey.getEncoded()));
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    public void generateSecretKey(){
        try {
            this.secretKey = KeyGenerator.getInstance("AES").generateKey();
            System.out.println("Secret Key successfully generated! "+UsedUtils.toHexString(secretKey.getEncoded()));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public byte[] encryptSecret(){
        byte[] tmp;
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.WRAP_MODE, this.pubKey);
            tmp = cipher.wrap(secretKey);
            System.out.println("Secret Key successfully encrypted!" + UsedUtils.toHexString(tmp));
            return tmp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public byte[] decryptMessage(byte[] messageFromServer){
        byte [] tmp = null;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, this.secretKey);
            tmp =  cipher.doFinal(messageFromServer);
            System.out.println("Message successfully decrypted!");
            return tmp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return tmp;
    }
}
