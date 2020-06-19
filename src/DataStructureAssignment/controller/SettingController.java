package DataStructureAssignment.controller;

import DataStructureAssignment.Main;
import DataStructureAssignment.MusicList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SettingController implements Initializable {
    @FXML
    private AnchorPane rootSetting;

    @FXML
    private Label songName;

    private MusicList musicList;
    private Alert alert = new Alert(Alert.AlertType.NONE);

    //original index is the index of the music playing before the changes are applied
    private int originalIndex;

    //if apply == false, it means there are some changes made by havent apply it
    private boolean apply;

    private boolean playing = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        When this setting page is initialized:
        1)get the music list from the Main class
        2)get the original index of the song playing before the changes are made
        3)displays the song name of the song playing on the label text
        4)by default, apply = true because no changes are made in the beginning
         */

        musicList = Main.getMusicList();
        originalIndex = musicList.getCurrentMusicIndex();
        songName.setText(musicList.getMusicPlaying().toString());
        apply = true;
    }

    //apply the change to music playing
    public void applyChange(){
        apply = true;
        originalIndex = musicList.getCurrentMusicIndex();
    }

    public void apply(ActionEvent actionEvent) {
        ///asks for confirmation to apply the changes
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Do you want to save the change applied?");
        alert.showAndWait();

        //if confirms to apply the changes
        if (alert.getResult() == ButtonType.OK) {
            applyChange();
        }
    }

    public void next(ActionEvent actionEvent) {
        //once music playing is changed, means there are some changes
        //so apply = false
        apply = false;

        //plays the next song in the music list for the user to listen
        musicList.forward();

        //shows the name of the song on the label text
        songName.setText(musicList.getMusicPlaying().toString());
    }

    public void previous(ActionEvent actionEvent) {
        //once music playing is changed, means there are some changes
        //so apply = false
        apply = false;

        //plays the previous song in the music list for the user to listen
        musicList.back();

        //shows the name of the song on the label text
        songName.setText(musicList.getMusicPlaying().toString());
    }

    //stop or play the background music
    public void stopOrPlay(ActionEvent actionEvent) {
        if(playing){
            musicList.stop();
            playing = false;
        }else{
            musicList.play();
            playing = true;
        }
    }

    public void toSimPage(ActionEvent actionEvent) {
        //if the changes are not applied yet, ask for confirmation to apply it
        if(apply == false) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to save the change applied?");
            alert.showAndWait();

            //if confirms to apply the changes
            if (alert.getResult() == ButtonType.OK) {
                applyChange();
            }else{
                //if user does not want to apply the changes, plays back the original song
                musicList.play(originalIndex);
            }
        }

        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/mainmenu.fxml")));
            //switches from setting page to menu page which is by default the simulation mode selection page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void toHistoryPage(ActionEvent actionEvent) {
        //if the changes are not applied yet, ask for confirmation to apply it
        if(apply == false) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to save the change applied?");
            alert.showAndWait();

            //if confirms to apply the changes
            if (alert.getResult() == ButtonType.OK) {
                applyChange();
            }else{
                //if user does not want to apply the changes, plays back the original song
                musicList.play(originalIndex);
            }
        }

        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/records.fxml")));
            //switches from setting page to the history page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void toSettings(ActionEvent actionEvent) {
        //if the changes are not applied yet, ask for confirmation to apply it
        if(apply == false) {
            alert.setAlertType(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Do you want to save the change applied?");
            alert.showAndWait();

            //if confirms to apply the changes
            if (alert.getResult() == ButtonType.OK) {
                applyChange();
            }else{
                //if user does not want to apply the changes, plays back the original song
                musicList.play(originalIndex);
            }
        }

        /*
        This is needed due to some special conditions like:
        1) The user changes the song but has not apply the change
         */
        try{
            Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/settings.fxml")));
            //switches from setting page to the setting page
            Main.switchScene(newScene);
            return;
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quit(ActionEvent actionEvent) {
        //asks the user for confirmation to quit the program
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to quit the program?");
        alert.showAndWait();

        //if confirms to quit
        if (alert.getResult() == ButtonType.OK) {
            //quit the program
            Main.exit();
        }
    }

    public void logOut(ActionEvent actionEvent) {
        //asks the user for confirmation to log out from this account
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Are you sure to log out from this account?");
        alert.showAndWait();

        //if confirms to log out
        if (alert.getResult() == ButtonType.OK) {
            try {
                Scene newScene = new Scene(FXMLLoader.load(getClass().getResource("../fxml/login.fxml")));
                //Switches from setting page to login page by fading
                Main.switchSceneByFading(newScene,rootSetting);
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
