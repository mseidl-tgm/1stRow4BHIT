
import java.net.Inet4Address;

import java.net.UnknownHostException;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * The class JMSChatSender implements a chat/mail service, where you send messages to a message broker and the message gets
 * enqueued until the receiver is asking for.
 * @author seidl
 * @since 2016-05-03
 */

public class JMSChatSender {

	//Log-in informations
	private static String user = ActiveMQConnection.DEFAULT_USER;
	private static String password = ActiveMQConnection.DEFAULT_PASSWORD;
	private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
	private static String subject = "VSDBChat";

	// Create the connection.
	private ConnectionFactory connectionFactory;
	private Session session = null;
	private Connection connection = null;
	private MessageProducer producer = null;
	private Destination destination = null;
	private MessageConsumer consumer = null;

	// Message
	private TextMessage message = null;

	// Queue
	private Queue queue;
	private MessageProducer MProducer;
	private MessageConsumer MConsumer;

	public JMSChatSender() {

		try {
			//create Connection 
			connectionFactory = new ActiveMQConnectionFactory(user, password, url);
			connection = connectionFactory.createConnection();
			connection.start();

			// Create the session
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			destination = session.createTopic(subject);

			// Create the producer.
			producer = session.createProducer(destination);
			producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			// Create the consumer
			consumer = session.createConsumer(destination);

		} catch (Exception e) {

			System.out.println("Following Exception occured: " + e);
			e.printStackTrace();

		}

	}
	
	/*
	 * The method close closes all connections with between the program and the message-broker 
	 */
	public void close() {
		try {
			connection.stop();
			producer.close();
			session.close();

		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * getter-method for the consumer.
	 */
	public MessageConsumer getCostumer() {
		return this.consumer;
	}
		/*
		 * The method mailbox implements a mailbox and creates a queue where all messages get stacked and waiting for the receiver to
		 * accept.
		 */
		public void mailbox() {
			try {
				queue = session.createQueue(user);
				MConsumer = session.createConsumer(queue);
				message = (TextMessage) MConsumer.receive();
				System.out.println(message.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
	
		}
	/*
	 * The method mail implments the written textmessage which gets send to the message-broker
	 */
	public void mail(String chat, TextMessage m) {
		try {
			queue = session.createQueue(chat);
			MProducer = session.createProducer(queue);
			MProducer.send(m);
		} catch (JMSException e) {
			e.printStackTrace();
		}

	}
	/*
	 * The method generateM generates a testmessage and make it fitable for the message-broker
	 */
	public TextMessage generateM(String m) {
		try {
			return session.createTextMessage(user + "(" + Inet4Address.getLocalHost().getHostAddress() + "): " + m);
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		//parameters
		if (args.length == 3) {
			url = "failover://tcp://" + args[0] + ":61616";
			user = args[1];
			subject = args[2];
		} else {
			System.err.println("Please give 3 arguements: <ip_message_broker> <benutzername> <chatroom>");
			System.exit(-1);
		}
		
		//Object chatSender
		JMSChatSender chatSender= new JMSChatSender();

		
		JMSChatReceiver chatReceiver = new JMSChatReceiver(chatSender.getCostumer());
		//Starts a new thread for the receiver
		new Thread(chatReceiver).start();
		//command menu for better usage of the program
		do {
			String input = InputReader.readLine("Enter one of the following commands: <mail> / <exit> / <mailbox> ");
			String[] commands  = input.split(" ");

			try{
			if (commands[0].equals("mail")) {
				chatSender.mail(commands[1], chatSender.generateM(commands[2]));
				System.out.println("Mail successfully sent!");
			} else if (commands[0].equals("exit")) {
				System.out.println("Connection closed.");
				break;
			} else if (commands[0].equals("mailbox")) {
				chatSender.mailbox();
			} else {
				System.out.println("Please enter a vaild command");
				continue;
			}
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println("mail usage: mail user message");
			}

		} while (true);

		chatSender.close();
		System.exit(0);

	}

}
