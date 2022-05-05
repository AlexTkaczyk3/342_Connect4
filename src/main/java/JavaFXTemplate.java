//
// Author: Alexander Tkaczyk
// CS 342 Project 2: Connect 4
// JavaFXTemplate:
/* Description:
 * Extends Application
 * Inherits GameButton functionality (+game logic thru hierarchical inheritance) from GameButton class
 * .....
 */
// Java Utilities/Structures
import java.io.File;
import java.util.HashMap;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
// Stage Libraries
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Node;
// Scene Libraries
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Event Libraries
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

// Geometry Libraries
import javafx.geometry.Insets;
// Image Libraries
import javafx.geometry.Pos;

public class JavaFXTemplate extends Application {
	
	// include public structures, FXfeatures, handlers, etc....
	HashMap<String, Scene> sceneMap;
	ConnectFour mainGame = new ConnectFour();
	boolean newGame = false;
	GridPane gameBoard;
	String playerClicked;
	String theme = "Original";
	ListView<String> turnHistoryList;
	Image bgPic = new Image("empty.png");
	
	public static void main(String[] args) {
		launch(args);
	}

	// start:
	// set start scene as primary scene
	/* Start Scene:
	 * -welcomes players to the game.
	 * -only shown at launch of application.
	 * has some sort of design other than the default color and style of JavaFX.
	 * Example: In past semesters, this is a good opportunity to use images as background and play with the style of the graphical elements.
	 * 
	 * -has a button that allows the player to start playing the game.
	 * • button changes the GUI to the game play screen.
	 */
	// 
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Welcome to Connect Four!");
		theme = "Original";
		sceneMap = new HashMap<String,Scene>();
		BorderPane pane = new BorderPane();
		
		Image welcomeText = new Image("WELCOME.png");
		ImageView welcomeImageView = new ImageView(welcomeText);
		welcomeImageView.setFitWidth(300);
		welcomeImageView.setFitHeight(300);
		
		Image c4Logo = new Image("C4-logo.png");
		ImageView logoView = new ImageView(c4Logo);
		logoView.setFitHeight(500);
		logoView.setFitWidth(500);
		logoView.setPreserveRatio(true);

		Button playButton = new Button();
		Image playPic = new Image("play_button.png");
		ImageView playPicView = new ImageView(playPic);
		playPicView.setFitWidth(300);
		playPicView.setFitHeight(300);
		playPicView.setPreserveRatio(true);
		playButton.setGraphic(playPicView);
		playButton.setStyle("-fx-border-size: 1;" +  
				"-fx-background-radius: 25em;" + 
				"-fx-background-color: transparent;" +
				"-fx-text-fill: white");
		playButton.setMinSize(200, 200);
		playButton.setOnAction(e->primaryStage.setScene(sceneMap.get("gameScene")));
		
		
		VBox welcomeHolder = new VBox(-50, welcomeImageView, logoView);
		welcomeHolder.setAlignment(Pos.CENTER);
		VBox paneCenter = new VBox(-10, welcomeHolder, playButton);
		pane.setBackground(new Background(new BackgroundFill(Color.DEEPSKYBLUE, null, null)));
		paneCenter.setAlignment(Pos.CENTER);
		pane.setCenter(paneCenter);
	    
		Scene scene = new Scene(pane, 900, 750);
		scene.setFill(Color.DEEPSKYBLUE);
		scene.getRoot().setStyle("-fx-font-family: 'Kohinoor Bangla'");
		
		sceneMap.put("startScene", scene);
		sceneMap.put("gameScene", createGameScene(primaryStage));
		sceneMap.put("endScene", createEndScene("Tie", primaryStage));
		
