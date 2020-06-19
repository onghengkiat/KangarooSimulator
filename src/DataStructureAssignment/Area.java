
package DataStructureAssignment;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Random;

public class Area {
    private int threshold;
    private int ID;
    private int maxKangaroo;
    private int pathNum;
    private int numOfKangaroo;
    private int numOfFemaleKangaroo;
    private int regenerateRate;
    private Path[] path;
    private Circle guiArea;
    private Group guiAreaWithLabel;
    private int food;
    private static PseudoMeshTopology pseudoMeshTopology;

    public Area(int ID, int food, int maxKangaroo, int pathNum, int threshold,int regenerateRate) {
        this.ID = ID;
        this.food = food;
        this.maxKangaroo = maxKangaroo;
        this.pathNum = pathNum;
        this.path = new Path[pathNum];
        this.threshold = threshold;
        this.regenerateRate = regenerateRate;
        numOfKangaroo = 0;
        numOfFemaleKangaroo = 0;
    }
    
    public int getPathNum(){
        return pathNum;
    }
    public int getFood(){
        return food;
    }
    public int getID(){
        return ID;
    }
    public int getNumOfKangaroo() { return numOfKangaroo; }
    public int getNumOfFemaleKangaroo() { return numOfFemaleKangaroo; }
    public int getThreshold() {return threshold;}
    public int getRegenerateRate() { return regenerateRate; }
    public int getMaxKangaroo() { return maxKangaroo; }
    public Path[] getPath() {
        return path;
    }
    public Circle getGuiArea(){
        return guiArea;
    }
    public Group getGuiAreaWithLabel(){
        return guiAreaWithLabel;
    }

    //decreases the amount of the food after being taken by the kangaroo
    public void takeFood(int amount){
        food -= amount;
    }

    /*
    check the area is colony or not, have to met 2 requirements:
    1) number of kangaroo more than or equal to the threshold value
    2) there is at least one female kangaroo in that area
     */
    public boolean isColony(){
        return existFemale() && numOfKangaroo >= threshold;
    }

    //check the area is full of kangaroo or not
    public boolean isFull(){
        return numOfKangaroo >= maxKangaroo;
    }

    //check is there any female kangaroo in the area
    public boolean existFemale(){ return numOfFemaleKangaroo > 0; }

    //assign the DataStructureAssignment.Path object to the DataStructureAssignment.Path array
    public void buildPath(Path path, int currentPathInput) {
        this.path[currentPathInput] = path;
    }

    //accumulator for the number of kangaroos
    public void addFemaleKangarooNum(){ numOfFemaleKangaroo ++; }
    public void addKangarooNum(){ numOfKangaroo ++; }
    public void minusKangarooNum(){ numOfKangaroo--; }

    //method for the area to regenerate food after each day is ended
    public void regenerateFood(){
        food += regenerateRate;
    }
    public void putFood(int amount){
        this.food += amount;
    }

    public void generateMeshGuiArea(){

        this.guiArea = new Circle();
        double radius = 40;

        //horizontal distance of the point from center is
        double centerX = pseudoMeshTopology.getCoordinateX(getID());
        double centerY = pseudoMeshTopology.getCoordinateY(getID());
        this.guiArea.setCenterY(centerY);
        this.guiArea.setCenterX(centerX);
        this.guiArea.setRadius(radius);

        //designs the appearance of the circle
        this.guiArea.setStrokeWidth(5);
        this.guiArea.setFill(Color.TRANSPARENT);

        //if it is colony makes its stroke filled with FORESTGREEN, else TOMATO
        if(isColony()){
            this.guiArea.setStroke(Color.FORESTGREEN);
        }else {
            this.guiArea.setStroke(Color.TOMATO);
        }

        //makes a label text on left corner of the gui area
        Text text = new Text(Integer.toString(getID()));
        text.setX(centerX - radius);
        text.setY(centerY - radius);

        //designs the text style
        text.setFont(Font.font("Arial", FontWeight.BLACK, FontPosture.REGULAR,20));
        text.setFill(Color.BLUE);

        //incorporate them into the Group object
        guiAreaWithLabel = new Group(text,guiArea);
    }

    public void generateGuiArea(int maxX, int maxY, Area [] map ,int currentNumOfArea){
        Random rnd = new Random();
        this.guiArea = new Circle();
        boolean overlapping = true;
        double centerX = 0;
        double centerY = 0;
        double radius = 40;

        //check is this Circle generated which represents the area is overlapping with other area
        //while it is overlapping, keep on randomly generates a new position for it
        while(overlapping) {
            overlapping = false;

            //randomly generates the position for the circle
            //+100 is to make sure it wont crash with the menu bar
            centerY = rnd.nextInt(maxY-(int)Math.ceil(radius) - 100) + radius + 100;
            //+100 to make sure it wont crash with the Legends
            centerX = rnd.nextInt(maxX - (int)Math.ceil(radius) - 100 ) + radius + 100;
            this.guiArea.setCenterY(centerY);
            this.guiArea.setCenterX(centerX);
            this.guiArea.setRadius(radius);

            //check is it overlapping with other areas
            for(int i = 0; i < currentNumOfArea ; i++){
                if(this.isOverlap(map[i])) {
                    overlapping = true;
                    break;
                }
            }
        }
        //designs the appearance of the circle
        this.guiArea.setFill(Color.TRANSPARENT);
        this.guiArea.setStrokeWidth(5);
        //if it is colony makes its stroke filled with FORESTGREEN, else TOMATO
        if(isColony()){
            this.guiArea.setStroke(Color.FORESTGREEN);
        }else {
            this.guiArea.setStroke(Color.TOMATO);
        }

        //makes a label text on left corner of the gui area
        Text text = new Text(Integer.toString(getID()));
        text.setX(centerX - radius);
        text.setY(centerY - radius);

        //designs the text style
        text.setFont(Font.font("Arial", FontWeight.BLACK, FontPosture.REGULAR,20));
        text.setFill(Color.BLUE);

        //incorporate them into the Group object
        guiAreaWithLabel = new Group(text,guiArea);
    }


    public boolean isOverlap(Area area){
        //hypot return square root of sum of square of the two arguments
        double distance = Math.hypot( this.getGuiArea().getCenterX() - area.getGuiArea().getCenterX()
                , this.getGuiArea().getCenterY() - area.getGuiArea().getCenterY() );

        //if the sum of the radius of the two areas are bigger than the distance between them, meaning they are not overlapping with each other
        //I add 10 on the sum of the radius, to make sure there are more spaces between the area generated
        if(distance > this.getGuiArea().getRadius() + area.getGuiArea().getRadius() + 10){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public String toString() {
        return (isColony()?"Colony ":"")+"Area - " + ID +
                "\n\tfood = " + food +
                "\n\tthreshold = " + threshold +
                "\n\tmaxKangaroo = " + maxKangaroo +
                "\n\tnumOfKangaroo = " + numOfKangaroo +
                "\n\tnumOfFemaleKangaroo = " + numOfFemaleKangaroo +
                "\n\tregenerateRate = " + regenerateRate;
    }
    
    public static void setPseudoMeshTopology(PseudoMeshTopology pseudoMeshTopology) {
        Area.pseudoMeshTopology = pseudoMeshTopology;
    }

}
