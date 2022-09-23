public class DecoratorPatternTest {

	public static void main(String[] args) {
		Chat encryptedChat = new EncryptedChat(new BasicChat());
		encryptedChat.Assemble();
		System.out.println("\n*****");
		
		Chat coloredChat = new ColoredChat(new BasicChat());
		coloredChat.Assemble();
		
		System.out.println("End.");
		//System.close();
		
	}

}