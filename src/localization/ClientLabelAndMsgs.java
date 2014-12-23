package localization;

import settings.Config;

public class ClientLabelAndMsgs {
	
	//client
	public static final String MAIN_TAB_NAME = "Main";
	public static final String SETTINGS_TAB_NAME = "Settings";
	public static final String HELP_TAB_NAME = "Help";
	
	public static final String DEFINITION_AREA_LABEL = "Definition";
	public static final String SEARCH_FIELD_LABEL = "Term:";
	
	public static final String FIND_BUTTON_NAME = "Find";
	public static final String ADD_BUTTON_NAME = "Add";
	public static final String EDIT_BUTTON_NAME = "Edit";
	public static final String DEL_BUTTON_NAME = "Delete";
	public static final String NEXT_BUTTON_NAME = "Next";
	public static final String PREV_BUTTON_NAME = "Previous";
	public static final String EXIT_BUTTON_NAME = "Exit";
	
	public static final String HOST_LABEL_PREFIX = "Host: ";
	public static final String PORT_LABEL_PREFIX = "Port: ";
	
	public static final String NO_CONNECTION_MSG = "No connection.\nUse settings to connect";
	public static final String CFG_FILE_ERROR = "Can't read config from file";
	
	public static final String CONNECTED_MSG = "Client: connected";
	public static final String DISCONNECTED_MSG = "Client: disconnected";
	public static final String SOCKET_CLOSE_ERR_MSG = "Error occured while closing socket";	
	public static final String NO_SOCKET_MSG = "There is no socket to close";	
	public static final String SOCKET_CREATION_ERR_MSG = "Can't create connection";
	
	public static final String ADD_SUCCESS = "Term succesufly added";
	
	public static final String OBJECT_STREAM_ERROR = "Error during data transfer";
	public static final String CONNECTION_PROBLEM = "There is connection problem";
	
	public static final String HELP_FILE_PROBLEM = "Can't fetch help file";
	
	public static final String CHOOSE_FILE_BUTTON_NAME = "Choose config file";
	public static final String FILE_CHOOSER_DIALOG_TITLE = "Open";
	public static final String CONNECT_BUTTON_NAME = "Connect";
	
	public static final String FILE_CHOOSER_FILTER_DESCRIPTON = 
			Config.CONFIG_FILE_EXTENSION + " files only";
}
