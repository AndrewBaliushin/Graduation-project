package server_db;

import java.io.*;

import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import common.Commandable;
import common.Data;
import common.Item;
import common.JHelp;
import common.KeyboardCommandCaller;


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
    
    private Statement simpleStmt;
    private PreparedStatement prepStmtFindTerm;
    private PreparedStatement prepStmtFindTermDefinitions; 
	private PreparedStatement prepStmtInsertIntoDef;
	private PreparedStatement prepStmtInsertIntoTerm;
	private PreparedStatement prepStmtUpdataTermDef;
    
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
        createAndPrepareStatements();
        
        Thread cmdListner = new Thread(KeyboardCommandCaller.getListner(this));
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
    
    private void createAndPrepareStatements() {
    	try {
    		prepStmtFindTerm = dbConnection.prepareStatement(FIND_TERM);
    		prepStmtInsertIntoDef = dbConnection.prepareStatement(INSERT_NEW_DEF);
    		prepStmtInsertIntoTerm = dbConnection.prepareStatement(INSERT_NEW_TERM);
    		prepStmtUpdataTermDef = dbConnection.prepareStatement(UPDATE_DEFINITION);
    		
			prepStmtFindTermDefinitions = dbConnection
					.prepareStatement(FIND_QRY);
			
			simpleStmt = dbConnection.createStatement();
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
     * @param data object of {@link common.Data} type with request to database.
     * @return object of {@link common.Data} type with results of request to a
     * database.
     * @see Data
     */
    public Data getData(Data data) {
        if(data.getOperation() == JHelp.SELECT) {
        	return findDefinitionsForTerm(data);
        } else if (data.getOperation() == JHelp.INSERT) {
        	return addNewEntryToDB(data);
        } else if (data.getOperation() == JHelp.UPDATE) {
        	return updateDefinition(data);
        } else {
            return Data.getErrorData(UNKNOW_OPERATION);
        }
    }
    
    private Data findDefinitionsForTerm(Data data) {
		String term = data.getKey().getItem();
		
		if(term.trim().isEmpty()) {
			return Data.getErrorData(REQUSET_WITH_EMPTY_ARG_MSG);
		}    	
		
		data.setValues(null);
		
		List<Item> tmp = new ArrayList<>();
		
		try {
			prepStmtFindTermDefinitions.setString(1, term);
			
			prepStmtFindTermDefinitions.execute();
			ResultSet rs = prepStmtFindTermDefinitions.getResultSet();
			while (rs.next()) {
				String definition = rs.getString(1);
				int defId = rs.getInt(3);
				tmp.add(new Item(defId, definition, JHelp.ORIGIN));
			}

			if (tmp.isEmpty()) {
				return Data.getErrorData(TERM_NOT_FOUND);
			} else {
				data.setValues(tmp.toArray(new Item[tmp.size()]));
				return data;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return Data.getErrorData(ERROR_DURING_DB_OPERATION);
		}
	}

	private Data addNewEntryToDB(Data data) {
    	String term = data.getKey().getItem();
    	String def = data.getValue(0).getItem();
    	
    	term = term.trim();
    	def = def.trim();
    	
    	if(term.isEmpty() || def.isEmpty()) {
    		return Data.getErrorData(REQUSET_WITH_EMPTY_ARG_MSG);
    	}
    	
    	try {
			if(isTermExist(term)) {
				return Data.getErrorData(REQUSET_TO_ADD_EXISTING);
			}
			
			int newDefId = getDefMaxId() + 1;
			int newTermId = getTermMaxId() + 1;
			
			prepStmtInsertIntoTerm.setInt(1, newTermId);
			prepStmtInsertIntoTerm.setString(2, term);
			
			prepStmtInsertIntoDef.setInt(1, newDefId);
			prepStmtInsertIntoDef.setString(2, def);
			prepStmtInsertIntoDef.setInt(3, newTermId);	
			
			prepStmtInsertIntoTerm.executeUpdate();
			prepStmtInsertIntoDef.executeUpdate();	
			
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return Data.getErrorData(ERROR_DURING_DB_OPERATION);
		}
    }
    
    private Data updateDefinition(Data data) {
		String def = data.getValue(0).getItem();
		int id = data.getValue(0).getId();
		
		def = def.trim();
		
		if(def.isEmpty()) {
			return Data.getErrorData(REQUSET_WITH_EMPTY_ARG_MSG);
		}
		if(id == 0) {
			return Data.getErrorData(NO_ID_IN_DEFINITION);
		}
		
		try {			
			prepStmtUpdataTermDef.setString(1, def);
			prepStmtUpdataTermDef.setInt(2, id);
			
			System.out.println(id);
			System.out.println(prepStmtUpdataTermDef.executeUpdate());
			
			return data;
		} catch (SQLException e) {
			e.printStackTrace();
			return Data.getErrorData(ERROR_DURING_DB_OPERATION);
		}
	}

	private boolean isTermExist(String term) throws SQLException {
    	prepStmtFindTerm.setString(1, term);
    	prepStmtFindTerm.execute();
    	ResultSet rs = prepStmtFindTerm.getResultSet();
    	return rs.next(); //if rs have rows
    }
    
    private int getTermMaxId() throws SQLException {
    	return getMaxId(MAX_TERM_ID);
    }
    
    private int getDefMaxId() throws SQLException {
    	return getMaxId(MAX_DEF_ID);
    }
    
    private int getMaxId(String sql) throws SQLException {
    	simpleStmt.execute(sql);
    	ResultSet rs = simpleStmt.getResultSet();
    	if(rs.next()) {
    		return rs.getInt(1); //MAX(id)
    	} else {
    		return 0;
    	}
    }
    
    /**
     * Disconnects <code>ServerDb</code> object from a database and closes all 
     * opened sockets. Errors ignored.
     * @return {@link JHelp#DISCONNECT}
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
