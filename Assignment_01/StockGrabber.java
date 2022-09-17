import java.util.ArrayList;

public class StockGrabber implements Subject {

	private ArrayList<Observer> observers;
	private double ibmPrice;
	private double aaplPrice;
	private double googPrice;
	
	public StockGrabber() {
		
		observers = new ArrayList<Observer>();
		
	}
	
	// register a new observer
	public void register(Observer o) {

		observers.add(o);
		
	}

	// delete observer from observers list
	public void unregister(Observer o) {

		int observerIndex = observers.indexOf(o);
		observers.remove(observerIndex);
		
	}

	// notify all observers
	public void notifyObservers() {

		for(Observer observer : observers) {
			
			observer.update(ibmPrice, aaplPrice, googPrice);
		}
		
	}
	
	public void setIBMPrice(double newPrice) {
		
		this.ibmPrice = newPrice;
		notifyObservers();
		
	}
	
	
	public void setAAPLPrice(double newPrice) {
		
		this.aaplPrice = newPrice;
		notifyObservers();
		
	}
	
	
	public void setGOOGPrice(double newPrice) {
		
		this.googPrice = newPrice;
		notifyObservers();
		
	}
	
	

}
