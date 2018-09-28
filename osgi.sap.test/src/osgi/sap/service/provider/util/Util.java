package osgi.sap.service.provider.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

public class Util {
    public static void createDestinationDataFile(String destinationName, Properties connectProperties) {
        File destCfg = new File(destinationName+".jcoDestination");
        
//        System.out.println(destCfg.getAbsolutePath());
        
        try {
            FileOutputStream fos = new FileOutputStream(destCfg, false);
            
            connectProperties.store(fos, "connection properties");
            
            fos.close();
        }
        
        catch (Exception e) {
            throw new RuntimeException("Unable to create the destination files", e);
        }
    }
}