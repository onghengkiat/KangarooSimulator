package DataStructureAssignment;

import javafx.scene.image.ImageView;

import java.util.*;

public class Kangaroo {
    private String ID;
    private int foodInPouch;
    private int jumpingAbility;
    private int gender;
    private int maxFoodInPouch;
    private Area location;
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    private ImageView guiKangaroo;
    
    public Kangaroo(String ID,int jumpingAbility,Area location, int gender, int maxFoodInPouch) {
        this.ID = ID;
        this.foodInPouch = 0;
        this.location = location;
        this.jumpingAbility = jumpingAbility;
        this.gender = gender;
        this.maxFoodInPouch = maxFoodInPouch;
    }

    public String getID() {return ID; }
    public int getMaxFoodInPouch() { return maxFoodInPouch; }
    public int getFoodInPouch() {
        return foodInPouch;
    }
    public Area getLocation() {
        return location;
    }
    public int getGender() {
        return gender;
    }
    public int getJumpingAbility() { return jumpingAbility; }
    public ImageView getGuiKangaroo() {
        return guiKangaroo;
    }

    public void generateGuiKangaroo(){
        //obtains the image for the kangaroo
        ImageView image ;
        if(getGender() == Kangaroo.FEMALE){
            image = new ImageView("DataStructureAssignment/res/femaleKangaroo.png");
        }else{
            image = new ImageView("DataStructureAssignment/res/maleKangaroo.png");
        }

        //set the size of the image to be 40*40
        int imageHeight = 40;
        int imageWidth = 40;

        //when set the coordinate of the image, needs to minus imageWidth/2 and imageHeight/2
        //this is because this coordinate is the coordinate of the top left corner of the kangaroo image
        double xcoor = randomCoorX(getLocation());
        double ycoor = randomCoorY(getLocation(),xcoor);
        image.setX(xcoor-imageWidth/2);
        image.setY(ycoor-imageHeight/2);

        image.setFitHeight(imageHeight);
        image.setFitWidth(imageWidth);

        //assign the image to the variable guiKangaroo
        this.guiKangaroo = image;
    }

    public double randomCoorX(Area location){
        Random rnd = new Random();
        int area_radius = (int) location.getGuiArea().getRadius();
        double centerX = location.getGuiArea().getCenterX();

        //randomly set the x coordinate of the kangaroo in between the circle
        double coorX = rnd.nextInt(area_radius) - rnd.nextInt(area_radius) + centerX ;
        return coorX;
    }

    public double randomCoorY(Area location, double coorX){
        Random rnd = new Random();
        int area_radius = (int) location.getGuiArea().getRadius();
        double centerY = location.getGuiArea().getCenterY();
        double centerX = location.getGuiArea().getCenterX();
        /*
        formula of circle:
        (x-centerX)square + (y-centerY)square = r square
        y = square root (r square - (x-centerX)square) + centerY
        uses this formula to calculate the maximum Y coordinate when the x coordinate is xcoor
         */
        double maxY = Math.sqrt(Math.pow(area_radius,2) - Math.pow(coorX - centerX,2)) + centerY;

        //calculates the difference between max y and centerY of the circle
        double range = maxY - centerY;

        //randomly generates the ycoor
        double coorY = rnd.nextInt((int)range) - rnd.nextInt((int)range) + centerY;
        return coorY;
    }

    public void storeFood(){
        //store the food from the area
        //calculates needs how many food to fill the pouch
        int spaceInPouch = maxFoodInPouch - foodInPouch;

        //if the location it stays got food more than the amount of food required
        if (location.getFood() >= spaceInPouch){

            //straight fill the pouch
            foodInPouch += spaceInPouch;

            //and decreases the amount of food in the location
            location.takeFood(spaceInPouch);
        }else{
            //else, takes all the food in the location
            foodInPouch += location.getFood();

            //and decreases the amount fo food in the location to 0
            location.takeFood(location.getFood());
        }
    }
    
    public void eatFood(int foodNeeded){

        //if the location got enough amount of food for the kangaroo to eat
        if (location.getFood() >= foodNeeded){
            //eat the food using food in the location
            location.takeFood(foodNeeded);

        }else{//destination not enough food
            //calculate need how many food after eating the food in the location
            //decreases the amount of the food needed in the pouch
            foodInPouch -= (foodNeeded - location.getFood());

            //and decreases the amount of food in the location to 0
            location.takeFood(location.getFood());
        }
    }

    public void setLocation(Area location) {
        this.location = location;
    }

    public Path searchBestPath(){

        //initial bestPath is the bfsPath which is from itself to itself
        bfsPath bestPath = new bfsPath(getFoodInPouch(), getMaxFoodInPouch(),getLocation().getFood(),getLocation().getNumOfFemaleKangaroo());

        //gets the best path to reach all the areas connected to it
        HashMap<Area,bfsPath> bestPathForEachArea = new HashMap<>();
        Queue<bfsPath> unexplored = new LinkedList<>();
        unexplored.add(bestPath);
        bestPathForEachArea = searchBestPathToAllAreasConnected(bestPathForEachArea,unexplored);

        //search condition to look for the best destination for it:
        //1st compare the remaining food at the destination point before jumping
        //2nd if remaining food same, then compare the number of female kangaroos
        for (Map.Entry<Area,bfsPath> entry : bestPathForEachArea.entrySet()){
            if(bestPath.getFoodInArea() < entry.getValue().getFoodInArea()){
                bestPath = entry.getValue();
            }else if(bestPath.getFoodInArea () == entry.getValue().getFoodInArea()){
                if(bestPath.getFemaleInArea() < entry.getValue().getFemaleInArea()){
                    bestPath = entry.getValue();
                }
            }
        }

        if(bestPath.getSearchPath().size() == 0){
            return null;
        }else {
            return bestPath.getFirstPath();
        }
    }

