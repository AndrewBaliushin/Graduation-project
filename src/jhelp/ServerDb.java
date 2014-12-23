package jhelp;

import java.io.*;

import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import settings.Config;

import static localization.ServerDBmsgs.*;
import static localization.ServerMsgs.COMMAND_NOT_FOUND;
import static settings.Config.*;
import static settings.SQLqueries.*;


/**
 * This class presents server directly working with database.
 * @author Andrew Baliushin
 */
public class ServerDb implements JHelp, Commandable {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    private Connection dbConnection;
    
    private PreparedStatement prepStmtFindTermDefinitions;    
    
	public static void main(String[] args) {
	    ServerDb sdb = new ServerDb();
	    sdb.run();
	}

	/**
     * Creates a new instance of <code>ServerDb</code> 
     * with {@link Config#DEFAULT_DB_SERV_PORT}
     */
    public ServerDb() {
        this(DEFAULT_DB_SERV_PORT);
    }

    /**
     * Constructor creates new instance of <code>ServerDb</code>. 
     * @param port defines port for {@link java.net.ServerSocket} object.
     */
    public ServerDb(int port) {
        createServerSocket(port);
        loadDbDriver();
        connect(); //to db
        prepareStatements();
        
        Thread cmdListner = new Thread(KeyboardCommand.getListner(this));
    	cmdListner.start();
    }
    
    /**
	 * Process client requests.
	 */
	public void run() {
		while(true) {
			if (clientSocket == null || clientSocket.isClosed()) {
				acceptConnection();
			}
			executeClientRequest();			
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
			System.err.println(SOCKET_CREATION_ERR);
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
    }

    private void loadDbDriver() {
		try {
	        Class.forName(DB_DRIVER);
	    } catch (ClassNotFoundException e) {
	    	System.err.println(DB_DRIVER_LOAD_ERR);
	    	System.err.println(e.getMessage());
	        e.printStackTrace();
	    }
	}
    
    private void prepareStatements() {
    	try {
			prepStmtFindTermDefinitions = dbConnection
					.prepareStatement(FIND_QRY);
		} catch (SQLException e) {
			System.err.println(STMT_PREPARATION_ERR);
	    	System.err.println(e.getMessage());
	        e.printStackTrace();
		}
    }
    
    private void acceptConnection() {
		try {
			clientSocket = serverSocket.accept();
			input = new ObjectInputStream(clientSocket.getInputStream());
			output = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			System.err.println(SOCKET_CREATION_ERR);
	    	System.err.println(e.getMessage());
	        e.printStackTrace();
		}
	}
    
    private void executeClientRequest() {
    	try {    		
			Data requestData = (Data) input.readObject();
			
			Data answerData = getData(requestData);
			
			output.writeObject(answerData);			
		} catch (StreamCorruptedException | EOFException e){
			System.out.println(CLIENT_CONNECTION_LOST);
			killClientConnection();
		} catch (ClassNotFoundException | IOException | ClassCastException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}    	
    }
    
    private void killClientConnection() {
    	try {
			input.close();
			output.close();
	        clientSocket.close();
		} catch (IOException e) {
			System.err.println(SOCKET_CLOSE_ERR);
		} finally {
			clientSocket = null;
		}
    }

	/**
	 * Connects to DB using default {@link Config}
     * @return error code. The method returns {@link JHelp#OK} if streams are
     * opened successfully, otherwise the method returns {@link JHelp#ERROR}.
     */
    public int connect() {
    	try {
            dbConnection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
            return JHelp.READY;
        } catch (SQLException e) {
        	System.err.println(DB_CONNECT_ERR);
	    	System.err.println(e.getMessage());
	        e.printStackTrace();
	        return JHelp.ERROR;
        }
    }

    /**
   	 * Straight redirect to {@link #connect()}
     */
    public int connect(String[] args) {
    	return connect();    	
    }

    /**
     * Method returns result of client request to a database.
     * @param data object of {@link jhelp.Data} type with request to database.
     * @return object of {@link jhelp.Data} type with results of request to a
     * database.
     * @see Data
     */
    public Data getData(Data data) {
        if(data.getOperation() == JHelp.SELECT) {
        	return findDefinitionsForTerm(data);
        }
        return Data.getErrorData("Unknown operation");
    }
    
    private Data findDefinitionsForTerm(Data rqstData) {
    	String term = rqstData.getKey().getItem();
    	
    	if(term.trim().isEmpty()) {
    		return Data.getErrorData(REQUSET_FOR_EMPTY_TERM_MSG);
    	}    	
    	
    	rqstData.setValues(null);
    	
    	List<Item> tmp = new ArrayList<>();
    	
		try {
			prepStmtFindTermDefinitions.setString(1, term);
			if(prepStmtFindTermDefinitions.execute()) {
				ResultSet rs = prepStmtFindTermDefinitions.getResultSet();
				while(rs.next()){
					String definition = rs.getString(1);
					int defId = rs.getInt(3);
					tmp.add(new Item(defId, definition, JHelp.ORIGIN));
				}
				rqstData.setValues(tmp.toArray(new Item[tmp.size()]));
				return rqstData;
			} else {
				return Data.getErrorData("Can't find definition");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Data.getErrorData(ERROR_DURING_DB_OPERATION);
		}
	}

	/**
     * Method disconnects <code>ServerDb</code> object from a database and closes
     * {@link java.net.ServerSocket} object.
     * @return disconnect result. Method returns {@link #DISCONNECT} value, if
     * the process ends successfully. Othewise the method returns error code,
     * for example {@link #ERROR}.
     * @see jhelp.JHelp#DISCONNECT
     * @since 1.0
     */
    public int disconnect() {
        try {
			clientSocket.close();
		} catch (Exception ignore) {}
        try {
			serverSocket.close();
		} catch (Exception ignore) {}
        try {
			prepStmtFindTermDefinitions.close();
		} catch (Exception ignore) {}
        try {
			dbConnection.close();
		} catch (Exception ignore) {}
        return JHelp.DISCONNECT;
    }
}
