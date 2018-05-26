package osgi.event.internal;

public interface IMessages {
	public void add(String message);

	public void remove();
	
	public void list();
	
	public void reverse();
	
	public int size();
	
	public void clear();
}