package osgi.shell.core.command.internal;

import java.util.Dictionary;
import java.util.Hashtable;

import org.apache.felix.service.command.CommandProcessor;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import osgi.shell.core.command.FirstCommand;

public class Activator implements BundleActivator {
	private BundleContext context;

	public BundleContext getContext() {
		return context;
	}

	@Override
	public void start(BundleContext context) throws Exception {
		this.context = context;
		
		Dictionary<String, Object> properties = new Hashtable<>();
		
		properties.put(CommandProcessor.COMMAND_SCOPE , ":String=system");
		properties.put("osgi.command.function", new String[] { "closure" });
		
		context.registerService(FirstCommand.class, new FirstCommand(), properties);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// TODO Auto-generated method stub
	}
}