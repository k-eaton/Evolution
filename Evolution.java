import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.border.Border;
import javax.swing.*;

public class Evolution {

	private final Matrix matrix = new Matrix(6);

	public Evolution() { 
		// build the frame
		JFrame frame = new JFrame("Evolution");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		// panels for things to live in
		JPanel matrixContainer = new JPanel();
		JPanel buttons = new JPanel();
		buttons.setPreferredSize(new Dimension(300, 50));

		// buttons
		JButton evolveButton = new JButton("Evolve");
		evolveButton.setPreferredSize(new Dimension(90, 30));
		evolveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				System.out.println("Evolve Button activated");

				matrix.evolve(matrix.cellMatrix, matrix.cellDeathMatrix);
			}
		});

		JButton resetButton = new JButton("Reset");
		resetButton.setPreferredSize(new Dimension(90, 30));
		resetButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt) {
				System.out.println("Reset Button activated");

				for(int i = 0; i < 6; i++) {
					for(int j = 0; j < 6; j++) {
						matrix.cellMatrix[i][j].updateCell(0, 0, 0);
						matrix.cellMatrix[i][j].setBorder(Cell.borderBlack);

					}
				}
			}
		});

		matrixContainer.add(matrix);
		buttons.add(evolveButton);
		buttons.add(resetButton);

		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 0;
		// c.anchor = GridBagConstraints.NORTH;
		c.gridx = 0;
		c.gridy = 0;

		frame.add(matrixContainer, c);

		c.gridy = 1;
		frame.add(buttons, c);

		frame.pack();
		frame.setVisible(true);
	}

	// public void 

	public static void main(String args[]) {

		new Evolution();


	}
}

class Matrix extends JPanel{

	// create matrices
	public Cell[][] cellMatrix;
	public Boolean[][] cellDeathMatrix;
	private int arrayLength = 1;

	public Matrix(int numOfCells) {

		super();

		this.arrayLength = numOfCells;
		this.setLayout(new GridLayout(numOfCells,numOfCells));

		// create matrices
		cellMatrix = new Cell[numOfCells][numOfCells];
		cellDeathMatrix = new Boolean[numOfCells][numOfCells];

		// populate matrix
		for(int i = 0; i < numOfCells; i++) {
			for(int j = 0; j < numOfCells; j++) {
				cellMatrix[i][j] = new Cell();
				cellMatrix[i][j].updateCell(0, 0, 0);
				this.add(cellMatrix[i][j]);
				cellDeathMatrix[i][j] = false;
			}
		}
	}

	public static Matrix getMatrix() {
		return new Matrix(6);
	}

	public void evolve(Cell[][] cellMatrix, Boolean[][] cellDeathMatrix) {

		// reset borders
		for(int i = 0; i < arrayLength; i++) {
			for(int j = 0; j < arrayLength; j++) {
				cellMatrix[i][j].setBorder(Cell.borderBlack);
			}
		}

		// compute fitness
		for(int i = 0; i < arrayLength - 1; i++){
			for(int j = 0; j < arrayLength - 1; j++) {
				// passing a 2 x 2 matrix of colors:
				// a b
				// c d
				Color a = cellMatrix[i][j].getBackground();
				Color b = cellMatrix[i][j + 1].getBackground();
				Color c = cellMatrix[i + 1][j].getBackground();
				Color d = cellMatrix[i + 1][j + 1].getBackground();
				Color toRemove = computeFitness(a, b, c, d);

				// identify who to kill off (only 1 of the 4) & recombination
				if (toRemove.getRGB() == a.getRGB()) {
					cellDeathMatrix[i][j] = true;

					// create new cell through recombination
					cellMatrix[i][j] = recombination(b, c, d, cellMatrix[i][j]);
					cellMatrix[i][j].setBorder(Cell.borderRed);

				} else if (toRemove.getRGB() == b.getRGB()) {
					cellDeathMatrix[i][j + 1] = true;
					
					// create new cell through recombination
					cellMatrix[i][j + 1] = recombination(a, c, d, cellMatrix[i][j + 1]);
					cellMatrix[i][j + 1].setBorder(Cell.borderRed);

				} else if (toRemove.getRGB() == c.getRGB()) {
					cellDeathMatrix[i + 1][j] = true;
					
					// create new cell through recombination
					cellMatrix[i + 1][j] = recombination(b, a, d, cellMatrix[i + 1][j]);
					cellMatrix[i + 1][j].setBorder(Cell.borderRed);

				} else {
					cellDeathMatrix[i + 1][j + 1] = true;
					
					// create new cell through recombination
					cellMatrix[i + 1][j + 1] = recombination(b, c, a, cellMatrix[i + 1][j + 1]);
					cellMatrix[i + 1][j + 1].setBorder(Cell.borderRed);
				}
			}
		}

		// mutate cells
		for(int i = 0; i < arrayLength; i++){
			for(int j = 0; j < arrayLength; j++) {
				if(cellDeathMatrix[i][j] == true) {
					cellMatrix[i][j] = mutate(cellMatrix[i][j]);
					cellDeathMatrix[i][j] = false;
				}
			}
		}
	}

