/*
 * Class ClientThread.
 */
package server;

import java.io.*;
import java.net.*;

import common.Data;
import common.JHelp;


import client.Client;


import static localization.ServerMsgs.*;

/**
 * This class provides a network connection between end client of
 * {@link client.Client} type and {@link server.Server} object. Every object of
 * this class may work in separate thread.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see client.Client
 * @see server.Server
 */
public class ClientThread implements JHelp, Runnable {

    
    private Server server;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    /**
     * Creates a new instance of Client
     * @param server reference to {@link Server} object.
     * @param socket reference to {@link java.net.Socket} object for connection
     * with client application.
     */
    public ClientThread(Server server, Socket socket) {
        this.server = server;
        this.clientSocket = socket;
        
		try {
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println(STREAMS_CREATION_ERR);
			e.printStackTrace();
		}
    }

    /**
     * The method defines main job cycle for the object.
     */
    public void run() {
        while( ! Thread.interrupted()) {
			try {
				sendDataToServerAndReturnAnswerToClient();
			} catch (StreamCorruptedException e) {
				System.err.println(CONNECTION_PROBLEM);
				disconnect();				
			} catch (EOFException e) {
				System.out.println(DISCONNECT);
				disconnect();
			}
        }
    }
    
    /**
     * Receive {@link Data} from {@link Client}, send to server, receive server answer 
     * as {@link Data} and send it to {@link Client}
     * @throws StreamCorruptedException - if connection down
     * @throws EOFException - when client disconnect
     */
    private void sendDataToServerAndReturnAnswerToClient() throws StreamCorruptedException, EOFException {
    	try {    		
			Data requestData = (Data) input.readObject();
			Data answerData = getData(requestData);
			output.writeObject(answerData);			
		} catch (StreamCorruptedException | EOFException e){
			throw e;
		} catch (ClassNotFoundException | IOException | ClassCastException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}    	
    }
    
    /**
     * Stub for Interface. Doesn't do anything.
     */
    public int connect() {
        return JHelp.OK;
    }

    /**
     * Stub for Interface. Doesn't do anything.
     */
    public int connect(String[] args) {
        return connect();
    }

    /**
     * Transports {@link Data} object from client application to {@link Server}
     * and returns modified {@link Data} object to client application.
     * @param data {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    public Data getData(Data data) {
       return server.getData(data);
    }

    /**
     * The method closes connection with client application. Errors ignored.
     * @return {@link JHelp#OK}
     */
    public int disconnect() {
        Thread.currentThread().interrupt();
        server.interuptAndRemoveClientThread(Thread.currentThread());
        
        try {
        	input.close();
        } catch (Exception ignore) {}
        try {
        	output.close();
        } catch (Exception ignore) {}
        try {
        	clientSocket.close();
        } catch (Exception ignore) {}
        
        return JHelp.OK;
    }
}
