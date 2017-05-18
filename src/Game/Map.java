package Game;

import java.awt.Rectangle;

public class Map {
	
	private Rectangle[] walls = new Rectangle[4];
	
	// Purely an encapsulation class for all rectangles
	public Map() {
		walls[0] = new Rectangle(347, 192, 50, 50);
		walls[1] = new Rectangle(979, 192, 50, 50);
		walls[2] = new Rectangle(347, 526, 50, 50);
		walls[3] = new Rectangle(979, 526, 50, 50);
	}
	
	public Rectangle[] getRectangles() {
		return walls;
	}
}
