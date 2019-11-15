import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.sound.*; 
import java.util.Queue; 
import java.util.LinkedList; 
import java.util.HashSet; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class MainClass extends PApplet {




Labyrinth lab;
int s;//the side of a square in the labyrinth

public void setup() {
  
  s = 20;
  stroke(255);
  strokeWeight(3);
  background(0);
  lab = new Labyrinth(width/s-2, height/s-2);
}



public void draw() {
  background(255);
  lab.drawLabyrinth();
}
class Cluster{
 
  private ArrayList<Tile> tiles;
  public int clusterColor;
  
  public Cluster(){
    tiles = new ArrayList<Tile>();
    clusterColor = color((int)random(255),(int)random(255),(int)random(255));
  }
  
  public ArrayList<Tile> getTiles(){
    return tiles;
  }
  
  public void addTile(Tile t){
    tiles.add(t);
    t.isClustered = true;
  }
  
  public void addTiles(Tile[] tilesToAdd){
    for(Tile t : tilesToAdd){
      tiles.add(t);
      t.isClustered = true;
    }
  }
  
  public int clusterLength(){
    return tiles.size();
  }
  
}
class Labyrinth{
  
  private int labWidth, labHeight;
  private Tile[][] tiles;
  private ArrayList<Cluster> clusters;
  private ArrayList<Cluster> biggestClusters;
  private ArrayList<Pawn> pawns;
  private int pawnNumber;
  
  public Labyrinth(int labWidth, int labHeight){
    pawnNumber = 5;
    this.labWidth = labWidth;
    this.labHeight = labHeight;
    tiles = new Tile[labWidth][labHeight];
    clusters = new ArrayList<Cluster>();//a cluster is a combined list of squares connected to eachother
    pawns = new ArrayList<Pawn>();
    buildLabyrinth();
  }
  
  private void buildLabyrinth(){
    //tile creation 
    for(int x = 0; x < labWidth; x++){
      for(int y = 0; y < labHeight; y++){
        tiles[x][y] = new Tile(x,y);
      }
    }
    
    //random vertical walls
    for(int x = 0; x < labWidth; x++){
      for(int y = 0; y < labHeight; y++){
        if((int)random(3)>0){
          tiles[x][y].setRight(true);
          if(x+1<labWidth)
            tiles[x+1][y].setLeft(true);
        }
        if((int)random(3)>0){
          tiles[x][y].setBottom(true);
          if(y+1<labHeight)
            tiles[x][y+1].setTop(true);
        }
      }
    }
    
    
    //labyrinth borders
    for(int x = 0; x < labWidth; x++){
      tiles[x][0].setTop(true);
    }
    for(int y = 0; y < labHeight; y++){
      tiles[0][y].setLeft(true);
    }
    
    for(int x = 0; x < labWidth; x++){
      tiles[x][labHeight-1].setBottom(true);
    }
    for(int y = 0; y < labHeight; y++){
      tiles[labWidth-1][y].setRight(true);
    }
    
    fillClusters();
    
    //get the biggest clusters to put pawns in them
    biggestClusters = new ArrayList<Cluster>();
    for(Cluster c : clusters){
      if(biggestClusters.size()<pawnNumber){
        
          biggestClusters.add(c);
        
      }
      else{
        for(int i = 0; i<biggestClusters.size(); i++){
          if(biggestClusters.get(i).clusterLength()<c.clusterLength()){
            biggestClusters.remove(i);
            biggestClusters.add(c);
            break;
          }
        }
      }
    }
    
    //add pawns to the biggest clusters
    for(Cluster cluster : biggestClusters){
      addPawn(cluster);
    }
  }
  
  private void fillClusters(){
    for(int i = 0; i<labWidth; i++){
      for(Tile t : tiles[i]){
        if(!t.isClustered){
          Cluster cluster = new Cluster();
          checkAdjacent(t, cluster);
          clusters.add(cluster);
          
        }
      }
    }
  }
  
  private void checkAdjacent(Tile t, Cluster c){
    c.addTile(t);
    if(t.getY()>0 && !t.top && !tiles[t.getX()][t.getY()-1].isClustered){
      c.addTile(tiles[t.getX()][t.getY()-1]);
      checkAdjacent(tiles[t.getX()][t.getY()-1], c);
    }
    if(t.getY()<labHeight-1 && !t.bottom && !tiles[t.getX()][t.getY()+1].isClustered){
      c.addTile(tiles[t.getX()][t.getY()+1]);
      checkAdjacent(tiles[t.getX()][t.getY()+1], c);
    }
    if(t.getX()>0 && !t.left && !tiles[t.getX()-1][t.getY()].isClustered){
      c.addTile(tiles[t.getX()-1][t.getY()]);
      checkAdjacent(tiles[t.getX()-1][t.getY()], c);
    }
    if(t.getX()<labWidth-1 && !t.right && !tiles[t.getX()+1][t.getY()].isClustered){
      c.addTile(tiles[t.getX()+1][t.getY()]);
      checkAdjacent(tiles[t.getX()+1][t.getY()], c);
    }
    
  }
  
  public void drawLabyrinth(){
    
    for(Cluster clust : clusters){
      stroke(clust.clusterColor);
      fill(clust.clusterColor);
      for(Tile t : clust.getTiles()){
        rect(t.getX()*s+s+1, t.getY()*s+s+1, s-4, s-4);
      }
    }
    
    //drawing the left and top wall of each tile that has one/both
    stroke(0);
    for(int i = 0; i<labWidth; i++){
      for(Tile t : tiles[i]){
        if(t.getTop())
        line(t.getX()*s+s, t.getY()*s+s, t.getX()*s+s*2, t.getY()*s+s);
        
        if(t.getLeft()) 
        line(t.getX()*s+s, t.getY()*s+s, t.getX()*s+s, t.getY()*s+s*2);
      }
    }
    
    //Labyrinth right and bottom border
    line(labWidth*s+s, s, labWidth*s+s, labHeight*s+s);
    line(s, labHeight*s+s, labWidth*s+s, labHeight*s+s);
    
    for(Pawn pawn : pawns){
      pawn.drawPawn();
    }
    
  }
  
  public void addPawn(Cluster cluster){
    pawns.add(new Pawn(cluster));
  }
  
}




class Pawn{
 
  
  private ArrayList<Tile> tiles;
  private int currentMapX, currentMapY, sleepTimer;
  private Queue<Tile> path;
  private Tile currentTile, targetTile;
  private int targetColor;
  
  public Pawn(Cluster cluster){
    
    tiles = cluster.getTiles();
    //how many frames until pawn movement
    sleepTimer = 30;
    targetColor = cluster.clusterColor+color(127, 127, 127);
    
    currentTile = tiles.get((int)random(tiles.size()));
    
    currentMapX = currentTile.getX()*s+s;
    currentMapY = currentTile.getY()*s+s;
    findPath();
  }
  
  public void drawPawn(){
    
    if(sleepTimer-- == 0){
      if(path.isEmpty() || currentTile == targetTile){
        findPath();
      }  
      
      if(currentMapX > path.peek().getX()*s+s){
        currentMapX--;
      } else if(currentMapX < path.peek().getX()*s+s){
        currentMapX++;
      } else if(currentMapY > path.peek().getY()*s+s){
        currentMapY--;
      } else if(currentMapY < path.peek().getY()*s+s){
        currentMapY++;
      }
      
      if(currentMapX == path.peek().getX()*s+s && currentMapY == path.peek().getY()*s+s){
        currentTile = path.peek();
        path.remove();
      }
      
      sleepTimer = 10;
    }
    
    stroke(targetColor);
    fill(targetColor);
    rect(targetTile.getX()*s+s*1.25f, targetTile.getY()*s+s*1.25f, s/2, s/2); 
    
    stroke(0);
    fill(255);
    rect(currentMapX+s/4, currentMapY+s/4, s/2, s/2); 
  }
  
  public ArrayList<Tile> getNeighbouringTiles(Tile tile){
    int x = tile.getX(), y = tile.getY();
    ArrayList<Tile> neighbouringTiles = new ArrayList<Tile>();
        
    for(Tile t : tiles){
      if( t.getX() == x && t.getY() == y+1 && !tile.getBottom() ||
          t.getX() == x && t.getY() == y-1 && !tile.getTop() || 
          t.getX() == x+1 && t.getY() == y && !tile.getRight() ||
          t.getX() == x-1 && t.getY() == y && !tile.getLeft()){
        neighbouringTiles.add(t);
      }
    }
    
    return neighbouringTiles;
  }
  
  private void retracePath(){
    ArrayList<Tile> reversedPath = new ArrayList<Tile>();
    path = new LinkedList<Tile>();
    Tile currentRetraceTile = targetTile;
    while(currentRetraceTile != currentTile){
      reversedPath.add(currentRetraceTile);
      currentRetraceTile = currentRetraceTile.parent;
    }
    
    for(int i = reversedPath.size()-1; i>=0; i--){
      path.add(reversedPath.get(i));
    }
  }
  
  private void findPath(){
    do{
      targetTile = tiles.get((int)random(tiles.size()));
    }while(targetTile == currentTile);
    
    ArrayList<Tile> openSet = new ArrayList<Tile>();
    openSet.add(currentTile);
    HashSet<Tile> closedSet = new HashSet<Tile>();
    
    while(!openSet.isEmpty()){
      Tile currentPathTile = openSet.get(0);
      for(int i = 1; i < openSet.size(); i++){
        if(openSet.get(i).fCost() < currentPathTile.fCost() || (openSet.get(i).fCost() == currentPathTile.fCost() && openSet.get(i).hCost < currentPathTile.hCost)){
          currentPathTile = openSet.get(i);
        }
      }
      
      openSet.remove(currentPathTile);
      closedSet.add(currentPathTile);
      
      if(currentPathTile == targetTile){
        retracePath();
        return;
      }
      
      for(Tile neighbour : getNeighbouringTiles(currentPathTile)){
        if(!closedSet.contains(neighbour)){
          int newMovementCost = currentPathTile.gCost + 1;
          if(newMovementCost < neighbour.gCost || !openSet.contains(neighbour)){
            neighbour.gCost = newMovementCost;
            neighbour.setHCostTo(targetTile);
            neighbour.parent = currentPathTile;
            if(!openSet.contains(neighbour)){
              openSet.add(neighbour);
            }
          }
        }
      }
    }
  }
  
}
class Tile{
  
  private int xCoord, yCoord;
  public boolean isClustered = false;
  private boolean top = false, 
          bottom = false, 
          left = false, 
          right = false;
  
  public int gCost, hCost;
  public Tile parent;
  
  public void setHCostTo(Tile tile){
    hCost = Math.abs(tile.getX() - xCoord) + Math.abs(tile.getY() - yCoord);
  }
  
  public int fCost(){
    return gCost+hCost;
  }
  
  public Tile(int x, int y){
    xCoord = x;
    yCoord = y;
  }
  
  public int getX(){
    return xCoord;
  }
  public int getY(){
    return yCoord;
  }
  public boolean getTop(){
    return top;
  }
  public boolean getBottom(){
    return bottom;
  }
  public boolean getLeft(){
    return left;
  }
  public boolean getRight(){
    return right;
  }
  
  public void setTop(boolean setTop){
    top = setTop;
  }
  public void setBottom(boolean setBottom){
    bottom = setBottom;
  }
  public void setLeft(boolean setLeft){
    left = setLeft;
  }
  public void setRight(boolean setRight){
    right = setRight;
  }
}
  public void settings() {  size(840, 640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "MainClass" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
