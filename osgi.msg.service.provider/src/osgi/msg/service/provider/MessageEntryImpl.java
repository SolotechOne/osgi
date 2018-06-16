package osgi.msg.service.provider;

import osgi.msg.service.interfaces.MessageEntry;

public class MessageEntryImpl implements MessageEntry {
	private final String system;
	
	private final long time;
	
	private final String insert;
	
	private final int number;
	
	private final String text;
	
	private final int type;
	
	public MessageEntryImpl(String system, String insert, int number, String text, int type) {
		this.system = system;
		this.insert = insert;
		this.number = number;
		this.text = text;
		this.type = type;
		this.time = System.currentTimeMillis();
	}
	
	@Override
	public String getSystem() {
		return this.system;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public String getInsert() {
		return this.insert;
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public String getText() {
		return this.text;
	}

	@Override
	public int getType() {
		return this.type;
	}
}