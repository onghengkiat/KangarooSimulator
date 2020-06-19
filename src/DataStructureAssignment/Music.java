package DataStructureAssignment;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Music{
    private String musicTitle, fileName;
    private Media hit ;
    private MediaPlayer mediaPlayer ;

    public Music(String musicTitle, String fileName) {
        this.musicTitle = musicTitle;
        this.fileName = fileName;
        hit = new Media(new File(fileName).toURI().toString());
        mediaPlayer = new MediaPlayer(hit);
        //keep on looping the music when it is played
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    public void play(){
        mediaPlayer.play();;
    }

    public void stop(){
        mediaPlayer.stop();
    }

    @Override
    public String toString(){
        return "Music : " + musicTitle ;
    }
}
