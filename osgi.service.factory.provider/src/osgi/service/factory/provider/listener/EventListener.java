package osgi.service.factory.provider.listener;

import java.io.IOException;
import java.time.LocalDateTime;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Reference;
import org.osgi.util.tracker.ServiceTracker;

import com.uc4.api.Form;
import com.uc4.api.TranslatedMessage;
import com.uc4.api.UC4ObjectName;
import com.uc4.communication.AsyncMessage;
import com.uc4.communication.DefaultNotificationListener;

import osgi.msg.service.interfaces.MessageService;

public class EventListener extends DefaultNotificationListener {
//	@Reference
	BundleContext context;
	
	ServiceTracker messageServiceTracker;
	
	MessageService messageservice;
	
	public EventListener() {
		this.context = FrameworkUtil.getBundle(EventListener.class).getBundleContext();

		messageServiceTracker = new ServiceTracker(context, MessageService.class.getName(), null);
		
//		System.out.println(messageServiceTracker);

		messageServiceTracker.open();
	}
	
	@Override
	public void activationMessages(int arg0, UC4ObjectName arg1, TranslatedMessage[] arg2) {
		// TODO Auto-generated method stub
		System.out.println("activationMessages()");
		super.activationMessages(arg0, arg1, arg2);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void activityListChanged() {
		// TODO Auto-generated method stub
//		System.out.println("activityListChanged()");
		super.activityListChanged();
	}

	@Override
	public void asyncMessage(AsyncMessage arg0) {
//		this.context = FrameworkUtil.getBundle(EventListener.class).getBundleContext();
		
//		System.out.println(FrameworkUtil.getBundle(EventListener.class).getBundleContext());
		
//		ServiceTracker messageServiceTracker = new ServiceTracker(context, MessageService.class.getName(), null);
		
//		System.out.println(messageServiceTracker);

//		messageServiceTracker.open();

		MessageService messageservice = (MessageService) messageServiceTracker.getService();

		if (messageservice != null)
			messageservice.log("aedev", MessageService.MESSAGE_TYPE_INFO, arg0.getNumber(), arg0.getText(), arg0.getInsert());
		else
			System.out.println("no logging service");

//		messageServiceTracker.close();
		
		// TODO Auto-generated method stub
//		System.out.println("asyncMessage()");
		
//		LocalDateTime _timestamp = LocalDateTime.of(
//				arg0.getDate().getYear(),
//				arg0.getDate().getMonth(),
//				arg0.getDate().getDay(),
//				arg0.getDate().getHour(),
//				arg0.getDate().getMinute(),
//				arg0.getDate().getSecond(),
//				0);
		
//		System.out.printf("%td.%<tm.%<tY %<tT %s%07d %s\n", _timestamp, arg0.getType(), arg0.getNumber(), arg0.getText());
//		System.out.println(arg0.getInsert());
		
//		super.asyncMessage(arg0);
	}

	@Override
	public void handleIOException(IOException arg0) {
		// TODO Auto-generated method stub
		System.out.println("handleIOException()");
		super.handleIOException(arg0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void highestCallOperatorChanged() {
		// TODO Auto-generated method stub
		System.out.println("highestCallOperatorChanged()");
		super.highestCallOperatorChanged();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processingStarted() {
		// TODO Auto-generated method stub
		System.out.println("processingStarted()");
		super.processingStarted();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void processingStopped() {
		// TODO Auto-generated method stub
		System.out.println("processingStopped()");
		super.processingStopped();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void promptInputRequired(int arg0) {
		// TODO Auto-generated method stub
		System.out.println("promptInputRequired()");
		super.promptInputRequired(arg0);
	}

	@Override
	public void read(Form arg0) {
		// TODO Auto-generated method stub
		System.out.println("read()");
		super.read(arg0);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void treeChanged() {
		// TODO Auto-generated method stub
		System.out.println("treeChanged()");
		super.treeChanged();
	}
}