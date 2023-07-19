package org.cis1200.clovecenezlobse;
import java.util.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;

/**
 * This class is the model of the game. It stores all information necessary to play the game and
 * is completely independent of any GUI/view factors
 */

public class CloveceNezlobSe {

    private Pawn[][] boardArr;
    private boolean gameOver;
    private int currDiceNum;
    private char playerTurn;
    private String gameWinner;
    private LinkedList<Pawn[][]> listOfBoards;

    private LinkedList<Integer> listOfDice;

    private int originalDie;

    /**
     * Normal constructor for game. The 2-D board array starts w/ all pawns in their "homes" (the
     * last 4 rows). Dice number is randomly generated using a helper method. Player turn starts w/
     * Blue by default. The list of Boards begins with the current board. Refer to the reset method
     * to see implementation.
     */
    public CloveceNezlobSe() {
        reset();
    }

    /**
     * The follow methods are various getters and setters for instance variables, to be used later
     * in the program and for testing
     */
    public Pawn[][] getBoardArray() {
        return this.boardArr;
    }

    public void setBoardArr(Pawn[][] newBoard) {
        this.boardArr = newBoard;
    }

    public boolean getGameOver() {
        return this.gameOver;
    }

    public void setGameOver(boolean gameOverOrNot) {
        this.gameOver = gameOverOrNot;
    }

    public int getCurrDiceNum() {
        return this.currDiceNum;
    }

    public void setCurrDiceNum(int newDie) {
        this.currDiceNum = newDie;
    }

    public char getPlayerTurn() {
        return this.playerTurn;
    }

    public String getGameWinner() {
        return this.gameWinner;
    }

    public void setGameWinner(String winner) {
        this.gameWinner = winner;
    }

    public LinkedList<Pawn[][]> getListOfBoards() {
        return this.listOfBoards;
    }

    public LinkedList<Integer> getListOfDice() {
        return this.listOfDice;
    }