	private Color computeFitness(Color a, Color b, Color c, Color d){
		// cell a
		int colorRedA = a.getRed();
		int colorGreenA = a.getGreen();
		int colorBlueA = a.getBlue();
		int totalColorA = colorRedA + colorGreenA + colorBlueA;

		// cell b
		int colorRedB = b.getRed();
		int colorGreenB = b.getGreen();
		int colorBlueB = b.getBlue();
		int totalColorB = colorRedB + colorGreenB + colorBlueB;

		// cell c
		int colorRedC = c.getRed();
		int colorGreenC = c.getGreen();
		int colorBlueC = c.getBlue();
		int totalColorC = colorRedC + colorGreenC + colorBlueC;
		System.out.println(totalColorC);

		// cell d
		int colorRedD = d.getRed();
		int colorGreenD = d.getGreen();
		int colorBlueD = d.getBlue();
		int totalColorD = colorRedD + colorGreenD + colorBlueD;

		// who has lowest fitness?
		int[] colorFitness = {totalColorA, totalColorB, totalColorC, totalColorD};
		Color[] associatedCell = {a, b, c, d};

		int lowestColorTotal = totalColorA;
		Color lowestColorCell = a;

		for(int i = 0; i < colorFitness.length; i++) {
			if(lowestColorTotal > colorFitness[i]) {
				lowestColorTotal = colorFitness[i];
				lowestColorCell = associatedCell[i];
				System.out.println("lowestColorCell = " + lowestColorCell);
			} 
		}

		return lowestColorCell;
	}

	private Cell recombination(Color a, Color b, Color c, Cell cell) {
		// shuffle the source colors
		Color[] colorArray = {a, b, c};

		List<Color> colorList = Arrays.asList(colorArray);

		Collections.shuffle(colorList);

		colorList.toArray(colorArray);

		cell.updateCell(colorArray[0].getRed(), colorArray[1].getGreen(), colorArray[2].getBlue());

		return cell;
	}

	private Cell mutate(Cell cell) {
		Random r = new Random();

		Color color = cell.getBackground();

		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		// has the cell reached white? if so, don't change anything
		// if(red + blue + green == 765) return cell;

		int[] colors = {red, green, blue};

		// gives a 60% chance of mutation
		if(getRandomNumberInRange(0, 100) > 40) {

			// how much are we mutating?
			int numToAdd = getRandomNumberInRange(-50, 50);
			System.out.println("Random number to add: " + numToAdd);

			// which color are we mutating?
			int whichColorToChange = getRandomNumberInRange(0, 2);

			// change the color value
			colors[whichColorToChange] += numToAdd;
			if(colors[whichColorToChange] > 255) colors[whichColorToChange] = 255;
			if(colors[whichColorToChange] < 0) colors[whichColorToChange] = 0;
		}

		cell.updateCell(colors[0], colors[1], colors[2]);
		return cell;
	} 

	private static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
	}
}

class Cell extends JLabel {

	public static Border borderBlack = BorderFactory.createLineBorder(Color.BLACK, 5);
	public static Border borderRed = BorderFactory.createLineBorder(Color.RED, 2);

	public Cell() {
		super();
		this.setOpaque(true);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setForeground(Color.WHITE);
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(100, 100));
		this.setBorder(borderBlack);
	}

	public void updateCell(int red, int green, int blue) {
		this.setBackground(new Color(red, green, blue));
		this.setText(red + " " + green + " " + blue);

		// at halfway color point change text to black for readability
		if(red + green + blue >= 350) {
			this.setForeground(Color.BLACK);
		} else {
			this.setForeground(Color.WHITE);
		}

		this.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void mutate() {

	}
}