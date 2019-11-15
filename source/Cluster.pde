class Cluster{
 
  private ArrayList<Tile> tiles;
  public color clusterColor;
  
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
