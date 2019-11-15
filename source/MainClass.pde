import processing.sound.*;


Labyrinth lab;
int s;//the side of a square in the labyrinth

void setup() {
  size(840, 640);
  s = 20;
  stroke(255);
  strokeWeight(3);
  background(0);
  lab = new Labyrinth(width/s-2, height/s-2);
}



void draw() {
  background(255);
  lab.drawLabyrinth();
}
