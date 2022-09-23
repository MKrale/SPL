
public class EncryptedChat extends ChatDecorator {

	public EncryptedChat(Chat c) {
		super(c);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void Assemble() {
		super.Assemble();
		System.out.println("Encryption feature added");
	}

}
