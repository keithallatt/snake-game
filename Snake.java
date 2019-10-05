import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Snake extends JPanel {
	public static void main(String[]args) {
		String difficulty = "easy";
		if (args.length > 0)
			difficulty = args[0];

		ai = false;
		anti = false;

		switch (difficulty.toLowerCase()) {
			case "easy":
			{
				width = 18;
				height = 15;
				speed = 175;
				break;
			}	
			case "medium":
			{
				width = 24;
				height = 20;
				speed = 100;
				break;
			}
			case "hard":
			{
				width = 30;
				height = 25;
				speed = 60;
				break;
			}
			case "why":
			{
				width = 36;
				height = 30;
				speed = 40;
				break;
			}
			case "ai":
			{
				width = 30;
				height = 25;
				speed = 10;
				ai = true;
				break;
			}
			case "anti":
			{
				width = 30;
				height = 25;
				speed = 60;
				anti = true;
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

	static boolean ai, anti;
	
	static int width, height;
	static int speed;
	
	/*
	 * board[i][j] will have an int 0 < z < width*height. where the head of the snake will set the contents of its location to its length.
	 * for all non-zero cells, each move will decrement the value. The new head postiion will get updated. If the snake head moves to a cell
	 * which is already non zero, then the snake collided with itself. therefore the board, the snake head and next apple are the only info
	 * needed. :)
	 */
	static int[][] board;
	
	static final int border = 5;
	static final int cell = 33;
	
	final Dimension size;
	
	final static int NORTH = 1, EAST = 2, SOUTH = 3, WEST = 4;

	static int snake_x, snake_y, snake_direction, snake_length, apple_x, apple_y;

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
				if (ai)
					snake_direction = SnakeAI.direction(width, height, snake_x, snake_y);

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
					boolean place = false;
					for (int i = 0; i < width; i++)
						for (int j = 0; j < height; j++) {
							if (board[i][j] == 0) {
								place = true;
								break;
							}
						if (place) break;
					}
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

		// temporary
		snake_length = 30;
		
		snake_direction = NORTH;		
		
		board[snake_x][snake_y + 0] = snake_length;
		//board[snake_x][snake_y + 1] = 2;
		//board[snake_x][snake_y + 2] = 1;

		do {
			apple_x = (int)(Math.random()*width);
			apple_y = (int)(Math.random()*height);
		} while (board[apple_x][apple_y] != 0);
				
	}

	static class SnakeAI {
		static int direction(int width, int height, int xpos, int ypos) {
			switch ((width % 2) * (1 + (height % 2))) {
				case 0: {
					// width even
					if (ypos == 0) {
						// top row, alternate EAST, SOUTH
						// make dips once length hits a threshold
						int n = (int)(Math.ceil((float)(snake_length - 2*width - 2*height) / (float)(2 * height - 2)));
						if (xpos >= width - 2*n - 2 || xpos <= 2*n + 2)
							 return new int[] {EAST, SOUTH}[xpos % 2];
												
						if (xpos % 2 == 0) return EAST;
						if (xpos == width - 1) return SOUTH;
						
						if (apple_x == xpos || apple_x == xpos+1)
							if (1 <= apple_y && apple_y <= height - 2)
								return SOUTH;
						return EAST;
						
					} else if (ypos == height - 2) {
						// second last row
						if (xpos == 0) return NORTH;
						if (xpos == width - 1) return SOUTH;
						return new int[] {NORTH, EAST}[xpos % 2];
					} else if (ypos == height - 1) {
						// bottom row
						if (xpos == 0) return NORTH;
						return WEST;
					} else {
						if (snake_direction == new int[]{SOUTH, NORTH}[xpos % 2]) return EAST;

						return new int[] {NORTH, SOUTH}[xpos % 2];
					}
				}
				case 1: {}
				case 2: {}
				default: {
					return NORTH;
				}
			}
		}
	}

	class Key implements KeyListener {
		public void keyPressed(KeyEvent e) {
			if (anti) // make someone upset
				if (Math.random() < 0.1)
					return;
			if (ai)  // no need for key commands
				return;

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
