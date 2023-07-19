package org.cis1200.clovecenezlobse;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TrueGameBoard extends JPanel {
    private CloveceNezlobSe cns; // model for the game
    private JLabel turnStatus;
    private JLabel dieStatus;
    private JLabel winnerStatus;
    private int startPosition;
    private int finalPosition;

    // Game constants
    public static final int BOARD_WIDTH = 800;
    public static final int BOARD_HEIGHT = 600;

    /**
     * Initializes the game board.
     */
    public TrueGameBoard(JLabel turnStatus, JLabel dieStatus, JLabel winnerStatus) {

        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        cns = new CloveceNezlobSe(); // initializes model for the game
        this.turnStatus = turnStatus; // initializes the status JLabel
        this.dieStatus = dieStatus;
        this.winnerStatus = winnerStatus;
        finalPosition = -1;
        startPosition = -1;

        /*
         * Listens for mouseclicks. Updates the model, then updates the game
         * board based off of the updated model.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                Point p = e.getPoint();

                // update model given the coordinates of the mouseclick, lots of boilerplate
                // b/c this board is not pretty

                // check if click in blue final base
                if (p.x > 40 && p.x < 60 && p.y < 360 && p.y > 340) {
                    updatePositions(41);
                }
                if (p.x > 60 && p.x < 80 && p.y < 340 && p.y > 320) {
                    updatePositions(42);
                }
                if (p.x > 80 && p.x < 100 && p.y < 320 && p.y > 300) {
                    updatePositions(43);
                }
                if (p.x > 100 && p.x < 120 && p.y < 300 && p.y > 280) {
                    updatePositions(44);
                }

                // check if click in green final base
                if (p.x > 40 && p.x < 60 && p.y > 40 && p.y < 60) {
                    updatePositions(45);
                }
                if (p.x > 60 && p.x < 80 && p.y > 60 && p.y < 80) {
                    updatePositions(46);
                }
                if (p.x > 80 && p.x < 100 && p.y > 80 && p.y < 100) {
                    updatePositions(47);
                }
                if (p.x > 100 && p.x < 120 && p.y > 100 && p.y < 120) {
                    updatePositions(48);
                }

                // check if click in yellow final base
                if (p.x > 340 && p.x < 360 && p.y > 40 && p.y < 60) {
                    updatePositions(49);
                }
                if (p.x > 320 && p.x < 340 && p.y > 60 && p.y < 80) {
                    updatePositions(50);
                }
                if (p.x > 300 && p.x < 320 && p.y > 80 && p.y < 100) {
                    updatePositions(51);
                }
                if (p.x > 280 && p.x < 300 && p.y > 100 && p.y < 120) {
                    updatePositions(52);
                }

                // check if click in red final base
                if (p.x > 340 && p.x < 360 && p.y > 340 && p.y < 360) {
                    updatePositions(53);
                }
                if (p.x > 320 && p.x < 340 && p.y > 320 && p.y < 340) {
                    updatePositions(54);
                }
                if (p.x > 300 && p.x < 320 && p.y > 300 && p.y < 320) {
                    updatePositions(55);
                }
                if (p.x > 280 && p.x < 300 && p.y > 280 && p.y < 300) {
                    updatePositions(56);
                }

                // check if point is in the "blue" section of the normal board
                for (int i = 0; i < 10; i++) {
                    if (p.x > 20 && p.x < 40 && p.y > (320 - 30 * i - 10) &&
                            p.y < (320 - 30 * i + 10)) {
                        updatePositions(i + 1);
                    }
                }

                // check if point is in "green" section of the normal board
                for (int i = 0; i < 10; i++) {
                    if (p.x > (80 + 30 * i - 10) && p.x < (80 + 30 * i + 10) && p.y > 20 &&
                            p.y < 40) {
                        updatePositions(i + 10 + 1);
                    }
                }

                // check if point is in "yellow" section of the normal board
                for (int i = 0; i < 10; i++) {
                    if (p.x > 360 && p.x < 380 && p.y > (80 + 30 * i - 10) &&
                            p.y < (80 + 30 * i + 10)) {
                        updatePositions(i + 20 + 1);
                    }
                }

                // check if point is in "red" section of the normal board
                for (int i = 0; i < 10; i++) {
                    if (p.x > (320 - 30 * i - 10) && p.x < (320 - 30 * i + 10) &&
                            p.y > 360 && p.y < 380) {
                        updatePositions(i + 30 + 1);
                    }
                }

                // try to play a turn, if turn is played reset startPosition and finalPosition
                if (cns.playTurn(startPosition, finalPosition)) {
                    startPosition = -1;
                    finalPosition = -1;
                }

                updateStatus(); // updates the status JLabel
                repaint(); // repaints the game board
            }
        });
    }

    /**
     * Helper method that takes in integer number and updates startPosition and finalPosition
     * accordingly. Assumes only
     *
     * @param num - integer that is either startPosition or finalPosition
     */
    private void updatePositions(int num) {
        if (startPosition == -1) {
            startPosition = num;
        } else if (finalPosition == -1) {
            finalPosition = num;
        }
    }

    /**
     * This method allows a player to start a turn again fresh, keeping the same die number
     */
    public void startFresh() {
        this.finalPosition = -1;
        this.startPosition = -1;
    }

    /**
     * This method resets the game to its original state
     */
    public void reset() {
        cns.reset();
        turnStatus.setText(String.valueOf(cns.getPlayerTurn()));
        dieStatus.setText(Integer.toString(cns.getCurrDiceNum()));
        winnerStatus.setText("No winner yet...");
        finalPosition = -1;
        startPosition = -1;

        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        turnStatus.setText(String.valueOf(cns.getPlayerTurn()));
        dieStatus.setText(Integer.toString(cns.getCurrDiceNum()));

        String winner = cns.getGameWinner();
        if (!winner.equals("")) {
            winnerStatus.setText(winner + " wins!");
        }
    }

    /**
     * Function called by the skip button
     */
    public void skipTurn() {
        cns.skipTurn();
        updateStatus();
        repaint();
        finalPosition = -1;
        startPosition = -1;
    }

    /**
     * Function called when a user presses the spawn button
     */
    public void spawn() {
        cns.spawnPawn();
        updateStatus();
        repaint();
        finalPosition = -1;
        startPosition = -1;
    }

    /**
     * Function called when a user presses undo button
     */
    public void undo() {
        cns.undo();
        updateStatus();
        repaint();
        finalPosition = -1;
        startPosition = -1;
    }

    /**
     * Function called when user presses save button
     */
    public void save() {
        cns.saveGame();
        updateStatus();
        finalPosition = -1;
        startPosition = -1;
    }

    /**
     * Function called when user presses resume button
     */
    public void resume() {
        cns.resumeGame();
        updateStatus();
        repaint();
        finalPosition = -1;
        startPosition = -1;
    }

    /**
     * Function called when user presses the open instructions button
     */
    public void openInstructions() {
        String stringOfInstructions = "This is the Czech board game ČLOVĚČE NEZLOB SE. " +
                "\n There are 4 players, and the winner is the one who gets all 4 of their " +
                "pieces into their final base. \n To do so, players take turns rolling a die. " +
                "They begin with their 4 pieces off of the board. \n To get a piece onto the board,"
                + " they must roll a 6 and “spawn” in a pawn. Once a pawn is on the board, the " +
                "player \n" + "must try to get all the way around the board and into their final " +
                "base. " + "If a pawn moves onto another \n pawn, the landed-on pawn is moved to " +
                "its " + "home base, which is off the board. \n If a player has no possible moves, "
                + "they must skip their turn. Players can also skip their turns at will." +
                "To play " + "this version \n the user must use buttons and their mouse. Rolls " +
                "happen automatically. To spawn a pawn, simply press the “Spawn” button. " +
                "To skip, press \n “Skip Turn.” To move a pawn, click on the pawn you want to " +
                "move " + "and then where you want it to go. If \n you mess up your clicks or " +
                "are having " + "trouble moving your pawn, press “Start Turn Fresh” \n to " +
                "reset the start and " + "final position." + "This game also allows players to " +
                "undo moves by pressing the " + "“Undo”\n button. They can also reset the entire " +
                "game by pressing “Reset”. " + "To save a game, they can \n press “Save Game,” " +
                "and to resume the most recent " + "saved game, a player can press “Resume Game.”"
                + "\n Note that if you undo after resuming a game, it will reset the board " +
                "entirely, because saving a game loses all the previous moves.";
        JOptionPane.showMessageDialog(null, stringOfInstructions,
                "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Draw a rectangular board
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draws board grid
        paintRedHomeBase(g);
        paintRedFinalBase(g);
        paintBlueHomeBase(g);
        paintBlueFinalBase(g);
        paintGreenHomeBase(g);
        paintGreenFinalBase(g);
        paintYellowHomeBase(g);
        paintYellowFinalBase(g);
        paintNonBaseBoard(g);
    }

    /**
     * Helper for paintComponent. Draws home base for red pawns. Outlined in red
     * whether full or not, filled with red only if there is a red pawn there
     *
     * @param g - a Graphics object
     */
    private void paintRedHomeBase(Graphics g) {
        Pawn[][] boardOfPawns = cns.getBoardArray();
        g.setColor(Color.RED);

        // go one-by-one
        if (boardOfPawns[8][0] == null) {
            g.drawOval(370, 370, 10, 10);
        } else {
            g.fillOval(370, 370, 10, 10);
        }

        if (boardOfPawns[8][1] == null) {
            g.drawOval(385, 370, 10, 10);
        } else {
            g.fillOval(385, 370, 10, 10);
        }

        if (boardOfPawns[8][2] == null) {
            g.drawOval(370, 385, 10, 10);
        } else {
            g.fillOval(370, 385, 10, 10);
        }

        if (boardOfPawns[8][3] == null) {
            g.drawOval(385, 385, 10, 10);
        } else {
            g.fillOval(385, 385, 10, 10);
        }
    }

    /**
     * Helper for paintComponent. Draws final base for red pawns. Outlined in red
     * whether full or not, filled with red only if there is a red pawn there
     *
     * @param g - a Graphics object
     */
    private void paintRedFinalBase(Graphics g) {
        g.setColor(Color.RED);
        for (int i = 0; i < 4; i++) {
            if (cns.getCell(3, i) != null) {
                g.fillOval(350 - 20 * i, 350 - 20 * i, 10, 10);
            } else {
                g.drawOval(350 - 20 * i, 350 - 20 * i, 10, 10);
            }
        }
    }

    /**
     * Helper for paintComponent. Draws home base for blue pawns. Outlined in blue
     * whether full or not, filled with blue only if there is a blue pawn there
     *
     * @param g - a Graphics object
     */
    private void paintGreenHomeBase(Graphics g) {
        Pawn[][] boardOfPawns = cns.getBoardArray();
        g.setColor(Color.GREEN);

        // go one-by-one
        if (boardOfPawns[6][0] == null) {
            g.drawOval(15, 15, 10, 10);
        } else {
            g.fillOval(15, 15, 10, 10);
        }

        if (boardOfPawns[6][1] == null) {
            g.drawOval(30, 15, 10, 10);
        } else {
            g.fillOval(30, 15, 10, 10);
        }

        if (boardOfPawns[6][2] == null) {
            g.drawOval(15, 30, 10, 10);
        } else {
            g.fillOval(15, 30, 10, 10);
        }

        if (boardOfPawns[6][3] == null) {
            g.drawOval(30, 30, 10, 10);
        } else {
            g.fillOval(30, 30, 10, 10);
        }
    }

    /**
     * Helper for paintComponent. Draws final base for blue pawns. Outlined in blue
     * whether full or not, filled with blue only if there is a blue pawn there
     *
     * @param g - a Graphics object
     */
    private void paintBlueFinalBase(Graphics g) {
        g.setColor(Color.BLUE);
        for (int i = 0; i < 4; i++) {
            if (cns.getCell(0, i) != null) {
                g.fillOval(50 + 20 * i, 350 - 20 * i, 10, 10);
            } else {
                g.drawOval(50 + 20 * i, 350 - 20 * i, 10, 10);
            }
        }
    }

    /**
     * Helper for paintComponent. Draws home base for green pawns.Outlined in green
     * whether full or not, filled with green only if there is a green pawn there
     *
     * @param g - a Graphics object
     */
    private void paintBlueHomeBase(Graphics g) {
        Pawn[][] boardOfPawns = cns.getBoardArray();
        g.setColor(Color.BLUE);

        // go one-by-one
        if (boardOfPawns[5][0] == null) {
            g.drawOval(15, 370, 10, 10);
        } else {
            g.fillOval(15, 370, 10, 10);
        }

        if (boardOfPawns[5][1] == null) {
            g.drawOval(30, 370, 10, 10);
        } else {
            g.fillOval(30, 370, 10, 10);
        }

        if (boardOfPawns[5][2] == null) {
            g.drawOval(15, 385, 10, 10);
        } else {
            g.fillOval(15, 385, 10, 10);
        }

        if (boardOfPawns[5][3] == null) {
            g.drawOval(30, 385, 10, 10);
        } else {
            g.fillOval(30, 385, 10, 10);
        }
    }

    /**
     * Helper for paintComponent. Draws final base for green pawns.Outlined in green
     * whether full or not, filled with green only if there is a green pawn there
     *
     * @param g - a Graphics object
     */
    private void paintGreenFinalBase(Graphics g) {
        g.setColor(Color.GREEN);
        for (int i = 0; i < 4; i++) {
            if (cns.getCell(1, i) != null) {
                g.fillOval(50 + 20 * i, 50 + 20 * i, 10, 10);
            } else {
                g.drawOval(50 + 20 * i, 50 + 20 * i, 10, 10);
            }
        }
    }

    /**
     * Helper for paintComponent. Draws home base for yellow pawns. Outlined in
     * yellow whether full or not, filled with yellow only if there is a yellow pawn there
     *
     * @param g - a Graphics object
     */
    private void paintYellowHomeBase(Graphics g) {
        Pawn[][] boardOfPawns = cns.getBoardArray();
        g.setColor(Color.YELLOW);

        // go one-by-one
        if (boardOfPawns[7][0] == null) {
            g.drawOval(370, 15, 10, 10);
        } else {
            g.fillOval(370, 15, 10, 10);
        }

        if (boardOfPawns[7][1] == null) {
            g.drawOval(385, 15, 10, 10);
        } else {
            g.fillOval(385, 15, 10, 10);
        }

        if (boardOfPawns[7][2] == null) {
            g.drawOval(370, 30, 10, 10);
        } else {
            g.fillOval(370, 30, 10, 10);
        }

        if (boardOfPawns[7][3] == null) {
            g.drawOval(385, 30, 10, 10);
        } else {
            g.fillOval(385, 30, 10, 10);
        }
    }

    /**
     * Helper for paintComponent. Draws home base for yellow pawns. Outlined in
     * yellow whether full or not, filled with yellow only if there is a yellow pawn there
     *
     * @param g - a Graphics object
     */
    private void paintYellowFinalBase(Graphics g) {
        g.setColor(Color.YELLOW);
        for (int i = 0; i < 4; i++) {
            if (cns.getCell(2, i) != null) {
                g.fillOval(350 - 20 * i, 50 + 20 * i, 10, 10);
            } else {
                g.drawOval(350 - 20 * i, 50 + 20 * i, 10, 10);
            }
        }
    }

    /**
     * Helper for paintComponent. Draws a rectangle representing the non-base spots. If a base
     * spawn is a spawn point for some pawn color, outline it in that color. Otherwise, draw an
     * empty circle outlined in black if there is no pawn, a filled circle of the color of the
     * occupying pawn if there is a pawn.
     *
     * @param g - a Graphics object
     */
    private void paintNonBaseBoard(Graphics g) {
        Pawn[][] boardOfPawns = cns.getBoardArray();

        // draw the "blue" section
        g.setColor(Color.BLUE);
        if (boardOfPawns[4][0] == null) {
            g.drawOval(30, 320, 10, 10);
        } else {
            updateColor(boardOfPawns[4][0], g);
            g.fillOval(30, 320, 10, 10);
        }
        g.setColor(Color.BLACK);
        for (int i = 1; i < 10; i++) {
            if (boardOfPawns[4][i] == null) {
                g.drawOval(30, (320 - 30 * i), 10, 10);
            } else {
                updateColor(boardOfPawns[4][i], g);
                g.fillOval(30, (320 - 30 * i), 10, 10);
                g.setColor(Color.BLACK);
            }
        }

        // draw the "green" section
        g.setColor(Color.GREEN);
        if (boardOfPawns[4][10] == null) {
            g.drawOval(80, 30, 10, 10);
        } else {
            updateColor(boardOfPawns[4][10], g);
            g.fillOval(80, 30, 10, 10);
        }
        g.setColor(Color.BLACK);
        for (int i = 11; i < 20; i++) {
            if (boardOfPawns[4][i] == null) {
                g.drawOval(80 + (30 * (i - 10)), 30, 10, 10);
            } else {
                updateColor(boardOfPawns[4][i], g);
                g.fillOval(80 + (30 * (i - 10)), 30, 10, 10);
                g.setColor(Color.BLACK);
            }
        }

        // draw the "yellow" section
        g.setColor(Color.YELLOW);
        if (boardOfPawns[4][20] == null) {
            g.drawOval(370, 80, 10, 10);
        } else {
            updateColor(boardOfPawns[4][20], g);
            g.fillOval(370, 80, 10, 10);
        }
        g.setColor(Color.BLACK);
        for (int i = 21; i < 30; i++) {
            if (boardOfPawns[4][i] == null) {
                g.drawOval(370, (80 + (30 * (i - 20))), 10, 10);
            } else {
                updateColor(boardOfPawns[4][i], g);
                g.fillOval(370, (80 + (30 * (i - 20))), 10, 10);
                g.setColor(Color.BLACK);
            }
        }

        // draw the "red" section
        g.setColor(Color.RED);
        if (boardOfPawns[4][30] == null) {
            g.drawOval(320, 370, 10, 10);
        } else {
            updateColor(boardOfPawns[4][30], g);
            g.fillOval(320, 370, 10, 10);
        }
        g.setColor(Color.BLACK);
        for (int i = 31; i < 40; i++) {
            if (boardOfPawns[4][i] == null) {
                g.drawOval(320 - (30 * (i - 30)), 370, 10, 10);
            } else {
                updateColor(boardOfPawns[4][i], g);
                g.fillOval(320 - (30 * (i - 30)), 370, 10, 10);
                g.setColor(Color.BLACK);
            }
        }
    }

    /**
     * Helper to set the color of the input graphics context to the appropriate color depending
     * on the input pawn
     */
    private void updateColor(Pawn p, Graphics g) {
        if (p.getPawnColor() == 'B') {
            g.setColor(Color.BLUE);
        } else if (p.getPawnColor() == 'G') {
            g.setColor(Color.GREEN);
        } else if (p.getPawnColor() == 'Y') {
            g.setColor(Color.YELLOW);
        } else if (p.getPawnColor() == 'R') {
            g.setColor(Color.RED);
        } else {
            g.setColor(Color.BLACK);
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