		primaryStage.setScene(sceneMap.get("startScene"));
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/* Game Screen:
	 * main in game scene
	 * • contains: menu bar
	 * Menu Bar:
	 * • "Game Play" menu - 1 clickable menu item: "Reverse Move"
	 * 		- clicking -> undo the last move made and allow another move from that player
	 * 		- clicking N times -> undoes the previous N moves
	 * • "Themes" menu - 3 clickable menu items: "Original Theme", "Theme One", "Theme Two"
	 * 		- clicking “original theme “ -> return to the design when the game started (same as start)
	 * 			- changing colors, fonts, images, etc
	 * 			- like skin on mobile device f.e. Star Wars theme
	 * 		- clicking either “theme one” or “theme two” -> change the look and feel of GUI
	 * • "Options" menu - 3 clickable menu items: “How to Play”, “New Game” and “Exit
	 * 		- clicking “how to play” -> display some text on how to play the game
	 * 		- clicking “new game” -> end the current game, reset the board and allow a new game to be played
	 * 		- clicking “exit” -> exit and terminate the program
	 * Area displaying which players' turn
	 * Area displaying move history (Example on bottom of page 5)
	 * • which player made the move
	 * • where the move was
	 * • if it is a valid move
	 * Game board as a GridPane filled with GameButtons (Example on top of page 6)
	 * • buttons inside of GridPane must be larger than the default size -> board is large enough to see + play
	 * • buttons must start out as different color or image than default color
	 * • each players move must turn the button into a unique color or image
	 * • spacing of buttons in GridPane must be more than the default spacing
	 * • may style GridPane in any way you think enhances the look/feel of game
	 */
	public Scene createGameScene(Stage primaryStage) {
		PauseTransition pause = new PauseTransition(Duration.seconds(5));
		BackgroundSize bSize = new BackgroundSize(900, 750, false, false, true, true);
		BorderPane pane = new BorderPane();
		final Menu gamePlayMenu = new Menu("Game Play");
		MenuItem reverseMove = new MenuItem("Reverse Move");
		gamePlayMenu.getItems().add(reverseMove);
		final Menu themesMenu = new Menu("Themes");
		MenuItem originalTheme = new MenuItem("Original");
		MenuItem themeOne = new MenuItem("Theme One");
		MenuItem themeTwo = new MenuItem("Theme Two");
		themesMenu.getItems().add(originalTheme);
		themesMenu.getItems().add(themeOne);
		themesMenu.getItems().add(themeTwo);
		final Menu optionsMenu = new Menu("Options");
		MenuItem howToPlay = new MenuItem("How To Play");
		MenuItem newGame = new MenuItem("New Game");
		MenuItem exit = new MenuItem("Exit");
		optionsMenu.getItems().add(howToPlay);
		optionsMenu.getItems().add(newGame);
		optionsMenu.getItems().add(exit);
		MenuBar mainMenu = new MenuBar();
		mainMenu.getMenus().add(gamePlayMenu);
		mainMenu.getMenus().add(themesMenu);
		mainMenu.getMenus().add(optionsMenu);
		
		Text whoseTurnText = new Text();
		whoseTurnText.setText(ConnectFour.currentMove.peek() + "'s Turn");
		Text turnHistoryTitle = new Text();
		turnHistoryTitle.setText("Turn History:");
		
		turnHistoryList = new ListView<String>();
		turnHistoryList.isDisable();
		gameBoard = new GridPane();
		final Stage howToPopUp = new Stage();
		howToPopUp.initOwner(primaryStage);

		reverseMove.setOnAction(e-> {
			if (!turnHistoryList.getChildrenUnmodifiable().isEmpty()) {
				String removed = turnHistoryList.getItems().remove(turnHistoryList.getItems().size() - 1);
				System.out.println(removed.length());
				if (removed.length() == 23) {
					String[] undoInfo = ConnectFour.undoMove();
					whoseTurnText.setText(ConnectFour.currentMove.peek() + "'s Turn");
					for (Node child: gameBoard.getChildren()) {
						if (GridPane.getRowIndex(child) == Integer.parseInt(undoInfo[2]) && GridPane.getColumnIndex(child) == Integer.parseInt(undoInfo[1])) {
							((GameButton)child).setDisable(false);
							((GameButton)child).setGraphic(null);
							((GameButton)child).setStyle("-fx-background-color: lightGrey;");
						}
					}
				}
			}
		});
		
		howToPlay.setOnAction(e -> {
			howToPopUp.setTitle("How To Play");
			String howToGuide = "To win:\n" +
								"A player must get 4 of their checkers in a row\n(horizontally, vertically, diagonally)\n" + 
								"To Play:\n" +
								"Players must click on button to place checker\n" +
								"Buttons clicked should have no free space beneath them\n" +
								"i.e. bottom of board or other checker";
			Label instructions = new Label();
			instructions.setText(howToGuide);
			Button dismissButton = new Button();
			dismissButton.setText("Dismiss Game Instructions");
			dismissButton.setOnAction(f-> {
				howToPopUp.hide();
				});
            VBox popUpBox = new VBox(3, instructions, dismissButton);
            Scene popUpScene = new Scene(popUpBox, 340, 180);
            howToPopUp.setScene(popUpScene);
            popUpScene.getRoot().setStyle("-fx-font-family: 'Kohinoor Bangla'");
            popUpScene.setFill(Color.DEEPSKYBLUE);
            if (!howToPopUp.isShowing()) {
            	howToPopUp.show();
            }
		});
		exit.setOnAction(e->Platform.exit());
		
		EventHandler<ActionEvent> playerHasMoved = new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event){
				GameButton button = (GameButton)event.getSource();
				String[] moveInfo = ConnectFour.moveMade(button.getRow(), button.getColumn());
				if (moveInfo[2] == "true") {
					String playerClickee = moveInfo[0];
					if (theme == "Original") {
						if (playerClickee == "PlayerOne") {
							button.setStyle("-fx-background-color: crimson;");
						} else { // only other option being: "PlayerTwo"
							button.setStyle("-fx-background-color: gold;");
						}
					} else if (theme == "ThemeOne") {
						Image pic;
						ImageView picView;
						if (playerClickee == "PlayerOne") {
							pic = new Image("cbum.png");
						} else { // only other option being: "PlayerTwo"
							pic = new Image("noel.png");
						}
						picView = new ImageView(pic);
						picView.fitWidthProperty().bind(button.widthProperty());
						picView.fitHeightProperty().bind(button.heightProperty());
						picView.setPreserveRatio(true);
						button.setGraphic(picView);
						button.setStyle("-fx-background-color: trasnparent;");
					} else if (theme == "ThemeTwo") {
						Image pic;
						ImageView picView;
						if (playerClickee == "PlayerOne") {
							pic = new Image("mai.png");
						} else { // only other option being: "PlayerTwo"
							pic = new Image("02.png");
						}
						picView = new ImageView(pic);
						picView.fitWidthProperty().bind(button.widthProperty());
						picView.fitHeightProperty().bind(button.heightProperty());
						picView.setPreserveRatio(true);
						button.setGraphic(picView);
						button.setStyle("-fx-background-color: trasnparent;");
					}
					whoseTurnText.setText(ConnectFour.currentMove.peek() + "'s Turn");
					button.setDisable(true);
					button.setOpacity(1);
				}
				turnHistoryList.getItems().add(moveInfo[1]);
				if (moveInfo[3] != "None") {
					for (Node child: gameBoard.getChildren()) {
						((GameButton)child).setDisable(true);
						((GameButton)child).setOpacity(0.4);
						if (GridPane.getColumnIndex(child) == ConnectFour.winningCheckers[0] && GridPane.getRowIndex(child) == ConnectFour.winningCheckers[1]) {
							((GameButton)child).setOpacity(1);
						} else if (GridPane.getColumnIndex(child) == ConnectFour.winningCheckers[2] && GridPane.getRowIndex(child) == ConnectFour.winningCheckers[3]) {
							((GameButton)child).setOpacity(1);
						} else if (GridPane.getColumnIndex(child) == ConnectFour.winningCheckers[4] && GridPane.getRowIndex(child) == ConnectFour.winningCheckers[5]) {
							((GameButton)child).setOpacity(1);
						} else if (GridPane.getColumnIndex(child) == ConnectFour.winningCheckers[6] && GridPane.getRowIndex(child) == ConnectFour.winningCheckers[7]) {
							((GameButton)child).setOpacity(1);
						}
					}
					pause.setOnFinished(e-> {
						sceneMap.replace("endScene", createEndScene(moveInfo[3], primaryStage));
						primaryStage.setScene(sceneMap.get("endScene"));
					});
					pause.play();
				}
			}
		};
		
		for (int i = 0; i < 7; i ++) {
			for (int j = 0; j < 6; j++) {
				GameButton button = new GameButton(i, j);
				button.setOnAction(playerHasMoved);
				button.setStyle("-fx-background-color: lightGrey;" + "-fx-background-radius: 5;");
				button.setMinSize(80, 80);
				button.setMaxSize(80, 80);
				gameBoard.add(button, i, j);
			}
		}
		
		newGame.setOnAction(e -> {
			mainGame.resetGame();
			gameBoard.getChildren().clear();
			whoseTurnText.setText(ConnectFour.currentMove.peek() + "'s Turn");
			for (int i = 0; i < 7; i ++) {
				for (int j = 0; j < 6; j++) {
					GameButton button = new GameButton(i, j);
					button.setOnAction(playerHasMoved);
					button.setStyle("-fx-background-color: lightGrey;" + "-fx-background-radius: 5;");
					button.setMinSize(80, 80);
					gameBoard.add(button, i, j);
				}
			}
			turnHistoryList.getItems().clear();
			});
		
		mainGame.resetGame();
		gameBoard.setHgap(10.0);
		gameBoard.setVgap(10.0);
		gameBoard.setPadding(new Insets(20));
		turnHistoryList.setPrefSize(325, 550);
		if (theme == "Original") {
			pane.setStyle("-fx-background-color: deepSkyBlue;" + "-fx-font-family: 'Kohinoor Bangla'");
			whoseTurnText.setFill(Color.BLACK);
			turnHistoryTitle.setFill(Color.BLACK);
		} else if (theme == "ThemeOne") {
			bgPic = new Image("emptyGym.jpg");
			pane.setBackground(new Background(new BackgroundImage(bgPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			pane.setStyle("-fx-font-family: 'Kohinoor Bangla'");
			whoseTurnText.setFill(Color.WHITE);
			turnHistoryTitle.setFill(Color.WHITE);
		} else {
			bgPic = new Image("themeTwoBG.jpeg");
			pane.setBackground(new Background(new BackgroundImage(bgPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			pane.setStyle("-fx-font-family: 'Kohinoor Bangla'");
			whoseTurnText.setFill(Color.WHITE);
			turnHistoryTitle.setFill(Color.WHITE);
		}
		whoseTurnText.setStyle("-fx-font-size: 25;");
		turnHistoryTitle.setStyle("-fx-font-size: 25;");
		mainMenu.setStyle("-fx-font-size: 20;");
		VBox paneCenter = new VBox(1, gameBoard);
		VBox paneTop = new VBox(2, mainMenu, whoseTurnText);
		VBox paneRight = new VBox(2, turnHistoryTitle, turnHistoryList);
		pane.setCenter(paneCenter);
		pane.setTop(paneTop);
		pane.setRight(paneRight);
		paneCenter.setAlignment(Pos.CENTER);
		paneTop.setAlignment(Pos.CENTER);
		paneRight.setAlignment(Pos.CENTER_LEFT);
		paneRight.setPadding(new Insets(10));
		gameBoard.setStyle("-fx-background-color: deepSkyBlue;" + "-fx-border-color: blue;" + "-fx-background-radius: 5;");
		originalTheme.setOnAction(e -> {
			theme = "Original";
			bgPic = new Image("BGcolor.png");
			pane.setBackground(new Background(new BackgroundImage(bgPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			pane.setStyle("-fx-background-color: deepSkyBlue;" + "-fx-font-family: 'Kohinoor Bangla'");
			whoseTurnText.setFill(Color.BLACK);
			turnHistoryTitle.setFill(Color.BLACK);
			for (Node child : gameBoard.getChildren()) {
				if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerOne") {
					((GameButton)child).setStyle("-fx-background-color: crimson;");
				} else if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerTwo") {
					((GameButton)child).setStyle("-fx-background-color: gold;");
				}
				((GameButton)child).setGraphic(null);
			}
		});
		themeOne.setOnAction(e -> {
			theme = "ThemeOne";
			bgPic = new Image("emptyGym.jpg");
			pane.setBackground(new Background(new BackgroundImage(bgPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			whoseTurnText.setFill(Color.WHITE);
			turnHistoryTitle.setFill(Color.WHITE);
			Image pic;
			ImageView picView;
			for (Node child : gameBoard.getChildren()) {
				((GameButton)child).setStyle("-fx-background-color: lightGrey;");
				if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerOne") {
					pic = new Image("cbum.png");
				} else if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerTwo") {
					pic = new Image("noel.png");
				} else {
					pic = null;
				}
				if (pic != null) {
					((GameButton)child).setStyle("-fx-background-color: transperent;");
				}
				picView = new ImageView(pic);
				picView.fitWidthProperty().bind(((GameButton)child).widthProperty());
				picView.fitHeightProperty().bind(((GameButton)child).heightProperty());
				((GameButton)child).setGraphic(picView);
			}
		});
		themeTwo.setOnAction(e -> {
			theme = "ThemeTwo";
			bgPic = new Image("themeTwoBG.jpeg");
			pane.setBackground(new Background(new BackgroundImage(bgPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			whoseTurnText.setFill(Color.WHITE);
			turnHistoryTitle.setFill(Color.WHITE);
			Image pic;
			ImageView picView;
			for (Node child : gameBoard.getChildren()) {
				((GameButton)child).setStyle("-fx-background-color: lightGrey;");
				if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerOne") {
					pic = new Image("mai.png");
				} else if (ConnectFour.boardPositions[GridPane.getColumnIndex(child)][GridPane.getRowIndex(child)] == "PlayerTwo") {
					pic = new Image("02.png");
				} else {
					pic = null;
				}
				if (pic != null) {
					((GameButton)child).setStyle("-fx-background-color: transperent;");
				}
				picView = new ImageView(pic);
				picView.fitWidthProperty().bind(((GameButton)child).widthProperty());
				picView.fitHeightProperty().bind(((GameButton)child).heightProperty());
				picView.setPreserveRatio(true);
				((GameButton)child).setGraphic(picView);
			}
		});
		return new Scene(pane, 1000, 750);
	}
	
	/* End Scene:
	 * Scene displayed when a player wins the game or there is a tie. 
	 * This scene will have three elements:
	 * • A message announcing who won the game or if there was a tie game 
	 * • A button to play another game
	 * • A button to exit the program
	 */
	public Scene createEndScene(String result, Stage primaryStage) {
		BorderPane pane = new BorderPane();
		pane.setPrefSize(900, 750);
		Button playAgainButton = new Button();
		playAgainButton.setOnAction(e -> {
			mainGame.resetGame();
			sceneMap.replace("gameScene", createGameScene(primaryStage));
			primaryStage.setScene(sceneMap.get("gameScene"));
			turnHistoryList.getItems().clear();
			});
		Button exitButton = new Button();
		exitButton.setOnAction(e->Platform.exit());

		playAgainButton.setMinSize(300, 100);
		playAgainButton.setMaxSize(300, 100);
		playAgainButton.setStyle("-fx-border-size: 10;" + 
				"-fx-background-radius: 5em;" + 
				"-fx-background-color: transparent;");
		exitButton.setMinSize(300, 100);
		exitButton.setMaxSize(300, 100);
		exitButton.setStyle("-fx-border-size: 10;" + 
				"-fx-background-radius: 5em;" + 
				"-fx-background-color: transparent;");
		
		Image playAgainPic = new Image("empty.png");
		ImageView playAgainPicView = new ImageView(playAgainPic);
		
		Image exitPic = new Image("exit.png");
		ImageView exitPicView = new ImageView(exitPic);

		Label whoWon = new Label(result + " won");
		whoWon.setFont(new Font("Kohinoor Bangla", 40));
		Image endPic = new Image("empty.png");
		ImageView endImageView = new ImageView(endPic);
		BackgroundSize bSize = new BackgroundSize(900, 750, false, false, true, true);
		
		if (theme == "Original") {
			playAgainPic = new Image("replay.png");
			playAgainPicView.setImage(playAgainPic);
			exitPic = new Image("exit.png");
			exitPicView.setImage(exitPic);
			if (result == "PlayerOne") {
				endPic = new Image("playerOneWins.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				pane.setStyle("-fx-background-color: linear-gradient(to bottom, #00BFFF, #DC143C);");
			} else if (result == "PlayerTwo") {
				endPic = new Image("playerTwoWins.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				pane.setStyle("-fx-background-color: linear-gradient(to bottom, #00BFFF, #FFD700);");
			} else {
				endPic = new Image("tieGame.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(300);
				endImageView.setFitHeight(300);
				endImageView.setPreserveRatio(true);
				pane.setStyle("-fx-background-color: deepSkyBlue");
			}
			playAgainPicView.fitWidthProperty().bind(playAgainButton.widthProperty());
			playAgainPicView.fitHeightProperty().bind(playAgainButton.heightProperty());
			playAgainPicView.setPreserveRatio(true);
			exitPicView.fitWidthProperty().bind(exitButton.widthProperty());
			exitPicView.fitHeightProperty().bind(exitButton.heightProperty());
			exitPicView.setPreserveRatio(true);
			exitButton.setGraphic(exitPicView);
			playAgainButton.setGraphic(playAgainPicView);
			VBox paneCenter = new VBox(3, endImageView, playAgainButton, exitButton);
			pane.setCenter(paneCenter);
			paneCenter.setAlignment(Pos.CENTER);
			VBox.setMargin(endImageView, new Insets(25,25,25,25));
			VBox.setMargin(playAgainButton, new Insets(25,25,25,25));
			VBox.setMargin(exitButton, new Insets(25,25,25,25));
		} else if (theme == "ThemeOne") {
			playAgainPic = new Image("replayT1.png");
			playAgainPicView.setImage(playAgainPic);
			exitPic = new Image("exitT1.png");
			exitPicView.setImage(exitPic);
			endPic = new Image("emptyGym.jpg");
			pane.setBackground(new Background(new BackgroundImage(endPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			if (result == "PlayerOne") {
				endPic = new Image("player1T1.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				whoWon.setTextFill(Color.CRIMSON);
			} else if (result == "PlayerTwo") {
				endPic = new Image("player2T1.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				whoWon.setTextFill(Color.GOLD);
			} else {
				whoWon.setTextFill(Color.DEEPSKYBLUE);
				whoWon.setText("Tie!");
				endPic = new Image("tieT1.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
			}
			playAgainPicView.fitWidthProperty().bind(playAgainButton.widthProperty());
			playAgainPicView.fitHeightProperty().bind(playAgainButton.heightProperty());
			playAgainPicView.setPreserveRatio(true);
			exitPicView.fitWidthProperty().bind(exitButton.widthProperty());
			exitPicView.fitHeightProperty().bind(exitButton.heightProperty());
			exitPicView.setPreserveRatio(true);
			exitButton.setGraphic(exitPicView);
			playAgainButton.setGraphic(playAgainPicView);
			HBox paneCenter = new HBox(3, playAgainButton, exitButton);
			paneCenter.setAlignment(Pos.BOTTOM_CENTER);
			VBox mainPane = new VBox(3, whoWon, endImageView, paneCenter);
			pane.setCenter(mainPane);
			mainPane.setAlignment(Pos.CENTER);
			VBox.setMargin(endImageView, new Insets(25,25,25,25));
			VBox.setMargin(playAgainButton, new Insets(25,25,25,25));
			VBox.setMargin(exitButton, new Insets(25,25,25,25));
		} else { // can only be "ThemeTwo"
			playAgainPic = new Image("replayT2.png");
			playAgainPicView.setImage(playAgainPic);
			exitPic = new Image("exitT2.png");
			exitPicView.setImage(exitPic);
			endPic = new Image("themeTwoBG.jpeg");
			pane.setBackground(new Background(new BackgroundImage(endPic, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, bSize)));
			if (result == "PlayerOne") {
				endPic = new Image("player1T2.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				whoWon.setTextFill(Color.CRIMSON);
			} else if (result == "PlayerTwo") {
				endPic = new Image("player2T2.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
				whoWon.setTextFill(Color.GOLD);
			} else {
				whoWon.setTextFill(Color.DEEPSKYBLUE);
				whoWon.setText("Tie!");
				endPic = new Image("tieT2.png");
				endImageView.setImage(endPic);
				endImageView.setFitWidth(600);
				endImageView.setFitHeight(500);
				endImageView.setPreserveRatio(true);
			}
			playAgainPicView.fitWidthProperty().bind(playAgainButton.widthProperty());
			playAgainPicView.fitHeightProperty().bind(playAgainButton.heightProperty());
			playAgainPicView.setPreserveRatio(true);
			exitPicView.fitWidthProperty().bind(exitButton.widthProperty());
			exitPicView.fitHeightProperty().bind(exitButton.heightProperty());
			exitPicView.setPreserveRatio(true);
			exitButton.setGraphic(exitPicView);
			playAgainButton.setGraphic(playAgainPicView);
			HBox paneCenter = new HBox(3, playAgainButton, exitButton);
			paneCenter.setAlignment(Pos.BOTTOM_CENTER);
			VBox mainPane = new VBox(3, whoWon, endImageView, paneCenter);
			pane.setCenter(mainPane);
			mainPane.setAlignment(Pos.CENTER);
			VBox.setMargin(endImageView, new Insets(25,25,25,25));
			VBox.setMargin(playAgainButton, new Insets(25,25,25,25));
			VBox.setMargin(exitButton, new Insets(25,25,25,25));
		}
		
		return new Scene(pane, 900, 750);
	}
}
