package osgi.dependency.manager.display;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.BundleContext;

import osgi.dependency.manager.temperature.interfaces.TemperatureService;

public class Activator extends DependencyActivatorBase {

//	private static BundleContext context;

    private JFrame window;
    private volatile TemperatureService temperatureService;
    private JButton refreshButton;
    private JLabel contentLabel;

//	static BundleContext getContext() {
//		return context;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
//	 */
//	public void start(BundleContext bundleContext) throws Exception {
//		Activator.context = bundleContext;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
//	 */
//	public void stop(BundleContext bundleContext) throws Exception {
//		Activator.context = null;
//	}

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
	}
	
    @Override
    public void destroy(BundleContext bundleContext, DependencyManager dependencyManager) {
        window.dispose();
    }

    @SuppressWarnings("unused")
	private void serviceAdded() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                enableUI(true);
                updateUI();
            }
        });
    }

    @SuppressWarnings("unused")
	private void serviceRemoved() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                enableUI(false);
            }
        });
    }

    private void enableUI(boolean on) {
        if (! on) {
            contentLabel.setText("");
        }
        refreshButton.setEnabled(on);
        contentLabel.setEnabled(on);
    }

    private void updateUI() {
        int temperature = temperatureService.getTemperature();
        contentLabel.setText("" + temperature + " \u2103");
    }

    private void createUI() {
        final String empty = " ";

        JPanel panel = new JPanel();
        refreshButton = new JButton("refresh");
        panel.add(refreshButton);

        contentLabel = new JLabel(empty);
        contentLabel.setPreferredSize(new Dimension(80, contentLabel.getPreferredSize().height));
        contentLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        panel.add(contentLabel);

        window = new JFrame("temperature display");
        window.getContentPane().add(panel);

        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                updateUI();
            }
        });

        enableUI(false);
        window.pack();
        window.setVisible(true);
    }
}