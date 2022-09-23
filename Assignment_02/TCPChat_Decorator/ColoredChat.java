public class ColoredChat extends ChatDecorator {

	public ColoredChat(Chat c) {
		super(c);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void Assemble() {
		super.Assemble();
		System.out.println("Colored text feature added");
	}
}
