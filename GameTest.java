package org.cis1200.clovecenezlobse;

import org.junit.jupiter.api.*;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * You can use this file (and others) to test your
 * implementation.
 */

public class GameTest {

    /**** resume and saveGame Tests ****/
    @Test
    public void saveAndThenResumeGameTest() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.setCurrDiceNum(4);
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(4);
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(1);
        freshBoard.playTurn(1, 2);

        // the array at this point is our expected array
        Pawn[][] expected = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        int expectedDice = freshBoard.getCurrDiceNum();
        char expectedTurn = freshBoard.getPlayerTurn();
        String expectedGameWinner = freshBoard.getGameWinner();

        freshBoard.saveGame();
        freshBoard.resumeGame();

        // check that all instance variables were preserved, except list of past boards
        assertTrue(Arrays.deepEquals(expected, freshBoard.getBoardArray()));
        assertFalse(freshBoard.getGameOver());
        assertEquals(expectedDice, freshBoard.getCurrDiceNum());
        assertEquals(expectedTurn, freshBoard.getPlayerTurn());
        assertEquals(expectedGameWinner, freshBoard.getGameWinner());
    }

    /**** reset Tests ****/
    @Test
    public void testResetAfterMultipleTurns() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.spawnPawn();
        freshBoard.spawnPawn();
        freshBoard.playTurn(1, freshBoard.getCurrDiceNum() + 1);
        freshBoard.playTurn(11, freshBoard.getCurrDiceNum() + 11);

        // reset board, which should reset all instance vars previously changed
        freshBoard.reset();

        // iterate through 1st 5 arrays, all should be null
        Pawn[][] boardAfterReset = freshBoard.getBoardArray();
        boolean allNull = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < boardAfterReset[i].length; j++) {
                if (boardAfterReset[i][j] != null) {
                    allNull = false;
                }
            }
        }
        assertTrue(allNull);

        // iterate through last 4 arrays, all should not be null b/c pawns
        boolean anyNull = false;
        for (int i = 5; i < 9; i++) {
            for (int j = 0; j < boardAfterReset[i].length; j++) {
                if (boardAfterReset[i][j] == null) {
                    anyNull = true;
                }
            }
        }
        assertFalse(anyNull);

        // check that rest of instance vars are correct
        assertFalse(freshBoard.getGameOver());
        assertEquals('B', freshBoard.getPlayerTurn());
        assertEquals(freshBoard.getGameWinner(), "");
        assertEquals(1, freshBoard.getListOfBoards().size());
    }

    @Test
    public void testResetAfterGameIsWon() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        Pawn[][] gameBoardArr = freshBoard.getBoardArray();

        // move all green pawns to their final base (row 1), which should win the game
        for (int i = 0; i < 4; i++) {
            Pawn greenPawn = gameBoardArr[6][i];
            gameBoardArr[1][i]  = greenPawn;
            gameBoardArr[6][i] = null;
        }
        freshBoard.setBoardArr(gameBoardArr);

        // reset board, which should reset all instance vars previously changed
        freshBoard.reset();

        // iterate through 1st 5 arrays, all should be null
        Pawn[][] boardAfterReset = freshBoard.getBoardArray();
        boolean allNull = true;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < boardAfterReset[i].length; j++) {
                if (boardAfterReset[i][j] != null) {
                    allNull = false;
                }
            }
        }
        assertTrue(allNull);

        // iterate through last 4 arrays, all should not be null b/c pawns
        boolean anyNull = false;
        for (int i = 5; i < 9; i++) {
            for (int j = 0; j < boardAfterReset[i].length; j++) {
                if (boardAfterReset[i][j] == null) {
                    anyNull = true;
                }
            }
        }
        assertFalse(anyNull);

        // check that the 1st pawns in the starting base arrays are of the correct color
        Pawn blueBasePawn = boardAfterReset[5][0];
        char bluePawnColor = 's';
        if (blueBasePawn != null) {
            bluePawnColor = blueBasePawn.getPawnColor();
        }
        assertEquals('B', bluePawnColor);

        Pawn greenBasePawn = boardAfterReset[6][0];
        char greenPawnColor = 's';
        if (greenBasePawn != null) {
            greenPawnColor = greenBasePawn.getPawnColor();
        }
        assertEquals('G', greenPawnColor);

        Pawn yellowBasePawn = boardAfterReset[7][0];
        char yellowPawnColor = 's';
        if (yellowBasePawn != null) {
            yellowPawnColor = yellowBasePawn.getPawnColor();
        }
        assertEquals('Y', yellowPawnColor);

        Pawn redBasePawn = boardAfterReset[8][0];
        char redPawnColor = 's';
        if (redBasePawn != null) {
            redPawnColor = redBasePawn.getPawnColor();
        }
        assertEquals('R', redPawnColor);

        // check that rest of instance vars are correct
        assertFalse(freshBoard.getGameOver());
        assertEquals('B', freshBoard.getPlayerTurn());
        assertEquals(freshBoard.getGameWinner(), "");
        assertEquals(1, freshBoard.getListOfBoards().size());
    }

    /**** checkIfWon Tests ****/
    @Test
    public void testCheckIfWonGameAlreadyOver() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setGameOver(true);
        freshBoard.setGameWinner("Blue");
        assertTrue(freshBoard.checkIfWon());
    }

    @Test
    public void testCheckIfWonGameNotWon() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        assertFalse(freshBoard.checkIfWon());

        // test that no side effects occurred
        assertFalse(freshBoard.getGameOver());
        assertTrue(freshBoard.getGameWinner().equals(""));
    }

    @Test
    public void testCheckIfWonGameNowWon() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        Pawn[][] gameBoardArr = freshBoard.getBoardArray();

        // move all green pawns to their final base (row 1), which should win the game
        for (int i = 0; i < 4; i++) {
            Pawn greenPawn = gameBoardArr[6][i];
            gameBoardArr[1][i]  = greenPawn;
            gameBoardArr[6][i] = null;
        }
        freshBoard.setBoardArr(gameBoardArr);

        assertTrue(freshBoard.checkIfWon());

        // test that side effects occurred appropriately
        assertTrue(freshBoard.getGameOver());
        assertTrue(freshBoard.getGameWinner().equals("Green"));
    }

    /**** rollDie Tests ****/
    @Test
    public void testDieIsChanging() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        int firstRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int secondRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int thirdRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int fourthRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();

        // this should almost always return false (very low probability it does not)
        boolean allEqual = (firstRoll == secondRoll && secondRoll == thirdRoll &&
                thirdRoll == fourthRoll);
        assertFalse(allEqual);
    }

    @Test
    public void testDieOnlyGeneratesOneToSix() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        int firstRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int secondRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int thirdRoll = freshBoard.getCurrDiceNum();
        freshBoard.rollDie();
        int fourthRoll = freshBoard.getCurrDiceNum();

        // this test, if passed repeatedly, gives a strong indication it's only numbers 1-6
        boolean firstIsCorrect = (firstRoll < 7 && firstRoll > 0);
        boolean secondIsCorrect = (secondRoll < 7 && secondRoll > 0);
        boolean thirdIsCorrect = (thirdRoll < 7 && thirdRoll > 0);
        boolean fourthIsCorrect = (fourthRoll < 7 && fourthRoll > 0);
        assertTrue(firstIsCorrect && secondIsCorrect && thirdIsCorrect && fourthIsCorrect);
    }

    /**** undo Tests ****/
    @Test
    public void undoListOnlyHasOneElement() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();

        // the board array and playerTurn instance vars should not change
        Pawn[][] arrayBeforeUndo = freshBoard.getBoardArray();
        char turnBeforeUndo = freshBoard.getPlayerTurn();
        freshBoard.undo();
        assertTrue(Arrays.deepEquals(arrayBeforeUndo, freshBoard.getBoardArray()));
        assertEquals(turnBeforeUndo, freshBoard.getPlayerTurn());
        assertEquals(1, freshBoard.getListOfDice().size());
    }

    @Test
    public void undoMultipleTurns() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.setCurrDiceNum(4);
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(4);
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(1);
        freshBoard.playTurn(1, 2);

        // the array at this point is our expected array
        Pawn[][] expected = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());

        freshBoard.setCurrDiceNum(1);
        freshBoard.playTurn(11,  12);
        freshBoard.undo();

        //assertArrayEquals(expected, freshBoard.getBoardArray());
        assertTrue(Arrays.deepEquals(expected, freshBoard.getBoardArray()));
        assertEquals('G', freshBoard.getPlayerTurn());
    }

    /**** writeToFile Tests ****/
    @Test
    public void writeToFileVisualTestOnly() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.saveGame();
    }

    @Test
    public void writeToFileThrowsExceptionWithNullFile() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        assertThrows(IllegalArgumentException.class, () -> freshBoard.writeToFile(null));
    }


    /**** playTurn Tests ****/
    @Test
    public void playTurnNormal() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);

        // create the expected board
        Pawn[][] expectedBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        expectedBoard[5][0] = null;
        Pawn bluePawnForTest = new Pawn('B', 1);
        expectedBoard[4][5] = bluePawnForTest;

        freshBoard.spawnPawn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(5);
        freshBoard.playTurn(1, 6);

        // check that instance vars appropriately updated
        assertEquals(6, freshBoard.getListOfBoards().size());
        assertEquals('G', freshBoard.getPlayerTurn());

        // check that board matches the expected board
        assertTrue(Arrays.deepEquals(expectedBoard, freshBoard.getBoardArray()));
    }

    @Test
    public void playTurnDifferenceTooBig() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(5);
        freshBoard.playTurn(1, 9);

        // make sure turn did not kick over
        assertEquals('B', freshBoard.getPlayerTurn());
    }

    @Test
    public void playTurnPawnOfWrongColorOtherBase() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(1);

        // alter game array so there's a blue pawn right before the red final base
        Pawn[][] gameArray = freshBoard.getBoardArray();
        gameArray[4][29] = gameArray[5][0];
        gameArray[5][0] = null;

        freshBoard.playTurn(30, 53);

        // check that instance vars did not kick over
        assertEquals('B', freshBoard.getPlayerTurn());
    }

    @Test
    public void playTurnNoPawnAtInput() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(1);
        freshBoard.playTurn(2, 3);

        // make sure turn did not kick over
        assertEquals('B', freshBoard.getPlayerTurn());
    }

    @Test
    public void playTurnPawnKnockedOffSpot() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(3);

        // create the expected board
        Pawn[][] expectedBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        expectedBoard[5][0] = null;
        expectedBoard[8][0] = null;
        Pawn bluePawnForTest = new Pawn('B', 1);
        Pawn redPawnForTest = new Pawn('R', 1);
        expectedBoard[8][0] = redPawnForTest;
        expectedBoard[4][4] = bluePawnForTest;

        // alter the original board s.t. the 1st red is 3 away from a blue
        Pawn[][] gameArray = freshBoard.getBoardArray();
        gameArray[4][4] = gameArray[8][0];
        gameArray[4][1] = gameArray[5][0];
        gameArray[5][0] = null;
        gameArray[8][0] = null;

        freshBoard.playTurn(2, 5);

        // check that instance vars appropriately updated
        assertEquals(2, freshBoard.getListOfBoards().size());
        assertEquals('G', freshBoard.getPlayerTurn());

        // check that board matches the expected board
        assertTrue(Arrays.deepEquals(expectedBoard, freshBoard.getBoardArray()));
    }

    @Test
    public void playTurnDifferenceDoesNotMatchDie() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);
        freshBoard.spawnPawn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.skipTurn();
        freshBoard.setCurrDiceNum(5);
        freshBoard.playTurn(1, 3);

        // make sure turn did not kick over
        assertEquals('B', freshBoard.getPlayerTurn());
    }

    /**** spawnPawn Tests****/
    @Test
    public void spawnPawnNotASix() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(5);
        assertFalse(freshBoard.spawnPawn());

        // make sure instance vars did not change, as turn shouldn't change
        assertEquals('B', freshBoard.getPlayerTurn());
        assertEquals(5, freshBoard.getCurrDiceNum());
    }

    @Test
    public void spawnPawnNormalNoOneInSpot() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        freshBoard.setCurrDiceNum(6);

        // store original board, then change it to expected board
        Pawn[][] expectedBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        expectedBoard[5][0] = null;
        Pawn bluePawnForTest = new Pawn('B', 1);
        expectedBoard[4][0] = bluePawnForTest;

        assertTrue(freshBoard.spawnPawn());

        // make sure instance vars change such that it's next player's turn and board is updated
        assertEquals('G', freshBoard.getPlayerTurn());
        assertEquals(2, freshBoard.getListOfBoards().size());
        //assertArrayEquals(expectedBoard, freshBoard.getBoardArray());
        assertTrue(Arrays.deepEquals(expectedBoard, freshBoard.getBoardArray()));
    }

    @Test
    public void spawnPawnOpposingPawnInSpot() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();

        // place a red pawn into the blue start spot
        Pawn redPawnInPlace = new Pawn('R', 1);
        freshBoard.getBoardArray()[4][1] = redPawnInPlace;

        freshBoard.setCurrDiceNum(6);

        // store original board, then change it to expected board
        Pawn[][] expectedBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        expectedBoard[5][0] = null;
        Pawn bluePawnForTest = new Pawn('B', 1);
        expectedBoard[4][0] = bluePawnForTest;

        assertTrue(freshBoard.spawnPawn());

        // iterate over the red base -> if any null, becomes fail
        boolean redFull = true;
        for (int i = 0; i < 4; i++) {
            if (freshBoard.getBoardArray()[8][i] == null) {
                redFull = false;
            }
        }

        assertTrue(redFull);

        // make sure instance vars change such that it's next player's turn and board is updated
        assertEquals('G', freshBoard.getPlayerTurn());
        assertEquals(2, freshBoard.getListOfBoards().size());
        //assertArrayEquals(expectedBoard, freshBoard.getBoardArray());
        assertTrue(Arrays.deepEquals(expectedBoard, freshBoard.getBoardArray()));
    }

    @Test
    public void spawnPawnNormalFriendlyPawnInSpot() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();

        // place a blue pawn into the blue start spot
        Pawn bluePawnInPlace = new Pawn('B', 2);
        freshBoard.getBoardArray()[4][1] = bluePawnInPlace;

        freshBoard.setCurrDiceNum(6);

        // store original board, then change it to expected board
        Pawn[][] expectedBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        expectedBoard[5][0] = null;
        Pawn bluePawnForTest = new Pawn('B', 1);
        expectedBoard[4][0] = bluePawnForTest;

        assertTrue(freshBoard.spawnPawn());

        // iterate over the blue base -> if null count is anything either than 1, fail
        int nullCountInBlueBase = 0;
        for (int i = 0; i < 4; i++) {
            if (freshBoard.getBoardArray()[5][i] == null) {
                nullCountInBlueBase++;
            }
        }

        assertEquals(1, nullCountInBlueBase);

        // make sure instance vars change such that it's next player's turn and board is updated
        assertEquals('G', freshBoard.getPlayerTurn());
        assertEquals(2, freshBoard.getListOfBoards().size());
        //assertArrayEquals(expectedBoard, freshBoard.getBoardArray());
        assertTrue(Arrays.deepEquals(expectedBoard, freshBoard.getBoardArray()));
    }

    @Test
    public void spawnPawnNoPawnsToSpawnIn() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();

        // make all values in blue base null
        Pawn[][] arrayRef = freshBoard.getBoardArray();
        arrayRef[5][0] = null;
        arrayRef[5][1] = null;
        arrayRef[5][2] = null;
        arrayRef[5][3] = null;

        assertFalse(freshBoard.spawnPawn());

        // make sure instance vars did not change
        assertEquals('B', freshBoard.getPlayerTurn());
        assertEquals(1, freshBoard.getListOfBoards().size());
    }

    /**** skipTurn Tests ****/
    @Test
    public void skipTurnOnlytest() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        Pawn[][] beforeSkipBoard = CloveceNezlobSe.createCopyArray(freshBoard.getBoardArray());
        freshBoard.skipTurn();
        freshBoard.skipTurn();

        // arrays should be same, turn dependent vars should've bumped over two spots
        //assertArrayEquals(beforeSkipBoard, freshBoard.getBoardArray());
        assertTrue(Arrays.deepEquals(beforeSkipBoard, freshBoard.getBoardArray()));
        assertEquals('Y', freshBoard.getPlayerTurn());
        assertEquals(3, (freshBoard.getListOfBoards().size()));
    }

    /**** getCell Tests ****/
    @Test
    public void getCellNullTest() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        assertNull(freshBoard.getCell(0, 0));
    }

    @Test
    public void getCellPawnTest() {
        CloveceNezlobSe freshBoard = new CloveceNezlobSe();
        assertFalse(null == freshBoard.getCell(5, 0));
    }
}
