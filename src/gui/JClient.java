package gui;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import common.Data;
import common.FileHelper;
import common.Item;
import common.JHelp;

import client.Client;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;


import static localization.ClientLabelAndMsgs.*;
import static settings.GUIconfig.*;
import static settings.Config.*;
import static gui.JClientActionMethodNames.*;

/**
 * GUI for {@link client.Client}
 * @author Andrew Baliushin
 */
@SuppressWarnings("serial")
public class JClient extends JFrame{
	
	private Client clientApp;
	
	private JHelpActionListnerDispetcher actionListnerDispetcher;
	
	private Data data;
	
	private int serverPort;
	private String serverHost;
	
	//MAIN tab
	private JButton findButton;
	private JButton addButton;
	private JButton editButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton exitButton;
	
	private JLabel searchFieldLabel;
	private JTextField searchField; 
	
	private JLabel responseAreaLabel;
	private JTextArea definitionArea;
	
	private JTextArea helpTextArea;
	
	//SETTINGS tab
	private JButton chooseFileButton;
	private JLabel hostLabel;
	private JLabel portLabel;
	private JButton connectButton;
	
	private String[] definitionContent;
	private int definitionCurrentIndex;
	
	public JClient(Client clientApp) {
		super(APP_TITLE);
		
		this.clientApp = clientApp;
		actionListnerDispetcher = new JHelpActionListnerDispetcher(this);
		
		createGlobalLayout();
		addTabs();
		attachActionListners();
		
		setPortHostAndTheirLabelsToDefault();
		
		fireStandartJFrameRoutines();
	}
	
	private void setPortHostAndTheirLabelsToDefault() {
		changeHostAndLabel(DEFAULT_SERV_HOST);
		changePortAndLabel(DEFAULT_SERV_PORT);
	}
		
	private void fireStandartJFrameRoutines() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
    
    private void createGlobalLayout() {
		setLayout(new BorderLayout(4, 4));
	}

	private void addTabs() {
		JTabbedPane jtp = new JTabbedPane();

		JPanel mainPanel = createMainTabPanel();
		JPanel helpPanel = createHelpTabPanel();
		JPanel settingsPanel = createSettingsTabPanel();
		
		jtp.add(MAIN_TAB_NAME, mainPanel);
		jtp.add(HELP_TAB_NAME, helpPanel);
		jtp.add(SETTINGS_TAB_NAME, settingsPanel);
		
		add(jtp, BorderLayout.NORTH);
	}
	
	private JPanel createMainTabPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		JPanel searchPanel = createSearchPanelOfMain();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.EAST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		mainPanel.add(searchPanel, gbc);
		
		findButton = new JButton(FIND_BUTTON_NAME);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.CENTER;
		mainPanel.add(findButton, gbc);
		
		responseAreaLabel = new JLabel(DEFINITION_AREA_LABEL);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.WEST;
		mainPanel.add(responseAreaLabel, gbc);
		
		JPanel emptyPanel = new JPanel();
		gbc.gridx = 1;
		gbc.gridy = 1;
		mainPanel.add(emptyPanel, gbc);
		
		JPanel definitionPanel = createDefinitionAreaOfMain();
		gbc.gridx = 0;
		gbc.gridy = 2;
		mainPanel.add(definitionPanel, gbc);
		
		JPanel buttonPanel = createButtonPanelOfMain();
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.NORTH;
		mainPanel.add(buttonPanel, gbc);		
		
