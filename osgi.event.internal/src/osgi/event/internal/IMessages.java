package osgi.event.internal;

import java.util.LinkedList;

public interface IMessages {
	public void add(String message);

	public void remove();
	
	public void list();
	
	public LinkedList<String> llist();
	
	public void reverse();
	
	public int size();
	
	public void clear();
}