package com.seidl;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.kohsuke.args4j.OptionHandlerFilter.ALL;

/**
 * Created by Maximilian Seidl on 18.10.2016.
 */
public class Controller {

    @Option(name="-S", usage="Server[s] or Client[c]",required = true)
    private char serverOrClient;

    @Option(name="-m", usage = "Message")
    private String message;

    @Option(name="-p", usage = "port", required = true)
    private int port;

    @Option(name="-l", usage = "hostname of LDAP",required = true)
    private String ldapHost;

    public void doMain(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
            Service1 service1;
            Ldap_Client ldap_client;


            ServerSocket serverSocket;
            Socket sCSocket;

            Socket clientSocket;

            if (this.serverOrClient == 's') {
                service1 = new Service1();
                service1.setToSendMessage(this.message);
                service1.genKeyPair();
                service1.setLdapHost(this.ldapHost);
                service1.storePubKeyinLDAP();
                try {
                    serverSocket = new ServerSocket(this.port);
                    sCSocket = serverSocket.accept();
                    service1.decryptSecretKey(UsedUtils.readFromSocket(sCSocket));
                    UsedUtils.writeToSocket(sCSocket, service1.encryptMessage());
                    sCSocket.close();
                    serverSocket.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            } else if (this.serverOrClient == 'c') {
                byte[] output;
                ldap_client = new Ldap_Client();
                ldap_client.setLdapHost(this.ldapHost);
                ldap_client.getPublicKeyFromLdap();
                ldap_client.generateSecretKey();
                try {
                    clientSocket = new Socket("localhost", this.port);
                    UsedUtils.writeToSocket(clientSocket, ldap_client.encryptSecret());
                    output = ldap_client.decryptMessage(UsedUtils.readFromSocket(clientSocket));
                    System.out.println("Decrypted Message from Service: "+ new String(output));


                } catch (IOException io) {
                    io.printStackTrace();
                }

            }
        }catch( CmdLineException e ) {
            // if there's a problem in the command line,
            // you'll get this exception. this will report
            // an error message.
            System.err.println(e.getMessage());
            System.err.println("java SampleMain [options...] arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();

            // print option sample. This is useful some time
            System.err.println("  Example: java SampleMain"+parser.printExample(ALL));

            return;
        }
    }

    public static void main(String[] args){
        new Controller().doMain(args);
    }
}
