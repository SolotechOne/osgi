package osgi.service.factory.provider.listener;

import com.uc4.communication.IKickEventListener;
import com.uc4.communication.KickEvent;

public class KickEventListener implements IKickEventListener {
	@Override
	public void kickPerformed(KickEvent arg0) {
		System.out.println(arg0 + " " + arg0.getOpcode());
	}
}