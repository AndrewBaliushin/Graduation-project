package jhelp;

import java.io.*;
import java.net.*;

import gui.JClient;

import static settings.Config.*;
import static localization.LabelsAndMsges.*;

/**
 * Client class provides users's interface of the application.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 */
public class Client implements JHelp {
	
	private Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;

    /**
     * Private Data object presents informational data.
     */
    private Data dataContainer;
	

    /**
	 * Method for application start
	 * @param args agrgument of command string
	 */
	static public void main(String[] args) {
		Client client = new Client();
		@SuppressWarnings("unused")
	    JClient jClient = new JClient(client);
	}
    
    /**
     * Method define main job cycle
     */
    public void run() {
        System.out.println("Client: run");
    }

    /**
     * Set connection to default server with default parameters
     * @return Status code from {@link JHelp}
     */
    public int connect() {
        return connect(DEFAULT_SERV_HOST, DEFAULT_SERV_PORT);
    }

    /**
     * Connect to server using
     * args[] where args[0] -- host name, args[1] -- port(String)
     */
    public int connect(String[] args) {
        if(args.length < 2) {
        	return JHelp.ERROR;
        } 
        try {
        	int port = Integer.parseInt(args[1]);
        	return connect(args[0], port);
        } catch (NumberFormatException e) {
        	return JHelp.ERROR;
        }
    }
    
    /**
     * Connect to server by host and port.
     * @param host 
     * @param port
     * @return Status code from {@link JHelp}
     */
    public int connect(String host, int port) {
    	if(isConnected()) {
    		disconnect();
    	}
    	
    	try {
			socket = new Socket(host, port);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			
			return JHelp.READY;
    	} catch (Exception e) {
    		System.err.println(SOCKET_CREATION_ERR_MSG);
			return JHelp.ERROR;
		}    	
    }
    
    //FIXME isConnected!!
    public boolean isConnected() {
    	//don't use socket.isConnected() (true if once was connected)
    	//or .isClosed() (true only if client closed connection
    	return (socket != null);
    }

    /**
     * Method gets data from data source
     * @param data initial object (template)
     * @return new object
     */
    public Data getData(Data data) {
        try {
			return requestDataFromServ(data);			
		} catch (IOException e) {
			e.printStackTrace();
			return Data.getErrorData(CONNECTION_PROBLEM);			
		}
    }
    
    private Data requestDataFromServ(Data reqData) throws StreamCorruptedException {
    	try {    
    		oos.writeObject(reqData);		
			Data answerData = (Data) ois.readObject();
			return answerData;
		} catch (StreamCorruptedException e){
			throw e;
		} catch (ClassNotFoundException | IOException | ClassCastException e) {
			System.err.println(OBJECT_STREAM_ERROR);
			e.printStackTrace();
			return null;
		}    	
    }

    /**
     * Method disconnects client and server
     * @return error code
     */
    public int disconnect() {
    	if(isConnected()) {
			try {
				oos.close();
				ois.close();
				socket.close();
				System.out.println(DISCONNECTED_MSG);
				return JHelp.OK;
			} catch (IOException e) {
				System.err.println(SOCKET_CLOSE_ERR_MSG);
				return JHelp.ERROR;
			} catch (NullPointerException e) {
				System.err.println(SOCKET_NULL_POINTER_MSG);
				return JHelp.OK;
			}
    	} else {
    		return JHelp.OK;
    	}
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
    
    public Data findRequest(Data data) {
		return null;    	
    }
    
    public Data addRequest() {
    	return null;
    }
    
    public Data editRequset() {
    	return null;
    }
}
