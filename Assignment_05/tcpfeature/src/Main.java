

/**
 * Encryption feature main 
 * By: Roel Duijsings
 * TODO description
 */
public class Main {
   protected void print() {
	   System.out.print("Encryption");
   }

   public static String rot13(String input) {
       StringBuilder sb = new StringBuilder();
       for (int i = 0; i < input.length(); i++) {
           char c = input.charAt(i);
           if (c >= 'a' && c <= 'm') c += 13;
           else if (c >= 'A' && c <= 'M') c += 13;
           else if (c >= 'n' && c <= 'z') c -= 13;
           else if (c >= 'N' && c <= 'Z') c -= 13;
           sb.append(c);
       }
       return sb.toString();
   }

   public static String reverse(String input) {
       StringBuilder input1 = new StringBuilder();
       input1.append(input);

       // reverse StringBuilder input1
       input1.reverse();
       return input1.toString();
   }
   static void sendString(String s) {
	   original();
	   synchronized (toSend) {
		   s = rot13(reverse(s));		
	   }
	   toSend.append(s + "\n");   
   }
}