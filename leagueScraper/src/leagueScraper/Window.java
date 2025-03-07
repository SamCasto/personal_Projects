package leagueScraper;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class Window {
	// Window class for our runner method
	// Window should have labels, input, and a go button
	// Will add a counter to see how many files have been downloaded as a way to
	// show progress
	// Will add a popup message to inform the user that the script is finished
	// Will take default values from strings.txt if possible
	// Otherwise will supply some kind of default text informing user what info
	// should
	// be where

	private JFrame mainWindow;
	private JPanel buttonPanel;
	private JPanel downloadPanel;
	private JPanel urlPanel;
	private JPanel startValPanel;
	private JPanel loopValPanel;
	private JPanel preWeeksPanel;
	// creating panels to be added to each level panel
	private JPanel downloadLabelPanel;
	private JPanel downloadTextPanel;
	private JPanel urlLabelPanel;
	private JPanel urlTextPanel;
	private JPanel startLabelPanel;
	private JPanel startTextPanel;
	private JPanel loopLabelPanel;
	private JPanel loopTextPanel;
	private JPanel preWeekLabelPanel;
	private JPanel preWeekTextPanel;
	// creating start button
	private Button start;
	// creating text boxes
	private JTextField downloadText;
	private JTextField urlText;
	private JTextField startText;
	private JTextField loopText;
	private JTextField preWeekText;
	// creating instance of main method to call caseDownload() here
	private leagueScraperRunner main;
	// creating colors to help users ID problems that we can validate
	private Color green = new Color(111, 212, 66);

	// Default constructor
	public Window(leagueScraperRunner temp) {
		initialize();
		setActionOnStart();
		this.main = temp;
	}

	// Custom methods
	// method for allowing window to be seen
	public void show() {
		mainWindow.setVisible(true);
	}

	// button action listener to start the whole process
	private void setActionOnStart() {
		start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				/*
				 * System.out.printf("Path value: %s \nURL value: %s \nStart Box Value: %d \n" +
				 * "Loop value: %d \nPreweek value: %d \n",getPath(),getURL(),getStartBox(),
				 * getLoopVal(),getPreVal());
				 */
				// some form of input validation
				Boolean pathFlag;
				Boolean urlFlag;
				Boolean boxFlag;
				Boolean loopFlag;
				Boolean preFlag;
				pathFlag = checkPath();
				urlFlag = checkURL();
				boxFlag = checkStart();
				loopFlag = checkLoop();
				preFlag = checkPre();

				// this needs to get converted to an if statement

				// should work, but takes int numPreWeeks, int boxScoreStart, int loopValue,
				// string leagueURL, string downloadToFile
				if (pathFlag && urlFlag && boxFlag && loopFlag && preFlag) {
					try {
						main.caseDownload(getPreVal(),getStartBox(),getLoopVal(),getURL(),getPath());
					} 
					catch (IOException e1) { 
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
				}
				 
			}
		});
	}

	// Getting textfield values to be returned
	private String getPath() {
		String path = downloadText.getText();
		//checking if path has "" marks at the beginning and end to remove them
		if (path.charAt(0)=='"' || path.charAt(path.length() - 1)=='"') {
			path = path.substring(1, path.length() - 1);
			//System.out.println("Printed from apostrophe");
			//System.out.println(path);
		}
		return path;
	}

	private String getURL() {
		String url = urlText.getText();
		return url;
	}

	private Integer getStartBox() {
		Integer box = Integer.parseInt(startText.getText());
		return box;
	}

	private Integer getLoopVal() {
		Integer loop = Integer.parseInt(loopText.getText());
		return loop;
	}

	private Integer getPreVal() {
		Integer preWeek = Integer.parseInt(preWeekText.getText());
		return preWeek;
	}

	private Boolean checkPath() {
		// we check if the path exists and give the user some slight feedback
		String temp = getPath();
		if (new File(temp).exists()) {
			downloadLabelPanel.setBackground(green);
			return true;
		}
		downloadLabelPanel.setBackground(Color.red);
		return false;
	}

	private Boolean checkURL() {
		// basic verification that the URL is compliant with RC 2396
		try {
			new URL(getURL()).toURI();
			urlLabelPanel.setBackground(green);
			return true;
		} catch (URISyntaxException | MalformedURLException e) {
			urlLabelPanel.setBackground(Color.red);
			return false;
		}
	}

	private Boolean checkStart() {
		// checking to make sure it is not negative or 0
		if (getStartBox() > 0) {
			startLabelPanel.setBackground(green);
			return true;
		}
		startLabelPanel.setBackground(Color.red);
		return false;
	}

	private Boolean checkLoop() {
		// check to make sure its not negative or 0
		if (getLoopVal() > 0) {
			loopLabelPanel.setBackground(green);
			return true;
		}
		loopLabelPanel.setBackground(Color.red);
		return false;
	}

	private Boolean checkPre() {
		// checking to make sure value is between 2 and 4
		if (getPreVal() >= 2 && getPreVal() <= 4) {
			preWeekLabelPanel.setBackground(green);
			return true;
		}
		preWeekLabelPanel.setBackground(Color.red);
		return false;
	}

	// method for creating window and window settings
	private void initialize() {
		mainWindow = new JFrame();
		mainWindow.setTitle("MFN Game Log Downloader");// setting title
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// stopping app when closed
		mainWindow.setSize(800, 600);// setting size
		mainWindow.setLocationRelativeTo(null);// opening in the middle of the screen
		mainWindow.setResizable(false);// staying same size
		mainWindow.setLayout(new GridLayout(6, 1, 25, 15));

		// creating the panels
		buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
		// we need multiple text field panels for the following five pieces of data
		/*
		 * 1 downloadToFile 2 leagueURL 3 boxScoreStart 4 loopValue 5 numPreWeeks
		 */
		// panels for each line that we need, these panels need two more panels as well,
		// one for label and one for text entry
		downloadPanel = new JPanel(new GridLayout(1, 2));
		urlPanel = new JPanel(new GridLayout(1, 2));
		startValPanel = new JPanel(new GridLayout(1, 2));
		loopValPanel = new JPanel(new GridLayout(1, 2));
		preWeeksPanel = new JPanel(new GridLayout(1, 2));

		// borders to help give us space between frame edges and items
		downloadPanel.setBorder(new EmptyBorder(10, 0, 0, 10));
		urlPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
		startValPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
		loopValPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
		preWeeksPanel.setBorder(new EmptyBorder(0, 0, 0, 10));

		// creating panels to be added to each level panel
		downloadLabelPanel = new JPanel(new GridLayout());
		downloadTextPanel = new JPanel(new GridLayout());
		urlLabelPanel = new JPanel(new GridLayout());
		urlTextPanel = new JPanel(new GridLayout());
		startLabelPanel = new JPanel(new GridLayout());
		startTextPanel = new JPanel(new GridLayout());
		loopLabelPanel = new JPanel(new GridLayout());
		loopTextPanel = new JPanel(new GridLayout());
		preWeekLabelPanel = new JPanel(new GridLayout());
		preWeekTextPanel = new JPanel(new GridLayout());

		// adding start button to buttonPanel and formatting
		// buttonPanel.setBackground(Color.blue);
		start = new Button("Start");
		start.setPreferredSize(new Dimension(180, 60));
		buttonPanel.add(start);

		// adding labels and text inputs to fieldPanel and formatting
		downloadPanel.add(downloadLabelPanel);
		downloadPanel.add(downloadTextPanel);
		urlPanel.add(urlLabelPanel);
		urlPanel.add(urlTextPanel);
		startValPanel.add(startLabelPanel);
		startValPanel.add(startTextPanel);
		loopValPanel.add(loopLabelPanel);
		loopValPanel.add(loopTextPanel);
		preWeeksPanel.add(preWeekLabelPanel);
		preWeeksPanel.add(preWeekTextPanel);

		// Creating font to be used by all labels
		Font fontLabel = new Font("Antique Olive", Font.BOLD, 25);
		Font fontTextBox = new Font("Antique Olive", Font.PLAIN, 16);

		/*
		 * //colors to make sure all panels have things added to them
		 * downloadPanel.setBackground(Color.red); urlPanel.setBackground(Color.black);
		 * startValPanel.setBackground(Color.cyan);
		 * loopValPanel.setBackground(Color.green);
		 * preWeeksPanel.setBackground(Color.yellow);
		 */

		// adding labels to corresponding panels
		JLabel downloadLabel = new JLabel("Download Path: ", SwingConstants.RIGHT);
		downloadLabel.setFont(fontLabel);
		downloadLabelPanel.add(downloadLabel);
		JLabel urlLabel = new JLabel("League URL: ", SwingConstants.RIGHT);
		urlLabel.setFont(fontLabel);
		urlLabelPanel.add(urlLabel);
		JLabel startValLabel = new JLabel("Box Score Starting Number: ", SwingConstants.RIGHT);
		startValLabel.setFont(fontLabel);
		startLabelPanel.add(startValLabel);
		JLabel loopValLabel = new JLabel("Number of Seasons: ", SwingConstants.RIGHT);
		loopValLabel.setFont(fontLabel);
		loopLabelPanel.add(loopValLabel);
		JLabel preWeeksLabel = new JLabel("Number of Preseason Games: ", SwingConstants.RIGHT);
		preWeeksLabel.setFont(fontLabel);
		preWeekLabelPanel.add(preWeeksLabel);

		// Adding text fields to corresponding panels
		downloadText = new JTextField("Sample\\Path\\Download");
		downloadText.setFont(fontTextBox);
		downloadTextPanel.add(downloadText);
		urlText = new JTextField("https://usflwfl.myfootballnow.com/");
		urlText.setFont(fontTextBox);
		urlTextPanel.add(urlText);
		startText = new JTextField("9908");
		startText.setFont(fontTextBox);
		startTextPanel.add(startText);
		loopText = new JTextField("1");
		loopText.setFont(fontTextBox);
		loopTextPanel.add(loopText);
		preWeekText = new JTextField("3");
		preWeekText.setFont(fontTextBox);
		preWeekTextPanel.add(preWeekText);

		// adding the panels to the layout
		mainWindow.add(downloadPanel);
		mainWindow.add(urlPanel);
		mainWindow.add(startValPanel);
		mainWindow.add(loopValPanel);
		mainWindow.add(preWeeksPanel);
		mainWindow.add(buttonPanel);

	}
}
