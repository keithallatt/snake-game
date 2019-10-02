import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Snake extends JPanel {
	public static void main(String[]args) {
		String difficulty = "easy";
		if (args.length > 0)
			difficulty = args[0];

		switch (difficulty.toLowerCase()) {
			case "easy":
			{
				width = 15;
				height = 18;
				speed = 175;
				break;
			}	
			case "medium":
			{
				width = 20;
				height = 24;
				speed = 100;
				break;
			}
			case "hard":
			{
				width = 25;
				height = 30;
				speed = 60;
				break;
			}
			case "why":
			{
				width = 30;
				height = 36;
				speed = 40;
				break;
			}
		}

		JFrame frame = new JFrame("Snake Game");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		Snake snake = new Snake();
		
		frame.getContentPane().add(snake);
			
		frame.pack();
		frame.setVisible(true);
	}
	
	static int width, height;
	static int speed;
	
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
	
	final int NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4;

	int snake_x, snake_y, snake_direction, snake_length, apple_x, apple_y;

	public Snake() {
		// board width and height in cell #
	
		size = new Dimension(2*border + cell*width, 2*border + cell*height);

		board = new int[width][height];

		setPreferredSize(size);
		setVisible(true);

		addKeyListener(new Key());

		setFocusable(true);
		requestFocus();

		
		
		reset();	

		new Timer(speed, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch (snake_direction) {
					case NORTH:
					{
						snake_y -= 1;
						break;
					} 
					case EAST:
					{
						snake_x += 1;
						break;
					} 
					case SOUTH:
					{
						snake_y += 1;
						break;
					} 
					case WEST:
					{
						snake_x -= 1;
						break;
					} 
				}		
				
				if (snake_x == apple_x && snake_y == apple_y) {
					do {
						apple_x = (int)(Math.random()*width);
						apple_y = (int)(Math.random()*height);
					} while (board[apple_x][apple_y] != 0);
					snake_length += 1;
				} else
					for (int i = 0; i < width; i++)
						for (int j = 0; j < height; j++)
							if (board[i][j] > 0)
								board[i][j] -= 1;

				try {
					if (board[snake_x][snake_y] > 0)
						reset();		
					board[snake_x][snake_y] = snake_length;
				} catch (ArrayIndexOutOfBoundsException aioobe) {
					reset();
				}
	
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
				if (board[i][j] > 0)
					page.fillRect(
						border + i*cell,
						border + j*cell,
						cell,
						cell
					);
				else if (apple_x == i && apple_y == j)
					page.fillOval(
						border + i*cell,
						border + j*cell,
						cell,
						cell
					);
			}
	}

	public void reset() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				board[i][j] = 0;

		snake_x = width / 2;
		snake_y = height / 2;
		snake_length = 3;
		
		snake_direction = NORTH;		
		
		board[snake_x][snake_y + 0] = 3;
		board[snake_x][snake_y + 1] = 2;
		board[snake_x][snake_y + 2] = 1;

		do {
			apple_x = (int)(Math.random()*width);
			apple_y = (int)(Math.random()*height);
		} while (board[apple_x][apple_y] != 0);
				
	}

	class Key implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				snake_direction = WEST;
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				snake_direction = EAST;
			} else if (e.getKeyCode() == KeyEvent.VK_UP) {
				snake_direction = NORTH;
			} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				snake_direction = SOUTH;
			}
		}

		public void keyReleased(KeyEvent e) {}

		public void keyTyped(KeyEvent e) {}
	}
}
