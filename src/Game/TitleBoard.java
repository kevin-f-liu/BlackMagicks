package Game;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.SystemColor;

public class TitleBoard extends JPanel {

	private Game game;
	private IO io;
	private Sounds sounds;

	public TitleBoard(Game g, Sounds s) {
		setForeground(new Color(0, 0, 0));
		
		// Set up sounds
		sounds = s;

		// Sets reference to the game object for game control
		game = g;
		
		// Initialize the IO
		io = new IO();

		setBackground(new Color(0, 0, 0));

		JLabel title = new JLabel("Magicks");
		title.setForeground(new Color(128, 128, 128));
		title.setBounds(610, 211, 145, 31);
		title.setVerticalAlignment(SwingConstants.TOP);
		title.setFont(new Font("Trajan Pro", Font.BOLD, 30));

		// Play button and action listener
		JButton btnPlay = new JButton("PLAY");
		btnPlay.setForeground(new Color(255, 255, 255));
		btnPlay.setBackground(new Color(105, 105, 105));
		btnPlay.setBounds(569, 306, 228, 56);
		btnPlay.setFont(new Font("Trajan Pro", Font.PLAIN, 13));
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Play sounds
				sounds.playGameStart();
				game.gameVisible();
			}
		});

		// Open instructions button and action listener
		JButton btnInstructions = new JButton("INSTRUCTIONS");
		btnInstructions.setForeground(new Color(255, 255, 255));
		btnInstructions.setBackground(new Color(105, 105, 105));
		btnInstructions.setBounds(569, 377, 228, 56);
		btnInstructions.setFont(new Font("Trajan Pro", Font.PLAIN, 13));
		setLayout(null);
		btnInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Play sounds
				sounds.playDialog();
				// Opens the file, reads it.
				io.openInputFile("instructions.txt");
				String line = io.readLine();
				String instructionsText = "";
				while (line != null) {
					instructionsText += "\n" + line;
					line = io.readLine();
				}
				// Close the file
				io.closeInputFile();

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setPreferredSize(new Dimension(500, 500));
				add(scrollPane);
				// The text area bound inside the scrollPane
				JTextArea instructions = new JTextArea(instructionsText);
				scrollPane.setViewportView(instructions);
				instructions.setBackground(Color.LIGHT_GRAY);
				instructions.setEditable(false);
				instructions.setFont(new Font("Trajan Pro", Font.PLAIN, 20));
				instructions.setLineWrap(true);
				// Creates dialog popup
				JOptionPane.showMessageDialog(null, scrollPane, "Instructions",
						JOptionPane.PLAIN_MESSAGE, null);
			}
		});

		JButton btnSpellBook = new JButton("SPELL BOOK");
		btnSpellBook.setForeground(Color.WHITE);
		btnSpellBook.setFont(new Font("Trajan Pro", Font.PLAIN, 13));
		btnSpellBook.setBackground(SystemColor.controlDkShadow);
		btnSpellBook.setBounds(569, 450, 228, 56);
		btnSpellBook.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Play sounds
				sounds.playDialog();
				// Opens the file, reads it.
				io.openInputFile("spells.txt");
				String line = io.readLine();
				String instructions = "";
				while (line != null) {
					instructions += "\n" + line;
					line = io.readLine();
				}
				// Close the file
				io.closeInputFile();

				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setPreferredSize(new Dimension(500, 500));
				add(scrollPane);
				// The text area bound inside the scrollPane
				JTextArea spellBook = new JTextArea(instructions);
				scrollPane.setViewportView(spellBook);
				spellBook.setBackground(Color.LIGHT_GRAY);
				spellBook.setEditable(false);
				spellBook.setFont(new Font("Trajan Pro", Font.PLAIN, 20));
				spellBook.setLineWrap(true);
				// Creates dialog popup
				JOptionPane.showMessageDialog(null, scrollPane, "Spell Book",
						JOptionPane.PLAIN_MESSAGE, null);
			}
		});

		// Add all components
		add(title);
		add(btnInstructions);
		add(btnPlay);
		add(btnSpellBook);
	}
}
