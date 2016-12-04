import javax.jms.MessageConsumer;
import javax.jms.TextMessage;

public class JMSChatReceiver implements Runnable {

	private MessageConsumer consumer = null;

	public JMSChatReceiver(MessageConsumer consumer) {
		this.consumer = consumer;
	}

	@Override
	public void run() {
		TextMessage message;
		while (true) {
			try {
				message = (TextMessage) consumer.receive();
				if (message != null) {
					System.out.println(message.getText());
					message.acknowledge();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
