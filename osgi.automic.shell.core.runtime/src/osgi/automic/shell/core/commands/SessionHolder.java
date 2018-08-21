package osgi.automic.shell.core.commands;

import java.util.List;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Function;
import org.osgi.service.component.annotations.Component;

//@Component(
//	property = {
//		CommandProcessor.COMMAND_SCOPE + ":String=session",
//		CommandProcessor.COMMAND_FUNCTION + ":String=execute"
//	},
//	service=SessionHolder.class
//)
@Component
public class SessionHolder implements Function {
	@Override
	public Object execute(CommandSession session, List<Object> arguments) throws Exception {
		Object prompt = session.get("prompt");
		
		System.out.println("prompt is: " + prompt);
		
        if (prompt instanceof Function) {
        	System.out.println("prompt is function");
        	
            try {
                prompt = ((Function) prompt).execute(session, null);
            }
            catch (Exception e) {
                System.out.println(prompt + ": " + e.getClass().getSimpleName() + ": " + e.getMessage());
                prompt = null;
            }
        }
        
		session.put("prompt", "\\#prompt={prompt=\"test> \"}");
		
		return null;
	}
}