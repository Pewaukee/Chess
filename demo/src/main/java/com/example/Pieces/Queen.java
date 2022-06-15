package com.example.Pieces;

import java.util.ArrayList;

public class Queen extends Piece {

    public Queen(int x, int y, String color) {
        super(x, y, color);
        if (this.color.equals("white")) {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteQueen.png");
        } else {
            setFileString("C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackQueen.png");
        }
    }
    @Override
    public ArrayList<Location> getMoves() {return moves;}

    @Override
    public void setMoves(ArrayList<Piece> pieces, Piece piece, King king, boolean lookForCheck) {
        // queen's moves are a combination of rook and bishop moves, diagonols and non-diagonols
        Bishop bishop = new Bishop(this.x, this.y, this.color);
        Rook rook = new Rook(this.x, this.y, this.color);
        ArrayList<Location> res = new ArrayList<Location>();
        bishop.setMoves(pieces, bishop, king, lookForCheck);
        rook.setMoves(pieces, rook, king, lookForCheck);
        res.addAll(bishop.getMoves());
        res.addAll(rook.getMoves());
        this.moves = res;
    }
}
