package Game;

import java.awt.Rectangle;

public class Projectile {

	private int speed, damage, type;
	private double x, y, distx, disty, speedx, speedy, radians;
	private Rectangle hitbox;
	private boolean penetrating;

	public Projectile(double x, double y, double destx, double desty,
			int damage, int speed, int type, boolean pen) {
		this.x = x;
		this.y = y;
		setProjectileVector(destx, desty);
		this.damage = damage;
		this.speed = speed;
		this.hitbox = hitbox;
		this.type = type;
		this.penetrating = pen;
	}

	private void setProjectileVector(double destx, double desty) {
		// Stores the distance between the mouse on the shot, relative to the
		// shot
		distx = (destx - x);
		// adjustment to align shot with mouse tip
		disty = (desty - y);
		// Calculates the angle; the returned angle is in the range -pi/2
		// through pi/2
		// If the mouse is to the left of the ball, then it returns the angle of
		// the line that runs through the mouse and the shot
		radians = Math.atan(disty / distx);
		// Changes the angle to usable hori/vert speeds
		speedx = Math.cos(radians);
		speedy = Math.sin(radians);
		// Corrects the final hori/vert speeds based on which quadrant the mouse
		// is in, relative to the player
		if (distx < 0 && disty < 0 || distx < 0 && disty > 0) {
			speedx = -1 * speedx;
			speedy = -1 * speedy;
		}
	}

	// Returns true if the projectile is traveling from left to right, false if
	// it traveling the other way.
	public boolean getRelativeLoc() {
		if (distx < 0) {
			return true;
		} else {
			return false;
		}
	}

	public void move() {
		x += speedx * speed;
		y += speedy * speed;
		updateHitbox();
	}

	private void updateHitbox() {
		hitbox.setLocation(getx(), gety());
	}

	// No need for decimal point precision anymore
	public int getx() {
		return (int) x;
	}

	// No need for decimal point precision anymore
	public int gety() {
		return (int) y;
	}

	public void setDamage(int dmg) {
		damage = dmg;
	}

	public int getDamage() {
		return damage;
	}

	public void setx(int x) {
		this.x = x;
	}

	public void sety(int y) {
		this.y = y;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public void setSpeed(int spd) {
		speed = spd;
	}

	public int getType() {
		return type;
	}

	public double getDirection() {
		return radians;
	}
	
	public boolean isPenetrating() {
		return penetrating;
	}
}
