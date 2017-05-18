package Game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import java.awt.GridBagConstraints;
import java.awt.Font;

import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.Color;
import java.util.ArrayList;

public class EndBoard extends JPanel {
	private JTextField nameField;
	private int score, rank;
	private ArrayList<String> highScores;
	private IO io;
	private Game game;
	// Needs to be global
	private JLabel lblRank;
	private Sounds sounds;

	// Takes the argument int score as this is the only data needed in this
	// class from the gameBoard.
	public EndBoard(Game game, int s, Sounds sd) {
		// Set the score
		this.score = s;
		
		// Set up sounds
		sounds = sd;

		// Sets reference to the game object for game control
		this.game = game;

		// Initialize IO
		io = new IO();

		// Initialize the highScore
		highScores = new ArrayList<String>();
		// Updates the score arrayList from the file "highscores.txt"
		updateScores();

		// JPanel setup
		setBackground(Color.BLACK);
		setLayout(null);

		JPanel scoreBorder = new JPanel();
		scoreBorder.setBackground(Color.GRAY);
		scoreBorder.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(
				178, 34, 34), new Color(0, 0, 0), new Color(178, 34, 34),
				new Color(0, 0, 0)));
		scoreBorder.setBounds(329, 156, 348, 172);
		add(scoreBorder);
		scoreBorder.setLayout(null);

		JLabel lblFinalScore = new JLabel("FINAL SCORE: " + score);
		lblFinalScore.setForeground(new Color(0, 0, 0));
		lblFinalScore.setBounds(12, 0, 324, 103);
		scoreBorder.add(lblFinalScore);
		lblFinalScore.setFont(new Font("Trajan Pro", Font.BOLD, 30));

		lblRank = new JLabel("");
		lblRank.setForeground(Color.DARK_GRAY);
		lblRank.setFont(new Font("Trajan Pro", Font.BOLD, 25));
		lblRank.setBounds(12, 69, 324, 103);
		scoreBorder.add(lblRank);

		JPanel nameBorder = new JPanel();
		nameBorder.setBackground(Color.GRAY);
		nameBorder.setBorder(new BevelBorder(BevelBorder.LOWERED, new Color(
				178, 34, 34), new Color(0, 0, 0), new Color(178, 34, 34),
				new Color(0, 0, 0)));
		nameBorder.setBounds(329, 340, 348, 70);
		add(nameBorder);
		nameBorder.setLayout(null);

		JLabel lblNameLabel = new JLabel("Name:");
		lblNameLabel.setFont(new Font("Trajan Pro", Font.PLAIN, 23));
		lblNameLabel.setBounds(12, 0, 82, 70);
		nameBorder.add(lblNameLabel);

		nameField = new JTextField();
		nameField.setBackground(Color.LIGHT_GRAY);
		nameField.setFont(new Font("Trajan Pro", Font.PLAIN, 23));
		nameField.setBounds(91, 18, 251, 35);
		nameBorder.add(nameField);
		nameField.setColumns(10);

		JPanel highscoreBorder = new JPanel();
		highscoreBorder.setBackground(Color.GRAY);
		highscoreBorder.setLayout(null);
		highscoreBorder.setBorder(new BevelBorder(BevelBorder.LOWERED,
				new Color(178, 34, 34), new Color(0, 0, 0), new Color(178, 34,
						34), new Color(0, 0, 0)));
		highscoreBorder.setBounds(683, 156, 354, 82);
		add(highscoreBorder);

		JLabel lblHighScores = new JLabel("High Scores");
		lblHighScores.setFont(new Font("Trajan Pro", Font.PLAIN, 23));
		lblHighScores.setBounds(97, 12, 163, 70);
		highscoreBorder.add(lblHighScores);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(683, 238, 354, 276);
		add(scrollPane);

		JTextArea highScoresArea = new JTextArea();
		scrollPane.setViewportView(highScoresArea);
		highScoresArea.setBackground(Color.LIGHT_GRAY);
		highScoresArea.setEditable(false);
		highScoresArea.setFont(new Font("Trajan Pro", Font.PLAIN, 20));
		highScoresArea.setLineWrap(true);
		// Prints scores
		printScores(highScoresArea);

		JButton btnSaveScore = new JButton("Save Score");
		btnSaveScore.setForeground(new Color(0, 0, 0));
		btnSaveScore.setBackground(new Color(192, 192, 192));
		btnSaveScore.setFont(new Font("Trajan Pro", Font.PLAIN, 16));
		btnSaveScore.setBounds(329, 422, 348, 40);
		btnSaveScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (validName()) {
					// Adds the score
					addScore(nameField.getText(), score);
					// Reprints all scores
					printScores(highScoresArea);

					// Makes it so that you can't enter the same score many
					// times
					btnSaveScore.setEnabled(false);
					// Clears the text area and disables it
					nameField.setText("");
					nameField.setEnabled(false);
				} else {
					nameField.setText("!Invalid!");
				}
			}
		});
		add(btnSaveScore);

		JButton btnMainMenu = new JButton("Main Menu");
		btnMainMenu.setBackground(new Color(192, 192, 192));
		btnMainMenu.setFont(new Font("Trajan Pro", Font.PLAIN, 16));
		btnMainMenu.setBounds(329, 474, 348, 40);
		btnMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Restarts everything, essentially
				game.titleVisible();
			}
		});
		add(btnMainMenu);
	}

	private void updateScores() {
		// Clears highScores as it is going to be rewritten
		highScores.clear();
		// Opens the file, reads it.
		io.openInputFile("highscores.txt");
		String line = io.readLine();
		while (line != null) {
			highScores.add(line);
			line = io.readLine();
		}

		// Close the file
		io.closeInputFile();
	}

	private void printScoreFile() {
		io.createOutputFile("highscores.txt");
		for (int i = 0; i < highScores.size(); i++) {
			io.println(highScores.get(i));
		}
		io.closeOutputFile();
	}

	// Fills the JTextArea with the highScores
	private void printScores(JTextArea a) {
		a.setText("");
		for (int i = 0; i < highScores.size(); i++) {
			a.append(highScores.get(i) + "\n");
		}
		// Also prints the rank The label only shows up if the player saves their score
		if (rank != 0) {
			lblRank.setVisible(true);
			lblRank.setText("Your rank: " + rank);
		} else {
			lblRank.setVisible(false);
		}

	}

	// Adds the name and score to the correct location in the arrayList
	//
	// I do not need to have a sort that would sort all the highScores as it is
	// assumed that they are always added in the correct order. If the actual
	// text file were to be changed, then errors would arise. However, I am not
	// going to make a fix for that as those files are not supposed to be
	// tampered with.
	//
	private void addScore(String name, int score) {
		// Separates the names and the scores from each other
		// Stores them into separate arrayLists
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Integer> scores = new ArrayList<Integer>();
		for (int i = 0; i < highScores.size(); i++) {
			String tempScore = highScores.get(i);
			names.add(tempScore.substring(0, tempScore.indexOf(":")));
			scores.add(Integer.parseInt(tempScore.substring(
					tempScore.lastIndexOf(" ") + 1, tempScore.length())));
		}
		// Inserts the score and name into their correct indices for their
		// corresponding arrayLists.
		for (int i = 0; i < scores.size(); i++) {
			if (score > scores.get(0)) {
				// If the score is the best one yet
				scores.add(0, score);
				names.add(0, name);
				rank = 1;
				break;
			} else if (i != scores.size() - 1
					&& (score <= scores.get(i) && score > scores.get(i + 1))) {
				// Fits the score after identical scores and before smaller
				// scores
				scores.add(i + 1, score);
				names.add(i + 1, name);
				rank = i + 2;
				break;
			} else if (i == scores.size() - 1) {
				// If the score is the lowest
				scores.add(score);
				names.add(name);
				rank = scores.size();
				break;
			}
		}
		if (scores.size() == 0) {
			// If there are no Scores
			scores.add(score);
			names.add(name);
			rank = 1;
		}
		// Changes the values of in the highScores index with the new ones
		highScores.clear();
		for (int i = 0; i < scores.size() - 1; i++) {
			highScores.add(names.get(i) + ": " + scores.get(i));
		}
		highScores.add(names.get(names.size() - 1) + ": "
				+ scores.get(scores.size() - 1));
		// Updates the score file
		printScoreFile();
	}

	// Checks the validity of the text in the nameField
	private boolean validName() {
		// Name has to be 10 characters or less and contain only letters
		String name = nameField.getText();
		if (name.matches("[a-zA-Z]+") && name.length() <= 10) {
			return true;
		} else {
			return false;
		}
	}
}
