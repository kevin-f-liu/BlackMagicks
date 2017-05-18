package Game;

import java.awt.Rectangle;

public class Moltenspear extends Projectile {
	
	private static final int DAMAGE = 10;
	private static final int SPEED = 15;
	private static final int SIZE = 10;
	// To get seconds, multiply this number by 1/50
	private static final int COOLDOWNMIN = 250;
	private Rectangle hitbox = new Rectangle();
	private static final String[] MOLTENSPEARURLS = { "/pics/Moltenspear1.png",
		"/pics/Moltenspear2.png"};

	public Moltenspear(int x, int y, double destx, double desty) {
		// Fireball is type 1
		super (x, y, destx, desty, DAMAGE, SPEED, 3, true);
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
		return MOLTENSPEARURLS;
	}
}
