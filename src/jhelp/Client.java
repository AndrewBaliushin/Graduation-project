package jhelp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import gui.JClient;

import static settings.Config.*;

/**
 * Client class provides users's interface of the application.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 */
public class Client implements JHelp {
	
	private int serverPort;
	private String serverIP;
	
	private Socket socket;
	private InputStream in;
	private OutputStream out;	

    /**
     * Private Data object presents informational data.
     */
    private Data data;

    /**
	 * Method for application start
	 * @param args agrgument of command string
	 */
	static public void main(String[] args) {
	
	    Client client = new Client();
	    
	    if (client.connect(args) == JHelp.OK) {
	        client.run();
	        client.disconnect();
	    }
	}

	/**
     * Constructor with parameters.
     * @param args Array of {@link String} objects. Each item of this array can
     * define any client's property.
     */
    public Client() {
	    @SuppressWarnings("unused")
		JClient jClient = new JClient(this);
    }
    
    /**
     * Method define main job cycle
     */
    public void run() {
        System.out.println("Client: run");
    }

    /**
     * Method set connection to default server with default parameters
     * @return error code
     */
    public int connect() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Method set connection to server with parameters defines by argument 
     * <code>args</code>
     * @return error code
     */
    public int connect(String[] args) {
        System.out.println("Client: connect");
        return JHelp.ERROR;
    }
    
    public boolean isConnected() {
    	return (socket != null && socket.isConnected());
    }

    /**
     * Method gets data from data source
     * @param data initial object (template)
     * @return new object
     */
    public Data getData(Data data) {
        System.out.println("Client: getData");
        return null;
    }

    /**
     * Method disconnects client and server
     * @return error code
     */
    public int disconnect() {
        System.out.println("Client: disconnect");
        return JHelp.ERROR;
    }
    
    public String getHelpFileContent() {
    	try {
			return FileOpearations.getTextFileContent(HELP_FILE_PATH);
		} catch (IOException e) {
			System.err.println(Thread.currentThread().getStackTrace()[1]
					.getMethodName() + " error " + e.getMessage());
			return "Help file error";
		}
	}    
}
