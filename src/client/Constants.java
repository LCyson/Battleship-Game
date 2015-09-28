package client;

import javax.swing.ImageIcon;

public class Constants {
	public static final String Miss= "Resources/Tiles/M.png";
	public static final String Hit= "Resources/Tiles/X.png";
	public static final String Explosion1= "Resources/explosion/expl1.png";
	public static final String Explosion2= "Resources/explosion/expl2.png";
	public static final String Explosion3= "Resources/explosion/expl3.png";
	public static final String Explosion4= "Resources/explosion/expl4.png";
	public static final String Explosion5= "Resources/explosion/expl5.png";
	public static final String Splash1= "Resources/splash/splash1.png";
	public static final String Splash2= "Resources/splash/splash2.png";
	public static final String Splash3= "Resources/splash/splash3.png";
	public static final String Splash4= "Resources/splash/splash4.png";
	public static final String Splash5= "Resources/splash/splash5.png";
	public static final String Splash6= "Resources/splash/splash6.png";
	public static final String Splash7= "Resources/splash/splash7.png";
	public static final String sCannon= "Resources/Sounds/cannon.wav";
	public static final String sExplode= "Resources/Sounds/explode.wav";
	public static final String sSinking= "Resources/Sounds/sinking.wav";
	public static final String sSplash= "Resources/Sounds/splash.wav";
	public static final String Water1 = "Resources/animatedWater/water1.png";
	public static final String Water2 = "Resources/animatedWater/water2.png";
	public static final String D= "Destroyer";
	public static final String A= "Aircraft Carrier";
	public static final String B= "Battleship";
	public static final String C= "Crusier";
	

	public static final ImageIcon icMiss = new ImageIcon((Constants.Miss));
	public static final ImageIcon icExplo[] = new ImageIcon[5];
	public static final ImageIcon icSplash[] = new ImageIcon[7];
	public static final ImageIcon icWater1 = new ImageIcon(Water1);
	public static final ImageIcon icWater2 = new ImageIcon(Water2);
	{
		for (int i = 0; i < 5; i++) {
			icExplo[i] = new ImageIcon(Constants.getExplosion(i+1));
			icSplash[i] = new ImageIcon(Constants.getSplash(i+1));
		}
		icSplash[5] = new ImageIcon(Constants.getSplash(6));
		icSplash[6] = new ImageIcon(Constants.getSplash(7));		
	}
	
	public static String get(char g) {
		switch (g) {
		case 'A':
			return A;
		case 'B':
			return B;
		case 'C':
			return C;
		case 'D':
			return D;			
		}
		return null;
	}
	public static String getExplosion(int g) {
		switch (g) {
		case 1:
			return Explosion1;
		case 2:
			return Explosion2;
		case 3:
			return Explosion3;
		case 4:
			return Explosion4;	
		case 5:
			return Explosion5;
		}
		return null;
	}
	public static String getSplash(int g) {
		switch (g) {
		case 1:
			return Splash1;
		case 2:
			return Splash2;
		case 3:
			return Splash3;
		case 4:
			return Splash4;	
		case 5:
			return Splash5;
		case 6:
			return Splash6;
		case 7:
			return Splash7;
		}
		return null;
	}
}
