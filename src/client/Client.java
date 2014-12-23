package client;

import java.io.*;
import java.net.*;

import common.Data;
import common.DataTransferHelper;
import common.FileHelper;
import common.JHelp;


import server.Server;
import settings.Config;

import gui.JClient;

import static settings.Config.*;
import static localization.ClientLabelAndMsgs.*;

/**
 * Provides connection to {@link Server} object
 * @author Andrew Baliushin
 */
public class Client implements JHelp {
	
	private Socket socket;
	private ObjectOutputStream objectOutStream;
	private ObjectInputStream objectInStream;	

	static public void main(String[] args) {
		Client client = new Client();
		@SuppressWarnings("unused")
	    JClient jClient = new JClient(client);
	}

    /**
     * Set connection to default server with default parameters {@link Config}
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
        	return connect();
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
			
			objectOutStream = new ObjectOutputStream(socket.getOutputStream());
			objectInStream = new ObjectInputStream(socket.getInputStream());
			
			return JHelp.READY;
    	} catch (Exception e) {
    		System.err.println(SOCKET_CREATION_ERR_MSG);
			return JHelp.ERROR;
		}    	
    }
    
    public boolean isConnected() {
    	//don't use socket.isConnected() (return true if once was connected)
    	//or .isClosed() (true only if connection was closed from client side)
    	return (socket != null);
    }

    /**
     * Method gets data from data source
     * @param data initial object (template)
     * @return new object
     */
    public Data getData(Data data) {
        try {
			return DataTransferHelper.exchangeDataWithServ(data, objectInStream, objectOutStream);		
		} catch (IOException e) {
			e.printStackTrace();
			return Data.getErrorData(CONNECTION_PROBLEM);			
		}
    }

    /**
     * Method disconnects client and server
     * @return error code
     */
    public int disconnect() {
    	try {
			objectInStream.close();
		} catch (Exception ignore) {}
    	try {
			objectOutStream.close();
    	} catch (Exception ignore) {}
    	try {
			socket.close();
    	} catch (Exception ignore) {}
    	
    	socket = null;
    	
    	return JHelp.OK;
    }
    
    public String getHelpFileContent() {
    	try {
			return FileHelper.getTextFileContent(HELP_FILE_PATH);
		} catch (IOException e) {
			System.err.println(HELP_FILE_PROBLEM);
			return HELP_FILE_PROBLEM;
		}
	}  
}
