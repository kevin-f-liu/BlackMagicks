package Game;

import java.awt.Point;

public class Enemy extends Character {

	private int damage, speed, totalHp;
	private double distx, disty, speedx, speedy, radians, adjustx, adjusty,
			targetx, targety;
	private static final String[] ENEMYURLS = { "/pics/Enemy1.png",
			"/pics/Enemy2.png", "/pics/Enemy3.png", "/pics/Enemy2.png" };

	public Enemy(int dmg, int spawnx, int spawny, double targetx,
			double targety, int size, int speed, int hp) {
		super(spawnx, spawny, size, hp);

		totalHp = hp;
		
		updateTarget(targetx, targety);

		damage = dmg;
		setSpeed(speed);

		hasCollidedx(false);
		hasCollidedy(false);
	}

	// Updates the target (locatinon of the player)
	public void updateTarget(double x, double y) {
		targetx = x;
		targety = y;
		updateVector(x, y);
	}

	// Updates the direction and the individual hori/vert speeds the enemy needs
	// to move by.
	public void updateVector(double tx, double ty) {
		// Adjusts the coordinates of the center of the enemy
		adjustx = getx() - getSize() / 2;
		adjusty = gety() - getSize() / 2;
		// Same code as for projectile
		distx = (tx - 30 - adjustx);
		disty = (ty - 30 - adjusty);
		// Making sure not to divide by zero
		if (distx == 0) {
			distx = 0.000000000001;
		}
		radians = Math.atan(disty / distx);
		speedx = Math.cos(radians);
		speedy = Math.sin(radians);
		// Fixes a glitch where enemies would walk away from the player if the
		// enemy was parallel
		if (distx < 0 && disty == 0) {
			speedx = -1;
		} else if (distx > 0 && disty == 0) {
			speedx = 1;
		}

		if (distx < 0 && disty < 0 || distx < 0 && disty > 0) {
			speedx = -1 * speedx;
			speedy = -1 * speedy;
		}
		setvx(speedx);
		setvy(speedy);
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int dmg) {
		damage = dmg;
	}

	public double getDirection() {
		return radians;
	}

	public double getDistx() {
		return distx;
	}

	public double getDisty() {
		return disty;
	}

	public static String[] getURLS() {
		return ENEMYURLS;
	}
	
	public int getTotalHp() {
		return totalHp;
	}
}
