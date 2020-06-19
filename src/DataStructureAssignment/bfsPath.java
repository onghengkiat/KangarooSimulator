package DataStructureAssignment;

import java.util.LinkedList;

public class bfsPath {
    private double totalCost;
    private LinkedList <Path> searchPath;
    private int kangarooFood;
    private int pouchCapacity;
    private int foodInArea;
    private int femaleInArea;

    //constructur for the initial path which is pointing to itself
    public bfsPath(int kangarooFood, int pouchCapacity,int foodInArea, int femaleInArea){
        this.totalCost = 0;
        searchPath = new LinkedList<>();
        this.kangarooFood = kangarooFood;
        this.pouchCapacity = pouchCapacity;
        this.foodInArea = foodInArea;
        this.femaleInArea = femaleInArea;
    }

    public bfsPath(bfsPath bfs_path, Path path) {
        //calculate the total path cost
        this.totalCost = bfs_path.getTotalCost() + path.foodNeeded(bfs_path.getRemainingFoodForKangaroo());
        searchPath = new LinkedList<>();
        searchPath.addAll(bfs_path.getSearchPath());
        searchPath.add(path);
        this.foodInArea = path.getDestination().getFood();
    }

    public LinkedList<Path> getSearchPath() {
        return searchPath;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public int getPathLength(){
        return searchPath.size();
    }

    public int getFemaleInArea() {
        return femaleInArea;
    }

    public int getFoodInArea() {
        return foodInArea;
    }


    public int getRemainingFoodForKangaroo(){
        return kangarooFood;
    }

    public int getPouchCapacity() {
        return pouchCapacity;
    }

    public void foodEatenForKangaroo(int foodEaten){
        kangarooFood -= foodEaten;
    }

    public void fillFoodForKangaroo(int foodFilled){
        kangarooFood += foodFilled;
    }

    public Path getLastPath(){
        return searchPath.getLast();
    }

    public Path getFirstPath(){
        return searchPath.getFirst();
    }

    public Area getDestination(){
        return getLastPath().getDestination();
    }


}
