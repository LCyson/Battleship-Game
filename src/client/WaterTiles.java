package client;

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class WaterTiles extends JPanel{
	ImageIcon icon;
	ImageIcon iconWater;
	ImageIcon Water1;
	ImageIcon Water2;
	ImageIcon Explo[];
	ImageIcon Splash[];
	JLabel label1;
	Graphics g;
	Clip explo, splash, sinking;
	File e, p, s;
	int num;
	
	 public WaterTiles(ImageIcon icon, ImageIcon Water1, ImageIcon Water2) {
		//initialize images
		Explo = new ImageIcon[5];
		Splash = new ImageIcon[7];
		for (int i = 0; i < 5; i++) {
			Explo[i] = new ImageIcon(Constants.getExplosion(i+1));
		}
		for (int i = 0; i < 7; i++) {
			Splash[i] = new ImageIcon(Constants.getSplash(i+1));
		}
		//initialize sound
		try {
	        splash = AudioSystem.getClip();
	        p = new File(Constants.sSplash);
	        explo = AudioSystem.getClip();
	        e = new File(Constants.sExplode);	
	        sinking = AudioSystem.getClip();
	        s = new File(Constants.sSinking);				
		} catch (Exception exc) {
			System.out.println(exc.getMessage());
		}
        
		//setup basic icons
		this.icon = icon;
        this.Water1 = new ImageIcon(Constants.Water1);
        iconWater = new ImageIcon(Constants.Water2);
        this.Water2 = new ImageIcon(Constants.Water2);  
//        this.Water1 = Water1;
//        iconWater = Water2;
//        this.Water2 = Water2; 
        label1 = new JLabel(icon); 
        num = 1;

        add(label1);
//        Thread t = new Thread(new WaterWave());
//        t.start();
	 }
	 @Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    this.g = g;
        g.drawImage(iconWater.getImage(), 0, 0, null);
	 }

	 public void explore(ImageIcon icon) {
		 Thread t = new Thread(new Explosion());
		 t.start();
		 this.icon = icon;
	 }

	 public void splash(ImageIcon icon) {
			try {
		        splash.open(AudioSystem.getAudioInputStream(p));
		        splash.start();		
			} catch (Exception exc) {
						System.out.println(exc.getMessage());	
			}
		 Thread t = new Thread(new Splash());
		 t.start();
		 this.icon = icon;
	 }

	 public void sink(ImageIcon icon) {
			try {
		        sinking.open(AudioSystem.getAudioInputStream(s));
		        sinking.start();		
			} catch (Exception exc) {
						System.out.println(exc.getMessage());	
			}
		 Thread t = new Thread(new Splash());
		 t.start();
		 this.icon = icon;
	 }
	 
	 
	 public void setIcon(ImageIcon icon) {
		 label1.setIcon(icon);
	 }
	 
	 public void changeImage() {
		 if (num == 1) {
			 iconWater.setImage(Water1.getImage());
			 num = 2;
			 repaint();
		 } else {
			 iconWater.setImage(Water2.getImage());
			 num = 1;
			 repaint();
		 }
	 }

	 private void ExploAnimation(){
		try {
	        explo.open(AudioSystem.getAudioInputStream(e));
	        explo.start();		
		} catch (Exception exc) {
					System.out.println(exc.getMessage());	
		}
		 for (int i = 0; i < 5; i++) {
			 setIcon(Explo[i]);
			 try {
				 Thread.sleep(200);				 
			 } catch (InterruptedException ie) {
				 System.out.println(ie.getMessage());
			 }
		 } 
		 setIcon(icon);
		 explo.close();
	 }
	 
	 private void SplashAnimation(){
		 for (int i = 0; i < 7; i++) {
			 setIcon(Splash[i]);
			 try {
				 Thread.sleep(300);				 
			 } catch (InterruptedException ie) {
				 System.out.println(ie.getMessage());
			 }
		 } 
		 setIcon(icon);
	 }
	 
	 
	 private class Explosion implements Runnable {
		 public void run() {
				try {
					ExploAnimation();
			        Thread.sleep(10);
				} catch (InterruptedException ie) {
					System.out.println("ie: " + ie.getMessage());
				}			 
		 }
	 }
	 
	 private class Splash implements Runnable {
		 public void run() {
				try {
					SplashAnimation();
			        Thread.sleep(10);
				} catch (InterruptedException ie) {
					System.out.println("ie: " + ie.getMessage());
				}			 
		 }
	 }
}
