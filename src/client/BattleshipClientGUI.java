package client;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import server.BattleshipServer;

public class BattleshipClientGUI extends JFrame{
	private String myIP;
	private JLabel YourIP, Name, EnterAnIP, Port;
	private JCheckBox HostGame, CustomPort, Maps;
	private JButton Refresh, Connect;
	private JTextField NameT, ipT, PortT, MapsT;
	private CardLayout cl;
	
	private int portNum;
	private String ipAddress, battleMaps, playerName;
	private BattleshipServer bss;
	private BattleshipClient bsc;
	
	public BattleshipClientGUI(BattleshipClient bsc) {
		super("Battleship Menu");
		this.bsc = bsc;
		
		initializeGUI();
		createGUI();
		addEventHandlers();
	}
	
	public void initializeGUI() {
		setSize(600, 200);
		setLocation(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//setup buttons
		HostGame = new JCheckBox("Host Game");
		CustomPort = new JCheckBox("Custom Port");
		Maps = new JCheckBox("201 Maps");
		
		Refresh = new JButton("Refresh");
		Refresh.setEnabled(false);
		Connect = new JButton("Connect");
		//setup IP Address
		try {			
			URL toCheckIp = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
			myIP = in.readLine();
		} catch (IOException ioe) {
			myIP = "Error";
			Refresh.setEnabled(true);
		}
		//setup labels and fields
		YourIP = new JLabel("Your IP: " + myIP);
		Name = new JLabel("Name: ");
		EnterAnIP = new JLabel("Enter an IP: ");
		Port = new JLabel("Port: ");
		
		NameT = new JTextField("");
		ipT = new JTextField("localhost");
		PortT = new JTextField("1234");
		PortT.setEnabled(false);
		MapsT = new JTextField("");
		
	}
	
	public void createGUI() {
		JPanel jp = new JPanel(new GridBagLayout());
		jp.setSize(new Dimension(700, 500));
		add(jp);
		GridBagConstraints gbc = new GridBagConstraints();
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		jp.add(YourIP, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		jp.add(Name, gbc);
		gbc.gridx = 1;
		gbc.ipadx = 100;
		jp.add(NameT,gbc);
		
		gbc.gridy = 4;
		gbc.gridx = 0;
		jp.add(HostGame, gbc);
		gbc.gridx = 1;
		jp.add(EnterAnIP, gbc);
		gbc.gridx = 2;
		jp.add(ipT, gbc);
		
		gbc.gridy = 5;
		gbc.gridx = 0;
		jp.add(CustomPort, gbc);
		gbc.gridx = 1;
		jp.add(Port, gbc);
		gbc.gridx = 2;
		jp.add(PortT, gbc);
		
		gbc.gridy = 7;
		gbc.gridx = 0;
		jp.add(Maps, gbc);
		gbc.gridx = 1;
		jp.add(MapsT, gbc);
		
		gbc.gridy = 9;
		gbc.gridx = 1;
		jp.add(Refresh, gbc);
		gbc.gridx = 2;
		jp.add(Connect,gbc);
		
	}
	
	public void addEventHandlers() {
		//Maps button
		Maps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (Maps.isSelected()) {
					HostGame.setEnabled(false);
					CustomPort.setEnabled(false);
				} else {
					HostGame.setEnabled(true);
					CustomPort.setEnabled(true);					
				}
			}
		});
		
		//Host Game or not
		HostGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (HostGame.isSelected()) {
					ipT.setEnabled(false);
				} else {
					ipT.setEnabled(true);			
				}
			}
		});

		//Custom Port
		CustomPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				if (CustomPort.isSelected()) {
					PortT.setEnabled(true);
				} else {
					PortT.setEnabled(false);			
				}
			}
		});		
		
		//Refresh 
		Refresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				try {			
					URL toCheckIp = new URL("http://checkip.amazonaws.com");
					BufferedReader in = new BufferedReader(new InputStreamReader(toCheckIp.openStream()));
					myIP = in.readLine();
					Refresh.setEnabled(false);
					YourIP.setText("Your IP: " + myIP);					
				} catch (IOException ioe) {
					myIP = "Error";
				}
			}
		});
		
		//Connect to server
		Connect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae){
				if ( NameT.getText().equals("") ) {
					JOptionPane.showMessageDialog(BattleshipClientGUI.this, "Please input a name", "Connection error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (!HostGame.isSelected() && ipT.getText().equals("")) {
					JOptionPane.showMessageDialog(BattleshipClientGUI.this, "Please input an IP Address", "Connection error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (CustomPort.isSelected() && PortT.getText().equals("")) {
					JOptionPane.showMessageDialog(BattleshipClientGUI.this, "Please input a port number", "Connection error", JOptionPane.ERROR_MESSAGE);
					return;					
				}
				
				if (HostGame.isSelected()) {
					if (CustomPort.isSelected()) {
						bss = new BattleshipServer( Integer.parseInt(PortT.getText()), NameT.getText() );		
					} else {
						bss = new BattleshipServer(1234, NameT.getText());								
					}
				} else {
					if (Maps.isSelected()) {
						bsc.aloneMode(MapsT.getText());
					} else if (CustomPort.isSelected()) {
						boolean connectionStatus = bsc.battleMode(ipT.getText(), Integer.parseInt(PortT.getText()), NameT.getText());
						if (!connectionStatus) {
							JOptionPane.showMessageDialog(BattleshipClientGUI.this, "Connection refused", "Connection error", JOptionPane.ERROR_MESSAGE);
							Refresh.setEnabled(true);
						}

					} else {
						boolean connectionStatus = bsc.battleMode(ipT.getText(), 1234, NameT.getText());
						if (!connectionStatus) {
							JOptionPane.showMessageDialog(BattleshipClientGUI.this, "Connection refused", "Connection error", JOptionPane.ERROR_MESSAGE);
							Refresh.setEnabled(true);
						}
					}
				}
			}
		});
	}
}
