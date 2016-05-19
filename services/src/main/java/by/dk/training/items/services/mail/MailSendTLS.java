package by.dk.training.items.services.mail;

public class MailSendTLS {
	
	private static Sender senderTLS = new Sender("denisov27111990@gmail.com", "php948409php");
	
	public static void main(String[] args) {
		
		senderTLS.send("Тестовой письмо", "Текст тестового письма", "denisov2006@inbox.ru");
	}

}
