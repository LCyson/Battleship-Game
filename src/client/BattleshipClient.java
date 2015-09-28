package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;

public class BattleshipClient {
	int myPort;
	MainWindow battleship;
	private PrintWriter pw;
	private BufferedReader br;
	private Socket s;
	private InputStreamReader isr;
	
	public BattleshipClient(String hostname, int port) {		
		BattleshipClientGUI bcg = new BattleshipClientGUI(this);
		bcg.setVisible(true);
	}
	
	public boolean battleMode(String hostname, int port, String PlayerName) {
		try {			
			InetAddress addr = InetAddress.getByName(hostname);
			  hostname = addr.getHostName();
			  System.out.println(hostname);
			  
			s = new Socket(hostname, port);
			pw = new PrintWriter(s.getOutputStream());
			isr = new InputStreamReader(s.getInputStream());
			br = new BufferedReader(isr);
			pw.println(PlayerName);
			pw.flush();
			String EnemyName = br.readLine();
			
			battleship = new MainWindow(pw, br, true, PlayerName, EnemyName);
			battleship.setVisible(true);
			
//			s.close();
//			isr.close();		
			return true;
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			return false;
		}
	}
	
	public void aloneMode(String MapsName) {			
			battleship = new MainWindow(pw, br, MapsName);
			battleship.setVisible(true);
	}
	
	public static void main(String [] args) {
		BattleshipClient MyClient = new BattleshipClient("localhost", 6789);
	}
}
