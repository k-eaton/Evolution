import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.border.Border;
import javax.swing.*;

public class Evolution {

	// private Cell cell = new Cell();

	public Evolution() { 
		// build the frame
		JFrame frame = new JFrame("Evolution");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);

		// build cells
		Matrix matrix = new Matrix(5);
		// cell1.updateText();

		frame.add(matrix);


		frame.pack();
		frame.setVisible(true);
	}

	// public void 

	public static void main(String args[]) {

		new Evolution();


	}
}

class Matrix extends JPanel{

	public Matrix(int numOfCells) {

		super();
		// Gridlayout layout = new GridLayout(numOfCells,numOfCells);
		this.setLayout(new GridLayout(numOfCells,numOfCells));

		// create matrix
		Cell[][] iArray = new Cell[numOfCells][numOfCells];
		// for(int i = 0; i < numOfCells; i++) {
		// 	iArray[i] = new Cell[numOfCells];
		// }

		// populate matrix
		for(int i = 0; i < numOfCells; i++) {
			for(int j = 0; j < numOfCells; j++) {
				iArray[i][j] = new Cell();
				iArray[i][j].updateText();
				this.add(iArray[i][j]);
			}
		}


		// add matrix to panel
		// for(int i = 0; i < numOfCells; i++) {
		// 	for(int j = 0; j < numOfCells) {
		// 		this.add(iArray[i][j]);
		// 	}
		// }

	}
}

class Cell extends JLabel {

	private Border border = BorderFactory.createLineBorder(Color.BLACK);

	public Cell() {
		super();
		this.setOpaque(true);
		this.setVerticalTextPosition(JLabel.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setBackground(Color.blue);
		this.setPreferredSize(new Dimension(75, 75));
		this.setBorder(border);
	}

	public void updateText() {
		Color color = this.getBackground();
		this.setText(color.getRed() + " " + color.getGreen() + " " + color.getBlue());
		this.setHorizontalAlignment(SwingConstants.CENTER);
	}

	public void mutate() {

	}
}