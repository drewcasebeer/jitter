import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.CardLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import twitter4j.*;
import twitter4j.auth.*;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JScrollPane;
import javax.swing.JList;

import java.awt.BorderLayout;

import javax.swing.JScrollBar;


public class Jitter {
	
	private static Jitter window;

	private JFrame frame;
	private final JPanel panel = new JPanel();
	private final JPanel panel_1 = new JPanel();
	private final JLabel lblPin = new JLabel("Pin:");
	private final JButton btnSignIn = new JButton("Sign In");
	private final JLabel lblAuthorizationUrl = new JLabel("Authorization URL:");
	private final JTextArea authorizationURLTextArea = new JTextArea();
	private final JTextField pinTxtField = new JTextField();
	
	private static Twitter twitter;
	private static RequestToken requestToken;
	private static AccessToken accessToken;
	
	private final static String CONSUMER_KEY = "uFfVmokYxn3Z9h5SMCLg";
	private final static String CONSUMER_KEY_SECRET = "biqjo1cy3uhfgZ6fnlrx1XozHWTQu67qkVyzkeF8";
	private final static String ACCESS_KEY = "";
	private final static String ACCESS_KEY_SECRET = "";
	private final JTextArea timelineTextArea = new JTextArea();
	private final JScrollPane scrollPane = new JScrollPane();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	private final JTextArea publishTextArea = new JTextArea();
	private final JButton btnNewButton = new JButton("Publish");
	private final JButton btnNewButton_1 = new JButton("Sign Out");

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Jitter();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws TwitterException 
	 */
	public Jitter() throws TwitterException {
		twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
		requestToken = twitter.getOAuthRequestToken();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		pinTxtField.setBounds(56, 183, 200, 28);
		pinTxtField.setColumns(10);
		frame = new JFrame();
		frame.setBounds(100, 100, 570, 316);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new CardLayout(0, 0));
		
		frame.getContentPane().add(panel, "name_1381815648521069000");
		panel.setLayout(null);
		lblPin.setBounds(21, 189, 23, 16);
		
		panel.add(lblPin);
		btnSignIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String pin = pinTxtField.getText();
				try {
					accessToken = twitter.getOAuthAccessToken(requestToken, pin);
				}
				catch (TwitterException exception){
					System.out.println("Error twitter.getOauthAccessToken");
					System.exit(1);
				}
				updateTimelineTextArea();
				CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
				cardLayout.next(frame.getContentPane());
			}
		});
		btnSignIn.setBounds(445, 221, 119, 67);
		
		panel.add(btnSignIn);
		lblAuthorizationUrl.setBounds(21, 49, 129, 16);
		
		panel.add(lblAuthorizationUrl);
		authorizationURLTextArea.setWrapStyleWord(true);
		authorizationURLTextArea.setLineWrap(true);
		authorizationURLTextArea.setEditable(false);
		authorizationURLTextArea.setBounds(21, 77, 543, 98);
		
		panel.add(authorizationURLTextArea);
		authorizationURLTextArea.setText(requestToken.getAuthorizationURL());
		
		panel.add(pinTxtField);
		
		frame.getContentPane().add(panel_1, "name_1381815662401640000");
		panel_1.setLayout(null);
		scrollPane.setBounds(6, 6, 386, 206);
		
		panel_1.add(scrollPane);
		scrollPane.setViewportView(timelineTextArea);
		timelineTextArea.setWrapStyleWord(true);
		timelineTextArea.setLineWrap(true);
		scrollPane_1.setBounds(6, 224, 296, 64);
		
		panel_1.add(scrollPane_1);
		publishTextArea.setText("Type Status Here:");
		
		scrollPane_1.setViewportView(publishTextArea);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(publishTextArea.getText().equals("Type Status Here:")){
					return;
				}
			String publishString = publishTextArea.getText();
			try {
				twitter.updateStatus(publishString);
				updateTimelineTextArea();
			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			publishTextArea.setText("Type Status Here:");
			}
		});
		btnNewButton.setBounds(314, 224, 78, 64);
		
		panel_1.add(btnNewButton);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
					cardLayout.previous(frame.getContentPane());
					window = new Jitter();
					pinTxtField.setText("");
					authorizationURLTextArea.setText(requestToken.getAuthorizationURL());
				} catch (TwitterException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(447, 224, 117, 64);
		
		panel_1.add(btnNewButton_1);
	}
	
	private void updateTimelineTextArea(){
		timelineTextArea.setText("");
		ResponseList<Status> alist;
		try {
			alist = twitter.getHomeTimeline();
			for (Status each : alist){
				timelineTextArea.append("Sent by: @" + each.getUser().getScreenName() + " - " + each.getUser().getName() + "\n" + each.getText() + "\n\n");
				}
		} catch (TwitterException e) {
			System.out.println("Error twitter.getHomeTimeline");
			System.exit(1);
			e.printStackTrace();
		}
	}
}
