package client;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class MainWindow extends JFrame{
	
	public static final long serialVersionUID = 1;

	private JLabel borderLetters[][], borderLetters1[][], Logon, FileLabel, PlayerName, ComputerName, TimeCounter, Filter, ChatName;
	private WaterTiles ComputerBoard[][], PlayerBoard[][];
	private JButton SelectFile, Start, Send;
	private JMenuBar jmb;
	private JMenu fileMenu;
	private JMenuItem HowTo, About; 
	private JDialog HowToDialog, AboutDialog;
	private ImageIcon questionMark, Water1, Water2;
	private TextArea LogInfo, ChatInfo, BattleInfo;
	private TextField ChatField;
	private JCheckBox Chat, Events;
	private JPanel outerPanel;
	
	
	Scanner scan;
	private boolean shipValid[], selectFlag, gameEnable, ComputerFinish, PlayerFinish, MultiPlayer;
	private char Wat[][], ComputerWat[][];
	private int playerCount, computerCount, SleepTime, round, time, p2Row, p2Col;
	private Random rand;
	private String LeftName, RightName, MessageIn, MessageOut, MapsName;
	private Vector<WaterTiles> cAircraft, cBattleship, cCruiser, cDestroyer1,  cDestroyer2, 
					pAircraft, pBattleship, pCruiser, pDestroyer1,  pDestroyer2;
	
	private Buffer bufferTimer, bufferComputer;
	private PrintWriter pw;
	private BufferedReader br;

//	public static void main(String [] args) {
//		MainWindow mw = new MainWindow(null, null, true, "ppppp1", "hahahaha2");
//		mw.setVisible(true);
//	}
	

	public MainWindow(PrintWriter pw, BufferedReader br, boolean MultiPlayer, String LeftName, String RightName) {
		super("BattleShip");
		this.MultiPlayer = MultiPlayer;
		this.pw = pw;
		this.br = br;
		this.LeftName = LeftName;
		this.RightName = RightName;

		initializeMainWindow();
		createDialog();
		createMainWindow();
		addEventHandlers();
	}
	
	public MainWindow(PrintWriter pw, BufferedReader br, String MapsName) {
		super("BattleShip");
		this.MultiPlayer = false;
		this.LeftName = "Player";
		this.RightName = "Computer";
		this.MapsName = MapsName;
//		System.out.println(MapsName);

		initializeMainWindow();
		createDialog();
		createMainWindow();
		addEventHandlers();
	}
	
	private void initializeMainWindow() {
		setSize(1000, 700);
		setLocation(400, 250);
		
		//initialize Players' info
		Wat = new char[10][10];
		ComputerWat = new char[10][10];
		//setup image
		questionMark = new ImageIcon("Resources/Tiles/Q.png");
		Water1 = new ImageIcon(Constants.Water1);
		Water2 = new ImageIcon(Constants.Water2);
		//initialize Labels
		PlayerName = new JLabel(LeftName);
		ComputerName = new JLabel(RightName);
		FileLabel = new JLabel("File: ");
		TimeCounter = new JLabel("Time - 0:15");
		Logon = new JLabel("Log: You are in edit mode, click to place your ships. ");
		FileLabel = new JLabel("File: ");
		PlayerBoard = new WaterTiles[11][11];
		borderLetters = new JLabel[11][11];
		borderLetters1 = new JLabel[11][11];
		ComputerBoard = new WaterTiles[11][11];
		int x = 0;
		for (char i = 'A'; i < 'K'; i++) {
			borderLetters[x][0] = new JLabel(Character.toString(i));
			borderLetters1[x][0] = new JLabel(Character.toString(i));
			x++;
		}
		borderLetters[10][0] = new JLabel("");
		borderLetters1[10][0] = new JLabel("");

		for (int i = 0; i < 10; i++) {
			for (int j = 1; j < 11; j++) {
				PlayerBoard[i][j] = new WaterTiles(questionMark, Water1, Water2);
				ComputerBoard[i][j] = new WaterTiles(questionMark, Water1, Water2);
				PlayerBoard[i][j].setBorder(BorderFactory.createLineBorder(Color.red));
				ComputerBoard[i][j].setBorder(BorderFactory.createLineBorder(Color.red));
				PlayerBoard[i][j].setPreferredSize(new Dimension(5,5));
				ComputerBoard[i][j].setPreferredSize(new Dimension(5,5));
				Wat[i][j-1] = 'X';
				ComputerWat[i][j-1] = 'X';
			}
		}
		for (int i = 0; i < 10; i++) {
			borderLetters[10][i+1] = new JLabel(Integer.toString(i+1));	
			borderLetters1[10][i+1] = new JLabel(Integer.toString(i+1));	
		}
		//initialize Buttons
		SelectFile = new JButton("Select File...");
		Start = new JButton("START");
		Start.setEnabled(false);
		//initialize boolean
		shipValid = new boolean[5];
		selectFlag = false;
		gameEnable = false;
		PlayerFinish = false;
		ComputerFinish = false;
		for (int i = 0; i < 5; i++) {
			shipValid[i] = true;
		}
		//initialize Menu
		jmb = new JMenuBar();
		fileMenu = new JMenu("Info");
		HowTo = new JMenuItem("How To");
		About = new JMenuItem("About");
		//initialize count
		computerCount = 0;
		playerCount = 0;
		rand = new Random();
		round = 1;
		time = 0;
		//initialize Dialog
		HowToDialog = new JDialog();
		AboutDialog = new JDialog();
		//initialize ImageIcon
		//thread executor
		bufferComputer = new Buffer();
		bufferTimer = new Buffer();
		//ScrollPane
		LogInfo = new TextArea();
		ChatInfo = new TextArea();
		BattleInfo = new TextArea();
		//Sink condition check
		cAircraft = new Vector<WaterTiles>();
		cBattleship = new Vector<WaterTiles>();
		cCruiser = new Vector<WaterTiles>();
		cDestroyer1 = new Vector<WaterTiles>();
		cDestroyer2 = new Vector<WaterTiles>();
		pAircraft = new Vector<WaterTiles>();
		pBattleship = new Vector<WaterTiles>();
		pCruiser = new Vector<WaterTiles>();
		pDestroyer1 = new Vector<WaterTiles>();
		pDestroyer2 = new Vector<WaterTiles>();
		//setup Chat Units
		Chat = new JCheckBox("Chat");
		Events = new JCheckBox("Events");
		Filter = new JLabel("Filter: ");
		ChatName = new JLabel(LeftName + ": ");
		ChatField = new TextField();
		Send = new JButton("Send");
		Send.setEnabled(false);
		MessageIn = "";
		MessageOut = "";
	}
	
	private void createMainWindow() {
		//BoxLayout
		setLayout(new BorderLayout());
		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		JPanel mid = new JPanel();
		mid.setLayout(new BoxLayout(mid, BoxLayout.X_AXIS));
		JPanel but = new JPanel();
		but.setLayout(new BoxLayout(but, BoxLayout.X_AXIS));
		JPanel buttom = new JPanel();
		buttom.setLayout(new BorderLayout());

		add(top, BorderLayout.NORTH);
		add(mid, BorderLayout.CENTER);
		add(buttom, BorderLayout.SOUTH);
		
		//setup Panels
		JPanel leftPanel = new JPanel();
		JPanel rightPanel = new JPanel();
		leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		leftPanel.setBackground(Color.blue);
		rightPanel.setBackground(Color.BLUE);
		
		GridLayout left = new GridLayout(11, 11);
		leftPanel.setLayout( left );
		for (int i = 0; i < 10; i++) {
			leftPanel.add(borderLetters[i][0]);
			for (int j = 1; j < 11; j++) {
				leftPanel.add(PlayerBoard[i][j]);
			}		
		}
		for (int j = 0; j < 11; j++) {
			leftPanel.add(borderLetters[10][j]);
		}

		GridLayout right = new GridLayout(11, 11);
		rightPanel.setLayout( right );
		for (int i = 0; i < 10; i++) {
			rightPanel.add(borderLetters1[i][0]);
			for (int j = 1; j < 11; j++) {
				rightPanel.add(ComputerBoard[i][j]);
			}		
		}
		for (int j = 0; j < 11; j++) {
			rightPanel.add(borderLetters1[10][j]);
		}
		//top
		top.add(PlayerName);
		top.add(Box.createGlue());
		top.add(TimeCounter, Component.CENTER_ALIGNMENT);
		top.add(Box.createGlue());
		top.add(ComputerName);
		//mid
		mid.add(leftPanel);
		mid.add(rightPanel);
		//buttom		
		but.add(Logon);
		but.add(Box.createGlue());
//		but.add(SelectFile);
//		but.add(FileLabel);
		but.add(Start);
		buttom.add(but, BorderLayout.NORTH);
		//Scrollable Area
		outerPanel = new JPanel();
		outerPanel.setLayout(new CardLayout());

		JPanel GeneralPanel = new JPanel();
		GeneralPanel.add(LogInfo);
		LogInfo.setPreferredSize(new Dimension(850, 120));
		
		JPanel ChatPanel = new JPanel();
		ChatPanel.add(ChatInfo);
		ChatInfo.setPreferredSize(new Dimension(850, 120));
		
		JPanel EventPanel = new JPanel();
		EventPanel.add(BattleInfo);
		BattleInfo.setPreferredSize(new Dimension(850, 120));

		outerPanel.add(GeneralPanel, "General");
		outerPanel.add(ChatPanel, "Chat");
		outerPanel.add(EventPanel, "Event");
		outerPanel.setBorder(BorderFactory.createTitledBorder("Game Log"));
		buttom.add(outerPanel, BorderLayout.CENTER);	
		//Filter Area
		JPanel eastBut = new JPanel();
		eastBut.setLayout(new BoxLayout(eastBut, BoxLayout.Y_AXIS));
		eastBut.add(Filter);
		eastBut.add(Chat);
		eastBut.add(Events);
		buttom.add(eastBut, BorderLayout.EAST);	
		//Input Area
		JPanel southBut = new JPanel();
		southBut.setLayout(new BoxLayout(southBut, BoxLayout.X_AXIS));
		southBut.add(ChatName);
		southBut.add(ChatField);
		southBut.add(Send);
		buttom.add(southBut, BorderLayout.SOUTH);	
		//create Menu
		setJMenuBar(jmb);
		jmb.add(fileMenu);
		fileMenu.add(HowTo);
		fileMenu.add(About);
		//create Water animation
		Thread t = new Thread(new WaterWave());
		t.start();
	}
	
	private void addEventHandlers() {
		//Menu Event
		HowTo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
		About.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));

		HowTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				HowToDialog.setVisible(true);
			}
		});

		About.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				AboutDialog.setVisible(true);
			}
		});
		
