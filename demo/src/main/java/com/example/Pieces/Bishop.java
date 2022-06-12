package com.example.Pieces;

import java.util.ArrayList;

public class Bishop extends Piece {

    public Bishop(int x, int y, String color) {
        super(x, y, color);
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces) {

    }
}
