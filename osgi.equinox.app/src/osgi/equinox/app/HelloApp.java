package osgi.equinox.app;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class HelloApp implements IApplication {
	@Override
	public Object start(IApplicationContext context) throws Exception {
		System.out.println("Hello from Eclipse application");
		
		Thread.sleep(5000);
		
        return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
}