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
