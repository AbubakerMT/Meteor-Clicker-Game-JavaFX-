package com.example.project;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class endController {
    public Label userTop;
    @FXML
    private Label top;

    @FXML
    private Label currentScore;

    @FXML
    private MediaView mediaView;

    private ArrayList<Integer> scores = new ArrayList<>();


    public void initialize() {
        try{
            Scanner input = new Scanner(new File("Scores.txt"));
            while(input.hasNext()){
                String line = input.nextLine();
                if(line.contains(startController.USERNAME) && line.contains(playController.getDifficulty())) {
                    String[] scoreArray = line.split(" ");
                    scores.add(Integer.valueOf(scoreArray[2]));
                }
            }
            userTop.setText(startController.USERNAME + "'s TOP 5 on " + playController.getDifficulty() + " mode: ");
            Collections.sort(scores, Collections.reverseOrder());
            String top5 = "";
            for (int i = 0; i < Math.min(5, scores.size()); i++)
                top5 = top5+scores.get(i)+"\n";
            top.setText(top5);
        }
        catch (FileNotFoundException ex){
            System.out.println("File not found!");
        }
        try {
            mediaView.setMediaPlayer(new MediaPlayer(new Media(getClass().getResource("GameOver.mp3").toURI().toString())));
            mediaView.getMediaPlayer().setVolume(startController.getSoundLevel());
        }
        catch (Exception e){
            e.getCause();
        }
        mediaView.getMediaPlayer().play();
    }

    public void setScore(String score){
        currentScore.setText(score);
    }

    public void exitHandler(){
        Platform.exit();
    }

    public void playHandler() throws IOException {
        mediaView.getMediaPlayer().stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playPane.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Main.stg.setScene(scene);
    }

    public void menuHandler() throws IOException {
        mediaView.getMediaPlayer().stop();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("startPane.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Main.stg.setScene(scene);
    }
}