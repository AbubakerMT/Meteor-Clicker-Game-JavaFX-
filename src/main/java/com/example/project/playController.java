package com.example.project;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import java.io.FileWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;


@SuppressWarnings("ALL")
public class playController {

    @FXML
    private Label displayScore;

    @FXML
    private Circle fallingCircle,fallingCircle1,fallingCircle2;

    @FXML
    private MediaView mediaView;

    @FXML
    ProgressBar progressBar;

    @FXML
    private Label pause;

    @FXML
    Label numberCircles;

    private Timeline animation;

    private final int circlesNom = 20;

    private int remain = circlesNom;

    private int score = 10;

    private double combo = 1;

    private static double circleSpeed = 2.5;

    private static String difficulty = "Easy";

    private int currentScore;
    private ArrayList<Integer> randomizer = new ArrayList<Integer>();


    public void initialize(){
        try {
            fallingCircle.setFill(new ImagePattern(new Image("https://cdn.discordapp.com/attachments/1008487453331574817/1105475586543059025/rocketShip.gif")));
            fallingCircle1.setFill(Color.TRANSPARENT);
            fallingCircle2.setFill(new ImagePattern(new Image("https://cdn.discordapp.com/attachments/1008487453331574817/1105475585855193219/meteor.gif")));
            fallingCircle2.setVisible(false);
            for (int i = 0; i < 8; i++) {randomizer.add(1);}
            for (int i = 0; i < 12; i++) {randomizer.add(0);}
            Collections.shuffle(randomizer);
            mediaView.setMediaPlayer(new MediaPlayer(new Media(getClass().getResource("BattleTheme.mp3").toURI().toString())));
            mediaView.getMediaPlayer().setVolume(startController.getSoundLevel());
            mediaView.getMediaPlayer().setOnEndOfMedia(() -> mediaView.getMediaPlayer().seek(Duration.ZERO));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mediaView.getMediaPlayer().play();
        fallingCircle.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(new PauseHandler());
            }
        });
        fallingCircle.setCenterX(-550 + Math.random() * 1150);
        fallingCircle.setCenterY(-300);
        animation = new Timeline( new KeyFrame (Duration.millis(circleSpeed), new AnimationHandler()) );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        progressBar.setStyle("-fx-accent: Moccasin");
        numberCircles.setText("0/"+circlesNom);
    }

    class AnimationHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if(remain == 0) {
                animation.stop();
                mediaView.getMediaPlayer().stop();
                fallingCircle.setVisible(false); // to remove the circle from the pane when the game end
                fallingCircle2.setVisible(false);
                try{
                    FileWriter input = new FileWriter("Scores.txt", true);
                    input.write(startController.USERNAME+" " +playController.getDifficulty() +" "+currentScore+"\n");
                    input.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("endPane.fxml"));
                    Parent root = loader.load();
                    endController endController = loader.getController();
                    endController.setScore(currentScore+"");
                    Scene scene = new Scene(root);
                    Main.stg.setScene(scene);
                }
                catch (Exception e){
                    e.getCause();
                }
            }
            if(fallingCircle.getCenterY() >= 900){ // when the circle reach bottom limit
                fallingCircle.setCenterX(-550 + Math.random() * 1150);
                fallingCircle.setCenterY(-50);
                remain--;
                score += 10;
                combo =1;
                progressBar.setProgress(progressBar.getProgress() + ((double) 1) / circlesNom );
                numberCircles.setText((circlesNom - remain) + "/" + circlesNom);
                animation.setRate(animation.getCurrentRate()+0.05);
                circle2Activator(randomizer.remove(0));
            }
            else { // to keep the circle in motion
                fallingCircle.setCenterY(fallingCircle.getCenterY() + 1);
            }
            if(fallingCircle2.getCenterY() >= 900 && fallingCircle2.isVisible() == true){ // when the circle reach bottom limit
                fallingCircle1.setCenterX(-550 + Math.random() * 1150);
                fallingCircle1.setCenterY(-50);
                fallingCircle2.setCenterX(fallingCircle1.getCenterX());
                fallingCircle2.setCenterY(-50);
                remain--;
                score += 10;
                combo =1;
                progressBar.setProgress(progressBar.getProgress() + ((double) 1) / circlesNom );
                numberCircles.setText((circlesNom - remain) + "/" + circlesNom);
                animation.setRate(animation.getCurrentRate()+0.05);
                fallingCircle2.setVisible(false);
            }
            else if (fallingCircle2.getCenterY() >= 900 && fallingCircle2.isVisible() == false){
                fallingCircle1.setCenterX(-550 + Math.random() * 1150);
                fallingCircle1.setCenterY(-50);
                fallingCircle2.setCenterX(fallingCircle1.getCenterX());
                fallingCircle2.setCenterY(-50);
            }
            else { // to keep the circle in motion
                fallingCircle1.setCenterY(fallingCircle1.getCenterY() + 1.5);
                fallingCircle2.setCenterY(fallingCircle2.getCenterY() + 1.5);
            }
        }

    }

    public void addScore(Circle circle , double multiplier){
        currentScore = currentScore + (int) (score*combo*multiplier) ;
        circle.setCenterY(-50);
        circle.setCenterX(-550 + Math.random() * 1150);
        score += 10;
        combo +=0.1;
        remain--;
        progressBar.setProgress(progressBar.getProgress() + ((double) 1) / circlesNom );
        numberCircles.setText((circlesNom - remain) + "/" + circlesNom);
        displayScore.setText(currentScore+"");
        animation.setRate(animation.getCurrentRate()+0.05);
        circle2Activator(randomizer.remove(0));
    }

    public void circle2Activator(int i){
        if(i == 0){}
        else if(i == 1 && !fallingCircle2.isVisible()){
            fallingCircle2.setVisible(true);
            fallingCircle1.setCenterX(-550 + Math.random() * 1150);
            fallingCircle1.setCenterY(-50);
            fallingCircle2.setCenterX(fallingCircle1.getCenterX());
            fallingCircle2.setCenterY(-50);
        }
    }
    public void addScore1(){addScore(fallingCircle , 1);}

    public void addScore2(){
        addScore(fallingCircle1, 1.5);
        fallingCircle2.setVisible(false);
    }

    class PauseHandler implements EventHandler<KeyEvent>{

        private int remainingTime = 3;

        @Override
        public void handle(KeyEvent e) {
            if(e.getCode() == KeyCode.ESCAPE ) { //associating the ESC key with the pause/play function
                if (animation.getStatus() == Animation.Status.RUNNING) {
                    animation.pause();
                    fallingCircle.setMouseTransparent(true); // to prevent the user from gaining scores while the game is paused
                    fallingCircle1.setMouseTransparent(true);
                    fallingCircle2.setMouseTransparent(true);
                    if(startController.getSoundLevel() > 0.1)
                        mediaView.getMediaPlayer().setVolume(0.05);
                    else if(startController.getSoundLevel() == 0)
                        mediaView.getMediaPlayer().setVolume(0);
                    else
                        mediaView.getMediaPlayer().setVolume(0.005);
                    pause.setText("Paused!");
                    pause.setVisible(true);
                }
                else {
                    pause.setVisible(true);
                    pause.setVisible(false);
                    animation.play();
                    mediaView.getMediaPlayer().setVolume(startController.getSoundLevel());
                    mediaView.getMediaPlayer().play();
                    fallingCircle.setMouseTransparent(false);
                    fallingCircle1.setMouseTransparent(false);
                    fallingCircle2.setMouseTransparent(false);
                }
            }
        }
    }

    public static void setCircleSpeed(double circleSpeed) {
        playController.circleSpeed = circleSpeed;
    }

    public static void setDifficulty(String difficulty) {
        playController.difficulty = difficulty;
    }

    public static String getDifficulty() {
        return difficulty;
    }

}


