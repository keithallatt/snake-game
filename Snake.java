import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Snake extends JPanel {
	public static void main(String[]args) {
		JFrame frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Snake snake = new Snake();
		
		frame.getContentPane().add(snake);
			
		frame.pack();
		frame.setVisible(true);
	}
	
	int width, height;
	
	/*
	 * board[i][j] will have an int 0 < z < width*height. where the head of the snake will set the contents of its location to its length.
	 * for all non-zero cells, each move will decrement the value. The new head postiion will get updated. If the snake head moves to a cell
	 * which is already non zero, then the snake collided with itself. therefore the board, the snake head and next apple are the only info
	 * needed. :)
	 */
	int[][] board;
	
	final int border = 5;
	final int cell = 33;
	
	final Dimension size;
	
	int snake_x, snake_y, snake_direction, apple_x, apple_y;

	public Snake() {
		// board width and height in cell #
		width = 15;
		height = 18;
			
		size = new Dimension(2*border + cell*width, 2*border + cell*height);

		board = new int[width][height];

		setPreferredSize(size);
		setVisible(true);
		
		

		new Timer(100, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				repaint();
			}			
		}).start();
	
	}

	public void paintComponent(Graphics g) {
		Graphics2D page = (Graphics2D)g;
		page.setColor(Color.BLACK);
		page.fillRect(0, 0, (int)size.width, (int)size.height);		
		
		page.setColor(Color.WHITE);
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				page.drawRect(
					border + i*cell,
					border + j*cell,
					cell,
					cell
					);
			}
	}
}
