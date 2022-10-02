public class Starter {

	public static void main(String[] args) {

		if (args.length != 1)
			System.out.println("Plugin name not specified");
		else {
			String pluginName = args[0];
			try {
				Class<?> pluginClass = Class.forName(pluginName);
				new TCPChat((Plugin) pluginClass.newInstance()).setVisible(true);
				} 
			catch (Exception e) {
		
				System.out.println("Cannot load plugin " + pluginName + ", reason: " + e);
			}
		}
	}
}
