import java.awt.Color;
import java.awt.Dimension;

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
		float[] rgb = new float[3];
		Cell cell1 = new Cell();
		cell1.updateText();

		frame.add(cell1);


		frame.pack();
		frame.setVisible(true);
	}

	// public void 

	public static void main(String args[]) {

		new Evolution();


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