package reconfigurator.executor;


public class Executor {

	public Executor() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * start new component
	 * Bundle dstBundle = ctx.installBundle(new component bundle file);
	 * dstBundle .start(); 
	 * @param component 
	 * @return void
	 */
	public void startNewComponent(String component) {
		
	}
	/**
	 * update properties (name = synchroneProp) of component instance
	 * ComponentInstance im;
	 * Properties props = new Properties();
	 * props.put("synchroneProp", "yes");
	 * im.reconfigure(props);
	 * @param property
	 * @param value
	 * @param component
	 * @return Void    
	 */
	public void updateProperty(String property, String value, String component) {
		
	}
	/**
	 * change connection of A to S2 instead of S1
	 * S1 and S2 implement Service 
	 * Component A uses interface Service from S1 (current conf)
	 * through a variable (e.g. Service sv)
	 * d is dependency of sv 
	 * Filter filter = FrameworkUtil.createFilter("(instance.name="+S2 instance name+")");
	 * d.setFilter(filter);
	 * 
	 */
	public void changeConnector(String componentName, 
			String placementComponent, 
			String replacementComponent) {
		
	}
	/**
	 * stop component instance (I) 
	 * Factory fct;
	 * instanceList = fct.getInstances
	 * search in instanceList for I
	 * i.stop()
	 * Bundle srcBundle = ctx.getBundle(srcComponentBundleFile);
	 * srcBundle.uninstall();
	 */
	public void stopComponent(String component) {
		
	}

}
