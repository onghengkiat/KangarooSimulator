package DataStructureAssignment;

import DataStructureAssignment.Area;
import DataStructureAssignment.Arrow;

public class Path {
    private int obstacleHeight;
    private Area source;
    private Area destination;
    private Arrow guiPath;
    private String ID;

    public Path(String ID,Area source, Area destination,int obstacleHeight) {
        this.ID = ID;
        this.source = source;
        this.destination = destination;
        this.obstacleHeight = obstacleHeight;
    }

    //calculate the food needed by the kangaroo to jump from the source to the destination
    public int foodNeeded(int foodInPouch){
        /*
        this is the formula get from the question, food required is the sum of:
        1) The obstacle height of the area
        2) 0.5*Food in the pouch of the kangaroo
        3) if the destination is colony, adds in the number of kangaroo in the destination also,
        the kangaroo has to give 1 food to every kangaroo in the destination
         */
        return (int)(getDestination().isColony()? obstacleHeight + 0.5*foodInPouch + getDestination().getNumOfKangaroo() :
                obstacleHeight +0.5*foodInPouch);
    }

    public int getObstacleHeight() {
        return obstacleHeight;
    }

    public String getID() {return ID;}
    public Area getSource() { return source; }
    public Area getDestination() { return destination; }
    public Arrow getGuiPath() { return guiPath; }
    public double getCoorDestX() { return guiPath.getCoorDestX();}
    public double getCoorDestY() { return guiPath.getCoorDestY();}

    //generates the arrow that represents the gui path
    public void generateGuiPath(){
        this.guiPath = new Arrow(source.getGuiArea().getCenterX(), source.getGuiArea().getCenterY(),
                destination.getGuiArea().getCenterX(),destination.getGuiArea().getCenterY(),
                source.getGuiArea().getRadius(), destination.getGuiArea().getRadius());
    }

    @Override
    public String toString() {
        return "Path " + getID() + ": obstacleHeight=" + obstacleHeight + ", source=" + source.getID() + ", destination=" + destination.getID();
    }
}
