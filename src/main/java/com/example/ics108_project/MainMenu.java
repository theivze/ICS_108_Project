package com.example.ics108_project;

import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.io.File;
import java.util.Scanner;


public class MainMenu {
    final static MediaPlayer mediaPlayer = getMediaPlayer("SuperMario.mp3");
    private static ImageView musicImageView;
    private static final double height = Screen.getPrimary().getBounds().getHeight();
    private static final double width = Screen.getPrimary().getBounds().getWidth();
    final static Background backGround = createBackGround();
    /**
     * Creates the main menu scene with all the components found
     * The method returns the scene with the title , game buttons , music buttons and the app creator name
     * @return the main menu scene that contains all the components
     */
    static Scene mainMenuScene()
    {
        //Creating main pane
        BorderPane mainPane = new BorderPane();

        //Creating top box
        HBox topBox = new HBox();

        //Making small panes to add to the big pane at the end
        topBox.setAlignment(Pos.TOP_RIGHT);
        topBox.setSpacing(420);

        //Add music and game name to top horizontal pane
        ImageView mainImage = new ImageView("ZiadAppleLogo.png");
        mainImage.setFitHeight(350);
        mainImage.setFitWidth(350);
        ImageView musicImageView = backGroundMusic();

        HBox.setHgrow(musicImageView, Priority.ALWAYS);
        HBox.setHgrow(mainImage, Priority.ALWAYS);
        topBox.getChildren().addAll(mainImage,musicImageView);

        //Add top pane to main pane
        mainPane.setTop(topBox);



        //Set background image
        mainPane.setBackground(backGround);



        //Start Button
        Button startButton = createButton("StartButton.png",200, 110);
        startButton.setOnAction(e -> {
            mediaPlayer.seek(Duration.ZERO);
            GameClass.stage.setScene(GameApp.gameScene());
            GameApp.initiate();
            GameClass.stage.setFullScreen(true);
        });

        //Scores Button
        Button scoreButton = createButton("Score.png",200,110);
        scoreButton.setOnMouseClicked(e ->
                {
                    GameClass.stage.setScene(scoreScene());
                    GameClass.stage.setFullScreen(true);
                }

        );

        //Quit Button
        Button quitButton = createButton("QuitButton.png",180,90);
        quitButton.setOnAction(e -> Platform.exit());//Exit program when clicked

        //Add Buttons To Pane
        VBox buttons = new VBox(startButton,scoreButton,quitButton);
        mainPane.setCenter(buttons);
        buttons.setAlignment(Pos.CENTER);

        Text aboutText = new Text("Ap-FALL-E ICS 108 Project by Al Aqsa Akbar and Ziad Al-Alami");
        aboutText.setFill(Paint.valueOf("cyan"));
        aboutText.setFont(Font.font("Rockwell Extra Bold",30));
        mainPane.setBottom(aboutText);
        BorderPane.setAlignment(aboutText,Pos.BOTTOM_CENTER);


        return new Scene(mainPane);
    }

    /**
     * Creates background music for the main menu scene of the app
     * @return an ImageView object that can turn on and off the music when clicked on
     */
    static ImageView backGroundMusic()
    {
        //Plays the music and set autoplay to true with infinite repetition
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(Timeline.INDEFINITE);

        //Adding music icon to turn the music on and off
        Image musicImage = new Image("musicNoBG.png");
        Image musicOff = new Image("muteMusicNoBG.png");

        //Create an ImageView object that displays the image and the current status of the background music
        musicImageView = new ImageView(musicImage);
        musicImageView.setFitWidth(70);
        musicImageView.setFitHeight(70);
        musicImageView.setStyle("-fx-cursor: hand;");
        mediaPlayer.setVolume(0.1);

        //Turn on and off the music when clicked on
        musicImageView.setOnMouseClicked(event -> {
            if(mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING)
            {
                mediaPlayer.pause();
                musicImageView.setImage(musicOff);
            }
            else
            {
                mediaPlayer.play();
                musicImageView.setImage(musicImage);
            }
        });

        return musicImageView;
    }
    /**
     * Creates a MediaPlayer object that for any given resource in the directory
     * @return the MediaPlayer object
     */
    static MediaPlayer getMediaPlayer(String mediaName)
    {
        File file = new File("src\\main\\resources\\" + mediaName);
        Media media = new Media(file.toURI().toString());
        return new MediaPlayer(media);
    }

