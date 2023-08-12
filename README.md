# Clovece-Nezlob-Se-Game
A board game from the Czech Republic. Game logic written in Java and the UI in Java Swing.

The following is the ReadMe from the course, as is: 
===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D Arrays: I used a 2-D array to represent the board. The board is represented by a 9-row 2D
  array of Pawn objects. The first 4 rows have 4 slots and represent the final bases for the blue,
  green, yellow, and red players, respectively. The 5th row has 40 elements and represents the 40
  general board (non-base) positions. The last 4 rows have 4 slots and represent the home bases for
  each of the colors, in the same order as above. It is an appropriate use of the 2D array concept
  because many of my methods required iterating over the board in order to update positions and
  check how turns would play out. 2D arrays are useful when iterating, so the way I used it was
  an appropriate use of the concept. I used a Pawn 2-D array to allow me to access key information
  about certian positions on the board. For example, if the spot was null, I knew it had no pawn
  there, and if there was a pawn, I could access its color and identity, which were vital to
  multiple functions.

  2. Collections and/or Maps: I used 2 separate Linked Lists of 2D Pawn arrays and integers
  to allow to user to undo turns by accessing the board and die number of previous turns,
  respectively. With this feature, the board and die number from each turn could be stored in
  seqeuntial order. LinkedLists are great for this functionality because they allowed me to add
  the most recent boards/die numbers to the end of the list, which could then be popped off
  sequentially as the user undid their turns. I got feedback on my proposal suggesting that I
  implement this to store board history, which I did. I found that it was slightly strange to be
  able to undo and have a different die number than before, however, so I also included a list
  to store past die numbers.

  3. File I/O: This concept was used for my methods that allow the user to save and resume games.
  To save a game, I created a BufferedWriter that writes all necessary game data to a file, which
  is then accessed when a user presses resume, which uses a BufferedReader to read in all
  necessary game data from the saved file line by line. This feature is an appropriate use of
  the concept because it allows the backend to save information to a file unknown to the user by
  writing lines that contain the appropriate data (current turn, current board, etc.). This
  information can then be retrieved via the reading method, without the user having an idea of
  what is going on behind the scenes.

  4. JUnit Testable Component: I used JUnit tests to write multiple tests for methods in the
  CloveceNezlobSe class. Testing allowed me to ensure that certain functionality was operating
  how it is supposed to. For example, I wanted to ensure that moving a pawn into the place
  of another pawn would knock off the former, so I wrote a test that captured that scenario.
  Writing JUnit tests was especially useful because it allowed my game to be mostly functional
  by the time I implemented the GUI part. I wrote tests before writing methods. This feature
  of my implementation utilizes test-driven development, which is the main use for JUnit tests
  in this class.

===============================
=: File Structure Screenshot :=
===============================
- Include a screenshot of your project's file structure. This should include
  all of the files in your project, and the folders they are in. You can
  upload this screenshot in your homework submission to gradescope, named 
  "file_structure.png".

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

  Pawn.java: This class was used to represent the pawns in the game, which each have a color
  and identity. Storing this information in a pawn object allowed me to store 2 piece of info
  in the 2D array of the game board, which I accessed via getters in the Pawn class.

  CloveceNezlobSe: This class contains all necessary logic for the game; it is essentially the
  backend of the program. It contains all necessary methods for constructing a board, saving a
  game, resetting it, moving pawns, spawning pawns, skipping turns, updating game status, and
  checking whether the game is won yet.

  RunCloveceNezlobSe: This class is very similar to RunTicTacToe, which is the base code for the
  class. It stores all the top-level GUI components for the game, like the panels and buttons.

  TrueGameBoard: This class is very similar to GameBoard for Tic Tac Toe. It updates the status
  of the game by calling on methods in CloveceNezlobSe depending on the user input. In addition
  to containing the methods the buttons call, this class also implements the listeners necessary
  to allow a player to make moves by clicking on the board.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  I had some issues with ArrayIndexOutOfBounds exceptions and mistyping array rows/columns in
  some of my methods. It wasn't easy to catch these w/ JUnit testing, but after I implemented the
  GUI I was able to find where they were happening. I also had difficulties w/ the undo function,
  because I realized that I was removing the board and then trying to access the last element of
  list of boards, which caused null pointer exceptions when the list of boards was empty. To fix
  this, I changed the order of removing and accessing, and I also made it so that the initial state
  of the game is stored in the list of boards, which wasn't the case before.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I think the modularity of this program is pretty solid. It completely separates the game logic
  from the GUI, which was incredibly useful. I also think encapsulation is quite good, especially
  because the array of the board cannot be altered without a method like playTurn, skip, etc. This
  actually made testing kind of difficult, because I could not alter the actual board array in some
  instances, but it was necessary since being able to alter the board array would pose huge
  challenge for game logic due to null pointer exceptions if pawns are removed. If I had the chance
  to refactor, I'd consider adding a method that checks whether a turn is possible to begin with
  and then skip the turn if it's not. I actually implemented a method that did this, but I found
  it was slightly confusing for the involved players if turns jumped over certain colors. If I
  refactored, I'd consider adding this if skipping turns made the game to cumbersome.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

  I used Oracle documentation for various aspects of this assignment, including the buffered
  readers and writers, the lists, and the Swing components.
