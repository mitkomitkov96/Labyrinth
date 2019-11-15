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
