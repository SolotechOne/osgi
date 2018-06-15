package osgi.msg.service.interfaces;

public abstract interface MessageService {
	public static final int MESSAGE_TYPE_INFO = 1;
	
	public static final int MESSAGE_TYPE_WARNING = 2;
	
	public static final int MESSAGE_TYPE_ERROR = 3;
	
	public static final int MESSAGE_TYPE_ABORT = 4;
	
	public static final int MESSAGE_TYPE_QUESTION = 5;
	
	public static final int MESSAGE_TYPE_CAPTION = 6;
	
	public abstract void log(int level, int number, String text, String insert);
}