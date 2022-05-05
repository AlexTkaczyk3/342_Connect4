//
// Author: Alexander Tkaczyk
// CS 342 Project 2: Connect 4
// ConnectFour:
/* Description:
 * Sets game logic for Connect 4
 * creates functions that:
 * 	- check for wins at a given position f.e. horizontal, vertical, etc...
 *  - look for a winner throughout entire gameboard
 *  - reset game
 */
// Java Utility/Structures Libraries
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import javafx.util.Pair;

public class ConnectFour {

	static String[][] boardPositions = new String[7][6]; // contains all current player moves
	static boolean foundWinner;
	static Stack<Pair<String, Pair<Integer, Integer>>> movesMadeStack = new Stack<Pair<String, Pair<Integer, Integer>>>();
	static Queue<String> currentMove = new LinkedList<String>();
	static String[] players = new String[] {"PlayerOne", "PlayerTwo"};
	static Integer[] winningCheckers;
	
	// constructor
	ConnectFour() {
		resetGame();
	}
	
	// validMove(int x, int y):
	// checks whether move at given positions is valid
	// returns string array containing: playerInfo, move message, validity of move, and placeholder String
	public static String[] validMove(int x, int y) {
		String player, msg, validity;
		if (boardPositions[x][y] == "Empty" && (y == 5 || boardPositions[x][y+1] != "Empty")) {
			player = currentMove.remove();
			currentMove.add(player);
			msg = player + " moved to " + x + ", " + y;
			validity = "true";
			String[] result = {player, msg, validity, "None"};
			return result;
		} else {
			player = currentMove.peek();
			msg = player + " moved to " + x + ", " + y + ". This is NOT a valid move.";
			validity = "false";
			String[] result = {player, msg, validity, "None"};
			return result;
		}
	}
	
	
	// moveMade(int x, int y):
	// sets boardPosition at specified coordinate (x,y) to name of player, which made move
	// pushes to stack all move info
	// returns updated moveMsg w/ name of winner
	public static String[] moveMade(int x, int y) {
		String[] moveMsg = validMove(x,y);
		if (moveMsg[2] == "true") {
			String player = moveMsg[0];
			boardPositions[x][y] = player;
			Pair<Integer, Integer> coordinates = new Pair<Integer, Integer> (x,y);
			Pair<String, Pair<Integer, Integer>> newMove= new Pair<String, Pair<Integer, Integer>> (player, coordinates);
			movesMadeStack.push(newMove);
		}
		moveMsg[3] = checkForWinner();
		return moveMsg;
	}
	
	// undoMove():
	// pops last move off of stack
	// returns string containing which player's move was removed and at what coordinate 
	public static String[] undoMove() { // potentially change return type
		Pair<String, Pair<Integer, Integer>> moveUndone = movesMadeStack.pop();
		String playerUndone = moveUndone.getKey();
		Pair<Integer,Integer> oldMovePosition = moveUndone.getValue();
		boardPositions[oldMovePosition.getKey()][oldMovePosition.getValue()] = "Empty";
		String player = currentMove.remove();
		currentMove.add(player);
		String[] undoInfo = {playerUndone, String.valueOf(oldMovePosition.getKey()), String.valueOf(oldMovePosition.getValue())};
		return undoInfo;
	}
	
	// horizontalWin(int x, int y):
	// compares gameboard position at [x][y] with next 3 right positions
	// • returns true: if all are equal
	// • returns true: if not equal
	public static boolean checkHorizontalWin(int x, int y) {
		if ((boardPositions[x][y] == boardPositions[x+1][y]) && (boardPositions[x][y] == boardPositions[x+2][y]) && (boardPositions[x][y] == boardPositions[x+3][y])) {
			foundWinner = true;
			return true;
		} else {
			return false;
		}
	}
	
