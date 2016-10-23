package com.seidl;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.security.*;
import java.util.Base64;

/**
 * Created by Maximilian Seidl on 18.10.2016.
 */
public class Service1 {

    //Attributes
    private KeyPair keyPair;
    private PublicKey pubKey;
    private PrivateKey privKey;

    private String ldapHost;

    private Key secretKey;

    private String toSendMessage = "";


    public void setToSendMessage(String toSendMessage){
        this.toSendMessage = toSendMessage;
    }
    public void setLdapHost(String ldapHost){
        this.ldapHost = ldapHost;
    }

    public void genKeyPair(){
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            generator.initialize(1024, random);
            this.keyPair = generator.generateKeyPair();

        } catch (Exception e) {
            e.printStackTrace();
        }
        pubKey = keyPair.getPublic();
        privKey = keyPair.getPrivate();

    }

    public void storePubKeyinLDAP(){
        LDAPConnector ldapConnector = new LDAPConnector(ldapHost);
        try {
            ldapConnector.updateAttribute("cn=group.Service1,dc=nodomain,dc=com", "description", UsedUtils.toHexString(pubKey.getEncoded()));
            System.out.println("public key stored in LDAP: "+ UsedUtils.toHexString(pubKey.getEncoded()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void decryptSecretKey(byte[] secretKeyByte){
        System.out.println("Encrypted Secret Key successfully transmitted!"+UsedUtils.toHexString(secretKeyByte));
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.UNWRAP_MODE, privKey);
            this.secretKey = cipher.unwrap(secretKeyByte, "AES", Cipher.SECRET_KEY);
            System.out.println("Decrypted Secret Key is: " + UsedUtils.toHexString(secretKey.getEncoded()));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public byte[] encryptMessage(){
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, this.secretKey);
            byte[] msgBytes = toSendMessage.getBytes();
            byte[] tmp = cipher.doFinal(msgBytes);
            System.out.println("encrypted Message: "+ UsedUtils.toHexString(tmp));
            return tmp;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
