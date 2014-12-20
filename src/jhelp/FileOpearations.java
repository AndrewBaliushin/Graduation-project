package jhelp;

import java.io.*;
import java.net.*;
import java.util.*;

import static settings.Config.*;

public class FileOpearations {	
	
	public static InetSocketAddress getIpAndPortFromConfigFile(File cfg) 
			throws IOException {
		String[] fileContent = getFileContent(cfg);
		Map<String, String> config = parseConfig(fileContent);
		
		String ip = config.get(IP_CONFIG_PARAM_NAME);
		String port = config.get(PORT_CONFIG_PARAM_NAME);
		int portInt = Integer.parseInt(port);
		InetAddress iAdress = InetAddress.getByName(ip);
		InetSocketAddress socketAdress = new InetSocketAddress(iAdress, portInt);
		
		return socketAdress;
	}
	
	private static String[] getFileContent(File file) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		List<String> lines = new ArrayList<>();
		
		String line = br.readLine();
	    while (line != null) {
	        lines.add(line);
	        line = br.readLine();
	    }
	    
	    br.close();
		return lines.toArray(new String[lines.size()]);
	}

	private static Map<String, String> parseConfig(String[] content) {
		Map<String, String> config = new HashMap<>();
		
		for(String l : content) {
			if(l.charAt(0) == CFG_COMMENT_LINE_CHAR) {
				continue;
			}
			String[] conf = l.split("=");
			config.put(conf[0], conf[1]);
		}
		
		return config;		
	}

}
