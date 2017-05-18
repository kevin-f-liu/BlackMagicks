package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameBoard extends JPanel implements ActionListener {

	// Game object to allow for the control of the game in other classes
	private Game game;

	// Attributes for entities
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	// ArrayList of animations to give each enemy their own animation timing
	private ArrayList<Animation> enemyAnimations = new ArrayList<Animation>();
	private ArrayList<Projectile> projectiles;
	private Animation fireballAnimation, icelanceAnimation,
			moltenspearAnimation;
	private Player player;
	private Animation playerAnimation;

	// Attributes for game setup
	private Sounds sounds;
	private BufferedImage floor;
	private int frameHeight, frameWidth;
	private int score;
	// This dictates the numbers of enemies to spawn each wave
	private int numEnemies;
	private static final Point[] SPAWNPOINTS = { new Point(0, 0),
			new Point(0, 750), new Point(1300, 0), new Point(1300, 750) };
	// These values are not hard-coded to allow for variable difficulty
	private int enemyDamage, enemySpeed, enemyHp, playerInvincibilityTimer,
			wave;
	private Timer timer;
	private boolean running;
	// So obtain seconds, divide this number by 50
	// This number is the time before the next wave of monsters spawn
	private int waveTimer;
	private static final int WAVELENGTH = 250;

	// Attributes for map
	private Map map;
	private Rectangle[] walls;

	// All element images
	private BufferedImage fireIcon, waterIcon, earthIcon;

	// Actions for the key bindings action map
	public class MoveAction extends AbstractAction {

		// Constants for directional movements; created for convenience
		static final int MOVE_UP = 1;
		static final int MOVE_DOWN = 2;
		static final int MOVE_LEFT = 3;
		static final int MOVE_RIGHT = 4;
		static final int RELEASE_UP = 10;
		static final int RELEASE_DOWN = 20;
		static final int RELEASE_LEFT = 30;
		static final int RELEASE_RIGHT = 40;
		private int direction;

		public MoveAction(int d) {
			direction = d;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// Checks as to whether the key was released or pressed.
			HashSet<Integer> press = new HashSet<Integer>(Arrays.asList(1, 2,
					3, 4));
			if (press.contains(direction)) {
				player.keyPressed(direction);
			} else {
				player.keyReleased(direction);
			}
		}
	}

	public class CombineAction extends AbstractAction {

		private int element;
		private boolean clear = false;

		// If this constructor is called, then that ActionMap element will deal
		// with adding elements to the player's bar
		public CombineAction(int a) {
			element = a;
		}

		// If this constructor is called, then that ActionMap element will deal
		// with clearing all elements in the player's bar
		public CombineAction() {
			clear = true;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (clear == false) {
				player.addElement(element);
			} else {
				player.clearElements();
			}
		}
	}

	// GameBoard Constructor
	public GameBoard(JFrame frame, Game game, Sounds s) {
		this.game = game;

		// Set up JPanel
		setDoubleBuffered(true);

		// Set up mouse bindings
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				player.fire();
			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}
		});

		// Set up key bindings
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'),
				"moveUp");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('s'),
				"moveDown");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'),
				"moveLeft");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('d'),
				"moveRight");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "rUp");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "rDown");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "rLeft");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "rRight");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, true), "element1");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_2, 0, true), "element2");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_3, 0, true), "element3");
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0, true), "clear");

		getActionMap().put("moveUp", new MoveAction(MoveAction.MOVE_UP));
		getActionMap().put("moveDown", new MoveAction(MoveAction.MOVE_DOWN));
		getActionMap().put("moveLeft", new MoveAction(MoveAction.MOVE_LEFT));
		getActionMap().put("moveRight", new MoveAction(MoveAction.MOVE_RIGHT));
		getActionMap().put("rUp", new MoveAction(MoveAction.RELEASE_UP));
		getActionMap().put("rDown", new MoveAction(MoveAction.RELEASE_DOWN));
		getActionMap().put("rLeft", new MoveAction(MoveAction.RELEASE_LEFT));
		getActionMap().put("rRight", new MoveAction(MoveAction.RELEASE_RIGHT));
		// Referring to the element number in the arguments of CombineAction
		getActionMap().put("element1", new CombineAction(0));
		getActionMap().put("element2", new CombineAction(1));
		getActionMap().put("element3", new CombineAction(2));
		getActionMap().put("clear", new CombineAction());

		// Set-up bounds to be used for collision detection
		frameHeight = frame.getHeight();
		frameWidth = frame.getWidth();

		// Initialize the timer but does not start it
		// Each game tick is 20 ms
		// This mean the game runs at 50 fps and 50 updates per second
		timer = new Timer(20, this);
		running = false;

		// Set up score
		score = 0;

		// Set up map
		map = new Map();
		walls = map.getRectangles();
		
		// Set up sounds
		sounds = s;

		// Set up element icons. Initialize images.
		try {
			fireIcon = ImageIO.read(GameBoard.class
					.getResource("/pics/FireIcon.png"));
			waterIcon = ImageIO.read(GameBoard.class
					.getResource("/pics/WaterIcon.png"));
			earthIcon = ImageIO.read(GameBoard.class
					.getResource("/pics/EarthIcon.png"));
			floor = ImageIO
					.read(GameBoard.class.getResource("/pics/Floor.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Set up player
		player = new Player(frameWidth / 2, frameHeight / 2, walls);
		playerInvincibilityTimer = 0;
		playerAnimation = new Animation(Player.getURLS());

		// Set up enemies/difficulty
		// This used so that it is possible to implement a difficulty setting
		wave = 1;
		waveTimer = 0;
		enemySpeed = 5;
		enemyDamage = 10;
		enemyHp = 50;
		numEnemies = 1;

		// Set up projectile ArrayList
		projectiles = new ArrayList<Projectile>();
		fireballAnimation = new Animation(Fireball.getURLS());
		icelanceAnimation = new Animation(Icelance.getURLS());
		moltenspearAnimation = new Animation(Moltenspear.getURLS());
	}

	// When called, starts the game
	public void start() {
		running = true;
		timer.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Movement updates
		updatePlayer();
		updateProjectiles();
		updateEnemies();

		// Increases all cooldowns; 3 is the number of spells
		for (int i = 1; i <= player.getNumSpells() - 1; i++) {
			player.cdi(i);
		}

		// Check if game over
		if (!running) {
			timer.stop();
			System.out.println("Player has died");
			// Ends the game by changing the panel
			game.endVisible(score);
		}

		// Render update
		repaint();

		// Detects whether the player is still alive or not. Game ends when
		// player HP <= 0.
		// If game ends, then running is false and ending the game logic is
		// handed off to the main actionPerformed method.
		if (player.getHealth() <= 0) {
			// Play death sounds
			sounds.playPlayerDeath();
			running = false;
		}

		// Spawns all enemies. Runs the code to determine enemy spawning
		spawnEnemies();
	}

	/*
	 * The next six methods handle the bulk of the game logic; this means that
	 * all collisions between objects are handled here First, every "update"
	 * method will call the respective "detect collision" methods, then, if it
	 * can, it will move the respective element. i.e. Player, Enemy, or
	 * Projectile
	 */
	// detectProjectileCollision
	private void detectProjectileCollision(int i) {
		// Collision with outer edge
		// Does not run unnecessary code if there are no projectiles anyways
		if (projectiles.size() > 0) {
			if (projectiles.get(i).getx() > frameWidth
					|| projectiles.get(i).getx() < 0
					|| projectiles.get(i).gety() > frameHeight
					|| projectiles.get(i).gety() < 0) {
				projectiles.remove(i);
				System.out.println("Hit boudaries");
			}
		}

		// Collision with obstacles
		// Does not run unnecessary code if there are no projectiles anyways
		if (projectiles.size() > 0) {
			for (int j = 0; j < walls.length; j++) {
				// Prevent index out of bounds error by making sure that
				// projectiles has
				// at least 1 more element than the index number.
				if (projectiles.size() > i && projectiles.size() != i
						&& walls[j].intersects(projectiles.get(i).getHitbox())) {
					projectiles.remove(i);
					System.out.println("Hit a wall");
				}
			}
		}

		// Collision with enemies
		// Does not run unnecessary code if there are no projectiles anyways
		if (projectiles.size() > 0) {
			for (int j = 0; j < enemies.size(); j++) {
				// Prevent index out of bounds error by making sure that
				// projectiles has
				// at least 1 more element than the index number.
				if (projectiles.size() > i
						&& enemies.get(j).getHitbox()
								.intersects(projectiles.get(i).getHitbox())) {
					// Play sounds
					sounds.playEnemyHit();
					enemies.get(j).takeHit(projectiles.get(i).getDamage());
					System.out.println("Hit an enemy; did "
							+ projectiles.get(i).getDamage() + " damage");
					// If the projectile had any special effects
					if (projectiles.get(i).getType() == Player.ICELANCE) {
						// Slows enemies, but does not allow for the speed to
						// drop below 2
						if (enemies.get(j).getSpeed()
								+ Icelance.getSpeedFactor() >= 2) {
							enemies.get(j).modSpeed(Icelance.getSpeedFactor());
						}
					}

					// Currently, these projectiles will proc their attack
					// multiple times, depending on their speed and the angle
					// that they hit the hitbox of the enemies.
					// Removes the projectile if the projectile is not supposed
					// to penetrate enemies
					if (!projectiles.get(i).isPenetrating()) {
						projectiles.remove(i);
					}
				}
			}
		}
	}

	private void detectPlayerCollision() {
		// Collisions with outer edge.
		// Adjust the values subtracted for different sprite sizes
		if (player.getx() + player.getvx() * player.getSpeed() < 0
				|| player.getx() + player.getvx() * player.getSpeed() > frameWidth - 18) {
			player.hasCollidedx(true);
		} else {
			player.hasCollidedx(false);
		}
		if (player.gety() + player.getvy() * player.getSpeed() < 0
				|| player.gety() + player.getvy() * player.getSpeed() > frameHeight - 40) {
			player.hasCollidedy(true);
		} else {
			player.hasCollidedy(false);
		}

		// Collisions with obstacles only runs if the player has NOT collided
		// yet
		if (!player.isCollidedx() && !player.isCollidedy()) {
			// Two separate hitboxes for vertical and horizontal movement
			// separately. This allows for the player to move in the other three
			// non-obstructed directions
			Rectangle playerFuturBoxX = new Rectangle(
					(int) (player.getx() + player.getvx() * player.getSpeed()),
					(int) (player.gety()), player.getSize(), player.getSize());
			Rectangle playerFuturBoxY = new Rectangle((int) (player.getx()),
					(int) (player.gety() + player.getvy() * player.getSpeed()),
					player.getSize(), player.getSize());
			for (int i = 0; i < walls.length; i++) {
				if (walls[i].intersects(playerFuturBoxX)) {
					player.hasCollidedx(true);
					break;
				} else {
					player.hasCollidedx(false);
				}
				if (walls[i].intersects(playerFuturBoxY)) {
					player.hasCollidedy(true);
					break;
				} else {
					player.hasCollidedy(false);
				}
			}
		}

		// Collisions with enemies
		// 20 ticks is 400ms of invincibility
		if (playerInvincibilityTimer >= 20) {
			player.invincibility(false);
			playerInvincibilityTimer = 0;
		}
		// Does the actual check for contact with enemies and does damage
		// accordingly
		for (int i = 0; i < enemies.size(); i++) {
			// Does not take damage if the player is still temporarily
			// invincible
			// Player becomes invincible after taking damage as to not get
			// insta-killed
			if (enemies.get(i).getHitbox().intersects(player.getHitbox())
					&& !player.isInvincible()) {
				// Play hit sound
				sounds.playPlayerhit();
				player.takeHit(enemies.get(i).getDamage());
				player.invincibility(true);
				// If the player now has low hp, play lowhp warning
				if (player.getHealth() <= 20) {
					sounds.playLowHP();
				}
			}
		}
		// Increases the player invincibility timer
		if (player.isInvincible()) {
			playerInvincibilityTimer++;
		}
	}

	private void detectEnemyCollision(int i) {
		// Collisions with obstacles as collision with projectiles and players
		// have already been handled
		Rectangle enemyFuturBoxX = new Rectangle(
				(int) (enemies.get(i).getx() + enemies.get(i).getvx()
						* enemies.get(i).getSpeed()),
				(int) (enemies.get(i).gety()), enemies.get(i).getSize(),
				enemies.get(i).getSize());
		Rectangle enemyFuturBoxY = new Rectangle((int) (enemies.get(i).getx()),
				(int) (enemies.get(i).gety() + enemies.get(i).getvy()
						* enemies.get(i).getSpeed()), enemies.get(i).getSize(),
				enemies.get(i).getSize());
		for (int j = 0; j < walls.length; j++) {
			if (walls[j].intersects(enemyFuturBoxX)) {
				enemies.get(i).hasCollidedx(true);
				break;
			} else {
				enemies.get(i).hasCollidedx(false);
			}
			if (walls[j].intersects(enemyFuturBoxY)) {
				enemies.get(i).hasCollidedy(true);
				break;
			} else {
				enemies.get(i).hasCollidedy(false);
			}
		}
	}

	private void updatePlayer() {
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, this);
		// Mouse update
		player.updateMouse(p);
		// Monster hit detection inside detectPlayerCollision
		detectPlayerCollision();
		player.move();
	}

	private void updateProjectiles() {
		projectiles = player.getProjectiles();

		// Move projectiles and remove them based on collisions
		for (int i = 0; i < projectiles.size(); i++) {
			projectiles.get(i).move();
			detectProjectileCollision(i);
		}
	}

	private void updateEnemies() {
		// Moves enemies and updates their target and detect collisions
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).updateTarget(player.getx(), player.gety());
			enemies.get(i).move();
			detectEnemyCollision(i);
			// Detected whether an enemy is dead. Increases score if an enemy is
			// killed based on the health of the enemy plus it's damage
			if (enemies.get(i).getHealth() <= 0) {
				// Play sounds
				sounds.playEnemyDeath();
				increaseScore(enemies.get(i).getDamage()
						+ enemies.get(i).getTotalHp());
				enemies.remove(i);
				enemyAnimations.remove(i);
			}
		}
	}

	// Does code to check if enemies should be spawned in
	// Handles all wave logic essentially. This means increased difficulty, spawn rates, ect.
	public void spawnEnemies() {
		// Spawns when the wave timer is at the correct time
		if (waveTimer >= WAVELENGTH) {
			// Heals player for 10 hp
			if (player.getHealth() <= 90) {
				player.setHealth(player.getHealth() + 10);
			}
			// Calculates difficulty based on the number of waves passed and
			// special waves.
			// Only runs if a wave can spawn
			if (wave != 0 && wave % 5 == 0) {
				// Every 5 waves, that wave's enemies HP is doubled Health is
				// doubled
				enemyHp = 100;
			}
			if (wave != 0 && wave % 10 == 0) {
				// Every 10 waves, that wave's enemies are faster and do more
				// damage, but have less health.
				enemyHp = 30;
				enemyDamage = 20;
				// For reference, player speed is 6
				enemySpeed = 8;
				// One more enemy will spawn every 10 waves
				numEnemies++;
			}

			System.out.print("Wave: " + wave);
			System.out
					.println(" :: " + numEnemies + " Enemies, " + "Enemy HP: "
							+ enemyHp + ", Enemy Damage: " + enemyDamage);
			for (int i = 0; i < numEnemies; i++) {
				int spawn = (int) (Math.random() * 4);
				enemies.add(new Enemy(enemyDamage, (int) SPAWNPOINTS[spawn]
						.getX(), (int) SPAWNPOINTS[spawn].getY(),
						player.getx(), player.gety(), 40, enemySpeed, enemyHp));
				enemyAnimations.add(new Animation(Enemy.getURLS()));
			}

			// Undo all temporary buffs to enemies
			if (wave != 0 && (wave) % 5 == 0) {
				enemyHp = 50;
			}
			if (wave != 0 && (wave) % 10 == 0) {
				// Every 10 waves, that wave's enemies are faster and do more
				// damage, but have less health.
				enemyHp = 50;
				enemyDamage = 10;
				// For reference, player speed is 6
				enemySpeed = 4;
			}

			// Reset the wave timer
			waveTimer = 0;
			// One wave has spawned
			wave++;
		} else {
			waveTimer++;
		}
	}

	// Rendering component
	@Override
	public void paintComponent(Graphics g) {
		// Clear panel
		super.paintComponent(g);
		// Set up the graphics
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		// All rendering
		g2d.drawImage(floor, 0, 0, this);
		renderObstacles(g2d);
		renderPlayer(g2d);
		renderEnemies(g2d);
		renderProjectiles(g2d);
		renderHUD(g2d);

		Toolkit.getDefaultToolkit().sync();
		g.dispose();
	}

	private void renderObstacles(Graphics2D g2d) {
		g2d.setColor(new Color(133, 75, 0));
		for (int i = 0; i < walls.length; i++) {
			g2d.fill(walls[i]);
		}
	}

	// To render the HUD, first all the major components are drawn.
	// Then the HUD elements and cooldown for the spell selected is rendered
	// on top of all the existing renderings
	private void renderHUD(Graphics2D g2d) {
		// Draw HealthBar
		// HP background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(0, 0, 100, 50, true);
		// HP text
		g2d.setFont(new Font("Arial", Font.BOLD, 20));
		g2d.setColor(Color.BLACK);
		g2d.drawString("HP: " + player.getHealth(), 5, 30);
		// HP bar background
		g2d.setColor(new Color(110, 110, 110));
		g2d.fill3DRect(100, 0, 500, 50, false);
		// HP bar
		g2d.setColor(new Color(209, 0, 0));
		g2d.fill3DRect(101, 5, (int) (player.getHealth() * 4.9), 40, true);

		// Score panel
		// Score background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(0, 50, 150, 30, true);
		// Score
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		g2d.drawString("SCORE: " + score, 5, 70);

		// Round timer
		// Timer background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(150, 50, 150, 30, true);
		// Time
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		g2d.drawString("NEXT WAVE IN: " + (WAVELENGTH / 50 - waveTimer / 50)
				+ "s", 155, 70);

		// Wave number
		// Wave number background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(300, 50, 150, 30, true);
		// Time
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		if (wave - 1 != 0) {
			g2d.drawString("WAVE: " + (wave - 1), 305, 70);
		} else {
			g2d.drawString("Prepare Yourself", 305, 70);
		}

		// Draw Elements
		// Elements background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(frameWidth / 2 - 100, frameHeight - 130, 200, 70, true);
		// Elements Circles
		g2d.setColor(new Color(110, 110, 110));
		g2d.fillOval(frameWidth / 2 - 95, frameHeight - 125, 60, 60);
		g2d.fillOval(frameWidth / 2 - 30, frameHeight - 125, 60, 60);
		g2d.fillOval(frameWidth / 2 + 35, frameHeight - 125, 60, 60);
		// Elements cooldown background
		g2d.setColor(new Color(150, 150, 150));
		g2d.fill3DRect(frameWidth / 2 - 100, frameHeight - 60, 200, 70, true);

		renderHudElements(g2d);
	}

	public void renderHudElements(Graphics2D g2d) {
		// first element coords: frameWidth / 2 - 95, frameHeight - 125
		// second element coords: frameWidth / 2 - 30, "
		// third element coors: frameWidth / 2 + 35, "
		int left = frameWidth / 2 - 95;
		int mid = frameWidth / 2 - 30;
		int right = frameWidth / 2 + 35;
		int yCoord = frameHeight - 125;
		int drawElement = -1;

		// Draws the icons based on the contents of the elements arraylist
		for (int i = 0; i < player.getElements().size(); i++) {
			if (player.getElements().get(i) == 0) {
				if (i == 0) {
					g2d.drawImage(fireIcon, left, yCoord, this);
				} else if (i == 1) {
					g2d.drawImage(fireIcon, mid, yCoord, this);
				} else {
					g2d.drawImage(fireIcon, right, yCoord, this);
				}
			} else if (player.getElements().get(i) == 1) {
				if (i == 0) {
					g2d.drawImage(waterIcon, left, yCoord, this);
				} else if (i == 1) {
					g2d.drawImage(waterIcon, mid, yCoord, this);
				} else {
					g2d.drawImage(waterIcon, right, yCoord, this);
				}
			} else if (player.getElements().get(i) == 2) {
				if (i == 0) {
					g2d.drawImage(earthIcon, left, yCoord, this);
				} else if (i == 1) {
					g2d.drawImage(earthIcon, mid, yCoord, this);
				} else {
					g2d.drawImage(earthIcon, right, yCoord, this);
				}
			}
		}

		// Cooldowns
		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", Font.BOLD, 15));
		// only draws the cooldown if there is a spell selected
		if (player.getCooldownOfSelected() != -1) {
			if (player.getCooldownOfSelected() == 0) {
				g2d.drawString("COOLDOWN: READY", frameWidth / 2 - 95,
						frameHeight - 42);
			} else {
				g2d.drawString("COOLDOWN: " + player.getCooldownOfSelected(),
						frameWidth / 2 - 95, frameHeight - 42);
			}
		}
	}

	private void renderPlayer(Graphics2D g2d) {
		double deg;
		if (player.getvx() < 0 && player.getvy() == 0) {
			// left
			deg = Math.toRadians(-90);
		} else if (player.getvx() > 0 && player.getvy() == 0) {
			// right
			deg = Math.toRadians(90);
		} else if (player.getvx() == 0 && player.getvy() < 0) {
			// up
			deg = Math.toRadians(0);
		} else if (player.getvx() == 0 && player.getvy() > 0) {
			// down
			deg = Math.toRadians(180);
		} else if (player.getvx() > 0 && player.getvy() < 0) {
			// upright
			deg = Math.toRadians(45);
		} else if (player.getvx() < 0 && player.getvy() < 0) {
			// upleft
			deg = Math.toRadians(-45);
		} else if (player.getvx() > 0 && player.getvy() > 0) {
			// downright
			deg = Math.toRadians(135);
		} else if (player.getvx() < 0 && player.getvy() > 0) {
			// downleft
			deg = Math.toRadians(-135);
		} else {
			deg = 0;
		}
		if (player.getvx() == 0 && player.getvy() == 0) {
			// Player not moving{
			playerAnimation.draw(g2d, deg, 0, player.getx(),
					player.gety() + 15, this, false, 5);
		} else {
			playerAnimation.draw(g2d, deg, 0, player.getx(),
					player.gety() + 15, this, true, 5);
		}
	}

	private void renderProjectiles(Graphics2D g2d) {
		double direction;
		// 0 = up, 1 = down, 2 = left, 3 = right
		int facing;

		// Runs through all the projectiles in the projectiles corrently on the
		// map
		for (int i = 0; i < projectiles.size(); i++) {
			// Actual animation
			direction = projectiles.get(i).getDirection();
			if (projectiles.get(i).getRelativeLoc() == true) {
				facing = 3;
			} else {
				facing = 2;
			}
			// The integer facing is used to correct any errors in the
			// projectile
			// drawing.
			// The added values to the x and the y of the projectile are to
			// correct
			// the locations of the hitboxes. The location is not correct
			// as to rotate the image, the image is centered at the coordinates
			// rather than the point in the upper left corner.
			if (projectiles.get(i).getType() == Player.FIREBALL) {
				// If the projectile is a fireball
				fireballAnimation.draw(g2d, direction, facing,
						projectiles.get(i).getx() - 10, projectiles.get(i)
								.gety() + 10, this, true, 5);
			} else if (projectiles.get(i).getType() == Player.ICELANCE) {
				// If the projectile is an icelance
				icelanceAnimation.draw(g2d, direction, facing,
						projectiles.get(i).getx() - 10, projectiles.get(i)
								.gety() + 10, this, true, 5);
			} else if (projectiles.get(i).getType() == Player.MOLTENSPEAR) {
				// If the projectile is an icelance
				moltenspearAnimation.draw(g2d, direction, facing, projectiles
						.get(i).getx() - 10, projectiles.get(i).gety() + 3,
						this, true, 5);
			}
		}
	}

	private void renderEnemies(Graphics2D g2d) {
		double direction;
		// 0 = up, 1 = down, 2 = left, 3 = right
		int facing;
		for (int i = 0; i < enemies.size(); i++) {
			direction = enemies.get(i).getDirection();
			if (enemies.get(i).getDistx() < 0) {
				// If the enemy is to the left of the player, it should face
				// right
				facing = 3;
			} else if (enemies.get(i).getDistx() > 0) {
				// If the enemy is to the right of the player, it should face
				// left
				facing = 2;
			} else {
				facing = 1;
			}
			// The integer facing is used to correct any errors in the enemy
			// drawing.
			// The added values to the x and the y of the enemy are to correct
			// the locations of the hitboxes. The location is not correct
			// as to rotate the image, the image is centered at the coordinates
			// rather than the point in the upper left corner.
			enemyAnimations.get(i).draw(g2d, direction, facing,
					enemies.get(i).getx() - enemies.get(i).getSize() / 2 + 25,
					enemies.get(i).gety() + enemies.get(i).getSize() / 2, this,
					true, 10);
			// Draws health bars on top of each enemy
			// First draws the background to show total health
			g2d.setColor(Color.BLACK);
			g2d.fillRect(enemies.get(i).getx() - 5, enemies.get(i).gety() - 15,
					50, 5);
			g2d.setColor(Color.RED);
			// Health bar is always 50 in length. Damage done is just a
			// percentage of that 50
			g2d.fillRect(
					enemies.get(i).getx() - 5,
					enemies.get(i).gety() - 15,
					(int) (((float) (enemies.get(i).getHealth()) / enemies.get(
							i).getTotalHp()) * 50), 5);
		}
	}

	public int getScore() {
		return score;
	}

	public void setScore(int nscore) {
		score = nscore;
	}

	public void increaseScore(int plus) {
		score += plus;
	}
}
