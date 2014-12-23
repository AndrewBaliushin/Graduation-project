package jhelp;

import static localization.ClientLabelAndMsgs.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

public class DataTransferHelper {

	/**
	 * Sends object of {@link Data} to {@link ObjectOutputStream}, receive 
	 * it back through {@link ObjectInputStream} with modifications made by server
	 * and return it.
	 * @param reqData
	 * @param in
	 * @param out
	 * @return modified Data container with server answer
	 * @throws StreamCorruptedException -- if connection with server have been lost
	 */
	public static Data exchangeDataWithServ(Data reqData, ObjectInputStream in, ObjectOutputStream out)
			throws StreamCorruptedException {
    	try {    
    		out.writeObject(reqData);		
			Data answerData = (Data) in.readObject();
			return answerData;
		} catch (StreamCorruptedException e){
			throw e;
		} catch (ClassNotFoundException | IOException | ClassCastException e) {
			System.err.println(OBJECT_STREAM_ERROR);
			e.printStackTrace();
			return null;
		}    	
    }

}
