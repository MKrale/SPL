 class  Main {
	
    String more;

	
 
    Main  () { more = "more";  last = "last"; }

	
 
     private void  print__wrappee__Colour  () {
        original();
        System.out.println( more );
    }

	
 
    void print() {
        print__wrappee__Colour();
        System.out.println( last );
    }

	
    String last;


}
