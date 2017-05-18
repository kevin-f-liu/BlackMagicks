package Game;

import java.awt.Rectangle;

public class Character {

	private int x, y, speed, size;
	private double vx, vy;
	// x and y collisions (horizontal and vertical) are handled separately to
	// allow for more fluid movement
	private boolean collidedx, collidedy;
	private static int numCharacters;
	private int health;
	private Rectangle hitbox;

	public Character(int x, int y, int size, int hp) {
		this.x = x;
		this.y = y;
		this.size = size;
		health = hp;
		collidedx = false;
		collidedy = false;
		numCharacters++;
		hitbox = new Rectangle(x, y, size, size);
	}

	public void move() {
		if (!collidedx) {
			x += vx * speed;
			updateHitbox();
		}
		if (!collidedy) {
			y += vy * speed;
			updateHitbox();
		}
	}

	private void updateHitbox() {
		hitbox.setLocation(x, y);
	}

	public void hasCollidedx(boolean b) {
		collidedx = b;
	}

	public void hasCollidedy(boolean b) {
		collidedy = b;
	}

	public int getx() {
		return x;
	}

	public int gety() {
		return y;
	}

	public double getvx() {
		return vx;
	}

	public double getvy() {
		return vy;
	}

	public int getSize() {
		return size;
	}

	public int getNumChar() {
		return numCharacters;
	}

	public int getHealth() {
		return health;
	}

	public int getSpeed() {
		return speed;
	}

	public void setHealth(int hp) {
		health = hp;
	}

	public void takeHit(int dmg) {
		health -= dmg;
	}

	public void setx(int x) {
		this.x = x;
	}

	public void sety(int y) {
		this.y = y;
	}

	public void setvx(double vx) {
		this.vx = vx;
	}

	public void setvy(double vy) {
		this.vy = vy;
	}

	public void setSpeed(int spd) {
		speed = spd;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public boolean isCollidedx() {
		return collidedx;
	}
	
	public boolean isCollidedy() {
		return collidedy;
	}
	
	// Useful player buffs and enemy debuffs
	public void modSpeed(int factor) {
		speed += factor;
	}
}
