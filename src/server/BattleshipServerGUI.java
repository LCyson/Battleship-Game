package server;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import client.BattleshipClientGUI;

public class BattleshipServerGUI extends JFrame{
	private JLabel loadingInfo;
	private int seconds;
	private Thread th;
	private boolean Connected;
	
	public BattleshipServerGUI() {
		super("Battleship Menu");
		Connected = false;
		initializeGUI();
//		createGUI();
		addEventHandlers();
	}
	
	public void initializeGUI() {
		setSize(600, 200);
		setLocation(400, 250);
		
		loadingInfo = new JLabel("here");
		add(loadingInfo);
	}
	
	public void addEventHandlers() {
		//
		seconds = 30;
		setText("Waiting for connections..." + seconds + "s until timeout.");
		
		//setup Timer
		Timer t = new Timer();
		th = new Thread(t);
		th.start();
	}
	
	public void exit() {
		System.exit(0);
	}
	
	public void setText(String s) {
		loadingInfo.setText(s);
	}
	
	public void stopCounting() {
		Connected = true;
	}



private class Timer implements Runnable {
	private Lock lock = new ReentrantLock();
	
	public void run() {
		lock.lock();
		try {
			for (int i = 30; i > 0; i--) {
					Thread.sleep(1000);
					seconds--;
					setText("Waiting for connections..." + seconds + "s until timeout.");
			}
			if (!Connected) {
				JOptionPane.showMessageDialog(BattleshipServerGUI.this, "Connection time out", "Connection error", JOptionPane.ERROR_MESSAGE);				
			}dispose();		
		} catch (InterruptedException ie) {
			System.out.println(ie.getMessage());
		} finally {
			lock.unlock();
		}	
	}
}
}
