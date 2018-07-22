package osgi.automic.shell.core.service.provider;

import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class CommandComponent {
	@SuppressWarnings("unused")
	private CommandProcessor processor;

	@Reference
	void setCommandProcessor(CommandProcessor component, Map<String, Object> properties) throws Exception {
		System.out.println("command processor found");
		
		this.processor = component;
		
		CommandSession session = processor.createSession(System.in, System.out, System.err);
		
		session.put("prompt", "api@aedev(4901)> ");

		session.getConsole().println("you");
		
		session.execute("lb");
	}
}