package osgi.msg.service.interfaces;

public abstract interface MessageEntry {
	public abstract long getTime();
	
	public abstract String getInsert();

	public abstract int getNumber();

	public abstract String getText();

	public abstract char getType();
}