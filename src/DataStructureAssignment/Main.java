package DataStructureAssignment;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    //create a static stage so that the scene could be changed from other class without having to create a Main object
    private static Stage stage;
    private static MusicList musicList;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        //starts with the menu scene
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("fxml/login.fxml")));

        //set the title of the stage to be Kangaroo Simulator
        stage.setTitle("Kangaroo Simulator");
        stage.setScene(scene);

        //sets the stage to be not resizable
        stage.setResizable(false);

        //set the stage to be always full screen
        stage.setFullScreen(true);

        //plays the music
        musicList = new MusicList();
        musicList.play();

        //show the scene
        stage.show();

    }

    //enables switching scene from other classes
    public static void switchScene(Scene scene){
        stage.setScene(scene);
        stage.setFullScreen(true);
    }

    //enables switching scene from other classes by using fade transition
    public static void switchSceneByFading(Scene newScene, AnchorPane oldScene) {
        //creates a fade transition animation
        FadeTransition f1 = new FadeTransition();

        //set the duration of the transition to 1 second
        f1.setDuration(Duration.millis(1000));

        //set the node to occur the transition
        f1.setNode(oldScene);

        //intially solid background
        f1.setFromValue(1);

        //then at the end becomes transparent
        f1.setToValue(0);

        //after finish fading transition, changes the scene to new scene
        f1.setOnFinished((ActionEvent event) -> {
            switchScene(newScene);
        });

        //after finish configuring the fade transition
        //plays the transition
        f1.play();
    }

    public static MusicList getMusicList() {
        return musicList;
    }

    //exit the program
    public static void exit(){
        Platform.exit();
        System.exit(0);
    }

    //launch the program
    public static void main(String[] args) {
        Application.launch(args);
    }

}
