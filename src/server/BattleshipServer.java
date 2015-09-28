package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JLabel;

import client.MainWindow;

public class BattleshipServer {
	private String loading;
	private JLabel loadingLabel;
	
	private BattleshipServerGUI bsg;
	private ServerSocket ss;
	
	private int port;
	private String LeftName, RightName;
	private MainWindow battleship;
	
	public BattleshipServer(int port, String LeftName) {
		this.port = port;
		this.LeftName = LeftName;
		//setup GUI
		bsg = new BattleshipServerGUI();
		bsg.setVisible(true); 
		
		Thread t = new Thread(new Connector());
		t.start();
	}

private class Connector implements Runnable {
	
	public void run() {
		try {
			ss = new ServerSocket(port);			
			Socket s = ss.accept(); //blocking line of code
			System.out.println("Client has connected.");
			bsg.stopCounting();
			InputStreamReader isr = new InputStreamReader(s.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(s.getOutputStream());
			pw.println(LeftName);
			pw.flush();
			RightName = br.readLine();

			battleship = new MainWindow(pw, br, true, LeftName, RightName);
			battleship.setVisible(true);

//			s.close();
			ss.close();			
		} catch(IOException ioe) {
			System.out.println("IOE: " + ioe.getMessage());
		}		
	}
}
	
//end of the Server class
}