//		//SelectFile
//		SelectFile.addActionListener(new ActionListener(){
//			public void actionPerformed(ActionEvent ae) {				
//				JFileChooser jfc = new JFileChooser();
//				jfc.setCurrentDirectory(new File("."));
//				int buttonClicked = jfc.showOpenDialog(null);
//				if (buttonClicked == JFileChooser.APPROVE_OPTION) {
//					File fileSelected = jfc.getSelectedFile();
//					readFile(fileSelected);
//					if ( !(shipValid[0]&&shipValid[1]&&shipValid[2]&&shipValid[3]&&shipValid[4]) && selectFlag) {
//						Start.setEnabled(true);
//					}
//				}
//			}
//		});
		
		//add ships to the board, initialize the mouseEvent Label
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				int row = i, col = j;
				PlayerBoard[i][j+1].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (shipValid[0]||shipValid[1]||shipValid[2]||shipValid[3]||shipValid[4]) {
							PlaceShips ps = new PlaceShips(row, col, Wat, PlayerBoard, shipValid);
						} 
						if ( !(shipValid[0]&&shipValid[1]&&shipValid[2]&&shipValid[3]&&shipValid[4]) ) {
							Start.setEnabled(true);
						}
					}
				});
				ComputerBoard[i][j+1].addMouseListener(new MouseAdapter() {
					public void mouseClicked(MouseEvent me) {
						if (!gameEnable || PlayerFinish) return;

						Cannon();
						if (ComputerWat[row][col] != 'M' && ComputerWat[row][col] != 'H') {
							if (ComputerWat[row][col] == 'X'){
								ComputerBoard[row][col+1].splash(Constants.icMiss);
								ComputerWat[row][col] = 'M';
								LogInfo.append("Player hits " + (Character.toString((char)('A'+row))) + (col+1) +
										"and missed!" + "!(0:" + String.format("%02d", time) + ")\n");
								BattleInfo.append("Player hits " + (Character.toString((char)('A'+row))) + (col+1) +
										"and missed!" + "!(0:" + String.format("%02d", time) + ")\n");
							} else {
								ImageIcon ic = new ImageIcon("Resources/Tiles/" + Character.toString(ComputerWat[row][col]) + ".png");
								ComputerBoard[row][col+1].explore(ic); 
								computerCount++;
								LogInfo.append("Player hits " + (Character.toString((char)('A'+row))) + (col+1) +
										"and hit a " + Constants.get(ComputerWat[row][col]) +
										"!(0:" + String.format("%02d", time) + ")\n");
								BattleInfo.append("Player hits " + (Character.toString((char)('A'+row))) + (col+1) +
										"and hit a " + Constants.get(ComputerWat[row][col]) +
										"!(0:" + String.format("%02d", time) + ")\n");
								checkSink(ComputerBoard[row][col+1], ComputerWat[row][col], false);
								System.out.println("check" + ComputerWat[row][col]);
								ComputerWat[row][col] = 'H';
							}
							if (MultiPlayer) {
								sendPosi(row, col);								
							}
						}
						
						gameEnd();
						PlayerFinish = true;
					}
				});
			}
		}

		//Start 
		Start.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae) {				
				Logon.setText("Log: Player:N/A Computer:N/A");
				gameEnable = true;
				
				if (MultiPlayer) {
					sendBoard(Wat);
					catchBoard();					
					Send.setEnabled(true);
				}
				gameStart();
				
				Start.setEnabled(false);
			}
		});
		
		//send message
		Send.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent ae) {
				if (!ChatField.getText().equals("")) {
					MessageOut = ChatField.getText();
					sendMessage();
				}
			}
		});
		
		//setup Chat Filter
		Chat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(Chat.isSelected()) {
					if (Events.isSelected()) {
						CardLayout c1 = (CardLayout)outerPanel.getLayout();
						c1.show(outerPanel, "General");
					} else {
						CardLayout c2 = (CardLayout)outerPanel.getLayout();
						c2.show(outerPanel, "Chat");						
					}
				} else {
					if (Events.isSelected()) {
						CardLayout c3 = (CardLayout)outerPanel.getLayout();
						c3.show(outerPanel, "Event");
					} else {
						CardLayout c1 = (CardLayout)outerPanel.getLayout();
						c1.show(outerPanel, "General");				
					}					
				}
			}
		});

		Events.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if(Events.isSelected()) {
					if (Chat.isSelected()) {
						CardLayout c1 = (CardLayout)outerPanel.getLayout();
						c1.show(outerPanel, "General");
					} else {
						CardLayout c3 = (CardLayout)outerPanel.getLayout();
						c3.show(outerPanel, "Event");				
					}
				} else {
					if (Chat.isSelected()) {
						CardLayout c2 = (CardLayout)outerPanel.getLayout();
						c2.show(outerPanel, "Chat");	
					} else {
						CardLayout c1 = (CardLayout)outerPanel.getLayout();
						c1.show(outerPanel, "General");				
					}					
				}

			}
		});
		
		//window closed
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {				
				if (gameEnable) {
					MessageOut = "quit";
					pw.println(MessageOut);
					pw.flush();
				}
				dispose();
			}
		});
	}
	
	private void checkSink(WaterTiles wt, char ship, boolean Com) {
		if (Com) {
			if (ship == 'A') {
				cAircraft.add(wt);
				if (cAircraft.size() == 5) { 
					sink(cAircraft); 
					LogInfo.append("Player has sunken a" + Constants.A + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.A + "!(0:" + String.format("%02d", time) + ")\n");
				}
			} else if (ship == 'B') {
				cBattleship.add(wt);
				if (cBattleship.size() == 4) { 
					sink(cBattleship); 
					LogInfo.append("Player has sunken a" + Constants.B + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.B + "!(0:" + String.format("%02d", time) + ")\n");
				}
			} else if (ship == 'C') {
				cCruiser.add(wt);
				if (cCruiser.size() == 3) {
					sink(cCruiser); 
					LogInfo.append("Player has sunken a" + Constants.C + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.C + "!(0:" + String.format("%02d", time) + ")\n");
				}
			} else if (ship == 'D') {
				cDestroyer1.add(wt);
				if (cDestroyer1.size() == 2) { 
					sink(cDestroyer1); 
					LogInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
				}
				else if (cDestroyer1.size() == 4) { 
					sink(cDestroyer1); 
					LogInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
				}
			}
		} else {
			if (ship == 'A') {
				pAircraft.add(wt);
				if (pAircraft.size() == 5) { 
					sink(pAircraft);
					LogInfo.append("Computer has sunken a" + Constants.A + "!(0:" + String.format("%02d", time) + ")\n"); }
					BattleInfo.append("Computer has sunken a" + Constants.A + "!(0:" + String.format("%02d", time) + ")\n"); 
			} else if (ship == 'B') {
				pBattleship.add(wt);
				if (pBattleship.size() == 4) { 
					sink(pBattleship); 
					LogInfo.append("Computer has sunken a" + Constants.B + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Computer has sunken a" + Constants.B + "!(0:" + String.format("%02d", time) + ")\n");}				
			} else if (ship == 'C') {
				pCruiser.add(wt);
				if (pCruiser.size() == 3) { 
					sink(pCruiser); 
					LogInfo.append("Player has sunken a" + Constants.C + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.C + "!(0:" + String.format("%02d", time) + ")\n");}				
			} else if (ship == 'D') {
				pDestroyer1.add(wt);
				if (pDestroyer1.size() == 2) { 
					sink(pDestroyer1); 
					LogInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");}	
				else if (pDestroyer1.size() == 4) { 
					sink(pDestroyer1); 
					LogInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");
					BattleInfo.append("Player has sunken a" + Constants.D + "!(0:" + String.format("%02d", time) + ")\n");}		
			}
		}
	}
	
	private void sink(Vector<WaterTiles> boat) {
		try {
			Thread.sleep(1000);			
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		}
		for (int i = 0; i < boat.size(); i++) {
			boat.get(i).sink(new ImageIcon(Constants.Hit));
		}
	}
	
	private void gameStart() {
		try {
			Thread cdt = new Thread(new CountDownTimer());
			cdt.start();
			if (!MultiPlayer) {
				try {
					URL u = new URL("http://www-scf.usc.edu/~csci201/assignments/" + MapsName + ".battle");	
//					System.out.println(u);			
					Scanner scan = new Scanner(u.openStream());
					readFile(scan);
				} catch (IOException ioe) {
					System.out.println(ioe.getMessage());
				}
				
				Thread cp = new Thread(new Computer());
				cp.start();				
			} else {
				Thread t = new Thread(new Player2());
				t.start();
			}
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		}
	}
	
	private void readFile(Scanner scan) {
		try {
			String line;		
			
			for ( int i = 0; i < 10; i++ ) {
				line = scan.nextLine();
				for ( int j = 0; j < 10; j++ ) {
					ComputerWat[i][j] = line.charAt(j);
				}
				System.out.println(line);
			}
			scan.close();
		} catch ( NoSuchElementException ioe ) {
			System.out.println("IOException: " + ioe.getMessage());
		} catch ( NumberFormatException nfe ) {
			System.out.println("NumberFormatException: " + nfe.getMessage());
		} catch ( ArrayIndexOutOfBoundsException aio ) {
			System.out.println("NumberFormatException: " + aio.getMessage());
		}
	}
	
	private void createDialog() {
		//HowTo Dialog
		HowToDialog.setTitle("Battleship Instructions");
		HowToDialog.setSize(500,300);
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(1,1));
		TextArea ta = new TextArea();
		ta.append("Instructions!!!!!!!!!!!!!!!!!!!!!!!!!");
		p.add(ta);
		
		JScrollPane sp = new JScrollPane(p);
		HowToDialog.getContentPane().add(sp);
		
		//About Dialog
		AboutDialog.setTitle("About");
		AboutDialog.setSize(500,300);
		
		JPanel j = new JPanel();
		j.setLayout(new BorderLayout());
		j.add(new JLabel("Made by Xingchen Liao", SwingConstants.CENTER), BorderLayout.NORTH);
		j.add(new JLabel(new ImageIcon("profile.jpg")), BorderLayout.CENTER);
		j.add(new JLabel("CSCI201 Assignement 3", SwingConstants.CENTER), BorderLayout.SOUTH);
	
		AboutDialog.add(j);
	}
	
	private void Cannon() {
		try {
	        Clip cannon = AudioSystem.getClip();
	        File c = new File(Constants.sCannon);
	        cannon.open(AudioSystem.getAudioInputStream(c));
	        cannon.start();		
	        Thread.sleep(1000);
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
	}
	
	private void gameEnd() {
		if (playerCount >= 16) {
			JOptionPane.showMessageDialog(this, "You lost!", "Game over", JOptionPane.INFORMATION_MESSAGE);
			reset();
		}
		if (computerCount >= 16) {
			JOptionPane.showMessageDialog(this, "You won!", "Game over", JOptionPane.INFORMATION_MESSAGE);
			reset();
		}
	}
	
	private void reset() {
		gameEnable = false;
		Start.setEnabled(false);
		Logon.setText("Log: You're in edit model");
		FileLabel.setText("File: ");
		playerCount = 0;
		computerCount = 0;
		
		for (int i = 0; i < 5; i++) {
			shipValid[i] = true;
		}
		
		for (int i = 0; i < 10; i++) {
			for (int j = 1; j < 11; j++) {
				Wat[i][j-1] = 'X';
				ComputerWat[i][j-1] = 'X';
				PlayerBoard[i][j].setIcon(questionMark);
				ComputerBoard[i][j].setIcon(questionMark);
			}
		}

		try {
			br.close();
			pw.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	//determine data caught is position data or chat message
	private void catchCheck() {
		try {
			String check = br.readLine();
			if(check == null) return;
			
			if (check.equals("Position")) {
				catchPosi();
			} else if (check.equals("Message")) {
				catchMessage();
			} else if (check.equals("quit") ) {
				JOptionPane.showMessageDialog(MainWindow.this, "The other player has quit!", "Connection error", JOptionPane.ERROR_MESSAGE);
				dispose();
			}
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}		
	}
	
	//receive chat message 
	private void catchMessage() {
		try {
			MessageIn = br.readLine();
			LogInfo.append(RightName + ": " + MessageIn + "\n");
			ChatInfo.append(RightName + ": " + MessageIn + "\n");
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
		}		
	}
	
	//send chat message
	private void sendMessage() {
		LogInfo.append(LeftName + ": " + MessageOut + "\n");
		ChatInfo.append(LeftName + ": " + MessageOut + "\n");
		pw.println("Message");
		pw.println(MessageOut);
		pw.flush();
	}
	
	//send Board data
	private void sendBoard(char Wat[][]) {
		String send = "";
		for (int i = 0; i < 10; i++){
			for (int j = 0; j < 10; j++) {
				send += Wat[i][j];
			}
			System.out.println(send);
			pw.println(send);
			pw.flush();
			send = "";
		}
	}
	
	//receive Board data
	private void catchBoard() {
		try {
			String catchBoard = "";
			for (int i = 0; i < 10; i++) {
				catchBoard = br.readLine();
				for (int j = 0; j < 10; j++) {
					ComputerWat[i][j] = catchBoard.charAt(j);		
					System.out.print(ComputerWat[i][j]);			
				}	
				System.out.println();	
			}
		} catch (IOException e) {
			System.out.println("Position catch error");
		}
	}

	//catch position data
	private void catchPosi() {
		try {
			p2Row = Integer.parseInt(br.readLine());
			p2Col = Integer.parseInt(br.readLine());
			if (Wat[p2Row][p2Col] == 'X') {
				PlayerBoard[p2Row][p2Col+1].splash(Constants.icMiss);
				Wat[p2Row][p2Col] = 'M';
				LogInfo.append("Computer hit " + (Character.toString((char)('A'+p2Row))) + (p2Col+1) +
						"and missed!(0:" + String.format("%02d", time) + ")\n");
				BattleInfo.append("Computer hit " + (Character.toString((char)('A'+p2Row))) + (p2Col+1) +
						"and missed!(0:" + String.format("%02d", time) + ")\n");
			} else {
				ImageIcon ic = new ImageIcon(Constants.Explosion2);
				PlayerBoard[p2Row][p2Col+1].explore(ic);
				playerCount++;
				LogInfo.append("Computer hit " + (Character.toString((char)('A'+p2Row))) + (p2Col+1) +
						"and hit a " + Constants.get(Wat[p2Row][p2Col]) +
						"!(0:" + String.format("%02d", time) + ")\n");
				BattleInfo.append("Computer hit " + (Character.toString((char)('A'+p2Row))) + (p2Col+1) +
						"and hit a " + Constants.get(Wat[p2Row][p2Col]) +
						"!(0:" + String.format("%02d", time) + ")\n");
				checkSink(PlayerBoard[p2Row][p2Col+1], Wat[p2Row][p2Col], true);
				Wat[p2Row][p2Col] = 'H';
			}			
		} catch (IOException e) {
			System.out.println("Position catch error");
			JOptionPane.showMessageDialog(MainWindow.this, "You have lost the connection", "Connection error", JOptionPane.WARNING_MESSAGE);
			dispose();			
		}
	}
	
	//send position data
	private void sendPosi(int row, int col) {
		pw.println("Position");
		pw.println(row);
		pw.println(col);
		pw.flush();
	}
	
	
	private class Player2 implements Runnable {
		public void run() {
			while (gameEnable) {
				catchCheck();	
			}
		}
	}
	
	private class Computer implements Runnable{
		public void run() {
			try {
				while (gameEnable) {
					Thread.sleep(1);
					bufferComputer.counterAttack();							
				}		 
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
		}
		//end of Computer
	}
	
	private class CountDownTimer implements Runnable {
		public void run() {
			try {
				while (gameEnable) {
					Thread.sleep(1);
					bufferTimer.CountDown();							
				}		 
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			}
		}
	}
	
	private class Buffer {
		private Lock lock = new ReentrantLock();
		
		private void counterAttack() {
			lock.lock();
			try {
				if (!ComputerFinish) {
					int t = rand.nextInt(15);
					Thread.sleep(t * 1000);
					Cannon();
					int row = rand.nextInt(10);
					int col = rand.nextInt(10);
					
					if ( Wat[row][col] != 'M' && Wat[row][col] != 'H' ) {
						if (Wat[row][col] == 'X') {
							PlayerBoard[row][col+1].splash(Constants.icMiss);
							Wat[row][col] = 'M';
							LogInfo.append("Computer hit " + (Character.toString((char)('A'+row))) + (col+1) +
									"and missed!(0:" + String.format("%02d", 15-t) + ")\n");
							BattleInfo.append("Computer hit " + (Character.toString((char)('A'+row))) + (col+1) +
									"and missed!(0:" + String.format("%02d", 15-t) + ")\n");
						} else {
							ImageIcon ic = new ImageIcon(Constants.Explosion2);
							PlayerBoard[row][col+1].explore(ic);
							playerCount++;
							LogInfo.append("Computer hit " + (Character.toString((char)('A'+row))) + (col+1) +
									"and hit a " + Constants.get(Wat[row][col]) +
									"!(0:" + String.format("%02d", 15-t) + ")\n");
							BattleInfo.append("Computer hit " + (Character.toString((char)('A'+row))) + (col+1) +
									"and hit a " + Constants.get(Wat[row][col]) +
									"!(0:" + String.format("%02d", 15-t) + ")\n");
							checkSink(PlayerBoard[row][col+1], Wat[row][col], true);
							Wat[row][col] = 'H';
						}
					} else {
						counterAttack();
					}
					gameEnd();		
					ComputerFinish = true;		
				}
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			} finally {
				lock.unlock();
			}
		//end of countAttack
		}
		
		private void CountDown() {
			lock.lock();
			try {
				PlayerFinish = false;
				ComputerFinish = false;
				LogInfo.append("Round" + round + "\n");
				BattleInfo.append("Round" + round + "\n");
				round++;
				for (int i = 15; i >= 0; i--) {
					time = i;
					TimeCounter.setText("Time - 0:" + String.format("%02d", i));
					if (!ComputerFinish || !PlayerFinish) {
						if (time == 3) {
							LogInfo.append("Warning! 3 seconds left! \n");
							BattleInfo.append("Warning! 3 seconds left! \n");
						}
						Thread.sleep(1000);
					}
				}
			} catch (InterruptedException ie) {
				System.out.println(ie.getMessage());
			} finally {
				lock.unlock();
			}
		}
	}
	
//WaterWave animation
	private class WaterWave implements Runnable {
			
		public void run() {
			try {
				while (true) {
					for (int i = 0; i < 10; i++) {
						for (int j = 1; j < 11; j++) {
							ComputerBoard[i][j].changeImage();
							PlayerBoard[i][j].changeImage();
							Thread.sleep(10);			
						}
					}	
					Thread.sleep(250);			
				}
			} catch (InterruptedException ie) {
				System.out.println("ie: " + ie.getMessage());
			}
		}
	 }
	 
//end of MainWindow class
}

