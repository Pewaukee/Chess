package com.example.Pieces;

import java.util.ArrayList;

public class Knight extends Piece{

    public Knight(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKnight.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKnight.png");
        }
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king) {

    }
}