    /**
     * This function resets the game back to its starting state. Refer to the constructor
     * description for an in-depth description of what each variable is set to.
     */
    public void reset() {
        // reset the simpler instance vars first
        this.gameOver = false;
        this.rollDie();
        this.originalDie = this.currDiceNum;
        this.playerTurn = 'B';
        this.gameWinner = "";

        // reset the board by creating a new board (first 5 rows null, last 4 full of colored pawns)
        Pawn[][] newBoard = generateEmptyBoard();
        char[] colors = {'B', 'G', 'Y', 'R'};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newBoard[(i + 5)][j] = new Pawn(colors[i], j + 1);
            }
        }

        this.boardArr = newBoard;

        // update the list of Boards to an empty list
        this.listOfBoards = new LinkedList<>();
        this.listOfDice = new LinkedList<>();

        // add the first board and dice to this list
        listOfBoards.add(createCopyArray(newBoard));
        listOfDice.add(currDiceNum);
    }

    /**
     * This is a helper function that creates a new Pawn 2-D array of the dimensions of the board
     * (9 rows, where the 1st 4 and last 4 have 4 spaces, the middle has 40).
     *
     * @return an empty (full of null) 2-D Pawn array of the desired dimensions.
     */
    private static Pawn[][] generateEmptyBoard() {
        Pawn[][] emptyBoardArray = new Pawn[9][];
        for (int i = 0; i < 9; i++) {
            if (i != 4) {
                emptyBoardArray[i] = new Pawn[4];
            } else {
                emptyBoardArray[i] = new Pawn[40];
            }
        }

        return emptyBoardArray;
    }

    /**
     * This is a helper function that creates an identical copy of the input 2-D Pawn array. Public
     * b/c used in testing
     *
     * @return A 2-d Pawn array containing identical elements to the input one
     */
    public static Pawn[][] createCopyArray(Pawn[][] originalArray) {
        Pawn[][] copiedArray = new Pawn[9][];
        for (int i = 0; i < 9; i++) {
            copiedArray[i] = Arrays.copyOf(originalArray[i], originalArray[i].length);
        }

        return copiedArray;
    }

    /**
     * This function checks whether a game has been won. It does so by checking instance variables
     * and then the 2-D array. It updates the gameOver and gameWinner fields accordingly.
     *
     * @return a boolean that's true if the game is won, false otherwise
     */
    public boolean checkIfWon() {

        // check if instance vars suggest game is already won
        if (gameOver) {
            return true;
        }

        /* iterate through the base arrays, updating instance vars if any suggest game is won.
        Assume invariants like a base can only have pawns matching base color and that there is
        only one base that is full, since the game should end once someone wins
         */
        for (int i = 0; i < 4; i++) {
            int basePawnCount = 0;
            for (int j = 0; j < 4; j++) {
                if (boardArr[i][j] != null) {
                    basePawnCount++;
                }
            }
            // if a base has 4 pawns, update instance vars accordingly
            if (basePawnCount == 4) {
                gameOver = true;

                if (i == 0) {
                    gameWinner = "Blue";
                } else if (i == 1) {
                    gameWinner = "Green";
                } else if (i == 2) {
                    gameWinner = "Yellow";
                } else {
                    gameWinner = "Red";
                }
            }
        }

        return gameOver;
    }

    /**
     * This function rolls a die, updating the game's internal state accordingly by updating the
     * currDiceNum field.
     */
    public void rollDie() {
        int dieNumber = (int)(Math.random() * 6) + 1;
        this.currDiceNum = dieNumber;
    }

    /**
     * This function undoes one turn, updating all internal states accordingly. For example, it
     * will take the new board and check whether it is won or not. This undo function does not,
     * however, restore the rolled die, instead generating a new one. If there is only one board
     * in the list, this undo function does nothing.
     */
    public void undo() {
        if (listOfBoards.size() < 2) {
            int originalDieStore = originalDie;
            reset();
            this.currDiceNum = originalDieStore;
        } else {
            this.currDiceNum = this.listOfDice.peekLast();
            this.listOfDice.removeLast();
            this.listOfBoards.removeLast();
            Pawn[][] newBoardArray = this.listOfBoards.peekLast();
            this.boardArr = newBoardArray;

            // update player turn depending on whose turn it was previously
            if (this.playerTurn == 'R') {
                this.playerTurn = 'Y';
            } else if (this.playerTurn == 'Y') {
                this.playerTurn = 'G';
            } else if (this.playerTurn == 'G') {
                this.playerTurn = 'B';
            } else {
                this.playerTurn = 'R';
            }

            // check if the game is won to update the winner vars
            checkIfWon();
        }
    }

    /**
     * This helper updates player turn depending on whose turn it currently is
     *
     * @param turn - char representing the current player turn
     */
    private void updatePlayerTurn(char turn) {
        if (turn == 'R') {
            this.playerTurn = 'B';
        } else if (turn == 'Y') {
            this.playerTurn = 'R';
        } else if (turn == 'G') {
            this.playerTurn = 'Y';
        } else if (turn == 'B') {
            this.playerTurn = 'G';
        }
    }

    /**
     * This function writes the current game state to the input filePath, which can be accessed
     * later by invoking the constructor with this same filePath name. If the filePath doesn't
     * already exist, this will make a new file w/ the filePath. In our game, we do not have to
     * worry about this b/c the game will only be saved to SavedGameState.txt
     *
     * @param filePath - String of the path to the file to write to
     */
    public void writeToFile(String filePath) {

        // create buffered writer
        if (filePath == null) {
            throw new IllegalArgumentException();
        }
        try {
            BufferedWriter bw = new BufferedWriter(new java.io.FileWriter(filePath, false));

            // write gameOver (t/f), currDiceNum(1-6), playerTurn(B,G,Y,R), and game winner
            if (this.gameOver) {
                try {
                    bw.write('t');
                } catch (IOException e) {
                    throw new IllegalArgumentException("IOException");
                }
            } else {
                try {
                    bw.write('f');
                } catch (IOException e) {
                    throw new IllegalArgumentException("IOException");
                }
            }

            try {
                bw.newLine();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            try {
                bw.write(Integer.toString(this.currDiceNum));
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            try {
                bw.newLine();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            try {
                bw.write(this.playerTurn);
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            try {
                bw.newLine();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            try {
                bw.write(this.gameWinner);
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            // iterate over all the board cells. If null, write "null". If pawn object, create a
            // string like "B1" to store color and identity
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < boardArr[i].length; j++) {

                    // write new line first
                    try {
                        bw.newLine();
                    } catch (IOException e) {
                        throw new IllegalArgumentException("IOException");
                    }

                    if (boardArr[i][j] == null) {
                        try {
                            bw.write("null");
                        } catch (IOException e) {
                            throw new IllegalArgumentException("IOException");
                        }
                    } else {
                        char color = boardArr[i][j].getPawnColor();
                        int pawnNumber = boardArr[i][j].getPawnIdentity();
                        String stringToWrite = Character.toString(color) +
                                Integer.toString(pawnNumber);
                        try {
                            bw.write(stringToWrite);
                        } catch (IOException e) {
                            throw new IllegalArgumentException("IOException");
                        }
                    }
                }
            }

            // close the writer
            try {
                bw.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("IOException");
        }
    }

    /**
     * This function uses the writeToFile function to save the game state to the SavedGameState.txt
     * file in the files folder. Assume user does not have access to this folder.
     */
    public void saveGame() {
        writeToFile("files/SavedGameState.txt");
    }

    /**
     * This function resumes the game by reading in data from the SavedGameState.txt file. If at
     * any point the file does contain the correct info (ie the saved game is corrupted in some
     * way), we call reset and the entire game is reset. This is also in case a player calls
     * continue in the GUI game, but they haven't saved anything.
     */
    public void resumeGame() {

        try {
            BufferedReader br =
                   new BufferedReader(new java.io.FileReader("files/SavedGameState.txt"));

           // retrieve gameOver
            try {
                String gameOverString = br.readLine();
                if (gameOverString.equals("f")) {
                    this.gameOver = false;
                } else if (gameOverString.equals("t")) {
                    this.gameOver = true;
                } else {
                    reset();
                }
            } catch (IOException e) {
                reset();
                throw new IllegalArgumentException("IOException");
            }

           // retrieve currDiceNum
            try {
                String currDiceNumString = br.readLine();
                try {
                    int diceVal = Integer.parseInt(currDiceNumString);
                    this.currDiceNum = diceVal;
                } catch (NumberFormatException e) {
                    reset();
                    throw new IllegalArgumentException("NumberFormatException");
                }
            } catch (IOException e) {
                reset();
                throw new IllegalArgumentException("IOException");
            }

            try {
                String currTurn = br.readLine();
                char turnChar = currTurn.charAt(0);
                this.playerTurn = turnChar;
            } catch (IOException e) {
                reset();
                throw new IllegalArgumentException("IOException");
            }

            // retrieve the game winner
            try {
                String winnerString = br.readLine();
                if (winnerString.equals("Blue")) {
                    this.gameWinner = winnerString;
                } else if (winnerString.equals("Green")) {
                    this.gameWinner = winnerString;
                } else if (winnerString.equals("Yellow")) {
                    this.gameWinner = winnerString;
                } else if (winnerString.equals("Red")) {
                    this.gameWinner = winnerString;
                } else {
                    this.gameWinner = "";
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }

            Pawn[][] potentialBoardArray = generateEmptyBoard();
            boolean useOrNot = true;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < potentialBoardArray[i].length; j++) {
                    try {
                        String pawnOrNull = br.readLine();
                        if (pawnOrNull.equals("null")) {
                            potentialBoardArray[i][j] = null;
                        } else if (pawnOrNull.equals("B1")) {
                            potentialBoardArray[i][j] = new Pawn('B', 1);
                        } else if (pawnOrNull.equals("B2")) {
                            potentialBoardArray[i][j] = new Pawn('B', 2);
                        } else if (pawnOrNull.equals("B3")) {
                            potentialBoardArray[i][j] = new Pawn('B', 3);
                        } else if (pawnOrNull.equals("B4")) {
                            potentialBoardArray[i][j] = new Pawn('B', 4);
                        } else if (pawnOrNull.equals("G1")) {
                            potentialBoardArray[i][j] = new Pawn('G', 1);
                        } else if (pawnOrNull.equals("G2")) {
                            potentialBoardArray[i][j] = new Pawn('G', 2);
                        } else if (pawnOrNull.equals("G3")) {
                            potentialBoardArray[i][j] = new Pawn('G', 3);
                        } else if (pawnOrNull.equals("G4")) {
                            potentialBoardArray[i][j] = new Pawn('G', 4);
                        } else if (pawnOrNull.equals("Y1")) {
                            potentialBoardArray[i][j] = new Pawn('Y', 1);
                        } else if (pawnOrNull.equals("Y2")) {
                            potentialBoardArray[i][j] = new Pawn('Y', 2);
                        } else if (pawnOrNull.equals("Y3")) {
                            potentialBoardArray[i][j] = new Pawn('Y', 3);
                        } else if (pawnOrNull.equals("Y4")) {
                            potentialBoardArray[i][j] = new Pawn('Y', 4);
                        } else if (pawnOrNull.equals("R1")) {
                            potentialBoardArray[i][j] = new Pawn('R', 1);
                        } else if (pawnOrNull.equals("R2")) {
                            potentialBoardArray[i][j] = new Pawn('R', 2);
                        } else if (pawnOrNull.equals("R3")) {
                            potentialBoardArray[i][j] = new Pawn('R', 3);
                        } else if (pawnOrNull.equals("R4")) {
                            potentialBoardArray[i][j] = new Pawn('R', 4);
                        } else {
                            useOrNot = false;
                        }

                        if (useOrNot) {
                            this.boardArr = potentialBoardArray;
                        } else {
                            reset();
                        }
                    } catch (IOException e) {
                        throw new IllegalArgumentException("IOException");
                    }
                }
            }
            try {
                br.close();
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException");
            }
        } catch (FileNotFoundException f) {
            reset();
            throw new IllegalArgumentException("FileNotFound");
        }
    }

    /**
     * This function deals with all game logic of playing a turn. Returns true if the move is valid
     * and successful, false if the location the player tries to move to is invalid.
     * If the turn is successful, playerTurn is updated. Also, appropriate fields are updated.
     * boardArr changes, gameOver/gameWinner are updated appropriately if the game is won, and the
     * pre-turn board is added to the listOfBoards. Assume a move is possible, since we'll call a
     * function to check this is the case in the GUI program. Note the input positions are
     * according to the GUI board and may not match the array positions.
     *
     * @param startpos - integer representing the position the user wants to move from
     * @param finalpos - integer representing the position the user wants to move to
     *
     * @return a boolean value of whether the turn was valid or not
     */
    public boolean playTurn(int startpos, int finalpos) {

        // check that both startpos and finalpos are valid numbers
        if (startpos < 0 || startpos > 56 || finalpos < 0 || finalpos > 56) {
            return false;
        }

        // check that difference is same as the die number if both slots aren't final bases
        if (startpos < 41 && startpos > 0 && finalpos < 41 & startpos > 0) {
            int difference = finalpos - startpos;

            // update difference in case there is a switch over from end to start of array
            if (startpos > 34 && startpos < 41 && finalpos > 0 && finalpos < 7) {
                difference = (finalpos + 40) - startpos;
            }
            if (difference != currDiceNum) {
                return false;
            }
        }

        // check that difference is same as die number if either slot is a final base
        int startRegEquivalent = startpos;
        int finalRegEquivalent = finalpos;
        if (finalpos > 40) {
            if (finalpos < 49 && finalpos > 44) {
                finalRegEquivalent = finalpos - 34;
            } else if (finalpos < 53 && finalpos > 44) {
                finalRegEquivalent = finalpos - 28;
            } else if (finalpos < 57 && finalpos > 44) {
                finalRegEquivalent = finalpos - 22;
            }
        }

        if (startpos > 40) {
            if (startpos < 49 && startpos > 44) {
                startRegEquivalent = startpos - 34;
            } else if (startpos < 53 && startpos > 44) {
                startRegEquivalent = startpos - 28;
            } else if (startpos < 57 && startpos > 44) {
                startRegEquivalent = startpos - 22;
            }
        }

        // ensure difference is not b/c of the switch from 37,38,39 to 0,1,2
        boolean switchingOver = false;
        if (startpos > 34 && finalpos < 7) {
            switchingOver = true;
        }
        if (finalRegEquivalent - startRegEquivalent != currDiceNum && !switchingOver) {
            return false;
        }

        // check that if the finalpos is a base, the correct player is trying to get in
        if (finalpos > 40 && finalpos < 45 && playerTurn != 'B') {
            return false;
        }
        if (finalpos > 44 && finalpos < 49 && playerTurn != 'G') {
            return false;
        }
        if (finalpos > 48 && finalpos < 53 && playerTurn != 'Y') {
            return false;
        }
        if (finalpos > 52 && finalpos < 57 && playerTurn != 'R') {
            return false;
        }

        // get pawn at start (if none, return false), make boardArr spot null if there's one
        Pawn pawnAtStart;
        if (startpos > 0 && startpos < 41) {
            pawnAtStart = boardArr[4][startpos - 1];
            boardArr[4][startpos - 1] = null;
        } else if (startpos > 40 && startpos < 45) {
            pawnAtStart = boardArr[0][startpos - 41]; // got rid of minus 1
            boardArr[0][startpos - 41] = null; //^^
        } else if (startpos > 44 && startpos < 49) {
            pawnAtStart = boardArr[1][startpos - 45]; // ^^
            boardArr[1][startpos - 45] = null;
        } else if (startpos > 48 && startpos < 52) {
            pawnAtStart = boardArr[2][startpos - 49]; //^^
            boardArr[2][startpos - 49] = null;
        } else if (startpos > 52 && startpos < 57) {
            pawnAtStart = boardArr[3][startpos - 53]; //^^
            boardArr[3][startpos - 53] = null;
        } else {
            return false;
        }

        if (pawnAtStart == null) {
            return false;
        }

        // get pawn in the final (if any), check if pawn is trying to move into diff clr base,
        // if there is a finalPawn make its current spot in the array null
        Pawn pawnFinal = null;
        if (finalpos > 0 && finalpos < 41) {
            pawnFinal = boardArr[4][finalpos - 1];
            boardArr[4][finalpos - 1] = null;
        } else if (finalpos > 40 && finalpos < 45) {
            if (pawnAtStart.getPawnColor() != 'B') {
                return false;
            }
            pawnFinal = boardArr[0][finalpos - 41];
            boardArr[0][finalpos - 41] = null;
        } else if (finalpos > 44 && finalpos < 49) {
            if (pawnAtStart.getPawnColor() != 'G') {
                return false;
            }
            pawnFinal = boardArr[1][finalpos - 45];
            boardArr[1][finalpos - 45] = null;
        } else if (finalpos > 48 && finalpos < 52) {
            if (pawnAtStart.getPawnColor() != 'Y') {
                return false;
            }
            pawnFinal = boardArr[2][finalpos - 49];
            boardArr[2][finalpos - 49] = null;
        } else if (finalpos > 52 && finalpos < 57) {
            if (pawnAtStart.getPawnColor() != 'R') {
                return false;
            }
            pawnFinal = boardArr[3][finalpos - 53]; // got rid of minus 1
            boardArr[3][finalpos - 53] = null;
        }

        // deal w/ moving the pawn and knock off finalpos pawn if there is one
        if (pawnFinal != null) {
            returnPawnToHome(pawnFinal);
        }
        if (finalpos < 41) {
            boardArr[4][finalpos - 1] = pawnAtStart; // got rid of minus 1 here
        } else if (finalpos < 45) {
            boardArr[0][finalpos - 41] = pawnAtStart; // used to be 41
        } else if (finalpos < 49) {
            boardArr[1][finalpos - 45] = pawnAtStart; // used to be 48
        } else if (finalpos < 53) {
            boardArr[2][finalpos - 49] = pawnAtStart; //used to be 52
        } else {
            boardArr[3][finalpos - 53] = pawnAtStart; // used to be 56
        }

        // make copy of variables for undo
        Pawn[][] copyOfBoardArray = createCopyArray(this.boardArr);
        int dieToSave = this.currDiceNum;

        // check if game was won, update instance vars appropriately
        checkIfWon();
        rollDie();
        updatePlayerTurn(this.playerTurn);
        listOfBoards.add(copyOfBoardArray);
        listOfDice.add(dieToSave);

        return true;
    }

    /**
     * This function represents another turn a player can take. It "spawns" in a pawn when a user
     * gets a 6. It returns true if the spawn is valid (the die number is 6 and there is a pawn
     * of the player's color that can be spawned in, false otherwise. If the turn is successful,
     * all appropriate instance vars are updated. If there is already a pawn in the "spawn" spot
     * and a user chooses to spawn, the new one kicks the old one off.
     *
     * @return a boolean value of whether the turn was valid or not
     */
    public boolean spawnPawn() {
        if (currDiceNum != 6) {
            return false;
        }

        // make sure there are pawns in the player's starting base array -> return false if not
        boolean baseIsNotFull = false;
        if (playerTurn == 'B') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[5][i] != null) {
                    baseIsNotFull = true;
                }
            }
        } else if (playerTurn == 'G') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[6][i] != null) {
                    baseIsNotFull = true;
                }
            }
        } else if (playerTurn == 'Y') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[7][i] != null) {
                    baseIsNotFull = true;
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (boardArr[8][i] != null) {
                    baseIsNotFull = true;
                }
            }
        }

        if (!baseIsNotFull) {
            return false;
        }

        /* save the pawn currently in the launch spot, find the first pawn in the spawning home
        * base and put it in appropriate array spot, put pawn previously in launch spot back in its
        * home base via a helper function
        */
        if (playerTurn == 'B') {
            Pawn prevLaunchPawn = boardArr[4][0];
            boardArr[4][0] = findFirstPawnInHome('B');
            returnPawnToHome(prevLaunchPawn);
        } else if (playerTurn == 'G') {
            Pawn prevLaunchPawn = boardArr[4][10];
            boardArr[4][10] = findFirstPawnInHome('G');
            returnPawnToHome(prevLaunchPawn);
        } else if (playerTurn == 'Y') {
            Pawn prevLaunchPawn = boardArr[4][20];
            boardArr[4][20] = findFirstPawnInHome('Y');
            returnPawnToHome(prevLaunchPawn);
        } else {
            Pawn prevLaunchPawn = boardArr[4][30];
            boardArr[4][30] = findFirstPawnInHome('R');
            returnPawnToHome(prevLaunchPawn);
        }

        //utilize skip turn at the end to kick over the turn, return true to signal turn is done
        skipTurn();
        return true;
    }

    /**
     * This function is a helper for spawnPawn that returns the first Pawn in the input color's
     * home base. It also sets the spot this pawn was in to be null. Assumes there exists a pawn
     * in the base. Assumes color is an actual game color.
     *
     * @return a pawn that is the first in the input color's base
     */
    private Pawn findFirstPawnInHome(char color) {
        Pawn pawnToReturn = new Pawn('B', 1);
        if (color == 'B') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[5][i] != null) {
                    pawnToReturn = boardArr[5][i];
                    boardArr[5][i] = null;
                    return pawnToReturn;
                }
            }
        } else if (color == 'G') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[6][i] != null) {
                    pawnToReturn = boardArr[6][i];
                    boardArr[6][i] = null;
                    return pawnToReturn;
                }
            }
        } else if (color == 'Y') {
            for (int i = 0; i < 4; i++) {
                if (boardArr[7][i] != null) {
                    pawnToReturn = boardArr[7][i];
                    boardArr[7][i] = null;
                    return pawnToReturn;
                }
            }
        } else {
            for (int i = 0; i < 4; i++) {
                if (boardArr[8][i] != null) {
                    pawnToReturn = boardArr[8][i];
                    boardArr[8][i] = null;
                    return pawnToReturn;
                }
            }
        }

        // function should never reach here, only for compiler's sake
        return pawnToReturn;
    }

    /**
     * This function is a helper for spawnPawn and playTurn that puts a knocked out Pawn back into
     * its home base, into the array slot corresponding to the Pawn's identity (0 for 1, 1 for 2,
     * etc. Assumes that home bases are already ordered properly. If the input Pawn is null,
     * returns immediately
     *
     * @param pawnToReturn - a Pawn object that should be returned to its home base
     */
    private void returnPawnToHome(Pawn pawnToReturn) {
        if (pawnToReturn == null) {
            return;
        }

        char pawnColor = pawnToReturn.getPawnColor();
        int pawnID = pawnToReturn.getPawnIdentity();

        if (pawnColor == 'B') {
            boardArr[5][pawnID - 1] = pawnToReturn;
        } else if (pawnColor == 'G') {
            boardArr[6][pawnID - 1] = pawnToReturn;
        } else if (pawnColor == 'Y') {
            boardArr[7][pawnID - 1] = pawnToReturn;
        } else {
            boardArr[8][pawnID - 1] = pawnToReturn;
        }
    }

    /**
     * This function is called when a player wants to skip their turn (such as when they are close
     * to the final base with one pawn and don't want to move at all). It doesn't alter the game
     * board at all, and simply updates the instance variables so that the next player can play.
     */
    public void skipTurn() {

        // store the current vars in the list for the undo function
        Pawn[][] preSkipBoard = createCopyArray(boardArr);
        listOfBoards.add(preSkipBoard);
        listOfDice.add(this.currDiceNum);

        this.rollDie();

        // kick over the player turn to move it one to the right
        // update player turn depending on whose turn it was previously
        if (this.playerTurn == 'B') {
            this.playerTurn = 'G';
        } else if (this.playerTurn == 'G') {
            this.playerTurn = 'Y';
        } else if (this.playerTurn == 'Y') {
            this.playerTurn = 'R';
        } else {
            this.playerTurn = 'B';
        }
    }

    /**
     * This function returns the pawn that is in the inputted row and column of the array. Assume
     * that the input position is not out of bounds.
     *
     * @param row - int representing the row of the cell to access
     * @param col - int representing the col of the cell to access
     *
     * @return The pawn object at the cell (null if no Pawn at that spot)
     */
    public Pawn getCell(int row, int col) {
        return this.boardArr[row][col];
    }

    /**
     * This function prints an input 2-D array of Pawn objects. It is only for testing purpose.
     *
     * @param pawnArr - the pawn array you want to print
     */
    public static void printPawnArray(Pawn[][] pawnArr) {
        for (int i = 0; i < pawnArr.length; i++) {
            for (int j = 0; j < pawnArr[i].length; j++) {
                if (pawnArr[i][j] != null) {
                    System.out.println(i);
                    System.out.print(j);
                    System.out.print(pawnArr[i][j].getPawnColor());
                    System.out.print(pawnArr[i][j].getPawnIdentity());
                } else {
                    System.out.println(pawnArr[i][j]);
                }
            }
        }
    }
}
