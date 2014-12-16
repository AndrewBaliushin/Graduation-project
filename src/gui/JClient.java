package gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.util.*;
import java.util.List;

import static localization.Labels.*;
import static settings.GUIconfig.*;
import static settings.Config.*;

@SuppressWarnings("serial")
public class JClient extends JFrame{
	
	//MAIN tab
	private JButton findButton;
	private JButton addButton;
	private JButton editButton;
	private JButton nextButton;
	private JButton previousButton;
	private JButton exitButton;
	
	private JLabel inputFieldLabel;
	private JTextField inputField; 
	
	private JLabel responseAreaLabel;
	private JTextArea responseArea;
	
	//SETTINGS tab
	private JButton chooseFileButton;
	private JFileChooser fileChooser;
	private JLabel ipAdressLabel;
	private JLabel portLabel;
	private JButton applySettingsButton;
	
	public JClient() {
		super(APP_TITLE);
		
		createGlobalLayout();
		addTabs();
		
		fireStandartJFrameRoutines();
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

		inputFieldLabel = new JLabel(SEARCH_FIELD_LABEL);
		inputField = new JTextField();

		searchPanel.add(inputFieldLabel);
		searchPanel.add(inputField);
		
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

		int spc = SPACE_BETWEEN_BUTTONS;
		for (JButton button : buttons) {
			buttonPanel.add(button);
			button.setAlignmentX(Component.CENTER_ALIGNMENT);
			buttonPanel.add(Box.createRigidArea(new Dimension(spc, spc)));
		}
		
		return buttonPanel;
	}
	
	private JPanel createHelpTabPanel() {
		JPanel helpTabPanel = new JPanel();
		helpTabPanel.setLayout(new BorderLayout());
		
		responseArea = new JTextArea();
		responseArea.setEditable(false);
		
		helpTabPanel.add(responseArea);
		
		return helpTabPanel;
	}
	
	private JPanel createSettingsTabPanel() {
		JPanel settingPanel = new JPanel();
		settingPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		
		//TODO delete stub
		chooseFileButton = new JButton(CHOOSE_FILE_BUTTON_NAME);
		applySettingsButton = new JButton(APPLY_CHANGES_BUTTON_NAME);
		ipAdressLabel = new JLabel("stub");
		portLabel = new JLabel("stub");
		
		int spc = SPACE_BETWEEN_ELEMS_IN_SETTING;
		gbc.insets = new Insets(spc, spc, spc, spc);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		int row = 0;
		gbc.gridy = row++;
		settingPanel.add(chooseFileButton, gbc);
		
		gbc.gridy = row++;
		settingPanel.add(applySettingsButton, gbc);
		
		gbc.gridy = row++;
		settingPanel.add(ipAdressLabel, gbc);
		
		gbc.gridy = row++;
		settingPanel.add(portLabel, gbc);
		
//		JFileChooser fileChooser = new JFileChooser();
//		FileNameExtensionFilter filter = new FileNameExtensionFilter(FILE_CHOOSER_FILTER_DESCRIPTON,
//				CONFIG_FILE_EXTENSION);
//		
		//int ret = fileChooser.showDialog(null, "Открыть файл");
		
		
		return settingPanel;
	}
}
