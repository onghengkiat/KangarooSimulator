package DataStructureAssignment;

import java.util.ArrayList;

public class MusicList {
    private ArrayList<Music> musicArrayList;
    //uses this variable to record the index of music is currently playing
    private int currentMusic;

    public MusicList(){
        this.musicArrayList = new ArrayList<>();
        currentMusic = 0;

        //adds the predefined music list
        addMusic(new Music("Wildlife Music 1","wildlife_music1.mp3"));
        addMusic(new Music("Wildlife Music 2","wildlife_music2.mp3"));
        addMusic(new Music("Wildlife Music 3","wildlife_music3.mp3"));
        addMusic(new Music("Wildlife Music 4","wildlife_music4.mp3"));
        addMusic(new Music("Wildlife Music 5","wildlife_music5.mp3"));
    }

    public void addMusic(Music music){
        musicArrayList.add(music);
    }

    //this plays the music of currentMusic index
    public void play(){
        musicArrayList.get(currentMusic).play();
    }

    //this can selectively play music of any index
    public void play(int index){
        //stop the music playing first
        stop();

        //then only plays the music
        currentMusic = index;
        musicArrayList.get(index).play();
    }

    public void stop(){
        musicArrayList.get(currentMusic).stop();
    }

    public void forward(){
        //stop the current music playing first
        musicArrayList.get(currentMusic).stop();

        //then plays the next music in the music list
        currentMusic++;

        //if the music playing is the last music in the music list, then plays the first music in the music list
        if(currentMusic >= musicArrayList.size()){
            currentMusic = 0;
        }

        //plays the music
        musicArrayList.get(currentMusic).play();
    }

    public void back(){
        //stops the current music playing first
        musicArrayList.get(currentMusic).stop();

        //then plays the previous music in the music list
        currentMusic--;

        //if the music playing is the first music in the music list, then plays the last music in the music list
        if(currentMusic < 0){
            currentMusic = musicArrayList.size() - 1;
        }

        //plays the music
        musicArrayList.get(currentMusic).play();
    }

    public int getCurrentMusicIndex() {
        return currentMusic;
    }
    public Music getMusicPlaying(){
        return musicArrayList.get(currentMusic);
    }

    @Override
    public String toString() {
        String str = "";
        int id = 0 ;
        for(Music music : musicArrayList){
            id ++;
            str += id + music.toString() + "\n";
        }
        return str;
    }
}
