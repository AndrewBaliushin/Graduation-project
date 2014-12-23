package server;

import java.io.*;
import java.net.*;
import java.util.*;

import common.*;

import server_db.ServerDb;


import static settings.Config.*;
import static localization.ServerMsgs.*;

/**
 * This class sets a network connection between many {@link client.Client} objects
 * and single {@link server_db.ServerDb} object.
 * @author Andrew Baliushin
 * @see client.Client
 * @see server.ClientThread
 * @see server_db.ServerDb
 */
public class Server implements JHelp, Commandable {
	
	private ServerSocket serverSocket;

    private List<Thread> clientsThreads;
 
    private Socket socketToDBServ;
    private ObjectInputStream inputFromDBServ;
    private ObjectOutputStream outputToDBServ;
    
	public static void main(String[] args) {
	    Server server = new Server();
	    server.run();
	}

	/** Construct with default settings {@link settings.Config} */
    public Server() {
        this(DEFAULT_SERV_PORT, DEFAULT_DATABASE_PORT);
    }

    /**
     * Construct with user settings
     * @param port -- port to run server
     * @param dbPort -- port of ServerDB
     */
    public Server(int port, int dbPort) {
    	clientsThreads = new ArrayList<>();
    	
    	Thread cmdListner = new Thread(KeyboardCommand.getListner(this));
    	cmdListner.start();
    	
        createServerSocket(port);
        
        connect();
    }
    
    public void run() {
	    while(true) {
	    	try {
	    		Socket newSocket = serverSocket.accept();
	    		Thread newClient = new Thread(new ClientThread(this, newSocket));
	    		clientsThreads.add(newClient);
	    		newClient.start();
	    	} catch (IOException e) {
				System.err.println(CLIENT_SOCKET_CREATION_ERR);
				e.printStackTrace();
			}
	    }
	}

	public void executeCommand(String cmd) {
		switch (cmd) {
		case EXIT_CMD:
			disconnect();
	    	System.exit(0);
			break;
		default:
			System.out.println(COMMAND_NOT_FOUND);
		}
	}

	private void createServerSocket(int port) {
    	try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println(SERVER_SOCKET_CREATION_ERR);
			e.printStackTrace();
		}
    }
    
    /**
     * The method sets connection to database ({@link server_db.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests. This method uses default parameters for connection.
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * successfully opened, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect() {
        return connect(new String[]{DEFAULT_DB_SERV_HOST,
        		Integer.toString(DEFAULT_DB_SERV_PORT)});
    }

    /**
     * The method sets connection to database ({@link server_db.ServerDb} object) and
     * create {@link java.net.ServerSocket} object for waiting of client's
     * connection requests.
     * @param args specifies properties of connection.
     * @return error code. The method returns {@link JHelp#OK} if connection are
     * openeds uccessfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect(String[] args) {
    	if(args.length < 2) {
        	return connect();
        } 
    	
    	String dbHost = args[0];
    	int dbPort = Integer.parseInt(args[1]);
    	
    	try {
			socketToDBServ = new Socket(dbHost, dbPort);
			
			outputToDBServ = new ObjectOutputStream(socketToDBServ.getOutputStream());
			inputFromDBServ = new ObjectInputStream(socketToDBServ.getInputStream());
			return JHelp.OK;
    	} catch (Exception e) {
    		System.err.println(SERVER_DB_CONNECTION_ERR);
    		return JHelp.ERROR;
		}    
    }

    /**
     * Transports initial {@link Data} object from {@link ClientThread} object to
     * {@link ServerDb} object and returns modified {@link Data} object to
     * {@link ClientThread} object.
     * @param data Initial {@link Data} object which was obtained from client
     * application.
     * @return modified {@link Data} object
     */   
    public Data getData(Data data) {
        try {
			return DataTransferHelper.exchangeDataWithServ(data, inputFromDBServ, outputToDBServ);			
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
			return Data.getErrorData(CONNECTION_TO_SDB_PROBLEM);			
		}
    }

    /**
     * The method closes connection with database.
     * @return error code. The method returns {@link JHelp#OK} if a connection
     * with database ({@link ServerDb} object) closed successfully,
     * otherwise the method returns {@link JHelp#ERROR} or any error code.
     */
    public int disconnect() {
    	for(Thread t : clientsThreads) {
    		interuptAndRemoveClientThread(t);
    	}
    	
    	try {
			inputFromDBServ.close();
		} catch (Exception ignore) {}
    	try {
			outputToDBServ.close();
    	} catch (Exception ignore) {}
    	try {
			socketToDBServ.close();
    	} catch (Exception ignore) {}
    	try {
			serverSocket.close();
    	} catch (Exception ignore) {}
    	
        return OK;
    }
    
    public void interuptAndRemoveClientThread(Thread clientThread) {
    	clientThread.interrupt();
    	clientsThreads.remove(clientThread);
    }
}
