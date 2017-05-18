package Game;

import java.awt.Color;

import javax.swing.JFrame;

public class Game {
	
	private GameBoard gameBoard;
	private TitleBoard titleBoard;
	private EndBoard endBoard;
	private JFrame frame;
	private Sounds sounds;

	public Game() {

		// Setup the JFrame
		frame = new JFrame("BlackMagicks");
		frame.setSize(1366, 768);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.BLACK);
		
		// Set up sounds class for all elements of the game. Saves memory.
		sounds = new Sounds();

		// Start with titleBoard
		titleVisible();

		// Render
		frame.setVisible(true);
	}

	public void titleVisible() {
		System.out.println("Creating title pane");
		frame.getContentPane().removeAll();
		frame.repaint();
		titleBoard = new TitleBoard(this, sounds);
		titleBoard.setVisible(true);
		frame.add(titleBoard);
		frame.pack();
		frame.setSize(1366, 768);
	}

	public void gameVisible() {
		System.out.println("Creating game pane");
		frame.getContentPane().removeAll();
		frame.repaint();
		gameBoard = new GameBoard(frame, this, sounds);
		frame.add(gameBoard);
		frame.pack();
		frame.setSize(1366, 768);
		gameBoard.start();
	}

	public void endVisible(int score) {
		System.out.println("Game class: game ended");
		System.out.println("Creating end pane");
		frame.getContentPane().removeAll();
		frame.repaint();
		endBoard = new EndBoard(this, score, sounds);
		frame.add(endBoard);
		frame.pack();
		frame.setSize(1366, 768);
	}
}
