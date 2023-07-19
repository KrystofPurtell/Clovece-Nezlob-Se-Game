package org.cis1200.clovecenezlobse;

import java.awt.*;
import javax.swing.*;
public class RunCloveceNezlobSe implements Runnable {
    public void run() {

        // Top-level frame for game
        final JFrame frame = new JFrame("CloveceNezlobSe");
        frame.setLocation(0, 0); // 500, 300 prev
        frame.setPreferredSize(new Dimension(800, 600));

        // Status panel, which will show current dice number, current turn, and whether anyone
        // has won
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel turnStatus = new JLabel("Setting up...");
        status_panel.add(turnStatus);
        final JLabel dieStatus = new JLabel("");
        status_panel.add(dieStatus);
        final JLabel winnerStatus = new JLabel("No winner yet...");
        status_panel.add(winnerStatus);

        // Game board
        final TrueGameBoard board = new TrueGameBoard(turnStatus, dieStatus, winnerStatus);
        frame.add(board, BorderLayout.CENTER);

        // user turns panel, which has spawn, skip, and start turn fresh buttons
        final JPanel turns_panel = new JPanel();
        frame.add(turns_panel, BorderLayout.EAST);
        final JButton skipButton = new JButton("Skip Turn");
        skipButton.addActionListener(e -> board.skipTurn());
        turns_panel.add(skipButton);
        final JButton spawnButton = new JButton("Spawn");
        spawnButton.addActionListener(e -> board.spawn());
        turns_panel.add(spawnButton);
        final JButton startTurnFreshButton = new JButton("Start Turn Fresh");
        startTurnFreshButton.addActionListener(e -> board.startFresh());
        turns_panel.add(startTurnFreshButton);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        // undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // save game button
        final JButton saveGame = new JButton("Save Game");
        saveGame.addActionListener(e -> board.save());
        control_panel.add(saveGame);

        // resume saved game button
        final JButton resumeGame = new JButton("Resume Game");
        resumeGame.addActionListener(e -> board.resume());
        control_panel.add(resumeGame);

        // open instructions button
        final JButton openInstructions = new JButton("Open Instructions");
        openInstructions.addActionListener(e -> board.openInstructions());
        control_panel.add(openInstructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
