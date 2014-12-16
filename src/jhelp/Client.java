/*
 * Client.java
 *
 */
package jhelp;

import gui.JClient;

import java.awt.*;
import java.util.Properties;
import javax.swing.*;

/**
 * Client class provides users's interface of the application.
 * @author <strong >Y.D.Zakovryashin, 2009</strong>
 * @version 1.0
 */
public class Client implements JHelp {

    /**
     * Static constant for serialization
     */
    public static final long serialVersionUID = 1234;
    /**
     * Programm properties
     */
    private Properties prop;
    /**
     * Private Data object presents informational data.
     */
    private Data data;

    /**
     * Constructor with parameters.
     * @param args Array of {@link String} objects. Each item of this array can
     * define any client's property.
     */
    public Client(String[] args) {
    	JClient gui = new JClient();
    }
    
    
    
    /**
     * Method for application start
     * @param args agrgument of command string
     */
    static public void main(String[] args) {

        Client client = new Client(args);
        if (client.connect(args) == JHelp.OK) {
            client.run();
            client.disconnect();
        }
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
}
