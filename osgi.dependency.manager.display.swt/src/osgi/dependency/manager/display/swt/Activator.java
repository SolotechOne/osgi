package osgi.dependency.manager.display.swt;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.osgi.framework.BundleContext;

import osgi.dependency.manager.temperature.interfaces.TemperatureService;

public class Activator extends DependencyActivatorBase {

//	@Inject
	private volatile TemperatureService temperatureService;
	
	private Display display;
	private Shell shell;
	private Button button;
	private Text text;
	
	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
//	public void start(BundleContext bundleContext) throws Exception {
//		Activator.context = bundleContext;
//	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	@Override
	public void init(BundleContext context, DependencyManager manager) throws Exception {
		createUI();

		manager.add(createComponent()
				.setImplementation(this)
				.add(createServiceDependency()
						.setService(TemperatureService.class)
						.setAutoConfig(true)     // Needed because with callbacks, auto config is not enabled by default
						.setCallbacks("serviceAdded", "serviceRemoved"))
				);
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	@Override
    public void destroy(BundleContext bundleContext, DependencyManager dependencyManager) {
//		display.dispose();
    }

    @SuppressWarnings("unused")
	private void serviceAdded() {
    	System.out.println("serviceAdded");
    	
    	display.asyncExec( new Runnable() {
    		@Override
    		public void run() {
    			enableUI(true);
    		}
    	} );
    }

    @SuppressWarnings("unused")
	private void serviceRemoved() {
    	System.out.println("serviceRemoved");
    	
    	display.asyncExec( new Runnable() {
			@Override
			public void run() {
				enableUI(false);
			}
    	} );
    }
    
    private void enableUI(boolean on) {
        if (! on) {
            text.setText("");
        }
        button.setEnabled(on);
        text.setEnabled(on);
    }

    private void updateUI() {
        int temperature = temperatureService.getTemperature();

        text.setText("" + temperature + " \u2103");
    }
    
	private void createUI() {
		display = new Display();

		shell = new Shell(display);
		
		shell.setBounds(200, 400, 400, 200);

		shell.setLayout(new FillLayout(SWT.VERTICAL));
		
		
		button =  new Button(shell, SWT.PUSH);
		button.setText("refresh");

		//register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateUI();
			}
		});
		
		text = new Text(shell, SWT.NONE);
		
		enableUI(false);
		
		shell.open();
    }
}