		return mainPanel;
	}
	
	private JPanel createSearchPanelOfMain() {
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
		int brdr = BORDER_SEARCH_PANEL;
		searchPanel.setBorder(BorderFactory.createEmptyBorder(brdr, 0, brdr, 0));

		searchFieldLabel = new JLabel(SEARCH_FIELD_LABEL);
		searchField = new JTextField();

		searchPanel.add(searchFieldLabel);
		searchPanel.add(searchField);
		
		return searchPanel;
	}
	
	private JPanel createDefinitionAreaOfMain() {
		JPanel defPanel = new JPanel();
		defPanel.setLayout(new BoxLayout(defPanel, BoxLayout.Y_AXIS));
		
		int[] dim = DEFINITION_AREA_DIMENSION;
		definitionArea = new JTextArea(dim[0], dim[1]);
		definitionArea.setBorder(BorderFactory.createLoweredBevelBorder());
		definitionArea.setLineWrap(true);
		definitionArea.setWrapStyleWord(true);
		
		defPanel.add(definitionArea);
		
		return defPanel;
	}
	
	private JPanel createButtonPanelOfMain() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		int brdr = BORDER_FOR_BUTTONS;
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, brdr, 0, brdr));
		buttonPanel.setAlignmentY(Component.TOP_ALIGNMENT);

		List<JButton> buttons = new ArrayList<>();

		addButton = new JButton(ADD_BUTTON_NAME);
		editButton = new JButton(EDIT_BUTTON_NAME);
		nextButton = new JButton(NEXT_BUTTON_NAME);
		previousButton = new JButton(PREV_BUTTON_NAME);
		exitButton = new JButton(EXIT_BUTTON_NAME);

		buttons.add(addButton);
		buttons.add(editButton);
		buttons.add(nextButton);
		buttons.add(previousButton);
		buttons.add(exitButton);
		
		disableNextPrevButtons();

		int spc = SPACE_BETWEEN_BUTTONS;
		for (JButton button : buttons) {
			buttonPanel.add(button);
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttonPanel.add(Box.createRigidArea(new Dimension(spc, spc)));
		}
		
		return buttonPanel;
	}
	
	private void disableNextPrevButtons() {
		nextButton.setEnabled(false);
		previousButton.setEnabled(false);
	}
	
	private JPanel createHelpTabPanel() {
		JPanel helpTabPanel = new JPanel();
		helpTabPanel.setLayout(new BorderLayout());
		
		helpTextArea = new JTextArea();
		helpTextArea.setEditable(false);
		
		String helpText = clientApp.getHelpFileContent();
		helpTextArea.setText(helpText);
		
		helpTabPanel.add(helpTextArea);
		
		return helpTabPanel;
	}
	
	private JPanel createSettingsTabPanel() {
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		chooseFileButton = new JButton(CHOOSE_FILE_BUTTON_NAME);
		connectButton = new JButton(CONNECT_BUTTON_NAME);
		hostLabel = new JLabel();
		portLabel = new JLabel();
		
		int spc = SPACE_BETWEEN_ELEMS_IN_SETTING;
		gbc.insets = new Insets(spc, spc, spc, spc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		int row = 0;
		gbc.gridy = row++;
		settingPanel.add(chooseFileButton, gbc);
				
		gbc.gridy = row++;
		settingPanel.add(hostLabel, gbc);
		
		gbc.gridy = row++;
		settingPanel.add(portLabel, gbc);

		gbc.gridy = row++;
		settingPanel.add(connectButton, gbc);
		
		return settingPanel;
	}
	
	private void changeHostAndLabel(String host) {
		this.serverHost = host;
		hostLabel.setText(HOST_LABEL_PREFIX + host);
	}
	
	private void changePortAndLabel(int port) {
		this.serverPort = port;
		portLabel.setText(PORT_LABEL_PREFIX + port);
	}
	
	private void showAlertWindow(String msg) {
		JOptionPane.showMessageDialog(null, 
				msg);
	}
	
	private boolean isDisconnectedAndShowAlertIfSo() {
		if( clientApp.isConnected()) {
			return false;
		} else {
			showAlertWindow(NO_CONNECTION_MSG);
			return true;
		}
	}
	
	private void refreshDataInGUI(Data newData) {
		Item[] definitions = newData.getValues();
		int defLength = definitions.length;
		
		if(defLength == 0) {
			return;
		}
		
		definitionContent = new String[defLength];
		for (int i = 0; i < definitions.length; i++) {
			definitionContent[i] = definitions[i].getItem();
		}
		
		definitionCurrentIndex = 0;
		setDefinition(definitionContent[definitionCurrentIndex]);
		refreshPrevNextButtonStatus();
	}
	
	private void setDefinition(String cont) {
		definitionArea.setText(cont);
	}
	
	private void refreshPrevNextButtonStatus() {
		if(definitionContent == null || definitionContent.length < 2) {
			previousButton.setEnabled(false);
			nextButton.setEnabled(false);
		}
		
		int defLen = definitionContent.length;
		if(definitionCurrentIndex > 0) {
			previousButton.setEnabled(true);
		}  else {
			previousButton.setEnabled(false);
		}
		if(definitionCurrentIndex < (defLen - 1)) {
			nextButton.setEnabled(true);
		} else {
			nextButton.setEnabled(false);
		}
	}
	
	private void attachActionListners() {
		Map<JButton, JClientActionMethodNames> actionsForButtons =
				new HashMap<JButton, JClientActionMethodNames>();
		
		actionsForButtons.put(findButton, FIND_BUTTON_ACTION);
		actionsForButtons.put(addButton, ADD_BUTON_ACTION);
		actionsForButtons.put(editButton, EDIT_BUTTON_ACTION);
		actionsForButtons.put(nextButton, NEXT_BUTTON_ACTION);
		actionsForButtons.put(previousButton, PREV_BUTTON_ACTION);
		actionsForButtons.put(exitButton, EXIT_BUTTON_ACTION);
		
		actionsForButtons.put(chooseFileButton, CHOOSE_FILE_BUTTON_ACTION);
		actionsForButtons.put(connectButton, CONNECT_BUTTON_ACTION);
		
		for (Entry<JButton, JClientActionMethodNames> m : actionsForButtons.entrySet()) {
			m.getKey().addActionListener(
					actionListnerDispetcher.createListnerWithAttachment(m.getValue()));			
		}
	}
	
	void findButtonAction() {
		if(isDisconnectedAndShowAlertIfSo()) {
			return;
		}
		
		data = new Data();
		data.setOperation(JHelp.SELECT);
		data.setKey(new Item(searchField.getText()));
		
		data = clientApp.getData(data);
		if(data.getOperation() == JHelp.ERROR) {
			showAlertWindow(data.getValue(0).getItem());
		} else {
			refreshDataInGUI(data);
		}						
	}
	
	void addButtonAction() {
		if(isDisconnectedAndShowAlertIfSo()) {
			return;
		}
		
		String term = searchField.getText();
		String def = definitionArea.getText();
		
		data = new Data();
		data.setOperation(JHelp.INSERT);
		data.setKey(new Item(term));
		data.setValues(new Item[]{new Item(def)});
		
		data = clientApp.getData(data);
		if(data.getOperation() == JHelp.ERROR) {
			showAlertWindow(data.getValue(0).getItem());
		} else {
			showAlertWindow(ADD_SUCCESS);
			refreshDataInGUI(data);
		}		
	}
	
	void editButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	void nextButtonAction() {
		String def = definitionContent[++definitionCurrentIndex];
		setDefinition(def);
		refreshPrevNextButtonStatus();
	}
	
	void prevButtonAction() {
		String def = definitionContent[--definitionCurrentIndex];
		setDefinition(def);
		refreshPrevNextButtonStatus();
	}
	
	void exitButtonAction() {
		clientApp.disconnect();
		System.exit(0);
	}
	
	void chooseFileAction() {
		JFileChooser fc = new JFileChooser();
		FileNameExtensionFilter fileExtensionFilter = new FileNameExtensionFilter(
				FILE_CHOOSER_FILTER_DESCRIPTON, CONFIG_FILE_EXTENSION);
		fc.setFileFilter(fileExtensionFilter);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setDialogTitle(FILE_CHOOSER_DIALOG_TITLE);
		
		int returnValue = fc.showOpenDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            try {
				InetSocketAddress socAdr = FileHelper.getIpAndPortFromConfigFile(file);
				changeHostAndLabel(socAdr.getHostName());
				changePortAndLabel(socAdr.getPort());				
			} catch (IOException e) {
				showAlertWindow(CFG_FILE_ERROR);
			}
         }   
	}
	
	void connectButtonAction() {
		int status = clientApp.connect(serverHost, serverPort);
		if(status == JHelp.READY) {
			showAlertWindow(CONNECTED_MSG);
		} else {
			showAlertWindow(SOCKET_CREATION_ERR_MSG);
		}
	}
}
