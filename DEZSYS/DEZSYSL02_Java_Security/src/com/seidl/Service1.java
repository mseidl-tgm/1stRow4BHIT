package com.seidl;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

import static com.seidl.ASymCrypto.toHexString;

/**
 * Created by Maximilian Seidl on 21.10.2016.
 */
public class Service1 {

    private byte[] msg;

    public Service1(byte[] msg){
        this.msg = msg;

    }
    public byte[] genKeyPair(){
        KeyPair keyPair = null;
        byte[] key = null;
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            generator.initialize(1024, random);
            keyPair = generator.generateKeyPair();

            key = keyPair.getPublic().getEncoded();
            return key;
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("KeyPair wurde nicht erstellt!");
        return key;
    }
    public void storePubKeyinLDAP(byte[] key){
        LDAPConnector ldapConnector = new LDAPConnector();
        try {
            ldapConnector.updateAttribute("cn=group.Service1,dc=nodomain,dc=com", "description", toHexString(key));
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void main(String[] args){
        byte[] msg = null;
        Service1 s1 = new Service1(msg);
        byte [] key  = s1.genKeyPair();
        System.out.println("keyPair created! \n" + key.toString());
        s1.storePubKeyinLDAP(key);
        System.out.println("Public Key stored in LDAP");
    }
}
