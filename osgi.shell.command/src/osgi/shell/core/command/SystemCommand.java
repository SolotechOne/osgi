package osgi.shell.core.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Descriptor;

import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=system",
		CommandProcessor.COMMAND_FUNCTION + ":String=prompt",
		CommandProcessor.COMMAND_FUNCTION + ":String=tail",
		CommandProcessor.COMMAND_FUNCTION + ":String=uname"
	},
	service=SystemCommand.class
)
public class SystemCommand {
	private BundleContext context;
	
	@Activate
	public void activate(BundleContext context) {
		this.context = context;
	}
	
	@Descriptor("change prompt")
	public void prompt(CommandSession session) throws Exception {
		session.put("prompt", "fuck> ");
		
		session.getConsole().println("you");
		
		session.execute("lb");
	}

	@Descriptor("tail command")
	public List<String> tail(CommandSession session, String... args) throws IOException {
//		List<String> argList = new LinkedList<>(Arrays.asList(args));
//		
//		String str1 = argList.get(0);
//		
//		System.out.println(str1);
		
		
		List<String> lines = new LinkedList<String>();
		
		if (Thread.currentThread().getName().contains("pipe")) {
			BufferedReader rdr = new BufferedReader(new InputStreamReader(System.in));
			
//			String line;
//			
//			while ((line = rdr.readLine()) != null) { // while loop begins here
////				System.out.println("<<<" + line + ">>>");
//				
//				lines.add(line);
//			}
						
			
			String lastLine = null;
			
			while (true) {
				String line = rdr.readLine();
				
				if (line == null) {
					if(lastLine != null && lastLine.isEmpty()){
						lines.remove(lines.size()-1);
					}
					
					break;
				}
				
				lines.add(line);
				
//				System.out.println(line);
				
				lastLine = line;
			}
			
			rdr.close();
		}

//		System.out.println(lines);

//		lines.remove(lines.size()-1);
		
		return lines;
	}

	@Descriptor("display system properties")
	public void uname() {
		String vendor = context.getProperty(Constants.FRAMEWORK_VENDOR);
        String version = context.getProperty(Constants.FRAMEWORK_VERSION);
        String osName = context.getProperty(Constants.FRAMEWORK_OS_NAME);
        String osVersion = context.getProperty(Constants.FRAMEWORK_OS_VERSION);
        
        System.out.println(vendor + " " + version + " (" + osName + " " + osVersion + ")");
	}
}