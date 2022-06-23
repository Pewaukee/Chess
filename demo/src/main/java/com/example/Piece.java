package com.example;

import java.util.ArrayList;

public class Piece extends Location {

    protected boolean alive;
    protected String color;
    protected ArrayList<Location> moves;
    protected String filename;
    protected String type;
    protected boolean check;
    protected boolean hasMoved;

    public Piece(int x, int y, String color, String type, String filename) {
        super(x, y);
        this.color = color;
        this.alive = true;
        this.type = type;

        // the hasMoved variable is for castling
        if (type.equals("king")) { // if king, set the check variable
            this.check = false;
            this.hasMoved = false;
        }
        if (type.equals("rook")) {this.hasMoved = false;}

        this.filename = filename;
        this.moves = new ArrayList<Location>();
    }

    public String getColor() {return color;}

    public String getFileString() {return this.filename;}
    public void setFileString(String filename) {this.filename = filename;}

    public boolean isAlive() {return alive;}
    public void setAlive(boolean alive) {this.alive = alive;}

    public String getType() {return type;}

    public ArrayList<Location> getMoves() {return moves;}

    public void addMove(ArrayList<Piece> pieces, Piece king, Location location) { // only for special en passant move
        int x = this.x;
        int y = this.y;
        this.x = location.x;
        this.y = location.y;
        if (!inCheck(pieces, king)) {
            this.moves.add(location);
        }
        this.x = x;
        this.y = y;

    }

