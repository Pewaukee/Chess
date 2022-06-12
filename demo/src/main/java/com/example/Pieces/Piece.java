package com.example.Pieces;

import java.util.ArrayList;

public class Piece extends Location {
    
    boolean alive;
    String color;
    ArrayList<Location> moves;
    
    public Piece(int x, int y, String color) {
        super(x, y);
        this.color = color;
        this.alive = true;
    }

    public String getColor() {return color;}
    
    public boolean isAlive() {return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}

    // these are just placement functions to be overwritten in subclasses
    public ArrayList<Location> getMoves() {return moves;}
    public void setMoves(ArrayList<Piece> pieces) {

    }
}
