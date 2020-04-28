import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.border.Border;
import javax.swing.*;

public class Evolution3 {

	private final int GRIDSIZE = 8;
	private final Matrix matrix = new Matrix(GRIDSIZE);

	public Evolution3() { 
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

				for(int i = 0; i < GRIDSIZE; i++) {
					for(int j = 0; j < GRIDSIZE; j++) {
						matrix.cellMatrix[i][j].updateCell(0, 0, 0);
						matrix.cellMatrix[i][j].setBorder(Cell.borderUnchanged);

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

		frame.add(matrix, c);

		c.gridy = 1;
		frame.add(buttons, c);

		frame.pack();
		frame.setVisible(true);
	}

	// public void 

	public static void main(String args[]) {

		new Evolution3();


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

		// populate matrices
		for(int i = 0; i < numOfCells; i++) {
			for(int j = 0; j < numOfCells; j++) {
				cellMatrix[i][j] = new Cell();
				cellMatrix[i][j].updateCell(0, 0, 0);
				this.add(cellMatrix[i][j]);
				cellDeathMatrix[i][j] = false;
			}
		}
	}

	public void evolve(Cell[][] cellMatrix, Boolean[][] cellDeathMatrix) {

		// reset borders
		for(int i = 0; i < arrayLength; i++) {
			for(int j = 0; j < arrayLength; j++) {
				cellMatrix[i][j].setBorder(Cell.borderUnchanged);
			}
		}

		// evolution process
		for(int i = 0; i < arrayLength - 1; i++){
			for(int j = 0; j < arrayLength - 1; j++) {
				// passing a 2 x 2 matrix of cells:
				// a b
				// c d

				ArrayList<Cell> cells = new ArrayList<>();

				cells.add(cellMatrix[i][j]);  //.getBackground();
				cells.add(cellMatrix[i][j + 1]);  //.getBackground();
				cells.add(cellMatrix[i + 1][j]);  //.getBackground();
				cells.add(cellMatrix[i + 1][j + 1]);  //.getBackground();

				// determine fitness
				Cell cellToKill = computeFitness(cells);

				cells.remove(cellToKill);

				// pass on ArrayList of healthy cells, and cell to kill to recombine
				Cell newCell = recombination(cells, cellToKill);
				newCell.setBorder(Cell.borderChanged);

				// possibly mutate the new cell
				mutate(newCell);
			}
		}
	}

	private Cell computeFitness(ArrayList<Cell> cells){

		// // who has lowest fitness?
		int[] colorFitness = {determineFitnessNumber(cells.get(0)),
							  determineFitnessNumber(cells.get(1)),
							  determineFitnessNumber(cells.get(2)),
							  determineFitnessNumber(cells.get(3))};


		int lowestColorTotal = colorFitness[0];

		// determine the lowest color value cell
		for(int i = 0; i < colorFitness.length; i++) {
			if(lowestColorTotal > colorFitness[i]) {
				lowestColorTotal = colorFitness[i];
			} 
		}

		// if more than one cell with the same fitness value
		ArrayList<Cell> deathCandidates = new ArrayList<>();

		for(int j = 0; j < 4; j++) {
			if(colorFitness[j] == lowestColorTotal) {
				deathCandidates.add(cells.get(j));
			}
		}

		// shuffle cell array
		List<Cell> list = deathCandidates;

		Collections.shuffle(list);

		deathCandidates = new ArrayList<Cell>(list);

		// ArrayList<Cell> cellDeathCandidates = shuffleArrayList(deathCandidates);
		Cell cellToKill = deathCandidates.get(0);
		return cellToKill;
	}

	private Cell recombination(ArrayList<Cell> cells, Cell cell) {
		// shuffle the source colors
		ArrayList<Color> colors = new ArrayList<>();
		colors.add(cells.get(0).getBackground());
		colors.add(cells.get(1).getBackground());
		colors.add(cells.get(2).getBackground());

		// shuffle ArrayList
		List<Color> list = colors;

		Collections.shuffle(list);

		colors = new ArrayList<Color>(list);

		// create the new child cell
		cell.updateCell(colors.get(0).getRed(), 
						colors.get(1).getGreen(), 
						colors.get(2).getBlue());

		return cell;
	}

	private Cell mutate(Cell cell) {
		Random r = new Random();

		Color color = cell.getBackground();

		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();

		int[] colors = {red, green, blue};

		// gives a 40% chance of mutation
		if(getRandomNumberInRange(0, 100) > 60) {

			// how much are we mutating?
			int numToAdd = getRandomNumberInRange(-40, 40);
			System.out.println("Random number to add: " + numToAdd);

			// which color are we mutating?
			int whichColorToChange = getRandomNumberInRange(0, 99);
			if(whichColorToChange < 33) whichColorToChange = 0;
			else if(whichColorToChange < 66) whichColorToChange = 1;
			else whichColorToChange = 2;

			// change the color value
			colors[whichColorToChange] += numToAdd;
			if(colors[whichColorToChange] > 255) colors[whichColorToChange] = 255;
			if(colors[whichColorToChange] < 0) colors[whichColorToChange] = 0;
		}

		cell.updateCell(colors[0], colors[1], colors[2]);
		return cell;
	} 

	private static int determineFitnessNumber(Cell cell) {

		// fitness is determined by adding together the RGB values
		Color cellColor = cell.getBackground();
		int colorRed = cellColor.getRed();
		int colorGreen = cellColor.getGreen();
		int colorBlue = cellColor.getBlue();
		int totalColor = colorRed + colorGreen + colorBlue;

		return totalColor;
	}

	private static int getRandomNumberInRange(int min, int max) {
		Random r = new Random();
		return r.ints(min, (max + 1)).findFirst().getAsInt();
	}
}
class Cell extends JLabel {

	public static Border borderUnchanged = BorderFactory.createLineBorder(Color.BLACK, 2);
	public static Border borderChanged = BorderFactory.createLineBorder(Color.BLUE, 2);

	public Cell() {
		super();
		this.setOpaque(true);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setForeground(Color.WHITE);
		this.setBackground(Color.BLACK);
		this.setPreferredSize(new Dimension(100, 100));
		this.setBorder(borderUnchanged);
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

	public boolean equals(Cell cell){
        if(this==cell) return true;
        return false;
	}
}