    /**
     * Generates a button from the image and gives it some styling
     * @param imageName the name of the image which the button is created from as in the directory
     * @param width the desired width of the button
     * @param height the desired height of the button
     * @return a Button instance with the given image , width and height properties with CSS styling
     */
    static Button createButton(String imageName, int width, int height)
    {
        ImageView imageView = new ImageView(imageName);
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        Button button = new Button("",imageView);
        button.setStyle("-fx-background-color: transparent;-fx-cursor: hand;");//Transparent
        return button;
    }

    private static Scene scoreScene()
    {
        Pane pane = new Pane();
        pane.setBackground(backGround);
        Rectangle opacityRectangle = new Rectangle();
        opacityRectangle.setFill(Color.BLACK);
        opacityRectangle.setOpacity(0.5);
        opacityRectangle.setHeight(height);
        opacityRectangle.setWidth(width);
        opacityRectangle.setX(0);
        opacityRectangle.setY(0);
        pane.getChildren().add(opacityRectangle);

        StackPane scoresBoard = new StackPane();
        Rectangle scoreRectangle = new Rectangle(650,650,Paint.valueOf("#FCBA03"));
        Scanner scoreScanner = Player.scoreFileScanner();
        assert scoreScanner != null;
        Label topScore = new Label("Top Score: " + scoreScanner.next() + "\n");
        Label secondScore = new Label("Second Top Score: " + scoreScanner.next() +"\n");
        Label thirdScore = new Label("Third Top Score: " + scoreScanner.next() + "\n");
        Label fourthScore = new Label("Fourth Top Score: " + scoreScanner.next() + "\n");
        Label fifthScore = new Label("Fifth Top Score: " + scoreScanner.next() + "\n");
        Label clearScore = new Label("Clear the scores?");
        VBox scoresBox = new VBox(topScore,secondScore,thirdScore,fourthScore,fifthScore,clearScore);
        scoresBox.setAlignment(Pos.CENTER);
        for(Node node : scoresBox.getChildren())
            ((Label) node).setFont(Font.font("Rockwell Extra Bold",40));

        scoresBoard.getChildren().addAll(scoreRectangle,scoresBox);

        Button menuButton = createButton("menu.png",200,100);
        menuButton.setOnMouseClicked(e ->
        {
        GameClass.stage.setScene(MainMenu.mainMenuScene());
        GameClass.stage.setFullScreen(true);
        });
        Button yesClear = createButton("yes.png",200,100);
        yesClear.setOnMouseClicked(e ->
        {
            Player.clearData();
            topScore.setText("Top Score: 0");
            secondScore.setText("Second Top Score: 0");
            thirdScore.setText("Third Top Score: 0");
            fourthScore.setText("Fourth Top Score: 0");
            fifthScore.setText("Fifth Top Score: 0");
        });
        HBox buttonsBox = new HBox();
        buttonsBox.getChildren().addAll(yesClear,menuButton);
        buttonsBox.setAlignment(Pos.BASELINE_CENTER);
        buttonsBox.setSpacing(200);

        VBox allNodes = new VBox(scoresBoard,buttonsBox);
        allNodes.layoutXProperty().bind(pane.widthProperty().subtract(allNodes.widthProperty()).divide(2));
        allNodes.layoutYProperty().bind(pane.heightProperty().subtract(allNodes.heightProperty()).divide(2));
        pane.getChildren().add(allNodes);

        return new Scene(pane);
    }

    static Background createBackGround()
    {
        Image image = new Image("BackGround.jpg");
        BackgroundSize backgroundSize = new BackgroundSize(1.0,1.0,true,true,false,false);
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.REPEAT,
                BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        return new Background(backgroundImage);
    }

}