import java.util.Queue;
import java.util.LinkedList;
import java.util.HashSet;

class Pawn{
 
  
  private ArrayList<Tile> tiles;
  private int currentMapX, currentMapY, sleepTimer;
  private Queue<Tile> path;
  private Tile currentTile, targetTile;
  private color targetColor;
  
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
    rect(targetTile.getX()*s+s*1.25, targetTile.getY()*s+s*1.25, s/2, s/2); 
    
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