    public HashMap<Area, bfsPath> searchBestPathToAllAreasConnected(HashMap<Area, bfsPath> explored, Queue <bfsPath> unexplored){
        if(!unexplored.isEmpty()) {
            //continue to iterate on this bfs_path
            bfsPath old_bfsPath = unexplored.poll();

            //get the path that connecting from destination of the bfs_path
            Path[] paths ;
            if(old_bfsPath.getSearchPath().size() == 0){
                paths = getLocation().getPath();
            }else{
                paths = old_bfsPath.getDestination().getPath();
            }

            //iterate through every path connected from the destination of bfs_path
            for (int i = 0; i < paths.length; i++) {

                //build the new bfs path by adding in this new path to the old one
                bfsPath new_bfsPath = new bfsPath(old_bfsPath, paths[i]);

                //checks if the kangaroo can jump over the path, if cannot skip it
                if(canJump(new_bfsPath) == false){
                    continue;
                }else{
                    //if the kangaroo can jump over the path
                    //calculate the food remaining in the kangaroo
                    //if the food at the area not enough for it, it has to eat the food in its pouch
                    int foodCost = paths[i].foodNeeded(new_bfsPath.getRemainingFoodForKangaroo());
                    if(foodCost > paths[i].getDestination().getFood()){
                        new_bfsPath.foodEatenForKangaroo(foodCost - paths[i].getDestination().getFood());
                    }else{
                        //if the food at the area is enough for it, checks is the pouch full of food
                        //if not, fill it
                        int remainingFoodOfTheArea = paths[i].getDestination().getFood() - foodCost;
                        int spaceInPouch = new_bfsPath.getPouchCapacity() - new_bfsPath.getRemainingFoodForKangaroo();
                        if(spaceInPouch <= remainingFoodOfTheArea){
                            new_bfsPath.fillFoodForKangaroo(spaceInPouch);
                        }else{
                            new_bfsPath.fillFoodForKangaroo(remainingFoodOfTheArea);
                        }
                    }
                }

                //get the destination area of the path
                Area area = paths[i].getDestination();

                //if the destination area has already been explored before
                //compare it with the bfs_path we obtained before and see which one having lower cost
                if (explored.containsKey(area)) {
                    //if the new one has the lower cost, continue to explore using new path
                    if (new_bfsPath.getTotalCost() < explored.get(area).getTotalCost()) {
                        explored.put(area, new_bfsPath);
                        unexplored.add(new_bfsPath);

                    }else if(new_bfsPath.getTotalCost() == explored.get(area).getTotalCost()){
                        //if they have the same food cost, but the new one has the shorter path to reach there
                        //continue to explore using new path
                        if(new_bfsPath.getPathLength() < explored.get(area).getPathLength()){
                            explored.put(area, new_bfsPath);
                            unexplored.add(new_bfsPath);
                        }
                    }//else, just stop exploring on this new_bfsPath

                } else {
                    //if the destination area has not been explored before
                    //put it as the optimum path to reach the destination
                    explored.put(area, new_bfsPath);

                    //continue the exploration on this new_bfsPath
                    unexplored.add(new_bfsPath);
                }
            }

            //recursively execute this method on the remaining unexplored path
            return searchBestPathToAllAreasConnected(explored,unexplored);

        }else{
            return explored;
        }
    }
    
    public Path jump(){
        //search for the best path
        Path path = searchBestPath();

        //if the path is null, means the kangaroo prefer to stay at original place
        if(path == null){
            return null;
        }

        //else move the kangaroo to the destination
        setLocation(path.getDestination());

        //after that, eat the food to restore energy
        eatFood(path.foodNeeded(getFoodInPouch()));

        //then store the food if the pouch of the kangaroo is not full and the destination got any remaining food
        storeFood();

        //accumulate the number of kangaroo
        path.getDestination().addKangarooNum();
        path.getSource().minusKangarooNum();

        //return the path
        return path;
    }


    
    public boolean canJump(bfsPath path){
        /*
        Some conditions set for it to be able to jump
        1) jumping ability > obstacle height
        2) foodInPouch + food in the destination must be enough to jump
        3) The destination is not full
        4) It is not female kangaroo
        5) It does not stay at colony
        */
        //would be checking for the last path in bfsPath which is newly added, and see if it is valid
        Path pathToCheck = path.getLastPath();
        return getJumpingAbility() >= pathToCheck.getObstacleHeight() &&
                (path.getRemainingFoodForKangaroo() + pathToCheck.getDestination().getFood()) >=
                        pathToCheck.foodNeeded(path.getRemainingFoodForKangaroo()) &&
                !pathToCheck.getDestination().isFull() &&
                getGender() != Kangaroo.FEMALE &&
                !getLocation().isColony();
    }

    @Override
    public String toString() {
        return  "\n\tfoodInPouch = " + foodInPouch +
                "\n\tjumpingAbility = " + jumpingAbility +
                "\n\tgender = " + gender +
                "\n\tmaxFoodInPouch = " + maxFoodInPouch +
                "\n\tlocation = " + location.getID() +
                "\n\tcolony = " + (location.isColony()? "Yes" : "No");
    }
}
