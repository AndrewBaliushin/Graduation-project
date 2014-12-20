package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;
import java.util.List;
import java.util.Map.Entry;

import jhelp.Client;
import jhelp.FileOpearations;

import static localization.Labels.*;
import static settings.GUIconfig.*;
import static settings.Config.*;
import static gui.JClientActionMethodNames.*;

/**
 * GUI for {@link jhelp.Client}
 * {@link jhelp.Client} must be already connected to server.
 * @author Andrew Baliushin
 */
@SuppressWarnings("serial")
public class JClient extends JFrame{
	
	Client clientApp;
	
	JHelpActionListnerDispetcher actionListnerDispetcher;
	
	private int serverPort;
	private String serverIP;
	
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
	private JTextArea responseArea;
	
	//SETTINGS tab
	private JButton chooseFileButton;
	private JLabel ipAdressLabel;
	private JLabel portLabel;
	private JButton connectButton;
	
	public JClient(Client clientApp) {
		super(APP_TITLE);
		
		this.clientApp = clientApp;
		actionListnerDispetcher = new JHelpActionListnerDispetcher(this);
		
		setDefaultIpAndPort();
		
		createGlobalLayout();
		addTabs();
		attachActionListners();
		
		fireStandartJFrameRoutines();
	}
	
	private void setDefaultIpAndPort() {
		serverIP = DEFAULT_IP;
		serverPort = DEFAULT_PORT;
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
		responseArea = new JTextArea(dim[0], dim[1]);
		responseArea.setBorder(BorderFactory.createLoweredBevelBorder());
		
		defPanel.add(responseArea);
		
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
		
		responseArea = new JTextArea();
		responseArea.setEditable(false);
		
		String helpText = clientApp.getHelpFileContent();
		responseArea.setText(helpText);
		
		helpTabPanel.add(responseArea);
		
		return helpTabPanel;
	}
	
	private JPanel createSettingsTabPanel() {
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		chooseFileButton = new JButton(CHOOSE_FILE_BUTTON_NAME);
		connectButton = new JButton(CONNECT_BUTTON_NAME);
		ipAdressLabel = new JLabel();
		portLabel = new JLabel();
		
		changeIpAdressLabel(serverIP);
		changePortAdressLabel(serverPort);
		
		int spc = SPACE_BETWEEN_ELEMS_IN_SETTING;
		gbc.insets = new Insets(spc, spc, spc, spc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		int row = 0;
		gbc.gridy = row++;
		settingPanel.add(chooseFileButton, gbc);
				
		gbc.gridy = row++;
		settingPanel.add(ipAdressLabel, gbc);
		
		gbc.gridy = row++;
		settingPanel.add(portLabel, gbc);

		gbc.gridy = row++;
		settingPanel.add(connectButton, gbc);
		
		return settingPanel;
	}
	
	private void changeIpAdressLabel(String ip) {
		ipAdressLabel.setText(IP_LABEL_PREFIX + ip);
	}
	
	private void changePortAdressLabel(int port) {
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
	
	private void attachActionListners() {
		Map<JComponent, JClientActionMethodNames> actionsForComponents =
				new HashMap<JComponent, JClientActionMethodNames>();
		
		actionsForComponents.put(findButton, FIND_BUTTON_ACTION);
		actionsForComponents.put(addButton, ADD_BUTON_ACTION);
		actionsForComponents.put(editButton, EDIT_BUTTON_ACTION);
		actionsForComponents.put(nextButton, NEXT_BUTTON_ACTION);
		actionsForComponents.put(previousButton, PREV_BUTTON_ACTION);
		actionsForComponents.put(exitButton, EXIT_BUTTON_ACTION);
		
		
		actionsForComponents.put(chooseFileButton, CHOOSE_FILE_BUTTON_ACTION);
		actionsForComponents.put(connectButton, CONNECT_BUTTON_ACTION);
		
		for (Entry<JComponent, JClientActionMethodNames> m : actionsForComponents.entrySet()) {
			try {
				((JButton) m.getKey())
						.addActionListener(actionListnerDispetcher
								.createAListnerWithAttachment(m.getValue()));
			} catch (ClassCastException ex) {
				System.err.println("Exception in " + this.getClass().getName());
				System.err.println("Can't cast to JButton (method to attach: "
						+ m.getValue().getName());
			}
		}
	}
	
	void findButtonAction() {
		if(isDisconnectedAndShowAlertIfSo()) {
			return;
		}
		
		showAlertWindow(Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	void addButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	void editButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	void nextButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
	}
	
	void prevButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
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
				InetSocketAddress socAdr = FileOpearations.getIpAndPortFromConfigFile(file);
				changeIpAdressLabel(socAdr.getHostName());
				changePortAdressLabel(socAdr.getPort());				
			} catch (IOException e) {
				showAlertWindow(CFG_FILE_ERROR);
			}
         }   
	}
	
	void connectButtonAction() {
		JOptionPane.showMessageDialog(null, 
				Thread.currentThread().getStackTrace()[1].getMethodName());
	}
}
