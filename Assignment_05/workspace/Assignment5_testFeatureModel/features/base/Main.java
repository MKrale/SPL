public class Main {
    String base;
 
    Main() {  base = "base"; }
 
    void print() { 
       System.out.println( base ); 
    }
 
    /*****************/
  
    static Main me;
 
    public static void main( String[] args ) {
      me = new Main();
      me.print();
    }
}