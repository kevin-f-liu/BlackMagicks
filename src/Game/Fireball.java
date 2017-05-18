package Game;

import java.awt.Rectangle;

public class Fireball extends Projectile {
	
	private static final int DAMAGE = 25;
	private static final int SPEED = 10;
	private static final int SIZE = 20;
	// To get seconds, multiply this number by 1/50
	private static final int COOLDOWNMIN = 50;
	private Rectangle hitbox = new Rectangle();
	private static final String[] FIREBALLURLS = { "/pics/Fireball1.png",
		"/pics/Fireball2.png", "/pics/Fireball3.png", "/pics/Fireball2.png" };

	public Fireball(int x, int y, double destx, double desty) {
		// Fireball is type 1
		super (x, y, destx, desty, DAMAGE, SPEED, 1, false);
		// Initialize the rectangular hitbox
		hitbox = new Rectangle(x, y, SIZE, SIZE);
		setHitbox(hitbox);
	}

	// Gets the cooldown
	public static int getCooldownMin() {
		return COOLDOWNMIN;
	}
	
	// Allows for animations
	public static String[] getURLS() {
		return FIREBALLURLS;
	}
}
