/*
 * Server.java
 *
 */
package jhelp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static settings.Config.*;
import static localization.LabelsAndMsges.*;

/**
 * This class sets a network connection between end client's objects
 * of {@link jhelp.Client} type and single {@link jhelp.ServerDb} object.
 * @author Andrew Baliushin
 * @version 1.0
 * @see jhelp.Client
 * @see jhelp.ClientThread
 * @see jhelp.ServerDb
 */
public class Server implements JHelp {
	
	private ServerSocket serverSocket;

    private List<Thread> clientsThreads;
 
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    private boolean listningForConnections;
    
    /**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	    Server server = new Server();
	    if (server.connect(args) == JHelp.OK) {
	        server.run();
	        server.disconnect();
	    }
	}

	/** Creates a new instance of Server */
    public Server() {
        this(DEFAULT_SERV_PORT, DEFAULT_DATABASE_PORT);
    }

    /**
     *
     * @param port
     * @param dbPort
     */
    public Server(int port, int dbPort) {
    	clientsThreads = new ArrayList<>();
    	
    	Thread kyeboardListner = new Thread(keyboardCommandListner());
    	kyeboardListner.start();
    	
        try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     *
     */
    protected void run() {
    	listningForConnections = true;
    	
        while(listningForConnections) {
        	try {
        		Socket newSocket = serverSocket.accept();
        		Thread newClient = new Thread(new ClientThread(this, newSocket));
        		clientsThreads.add(newClient);
        		newClient.start();
        	} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
    }
    
    protected Runnable keyboardCommandListner() {
    	return new Runnable() {
            @Override
            public void run() {
                @SuppressWarnings("resource")
				Scanner scan = new Scanner(System.in);
                String input = "";
                while (true) {                 
                    input = scan.nextLine();
                    Server.this.executeCommand(input);
                }                
            }
    	};
    }
    
    protected void executeCommand(String cmd) {
    	switch (cmd) {
		case EXIT_CMD:
			exit();
			break;
		default:
			System.out.println(COMMAND_NOT_FOUND);
    	}
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests. This method uses default parameters for connection.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect() {
        System.out.println("SERVER: connect");
        return OK;
    }

    /**
     * The method sets connection to database ({@link jhelp.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests.
     * @param args specifies properties of connection.
     * @return error code. The method returns {@link JHelp#OK} if connection are
     * openeds uccessfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect(String[] args) {
        System.out.println("SERVER: connect");
        return OK;
    }

    /**
     * Transports initial {@link Data} object from {@link ClientThread} object to
     * {@link ServerDb} object and returns modified {@link Data} object to
     * {@link ClientThread} object.
     * @param data Initial {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */
    public synchronized Data getData(Data data) {
    	Item[] items = {new Item("test1"), new Item("test2")};
        return new Data(DISCONNECT, new Item("test"), items);
    }

    /**
     * The method closes connection with database.
     * @return error code. The method returns {@link JHelp#OK} if a connection
     * with database ({@link ServerDb} object) closed successfully,
     * otherwise the method returns {@link JHelp#ERROR} or any error code.
     */
    public int disconnect() {
        System.out.println("SERVER: disconnect");
        return OK;
    }
    
    public void exit() {
    	disconnect();
    	System.exit(0);
    }
    
    public void stopAndRemoveThread(Thread clientThread) {
    	clientThread.interrupt();
    	clientsThreads.remove(clientThread);
    }
}