    private void setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck, boolean flipped) {
        ArrayList<Location> res = new ArrayList<Location>();
        String otherColor = piece.color.equals("white") ? "black": "white";
        setFlippedPawnMoves(pieces, res, otherColor);
    }
    public void setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck) {
        ArrayList<Location> res = new ArrayList<Location>();
        String otherColor = piece.color.equals("white") ? "black": "white";
        switch (piece.type) {
            case "pawn": // set pawn moves
                pawnMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "knight": // set knight moves
                knightMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "bishop": // set bishop moves
                bishopMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "rook": // set rook moves
                rookMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "queen": // set queen moves
                queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "king": // set king moves
                kingMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            default:
                System.out.println("this shouldn't happen ever");
                break;
        }
    }

    private ArrayList<Location> setFlippedPawnMoves(ArrayList<Piece> pieces, ArrayList<Location> res, String otherColor) {
        //check diagonal takes when the board is flipped
        Location downRight = new Location(x+1, y+1);
        if (isOccupied(pieces, downRight) != null) {helper(pieces, downRight, res, otherColor);}
        Location downLeft = new Location(x-1, y+1);
        if (isOccupied(pieces, downLeft) != null) {helper(pieces, downLeft, res, otherColor);}
        this.moves = res;
        return res;
    }

    private ArrayList<Location> pawnMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck,  
        ArrayList<Location> res, String otherColor) {

        //up 1
        Location upOne = new Location(x, y-1);
        if (y > 0 && isOccupied(pieces, upOne) == null) {
            res.add(upOne);
            //up 2 and at starting position
            Location upTwo = new Location(x, y-2); // can only move 2 up if can move one up
            if (y == 6 && isOccupied(pieces, upTwo) == null) {res.add(upTwo);}
        }
        //check diagonals
        Location upRight = new Location(x+1, y-1);
        if (isOccupied(pieces, upRight) != null) {helper(pieces, upRight, res, otherColor);}
        Location upLeft = new Location(x-1, y-1);
        if (isOccupied(pieces, upLeft) != null) {helper(pieces, upLeft, res, otherColor);}
        
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;  
        
    }

    private ArrayList<Location> knightMoves(ArrayList<Piece> pieces, Piece piece, Piece king,
     boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        Location location;

        // up 1, left 2
        location = new Location(x-2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, left 1
        location = new Location(x-1, y-2);
        helper(pieces, location, res, otherColor);

        // up 1, right 2
        location = new Location(x+2, y-1);
        helper(pieces, location, res, otherColor);

        // up 2, right 1
        location = new Location(x+1, y-2);
        helper(pieces, location, res, otherColor);

        // down 1, left 2
        location = new Location(x-2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, left 1
        location = new Location(x-1, y+2);
        helper(pieces, location, res, otherColor);

        // down 1, right 2
        location = new Location(x+2, y+1);
        helper(pieces, location, res, otherColor);

        // down 2, right 1
        location = new Location(x+1, y+2);
        helper(pieces, location, res, otherColor);

        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;
    }

    private ArrayList<Location> bishopMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        boolean upRight, upLeft, downRight, downLeft;
        upRight = upLeft = downRight = downLeft = true;
        Location location;
        
        for (int i = 1; i <= 7; i++) {
            // up and right
            if (upRight) {
                location = new Location(x+i, y-i);
                if (!helper(pieces, location, res, otherColor)) {upRight = false;}
            }

            // up and left
            if (upLeft) {
                location = new Location(x-i, y-i);
                if (!helper(pieces, location, res, otherColor)) {upLeft = false;}
            }

            // down and right
            if (downRight) {
                location = new Location(x+i, y+i);
                if (!helper(pieces, location, res, otherColor)) {downRight = false;}
            }

            // down and left
            if (downLeft) {
                location = new Location(x-i, y+i);
                if (!helper(pieces, location, res, otherColor)) {downLeft = false;}
            }
        }

        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
        }
        this.moves = res;
        return res;
        
    }   

    private ArrayList<Location> rookMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
            Location location;
            boolean up, down, left, right;
            up = down = left = right = true;
            for (int i = 1; i <= 7; i++) {
                // up
                if (up) {
                    location = new Location(x, y-i);
                    if (!helper(pieces, location, res, otherColor)) {up = false;}
                }
                // down
                if (down) {
                    location = new Location(x, y+i);
                    if (!helper(pieces, location, res, otherColor)) {down = false;}
                }
                // right
                if (right) {
                    location = new Location(x+i, y);
                    if (!helper(pieces, location, res, otherColor)) {right = false;}
                }
                // left
                if (left) {
                    location = new Location(x-i, y);
                    if (!helper(pieces, location, res, otherColor)) {left = false;}
                }
            }
            if (lookForCheck) {
                res = findChecks(res, piece, king, pieces, x, y);
            }
            this.moves = res;
            return res;
        }

    private ArrayList<Location> queenMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        // queen's moves are a combination of rook and bishop moves, diagonols and non-diagonols
        bishopMoves(pieces, piece, king, lookForCheck, res, otherColor);
        rookMoves(pieces, piece, king, lookForCheck, res, otherColor);
        this.moves = res;
        return res;
    }

    private ArrayList<Location> kingMoves(ArrayList<Piece> pieces, Piece piece, Piece king, 
        boolean lookForCheck, ArrayList<Location> res, String otherColor) {
        // king's moves are all the queen's moves, but only for one square
        
        queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
        ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location location: res) {
            if (distance(location, new Location(this.x, this.y)) >= 1.5) {
                toRemove.add(location);
            }
        }
        for (Location loc: toRemove) {res.remove(loc);}
        
        if (lookForCheck) {
            res = findChecks(res, piece, king, pieces, x, y);
            res = findCastleMoves(pieces, king, res);
        }
        
        this.moves = res;
        return res;
    }

    private ArrayList<Location> findCastleMoves (ArrayList<Piece> pieces, Piece king, ArrayList<Location> res) {
        int x = this.x;
        int y = this.y;
        if (!king.hasMoved) {
            // because of App.flipBoard(), this changes to right and left instead of king and queen side
            boolean rightSideCastle = true;
            boolean leftSideCastle = true;
            for (int i = 1; i <= 2; i++) {
                if (isOccupied(pieces, new Location(x+i, this.y)) == null) {
                    this.x = x + i; // king side castle
                    if (inCheck(pieces, king)) {rightSideCastle = false;}
                } else {rightSideCastle = false;}
                    
                if (isOccupied(pieces, new Location(x-i, this.y)) == null) {
                    this.x = x - i; // queen side castle
                    if (inCheck(pieces, king)) {leftSideCastle = false;} 
                } else {leftSideCastle = false;}
                    
            }
            /*
             * for queen side castling, need to make sure 
             * there is another empty space that is vacant, 
             * so check both sides and see if there is a piece
             * that is not a rook and also hasn't moved
             */
            
            // checking right
            try {
                Piece rightPiece = isOccupied(pieces, new Location(this.x + 1, y));
                // if the piece is not a rook and has not moved
                if (!(rightPiece.type.equals("rook") && !rightPiece.hasMoved)) {rightSideCastle = false;}
            } catch (NullPointerException e) {}

            // checking left
            try {
                Piece leftPiece = isOccupied(pieces, new Location(this.x - 1, y));
                // if the left piece is not a rook and has not moved
                if (!(leftPiece.type.equals("rook") && !leftPiece.hasMoved)) {leftSideCastle = false;}
            } catch (NullPointerException e) {}
            
            this.x = x;
            this.y = y;
            
            // set the new moves

            if (rightSideCastle) {res.add(new Location(this.x + 2, y));}
            if (leftSideCastle) {res.add(new Location(this.x - 2, y));}
            
                  
        }
        return res;
    }

    private boolean helper(ArrayList<Piece> pieces, Location location, ArrayList<Location> res, String otherColor) {
        Piece occupiedPiece;
        if (inBounds(location)) {
            occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece == null) {
                res.add(location);
                return true;
            }
            else if (occupiedPiece.color.equals(otherColor)) {
                res.add(location);
                return false;
            }
        }
        return false;
    }

    public Piece isOccupied(ArrayList<Piece> pieces, Location location) {
        /* purpose
         * checks to see if a piece occupies a square,
         * if so return that piece
         */
        for (Piece piece: pieces) {
            if (piece.x == location.x && piece.y == location.y) {
                return piece;
            }
        }
        return null;
    }

    public ArrayList<Location> findChecks(ArrayList<Location> res, Piece piece, Piece king, ArrayList<Piece> pieces, int x, int y) {
        ArrayList<Location> toRemove = new ArrayList<Location>();
        
        for (Location location: res) {
            Piece occupiedPiece = isOccupied(pieces, location);
            
            if (occupiedPiece != null) {
                System.out.println(occupiedPiece.color + " " + occupiedPiece.type + " " + occupiedPiece.x + " " + occupiedPiece.y);
                occupiedPiece.alive = false;
            }

            piece.x = location.x;
            piece.y = location.y;
            
            if (inCheck(pieces, king)) {
                toRemove.add(location);
            }
            try {
                occupiedPiece.alive = true; // occupied piece may be null
            } catch (NullPointerException e) {}
        }
        for (Location location: toRemove) {res.remove(location);}
        piece.x = x;
        piece.y = y;
        return res;
    }
    
    public boolean inCheck(ArrayList<Piece> pieces, Piece king) { // see if a move causes check
        String otherColor = king.color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.alive) {
                if (piece.type.equals("pawn")) {piece.setMoves(pieces, piece, null, false, true);}
                else {piece.setMoves(pieces, piece, null, false);}
                for (Location location: piece.getMoves()) {
                    if (king.x == location.x && king.y == location.y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean checkMate(ArrayList<Piece> pieces, String turn) {
        int counter = 0;
        for (Piece piece: pieces) {
            if (piece.color.equals(turn)) {
                piece.setMoves(pieces, piece, this, true);
                counter = counter + piece.getMoves().size();
            }
            
        }
        if (counter == 0 && this.check) return true;
        else return false;

    }

    public boolean staleMate(ArrayList<Piece> pieces, String turn) {
        int counter = 0;
        for (Piece piece: pieces) {
            if (piece.color.equals(turn)) {
                piece.setMoves(pieces, piece, this, true);
                counter = counter + piece.getMoves().size();
            }
        }
        if (counter == 0 && !this.check) return true;
        return false;
    }
}
