package Game;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Animation {

	private String[] URLS;
	private ArrayList<BufferedImage> animation;
	int drawIndex, clock;
	double lastDirection = 0;

	// Constructor simply fills the arraylist for the animations with the given
	// URL array
	public Animation(String[] urls) {
		URLS = urls;
		animation = new ArrayList<BufferedImage>();
		try {
			for (int i = 0; i < URLS.length; i++) {
				animation
						.add(ImageIO.read(Animation.class.getResource(URLS[i])));
			}
		} catch (IOException e) {
			System.out.println("Unable to load images");
		}
	}

	public void draw(Graphics2D g2d, double deg, int facing, int x, int y,
			ImageObserver i, boolean moving, int speed) {
		Graphics2D g = g2d;
		AffineTransform at = new AffineTransform();
		at.translate(x + 15, y);
		
		// So that the player does not always face north when not moving
		if (!moving) {
			deg = lastDirection;
		}
		
		// This corrects the direction (specifically for the enemy and
		// projectile classes)
		// For the player, facing is defaulted to 0
		// Without this, the enemies would be pointing the wrong way when on the
		// right side of the player
		if (facing == 3) {
			at.rotate(deg - Math.PI / 2);
		} else if (facing == 2) {
			at.rotate(deg + Math.PI / 2);
		} else if (facing == 1) {
			at.rotate(deg + Math.PI / 2);
		} else {
			// Player class will pass facing as 0 because it does not need
			// adjustments
			at.rotate(deg);
		}

		// Gets draw index based on the clock
		drawIndex = clock / speed;

		// Makes sure that the index does not go out of bounds
		if (drawIndex < animation.size()) {
			at.translate(-animation.get(drawIndex).getWidth() / 2,
				-animation.get(drawIndex).getHeight() / 2);
		}
		
		if (!moving) {
			// Draws the neutral stance
			g.drawImage(animation.get(1), at, i);
		} else {
			// Draws which ever stance should be drawn when walking
			g.drawImage(animation.get(drawIndex), at, i);
		}
		// Resents the clock and the drawIndex when all the bufferedImages have
		// been played through
		if (moving) {
			clock++;
			// for the value speed, lower actually means a faster animation
			if (clock == animation.size() * speed) {
				clock = 0;
			}
		}
		
		// Set last direction
		lastDirection = deg;
	}
}
