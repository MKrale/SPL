import javax.swing.JPanel;

public aspect Base {
	after(): execution(void sendString(String )){
		System.out.println("Test Base");
	}
	before(TCPChat chat, JPanel p) : call(JPanel extend_ChatUI(JPanel)) && this(chat) && target(p){
		System.out.println("Test JPanel");
		System.out.println(thisJoinPoint);
		System.out.println(thisJoinPoint.getSignature());
		System.out.println(thisJoinPoint.getKind());
		System.out.println(thisJoinPoint.getSourceLocation());
	}
}