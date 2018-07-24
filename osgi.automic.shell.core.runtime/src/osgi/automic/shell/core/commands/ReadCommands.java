package osgi.automic.shell.core.commands;

import java.io.BufferedReader;
import java.io.IOException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.apache.felix.service.command.CommandProcessor;
import org.apache.felix.service.command.CommandSession;
import org.apache.felix.service.command.Descriptor;
import org.osgi.service.component.annotations.Component;

@Component(
	property = {
		CommandProcessor.COMMAND_SCOPE + ":String=test",
		CommandProcessor.COMMAND_FUNCTION + ":String=readline"
	},
	service = ReadCommands.class
)
public class ReadCommands {
    @Descriptor("read console input")
    public void readline(CommandSession session) throws IOException {
    	System.out.print("please enter username: ");
    	
    	Object prompt = session.get("prompt");
    	
    	session.put("prompt", "> ");
    	    	
    	PrintStream out = session.getConsole();
    	InputStream in = session.getKeyboard();
    	
    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuffer text = new StringBuffer();
        
        String line = br.readLine();
        
//        for ( String line; (line = br.readLine()) != null; )
//            text.append(line);
        
        System.out.println(line);
        		
//    	out.flush();
    	
    	session.put("prompt", prompt);
    }
}