	// verticalWin(int x, int y):
	// compares gameboard position at [x][y] with next 3 down positions
	// • returns true: if all are equal
	// • returns true: if not equal
	public static boolean checkVerticalWin(int x, int y) {
		if ((boardPositions[x][y] == boardPositions[x][y+1]) && (boardPositions[x][y] == boardPositions[x][y+2]) && (boardPositions[x][y] == boardPositions[x][y+3])) {
			foundWinner = true;
			return true;
		} else {
			return false;
		}
	}
	
	// diagonalDRWin(int x, int y):
	// compares gameboard position at [x][y] with next 3 diagonal positions (down right)
	// • returns true: if all are equal
	// • returns true: if not equal
	public static boolean checkDiagonalDRWin(int x, int y) {
		if ((boardPositions[x][y] == boardPositions[x+1][y+1]) && (boardPositions[x][y] == boardPositions[x+2][y+2]) && (boardPositions[x][y] == boardPositions[x+3][y+3])) {
			foundWinner = true;
			return true;
		} else {
			return false;
		}
	}
	
	// diagonalDLWin(int x, int y):
	// compares gameboard position at [x][y] with next 3 diagonal positions (down left)
	// • returns true: if all are equal
	// • returns true: if not equal
	public static boolean checkDiagonalDLWin(int x, int y) {
		if ((boardPositions[x][y] == boardPositions[x-1][y+1]) && (boardPositions[x][y] == boardPositions[x-2][y+2]) && (boardPositions[x][y] == boardPositions[x-3][y+3])) {
			foundWinner = true;
			return true;
		} else {
			return false;
		}
	}
	
	// checkForWinner():
	// iterates through boardPositions and for certain positions runs checkWin functions
	// if a win is found: 
	// • marks which player won
	// • passes which coordinates winning checkers were located
	// • stops iterating
	public static String checkForWinner() {
		String winner = "None";
		Integer[] winningPositions = new Integer[8];
		int counter = 0;
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 6; j++) {
				if (boardPositions[i][j] != "Empty" && foundWinner == false) {
					counter++;
					if (i < 4) {
						if (checkHorizontalWin(i,j)) {
							winningPositions[0] = i;
							winningPositions[1] = j;
							winningPositions[2] = i+1;
							winningPositions[3] = j;
							winningPositions[4] = i+2;
							winningPositions[5] = j;
							winningPositions[6] = i+3;
							winningPositions[7] = j;
							winner = boardPositions[i][j];
							break;
						}
					}
					if (j < 3) {
						if (checkVerticalWin(i,j)) {
							winningPositions[0] = i;
							winningPositions[1] = j;
							winningPositions[2] = i;
							winningPositions[3] = j+1;
							winningPositions[4] = i;
							winningPositions[5] = j+2;
							winningPositions[6] = i;
							winningPositions[7] = j+3;
							winner = boardPositions[i][j];
							break;
						}
					}
					if (i < 4 && j < 3) {
						if (checkDiagonalDRWin(i,j)) {
							winningPositions[0] = i;
							winningPositions[1] = j;
							winningPositions[2] = i+1;
							winningPositions[3] = j+1;
							winningPositions[4] = i+2;
							winningPositions[5] = j+2;
							winningPositions[6] = i+3;
							winningPositions[7] = j+3;
							winner = boardPositions[i][j];
							break;
						}
					}
					if (i > 2 && j < 3) {
						if (checkDiagonalDLWin(i,j)) {
							winningPositions[0] = i;
							winningPositions[1] = j;
							winningPositions[2] = i-1;
							winningPositions[3] = j+1;
							winningPositions[4] = i-2;
							winningPositions[5] = j+2;
							winningPositions[6] = i-3;
							winningPositions[7] = j+3;
							winner = boardPositions[i][j];
							break;
						}
					}
				}
			}
		}
		if (counter == 42) {
			winner = "Tie";
		}
		winningCheckers = winningPositions;
		return winner;
	}
	
	// resetGame():
	// initializes gameboard to empty
	void resetGame() {
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 6; j++) {
				boardPositions[i][j] = "Empty";
			}
		}
		currentMove.clear();
		currentMove.add(players[0]);
		currentMove.add(players[1]);
		foundWinner = false;
		movesMadeStack.clear();
		
	}
}
