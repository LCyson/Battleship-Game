package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class PlaceShips extends JFrame{
	
	static final long serialVersionUID = 771;
	
	int col, row;
	boolean shipValid[];
	String Direct;
	char Wat[][];
	Vector<String> options;
	JLabel shipName;
	WaterTiles PlayerBoard[][];
	JButton pShip;
	JComboBox<String> shipTypes;
	JRadioButton South, West, North, East;
	ButtonGroup bg;
	
	
	public PlaceShips(int row, int col, char Wat[][], JPanel PlayerBoard[][], boolean shipValid[]) {
		String name = "Select ship at " + (Character.toString((char)('A'+row))) + (col+1);
		this.setTitle(name);
		this.row = row; this.col = col;
		this.shipValid = shipValid;
		this.PlayerBoard = (WaterTiles[][])PlayerBoard; this.Wat = Wat;
		
		initializeGUI();
		createGUI();
		EventHandled();
	}
	
	
	private boolean overlap( String shipTypes, String Direct) {
		int num = 0; String s = "X";
		if (shipTypes.equals("Aircraft Carrier")) {
			num = 5; s = "A"; 
		}
		if (shipTypes.equals("Battleship")) {
			num = 4; s = "B"; 
		}
		if (shipTypes.equals("Cruiser")) {
			num = 3; s = "C"; 
		}
		if (shipTypes.equals("Destroyer")) {
			num = 2; s = "D"; 
		}
		
		try {
			if ( Direct.equals("South") ) {
				for ( int i = 0; i < num; i++ ) {
					if ( Wat[row+i][col] != 'X' ) {
						return false;
					}
				}
			} else if ( Direct.equals("North")) {
					for ( int i = 0; i < num; i++ ) {
						if ( Wat[row-i][col] != 'X' ) {
							return false;
						}
					}
			} else if ( Direct.equals("East")) {
				for ( int i = 0; i < num; i++ ) {
					if (Wat[row][col+i] != 'X' ) {
						return false;
					}
				}
			} else if ( Direct.equals("West")) {
				for ( int i = 0; i < num; i++ ) {
					if (Wat[row][col-i] != 'X' ) {
						return false;
					}
				}
			}
		} catch (IndexOutOfBoundsException aob) {
			return false;
		}
		
		return true;		
		//end
	}

	private void initializeGUI() {
		setSize(500,300);
		setLocation(500,400);
		setVisible(true);
		
		Direct = "West";
		shipName = new JLabel("Select Ship: ");
		options = new Vector<String>();
		if (shipValid[0]) options.add("Aircraft Carrier");
		if (shipValid[1]) options.add("Battleship");
		if (shipValid[2]) options.add("Cruiser"); 
		if (shipValid[3] || shipValid[4]) options.add("Destroyer");
		 
		shipTypes = new JComboBox<String>(options);
		shipTypes.setForeground(Color.BLUE);
		shipTypes.setSelectedItem("Aircraft Carrier");
		
		South = new JRadioButton("South");
		West = new JRadioButton("West");
		North = new JRadioButton("North");
		East = new JRadioButton("East");
		
		pShip = new JButton("Place Ship");
		pShip.setEnabled(false);
		
		bg = new ButtonGroup();
	}
	
	private void createGUI() {
		JPanel Select = new JPanel();
		Select.setLayout(new BorderLayout());
		//top 
		JPanel topPanel = new JPanel();
		topPanel.add(shipName);
		topPanel.add(shipTypes);
		//mid
		JPanel midPanel = new JPanel();
		midPanel.setLayout(new GridLayout(2,2));
		midPanel.add(North);
		midPanel.add(South);
		midPanel.add(East);
		midPanel.add(West);

		bg.add(North);
		bg.add(South);
		bg.add(East);
		bg.add(West);
		West.setSelected(true);
		//button
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(pShip);

		add(topPanel, BorderLayout.NORTH);
		add(midPanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private void EventHandled() {
		//add Buttons' ActionListeners
		South.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Direct = "South";
				boolean valid = overlap( (String)(shipTypes.getSelectedItem()), "South");
				if (valid) {
					pShip.setEnabled(true);
				} else {
					pShip.setEnabled(false);
				}
			}
		});
		North.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Direct = "North";
				boolean valid = overlap( (String)(shipTypes.getSelectedItem()), "North");
				if (valid) {
					pShip.setEnabled(true);
				} else {
					pShip.setEnabled(false);
				}
			}
		});
		West.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Direct = "West";
				boolean valid = overlap( (String)(shipTypes.getSelectedItem()), "West");
				if (valid) {
					pShip.setEnabled(true);
				} else {
					pShip.setEnabled(false);
				}
			}
		});
		East.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Direct = "East";
				boolean valid = overlap( (String)(shipTypes.getSelectedItem()), "East");
				if (valid) {
					pShip.setEnabled(true);
				} else {
					pShip.setEnabled(false);
				}
			}
		});
		
		shipTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				boolean valid = overlap( (String)(shipTypes.getSelectedItem()), Direct);
				if (valid) {
					pShip.setEnabled(true);
				} else {
					pShip.setEnabled(false);
				}
			}
		});
		
		pShip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				int num = 0; String s = "X";
				if (shipTypes.getSelectedItem().equals("Aircraft Carrier")) {
					num = 5; s = "A"; shipValid[0] = false;
				}
				if (shipTypes.getSelectedItem().equals("Battleship")) {
					num = 4; s = "B"; shipValid[1] = false;
				}
				if (shipTypes.getSelectedItem().equals("Cruiser")) {
					num = 3; s = "C"; shipValid[2] = false;
				}
				if (shipTypes.getSelectedItem().equals("Destroyer")) {
					num = 2; s = "D"; 
					if (shipValid[3]) {
						shipValid[3] = false;
					} else {
						shipValid[4] = false;
					}
				}
				
				if ( Direct.equals("South") ) {
					for ( int i = 0; i < num; i++ ) {
						ImageIcon ic = new ImageIcon(("Resources/Tiles/"+s+".png"));
						PlayerBoard[row+i][col+1].setIcon(ic);
						Wat[row+i][col] = s.charAt(0);
					}
				} else if ( Direct.equals("North")) {
						for ( int i = 0; i < num; i++ ) {
							ImageIcon ic = new ImageIcon(("Resources/Tiles/"+s+".png"));
							PlayerBoard[row-i][col+1].setIcon(ic);
							Wat[row-i][col] = s.charAt(0);
						}
				} else if ( Direct.equals("East")) {
					for ( int i = 0; i < num; i++ ) {
						ImageIcon ic = new ImageIcon(("Resources/Tiles/"+s+".png"));
						PlayerBoard[row][col+i+1].setIcon(ic);
						Wat[row][col+i] = s.charAt(0);
					}
				} else if ( Direct.equals("West")) {
					for ( int i = 0; i < num; i++ ) {
						ImageIcon ic = new ImageIcon(("Resources/Tiles/"+s+".png"));
						PlayerBoard[row][col-i+1].setIcon(ic);
						Wat[row][col-i] = s.charAt(0);
					}
				}
				

				dispose();
			}
		});
		
		
		
		
	}

}
