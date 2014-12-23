/*
 * Class ClientThread.
 */
package jhelp;

import java.io.*;
import java.net.*;

import static localization.ClientLabelAndMsgs.*;

/**
 * This class provides a network connection between end client of
 * {@link jhelp.Client} type and {@link jhelp.Server} object. Every object of
 * this class may work in separate thread.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.Server
 */
public class ClientThread implements JHelp, Runnable {

    /**
     *
     */
    private Server server;
    /**
     *
     */
    private Socket clientSocket;
    /**
     *
     */
    private ObjectInputStream input;
    /**
     *
     */
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
			// TODO Auto-generated catch block
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
				System.out.println(DISCONNECTED_MSG);
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
			System.err.println(OBJECT_STREAM_ERROR);
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}    	
    }
    
    /**
     * Opens input and output streams for data interchanging with
     * client application.  The method uses default parameters.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect() {
    	//TODO
        System.out.println("Already connected");
        return JHelp.OK;
    }

    /**
     * Opens input and output streams for data interchanging with
     * client application. This method uses parameters specified by parameter
     * <code>args</code>.
     * @param args defines properties for input and output streams.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
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
     * The method closes connection with client application.
     * @return error code. The method returns {@link JHelp#OK} if input/output 
     * streams and connection with client application was closed successfully,
     * otherwise the method returns {@link JHelp#ERROR}.
     */
    public int disconnect() {
        Thread.currentThread().interrupt();
        server.interuptAndRemoveThread(Thread.currentThread());
        
        try {
			input.close();
			output.close();
	        clientSocket.close();
		} catch (IOException e) {
			System.err.println(SOCKET_CLOSE_ERR_MSG);
		}
        
        return JHelp.OK;
    }
}
