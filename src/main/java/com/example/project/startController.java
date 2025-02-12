package com.example.project;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URISyntaxException;

public class startController {

    @FXML
    private Slider musicSlider;
    @FXML
    private MediaView mediaView;
    @FXML
    private TextField inputUser;

    @FXML
    private Label Error;

    static String USERNAME;

    private static double soundLevel = 0.3;


    public void initialize(){
        try {
            inputUser.setOnKeyPressed(new enterPlay());
            mediaView.setMediaPlayer(new MediaPlayer(new Media(getClass().getResource("MainMenu.mp3").toURI().toString())));
            mediaView.getMediaPlayer().setVolume(musicSlider.getValue());
            mediaView.getMediaPlayer().setOnEndOfMedia(() -> mediaView.getMediaPlayer().seek(Duration.ZERO));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaView.getMediaPlayer().play();
        musicSlider.setValue(mediaView.getMediaPlayer().getVolume());
        musicSlider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mediaView.getMediaPlayer().setVolume(musicSlider.getValue());
                soundLevel = musicSlider.getValue();
            }
        });
        inputUser.addEventFilter(KeyEvent.KEY_TYPED, e -> {
            Error.setText("");
            if (e.getCode() == KeyCode.BACK_SPACE){Error.setText("");}
            else if (inputUser.getText().length() > 10){
                e.consume();
                Error.setText("Username must be less than 10 characters");
            }
            else if (e.getCharacter().equals(" ")){
                e.consume();
                Error.setText("Username cannot contain spaces");
            }
            else if (!"0123456789abcdefghijklmnopqrstuvwxyz_\b\u007F".contains(e.getCharacter().toLowerCase())) {
                e.consume();
                Error.setText("Username must contain only letters, numbers, and underscores");
            }
        });
    }

    public void exitHandler(){
        Platform.exit();
    }

    public void playHandler() throws IOException {
        if (inputUser.getText().isEmpty()) {
            Error.setText("You must enter a username.");
        }
        else {
            USERNAME = inputUser.getText();
            mediaView.getMediaPlayer().stop();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("playPane.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Main.stg.setScene(scene);
        }
    }

    public void Easy(){
        playController.setCircleSpeed(2.5);
        playController.setDifficulty("Easy");
    }
    public void Normal(){
        playController.setCircleSpeed(2.0);
        playController.setDifficulty("Normal");
    }
    public void Hard(){
        playController.setCircleSpeed(1.75);
        playController.setDifficulty("Hard");
    }
    public void exHard(){
        playController.setCircleSpeed(1.5);
        playController.setDifficulty("Impossible");
    }
    public void howToPlay(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(new ImageView(new Image("https://cdn.discordapp.com/attachments/1008487453331574817/1105475587197390908/game.png",150,150,true,true)));
        alert.setHeaderText("");
        alert.setTitle("How to Play?");
        alert.setContentText("Welcome to our thrilling game! Your mission is to prevent a disaster by clicking on incoming objects - a rocket ship or a meteor - before they crash into the ground and explode." +
                " Your agility and quick reflexes will be put to the test as the objects fall faster and faster as you progress through the levels. With a limited number of objects," +
                " your goal is to catch as many as possible to earn maximum points and climb to the top of the leaderboard. Get ready for an adrenaline-fueled experience that will keep you on the edge of your seat! ");
        alert.show();
    }

    public void scoreSystem(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setGraphic(new ImageView(new Image("https://cdn.discordapp.com/attachments/1008487453331574817/1105475586903785523/score.png",150,150,true,true)));
        alert.setHeaderText("");
        alert.setTitle("Score System");
        alert.setContentText("""
                The scoring system in our game is based on four key elements, designed to engage and challenge users while providing a rewarding experience.\s

                1. Object Interaction:** To earn points, players must click on the objects before they make contact with the ground. Precise timing and accuracy are essential for success.

                2. Object Types:** Our game features two distinct object types, each offering a different point value. The rocket ship awards a set number of points, while the meteor provides twice the point value of the rocket ship.

                3. Combo System:** Players are incentivized to achieve consecutive successful clicks, as this will significantly boost their point totals. To illustrate the impact of this mechanic, four successive clicks can yield greater rewards than six non-consecutive clicks.

                4. Progressive Scoring:** As the game advances, the point value of each object will gradually increase. For example, a rocket ship that initially awards 15 points may later be worth 25 points as the player progresses through the game.

                Experience the excitement and test your skills in our engaging and dynamic game.""");
        alert.show();

    }

    class enterPlay implements EventHandler<KeyEvent> {

        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    playHandler();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public static double getSoundLevel() {
        return soundLevel;
    }
}


