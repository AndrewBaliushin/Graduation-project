package gui;

import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.List;

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
	//End of MAIN tab
	
	
	//Settings panel
	private JFileChooser fileChooser;
	private JLabel ipAdressLabel;
	private JLabel portLabel;
	private JButton applySettingsButton;	
	//End of settings panel
	
	
	public JClient() {
		super("Title");
		
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
//		JPanel settingsPanel = createSettingsTabPanel();
		
		jtp.add("Main", mainPanel);
		jtp.add("Help", helpPanel);
//		jtp.add("Settings", settingsPanel);
		
		add(jtp, BorderLayout.NORTH);

	}
	
	private JPanel createMainTabPanel() {
		JPanel mainPanel = new JPanel();
		
		mainPanel.setLayout(new BorderLayout());
		
		//UPPER FIELD SECTION
		JPanel upperPanel = new JPanel();
		upperPanel.setLayout(new BoxLayout(upperPanel, BoxLayout.X_AXIS));
		upperPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		//TODO
		inputFieldLabel = new JLabel("FIND IT");
		inputField = new JTextField();
		findButton = new JButton("FIND");
		
		upperPanel.add(inputFieldLabel);
		upperPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		upperPanel.add(inputField);
		upperPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		upperPanel.add(findButton);
		upperPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		
		
		//LEFT SIDE
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		
		responseAreaLabel = new JLabel("Response area");
		
		responseArea = new JTextArea(20, 40);
		responseArea.setBorder(BorderFactory.createLoweredBevelBorder());
		
		leftPanel.add(Box.createVerticalStrut(12));
		
		leftPanel.add(responseAreaLabel);
		leftPanel.add(responseArea);
		
		
		//RIGHT SIDE
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		List<JButton> buttons = new ArrayList<>();
		
		addButton = new JButton("add");
		editButton = new JButton("edit");
		nextButton = new JButton("next");
		previousButton = new JButton("previous");
		exitButton = new JButton("exit");
		
		buttons.add(addButton);
		buttons.add(editButton);
		buttons.add(nextButton);
		buttons.add(previousButton);
		buttons.add(exitButton);
		
		for(JButton button : buttons) {
			rightPanel.add(button);
			rightPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		}
		
		mainPanel.add(upperPanel, BorderLayout.NORTH);
		mainPanel.add(leftPanel, BorderLayout.WEST);
		mainPanel.add(rightPanel, BorderLayout.EAST);
		
		
		return mainPanel;
	}
	
	
	
	private JPanel createHelpTabPanel() {
		JPanel helpTabPanel = new JPanel();
		
		responseArea = new JTextArea(10, 10);
		responseArea.setEditable(false);
		responseArea.setLineWrap(true);
		
		//TODO Add help text
		responseArea.setText("Recieve your help");
		
		helpTabPanel.add(responseArea);
		
		return helpTabPanel;
	}
	
	private JPanel createSettingsTabPanel() {
		JPanel settingPanel = new JPanel();
		
		settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.PAGE_AXIS));
		
		JFileChooser fileChooser = new JFileChooser();
		int ret = fileChooser.showDialog(null, "Открыть файл");
		
		settingPanel.add(fileChooser);
		
		return settingPanel;
	}
}
