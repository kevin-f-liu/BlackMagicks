package Game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class Player extends Character {

	// This is the "master copy" of the projectiles arraylist. There is another
	// reference to this object in the gameBoard object. This is the total list
	// of all projectiles; each element in this arraylist is actually a subclass
	// of Projectile.
	private ArrayList<Projectile> projectiles;
	private ArrayList<Integer> elements;
	private int numProjectiles;
	private double mousex, mousey;
	private int[] spellCoolDowns;
	private int spellLocked, numSpells;
	private static final int SPEED = 6;
	private static final int SIZE = 30;
	private static final int HEALTH = 100;
	private boolean invincible;
	private Rectangle[] walls;
	// Animation urls
	private static final String[] PLAYERURLS = { "/pics/Player1.png",
			"/pics/Player2.png", "/pics/Player3.png", "/pics/Player2.png" };

	// Names of spells translated into usable ints; visibility is package
	static final int FIREBALL = 1;
	static final int TELEPORT = 4;
	static final int ICELANCE = 2;
	static final int MOLTENSPEAR = 3;
	// 30 seconds
	private static final int TELEPORT_COOLDOWN = 750;

	// Names of spells translated into arrays of elements. i.e their recipes
	// 0 = fire, 1 = water, 2 = earth
	private static final int[] FIREBALL_ELEMENTS = { 0, 0, 0 };
	private static final int[] TELEPORT_ELEMENTS = { 0, 2, 1 };
	private static final int[] ICELANCE_ELEMENTS = { 1, 1, 2 };
	private static final int[] MOLTENSPEAR_ELEMENTS = { 2, 0, 2 };

	public Player(int x, int y, Rectangle[] walls) {
		super(x, y, SIZE, HEALTH);
		elements = new ArrayList<Integer>();
		spellCoolDowns = new int[5]; // size is determined by the number of
										// spells available + 1 as the first
										// spot is always empty
		// Gives player access to all the spells at the start
		for (int i = 0; i < spellCoolDowns.length; i++) {
			spellCoolDowns[i] = 100000;
		}
		numSpells = spellCoolDowns.length;
		projectiles = new ArrayList<Projectile>();
		spellLocked = 0;
		invincible = false;
		setSpeed(SPEED);

		// Changes the bounds of the walls to be bigger than displayed. This
		// stop the player from teleporting into the wall
		this.walls = new Rectangle[walls.length];
		for (int i = 0; i < walls.length; i++) {
			this.walls[i] = new Rectangle((int) walls[i].getX() - 25,
					(int) walls[i].getY() - 25, 100, 100);
		}
	}

	// Purely dictates movements; k is passed from the action listeners in the
	// gameBoard
	public void keyPressed(int k) {
		// AND operators in the if statements are used to reject conflicting key
		// inputs. i.e, player is moving left and the input for right is given
		if (k == 1 && getvy() != 1) {
			// Direction up
			setvy(-1);
		}
		if (k == 2 && getvy() != -1) {
			// Direction down
			setvy(1);
		}
		if (k == 3 && getvx() != 1) {
			// Direction left
			setvx(-1);
		}
		if (k == 4 && getvx() != -1) {
			// Direction right
			setvx(1);
		}
	}

	// Purely dictates movements; k is passed from the action listeners in the
	// gameBoard
	public void keyReleased(int k) {
		// AND operators in the if statements are used to reject conflicting key
		// inputs. i.e, player is moving left and the input to stop horizontal
		// movement is given
		if (k == 10 && getvy() != 1) {
			// Stop up
			setvy(0);
		}
		if (k == 20 && getvy() != -1) {
			// Stop down
			setvy(0);
		}
		if (k == 30 && getvx() != 1) {
			// Stop left
			setvx(0);
		}
		if (k == 40 && getvx() != -1) {
			// Stop right
			setvx(0);
		}
	}

	public void fire() {
		boolean fired = false;

		// implement cooldowns with many if statements and selects the spell
		if (spellLocked == FIREBALL
				&& spellCoolDowns[FIREBALL] >= Fireball.getCooldownMin()) {
			projectiles.add(new Fireball(getx(), gety(), mousex, mousey));
			spellCoolDowns[FIREBALL] = 0;
			fired = true;
			System.out.println("Fire: FIREBALL");
		} else if (spellLocked == TELEPORT
				&& spellCoolDowns[TELEPORT] >= TELEPORT_COOLDOWN) {
			// Checks to see if the point the player wants to teleport to is
			// inside a wall. The spell will not be cast.
			boolean validLoc = true;
			Point mouse = new Point((int) mousex, (int) mousey);
			for (int i = 0; i < walls.length; i++) {
				if (walls[i].contains(mouse)) {
					validLoc = false;
					break;
				} else {
					validLoc = true;
				}
			}
			// Only teleports if valid
			if (validLoc) {
				setx((int) mousex);
				sety((int) mousey);
				spellCoolDowns[TELEPORT] = 0;
				fired = true;
				System.out.println("Fire: TELEPORT");
			}
		} else if (spellLocked == ICELANCE
				&& spellCoolDowns[ICELANCE] >= Icelance.getCooldownMin()) {
			projectiles.add(new Icelance(getx(), gety(), mousex, mousey));
			spellCoolDowns[ICELANCE] = 0;
			fired = true;
			System.out.println("Fire: ICELANCE");
		} else if (spellLocked == MOLTENSPEAR
				&& spellCoolDowns[MOLTENSPEAR] >= Moltenspear.getCooldownMin()) {
			projectiles.add(new Moltenspear(getx(), gety(), mousex, mousey));
			spellCoolDowns[MOLTENSPEAR] = 0;
			fired = true;
			System.out.println("Fire: MOLTENSPEAR");
		}

		// Resets the locked spell and all elements
		if (elements.size() == 3 && fired) {
			clearElements();
			spellLocked = 0;
			fired = false;
		}
	}

	// Counts up the cooldown of each spell. The game clock will increase all
	// cooldowns equally fast; however, based on usage, they will all be at
	// different values
	public void cdi(int spell) {
		spellCoolDowns[spell]++;
	}

	public int getSpell() {
		return spellLocked;
	}

	// returns the cooldown of the locked spell as a double counting down from
	// the cooldown in seconds of the spell
	public int getCooldownOfSelected() {
		// Must be handled on a case by case basis
		if (spellLocked == FIREBALL) {
			if (spellCoolDowns[spellLocked] < Fireball.getCooldownMin()) {
				return Fireball.getCooldownMin() / 50
						- spellCoolDowns[spellLocked] / 50;
			} else {
				return 0;
			}
		} else if (spellLocked == TELEPORT) {
			// Cooldown of teleport is 30 sec
			if (spellCoolDowns[spellLocked] < TELEPORT_COOLDOWN) {
				return TELEPORT_COOLDOWN / 50 - spellCoolDowns[spellLocked]
						/ 50;
			} else {
				return 0;
			}
		} else if (spellLocked == ICELANCE) {
			// Cooldown of teleport is 30 sec
			if (spellCoolDowns[spellLocked] < Icelance.getCooldownMin()) {
				return Icelance.getCooldownMin() / 50
						- spellCoolDowns[spellLocked] / 50;
			} else {
				return 0;
			}
		} else if (spellLocked == MOLTENSPEAR) {
			// Cooldown of teleport is 30 sec
			if (spellCoolDowns[spellLocked] < Moltenspear.getCooldownMin()) {
				return Moltenspear.getCooldownMin() / 50
						- spellCoolDowns[spellLocked] / 50;
			} else {
				return 0;
			}
		}
		// other spells here
		else {
			// Returning -1 means that no spell is selected.
			return -1;
		}
	}

	public ArrayList<Integer> getElements() {
		return elements;
	}

	public void addElement(int element) {
		// Shifts elements to fill only 3 indices in the ArrayList of elements
		if (elements.size() == 3) {
			elements.set(0, elements.get(1));
			elements.set(1, elements.get(2));
			elements.set(2, element);
			// Only locks a spell if there is a possible spell combo (all have 3
			// elements)
			lockSpell();
		} else {
			elements.add(element);
			if (elements.size() == 3) {
				lockSpell();
			}
		}
	}

	public void lockSpell() {
		int[] temp = new int[3];
		for (int i = 0; i < 3; i++) {
			temp[i] = elements.get(i);
		}

		// If statements to lock a spell based on the contents of the ArrayList
		if (Arrays.equals(temp, FIREBALL_ELEMENTS)) {
			spellLocked = FIREBALL;
		} else if (Arrays.equals(temp, TELEPORT_ELEMENTS)) {
			spellLocked = TELEPORT;
		} else if (Arrays.equals(temp, ICELANCE_ELEMENTS)) {
			spellLocked = ICELANCE;
		} else if (Arrays.equals(temp, MOLTENSPEAR_ELEMENTS)) {
			spellLocked = MOLTENSPEAR;
		} else {
			spellLocked = 0;
		}
	}

	public void clearElements() {
		// Clears all elements in the elements arraylist
		if (elements.size() != 0) {
			elements.clear();
			spellLocked = 0;
		}
	}

	public ArrayList<Projectile> getProjectiles() {
		return projectiles;
	}

	public void updateMouse(Point p) {
		mousex = p.getX();
		mousey = p.getY();
	}

	public void invincibility(boolean b) {
		invincible = b;
	}

	public boolean isInvincible() {
		return invincible;
	}

	public static String[] getURLS() {
		return PLAYERURLS;
	}

	public int getNumSpells() {
		return numSpells;
	}
}
