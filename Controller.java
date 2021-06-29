package com.internshala.connect4game;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {
	private static  int column=7;
	private static  int rows =6;
	private static final int circle_diameter=80;
	private static final String disc_1="#003366";
	private static final String disc_2="#006666";
	private static String player_one="Player One";
	private static String player_two="Player Two";
	private boolean isPlayerOneTurn=true;
	private boolean allowedToInsert=true;
	private Disc[][] insertedDiscArray=new Disc[rows][column];
	@FXML
	public GridPane mygridpane;
	@FXML
	public Pane diskpane;
	@FXML
	public Label playername;
	@FXML
	public TextField playeronetextfield;
	@FXML
	public TextField playertwotextfield;
	@FXML
	public Button selectnamebutton;

//createPlayground

	public void createPlayground() {
		
		selectnamebutton.setOnMouseClicked(event -> {
		selectnamebutton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String s=playeronetextfield.getText();
				playername.setText(s);
				convert();
			}
		} );
		});

		Shape rectangle = structuregrid();
		mygridpane.add(rectangle, 0, 1);
		List<Rectangle> rectangleList = clickablerectangle();
		for (Rectangle rectanglelist : rectangleList) {
			mygridpane.add(rectanglelist, 0, 1);
		}
	}

	//convert

	private void convert() {
		String user1 = playeronetextfield.getText();
			String user2 = playertwotextfield.getText();
			player_one=user1;
			player_two=user2;
	}

	//structuregrid

	public Shape structuregrid()
	{
		Shape rectangle=new Rectangle((column+1)*circle_diameter,(rows+1)*circle_diameter);
		for(int r=0;r<rows;r++)
		{
			for(int c=0;c<column;c++)
			{
				Circle circle=new Circle();
				circle.setRadius(circle_diameter/2);
				circle.setCenterX(circle_diameter/2);
				circle.setCenterY(circle_diameter/2);
				circle.setSmooth(true);
				circle.setTranslateX(c * (circle_diameter+5)+circle_diameter/4);
				circle.setTranslateY(r*( circle_diameter+5)+circle_diameter/4);
				rectangle=Shape.subtract(rectangle,circle);
			}
		}
		rectangle.setFill(Color.BLACK);
		return rectangle;
	}

	//clickablerectangle

	public List<Rectangle> clickablerectangle()
	{
		List<Rectangle> rectangleList=new ArrayList<>();
		for (int c = 0; c < column; c++) {
			Rectangle rectangle = new Rectangle(circle_diameter, (rows + 1) * circle_diameter);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(c * (circle_diameter + 5) + circle_diameter / 4);

			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
            final int col=c;
            rectangle.setOnMouseClicked(event ->{
					if(allowedToInsert){
						allowedToInsert=false;
					insertdisk(new Disc(isPlayerOneTurn), col);
					}
			});
			rectangleList.add(rectangle);
		}
		return rectangleList;
	}

	//insertdisk

	private void insertdisk(Disc disc,int column)
	{
int row=rows-1;
while(row>=0)
{
	if(getDiskIfPresent(row,column)==null)
		break;
	row--;
}
if(row<0)
	return;
		insertedDiscArray[row][column] = disc;
		diskpane.getChildren().add(disc);

		disc.setTranslateX(column* (circle_diameter+5)+circle_diameter/4);
int currentRow=row;
		TranslateTransition translateTransition= new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(row*( circle_diameter+5)+circle_diameter/4);
		translateTransition.setOnFinished(event -> {
			allowedToInsert=true;
			if(gameEnded(currentRow,column))
			{
				gameOver();
			}
			isPlayerOneTurn=!isPlayerOneTurn;
			playername.setText(isPlayerOneTurn? player_one:player_two);
		});
		translateTransition.play();
	}

	// gameEnded

	private boolean gameEnded(int row,int column)
	{
		List<Point2D> verticalPoint=IntStream.rangeClosed(row-3,row+3)
				.mapToObj(r->new Point2D(r,column))
				.collect(Collectors.toList());
		List<Point2D> horizontaPoint=IntStream.rangeClosed(column-3,column+3)
				.mapToObj(col->new Point2D(row,col))
				.collect(Collectors.toList());
		Point2D startpoint1=new Point2D(row-3,column+3);
		List<Point2D> diagonal1points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint1.add(i,-i))
				.collect(Collectors.toList());
		Point2D startpoint2=new Point2D(row-3,column-3);
		List<Point2D> diagonal2points=IntStream.rangeClosed(0,6)
				.mapToObj(i->startpoint2.add(i,i))
				.collect(Collectors.toList());
		boolean isEnded=checkCombination(verticalPoint) || checkCombination(horizontaPoint) ||  checkCombination(diagonal1points) ||  checkCombination(diagonal2points);
		return isEnded;
	}

	//ckeckCombination

	private boolean checkCombination(List<Point2D> points) {
		int chain=0;
		for(Point2D point:points) {
			int rowIndex = (int) point.getX();
			int columnIndex = (int) point.getY();
			Disc disc = getDiskIfPresent(rowIndex, columnIndex);
			if (disc != null && disc.isplayeronemove == isPlayerOneTurn) {
				chain++;
				if (chain == 4) {
					return true;
				}
				} else {
					chain = 0;
				}
			}

			return false;
	}
	//gameOver

	private void gameOver()
	{
		String winner=isPlayerOneTurn? player_one:player_two;
		System.out.println("Winner is: "+winner);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect4");
		alert.setHeaderText("Winner is: "+winner);
		alert.setContentText("Want to Play Again?");

		ButtonType yesbutton=new ButtonType("Yes");
		ButtonType nobutton=new ButtonType("No, Exit");

		alert.getButtonTypes().setAll(yesbutton,nobutton);

		Platform.runLater(()->{
					Optional<ButtonType> isclicked=alert.showAndWait();
					if(isclicked.isPresent() && isclicked.get() == yesbutton)
					{
					resetGame();
					}
					else {
						Platform.exit();
						System.exit(0);
					}
				}
				);
	}

	//resetGame

	public void resetGame() {

		playeronetextfield.clear();
		
		playertwotextfield.clear();

		selectnamebutton.setOnMouseClicked(event -> {
			String user1 = "Player_One";
			String user2 = "PlayerTwo";
			player_one=user1;
			player_two=user2;
				});
		diskpane.getChildren().clear();
		for(int row=0;row < insertedDiscArray.length;row++){
for(int col=0;col<insertedDiscArray[row].length;col++)
{
	insertedDiscArray[row][col]=null;
}
		}
		isPlayerOneTurn=true;
		playername.setText(player_one);
		createPlayground();
	}

	//getDiskIfPresent

	public Disc getDiskIfPresent(int row,int col)
{
	if(row>=rows || row<0 || col>=column || col<0)
	return null;

	return insertedDiscArray[row][col];

}

// Disc

	private static class Disc extends Circle
	{
		private final boolean isplayeronemove;
		public Disc(boolean isplayeronemove){
			this.isplayeronemove=isplayeronemove;
			setRadius(circle_diameter/2);
			setFill(isplayeronemove? Color.valueOf(disc_1):Color.valueOf(disc_2));
			setCenterX(circle_diameter/2);
			setCenterY(circle_diameter/2);
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {


	}
}