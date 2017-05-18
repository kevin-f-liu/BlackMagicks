package Game;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sounds {
	// All sounds
	private AudioClip openDialog1 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/pageflip1.wav"));
	private AudioClip openDialog2 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/pageflip2.wav"));
	private AudioClip startGame = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/gamestart.wav"));
	private AudioClip playerHit1 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/playerhit1.wav"));
	private AudioClip playerHit2 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/playerhit2.wav"));
	private AudioClip enemyDeath1 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/enemydeath1.wav"));
	private AudioClip enemyDeath2 = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/enemydeath2.wav"));
	private AudioClip enemyHit = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/enemyhit.wav"));
	private AudioClip playerDeath = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/playerdeath.wav"));
	private AudioClip playerLow = Applet.newAudioClip(Sounds.class
			.getResource("/sounds/lowhealth.wav"));
	
	public Sounds() {
		// nothing needed here
	}
	
	public void playDialog() {
		// Picks between the two sounds at random.
		// Gives better effect
		if (Math.random() > 0.5) {
			openDialog1.play();
		} else {
			openDialog2.play();
		}
	}
	
	public void playGameStart() {
		startGame.play();
	}
	
	public void playPlayerhit() {
		// Picks between the two sounds at random.
		// Gives better effect
		if (Math.random() > 0.5) {
			playerHit1.play();
		} else {
			playerHit2.play();
		}
	}
	
	public void playEnemyDeath() {
		// Picks between the two sounds at random.
		// Gives better effect
		if (Math.random() > 0.5) {
			enemyDeath1.play();
		} else {
			enemyDeath2.play();
		}
	}
	
	public void playEnemyHit() {
		enemyHit.play();
	}
	
	public void playPlayerDeath() {
		playerDeath.play();
	}
	
	public void playLowHP() {
		playerLow.play();
	}
}
