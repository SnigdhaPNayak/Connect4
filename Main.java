/*Name: Snigdha Priyadarsani Nayak
Date: 11-8-2020
Program: Connect Four Game
*/
package com.internshala.connect4game;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
private Controller controller;
    @Override
    public void start(Stage primaryStage) throws Exception{
      FXMLLoader loader= new FXMLLoader(getClass().getResource("game.fxml"));
        GridPane rootgridpane=loader.load();
        controller=loader.getController();
        controller.createPlayground();
        Scene scene=new Scene(rootgridpane);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Connect4");
        primaryStage.setResizable(false);
        primaryStage.show();

        Pane menuPane= (Pane) rootgridpane.getChildren().get(0);
        MenuBar menuBar=createMenu();
        menuPane.getChildren().add(menuBar);
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

    }

    //createMenu

public MenuBar createMenu()
{
      //file menu
        Menu filemenu=new Menu("File");
    MenuItem newgame=new MenuItem("New Game");
    newgame.setOnAction(event -> {

       controller.resetGame();
    });
    MenuItem resetgame=new MenuItem("Reset Game");
    resetgame.setOnAction(event -> {
        controller.resetGame();
    });
    SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
    MenuItem exitgame=new MenuItem("Exit Game");
    exitgame.setOnAction(event -> {
        exitGame();
    });
    filemenu.getItems().addAll(newgame,resetgame,separatorMenuItem,exitgame);

    //help menu
        Menu helpmenu =new Menu("Help");
    MenuItem aboutgame=new MenuItem("About Game");
    aboutgame.setOnAction(event -> {
        aboutGame();
    });
    SeparatorMenuItem separatorMenuItem1=new SeparatorMenuItem();
    MenuItem aboutme=new MenuItem("About Me");
    aboutme.setOnAction(event -> {
        aboutMe();
    });
    helpmenu.getItems().addAll(aboutgame,separatorMenuItem1,aboutme);

    //menu bar
    MenuBar menuBar=new MenuBar();
    menuBar.getMenus().addAll(filemenu,helpmenu);

    return menuBar;
}

//aboutMe

    private void aboutMe() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About The Developer");
        alert.setHeaderText("Snigdha Priyadarsani Nayak");
        alert.setContentText("Hi, I am Snigdha Priyadarsani Nayak. "+
                "I am an Engineering Student. "+
                "And I really enjoyed leaning about, how to develope the connect4 game");
        alert.show();
    }

    //aboutGame

    private void aboutGame() {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Connect4game");
        alert.setHeaderText("How to play?");
        alert.setContentText("Connect Four is a two-player connection game in which the players first choose a color and then take turns dropping colored discs from the top into a seven-column, six-row vertically suspended grid. The pieces fall straight down, occupying the next available space within the column. The objective of the game is to be the first to form a horizontal, vertical, or diagonal line of four of one's own discs. Connect Four is a solved game. The first player can always win by playing the right moves.");
        alert.show();
    }

    //exitGame

    private void exitGame() {
        Platform.exit();
        System.exit(0);
    }



    public static void main(String[] args) {

        launch(args);
    }
}
