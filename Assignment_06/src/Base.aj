import javax.swing.JPanel;

public aspect Base {
	after(String string): execution(void sendString(String )) && args(string){
		System.out.println("Test Base");
		System.out.println(string);
	}
	
	before(TCPChat chat, JPanel p) : (execution(JPanel TCPChat.extend_ChatUI(JPanel)) && this(chat) && target(p)){
		System.out.println("Test JPanel");
		System.out.println(thisJoinPoint);
		System.out.println(thisJoinPoint.getSignature());
		System.out.println(thisJoinPoint.getKind());
		System.out.println(thisJoinPoint.getSourceLocation());
	}
}