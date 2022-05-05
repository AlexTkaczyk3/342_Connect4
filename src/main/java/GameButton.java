//
// Author: Alexander Tkaczyk
// CS 342 Project 2: Connect 4
// GameButton:
/* Description:
 * Extends Button Class
 * ....
 */
//
import javafx.scene.control.Button;

public class GameButton extends Button {
	static Button button;
	private int row;
	private int column;
	
	GameButton(int x, int y) {
		button = new Button();
		row = x;
		column = y;
	}
	
	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
}
