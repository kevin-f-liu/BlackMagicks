package Game;

import java.awt.Rectangle;

public class Icelance extends Projectile {
	
	private static final int DAMAGE = 10;
	private static final int SPEED = 15;
	private static final int SIZE = 20;
	private static final int SPEEDFACTOR = -2;
	// To get seconds, multiply this number by 1/50
	private static final int COOLDOWNMIN = 100;
	private Rectangle hitbox = new Rectangle();
	private static final String[] ICELANCEURLS = { "/pics/Icelance1.png",
		"/pics/Icelance2.png", "/pics/Icelance3.png", "/pics/Icelance2.png" };

	public Icelance(int x, int y, double destx, double desty) {
		// Icelance is type 2
		super (x, y, destx, desty, DAMAGE, SPEED, 2, false);
		hitbox = new Rectangle(x, y, SIZE, SIZE);
		setHitbox(hitbox);
	}

	public static int getCooldownMin() {
		return COOLDOWNMIN;
	}
	
	public static String[] getURLS() {
		return ICELANCEURLS;
	}
	
	public static int getSpeedFactor() {
		return SPEEDFACTOR;
	}
}
