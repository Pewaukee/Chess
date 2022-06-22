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

    // these are just placement functions to be overwritten in subclasses
    public ArrayList<Location> getMoves() {return moves;}
    public void setMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck) {
        ArrayList<Location> res = new ArrayList<Location>();
        int x = this.x;
        int y = this.y;
        
        String otherColor = piece.color.equals("white") ? "black": "white";
        
        switch (piece.type) {
            case "pawn": // set pawn moves
                pawnMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "knight": // set knight moves
                knightMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "bishop":
                bishopMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "rook":
                rookMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "queen":
                queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            case "king":
                kingMoves(pieces, piece, king, lookForCheck, res, otherColor);
                break;
            default:
                System.out.println("this shouldn't happen ever");
                break;
        }
    }

    private ArrayList<Location> pawnMoves(ArrayList<Piece> pieces, Piece piece, Piece king, boolean lookForCheck,  
        ArrayList<Location> res, String otherColor) {
        // TODO en passant, promotion
        switch (piece.color) {
            case "white":
                //up 1 and not at the end of the board
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
                break;
            case "black":
                // up 1 and not at end of the board
                Location downOne = new Location(x, y+1);
                if (y < 7 && isOccupied(pieces, downOne) == null) {
                    res.add(downOne);
                    // up 2 and at starting position
                    Location downTwo = new Location(x, y+2); // can only move down two if can move down one
                    if (y == 1 && isOccupied(pieces, downTwo) == null) {res.add(downTwo);}
                }

                //check diagonals
                Location downRight = new Location(x+1, y+1);
                if (isOccupied(pieces, downRight) != null) {helper(pieces, downRight, res, otherColor);}
                Location downLeft = new Location(x-1, y+1);
                if (isOccupied(pieces, downLeft) != null) {helper(pieces, downLeft, res, otherColor);}
                break;
            default:
                System.out.println("this shouldn't ever happen");
                break;
        
        }
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
        //queenMoves(pieces, piece, king, lookForCheck, res, otherColor);
        /*ArrayList<Location> toRemove = new ArrayList<Location>();
        for (Location location: res) {
            if (distance(location, new Location(this.x, this.y)) >= 1.5) {
                toRemove.add(location);
            }
        }
        for (Location loc: toRemove) {res.remove(loc);}*/
        Location location;
        // up
        location = new Location(x, y-1);
        helper(pieces, location, res, otherColor);

        // down
        location = new Location(x, y+1);
        helper(pieces, location, res, otherColor);

        // right
        location = new Location(x+1, y);
        helper(pieces, location, res, otherColor);
        
        // left
        location = new Location(x-1, y);
        helper(pieces, location, res, otherColor);

        // up right
        location = new Location(x+1, y-1);
        helper(pieces, location, res, otherColor);

        // up left
        location = new Location(x-1, y-1);
        helper(pieces, location, res, otherColor);

        // down right
        location = new Location(x+1, y+1);
        helper(pieces, location, res, otherColor);

        // down left
        location = new Location(x-1, y+1);
        helper(pieces, location, res, otherColor);

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
            boolean kingSideCastle = true;
            boolean queenSideCastle = true;
            for (int i = 1; i <= 2; i++) {
                if (isOccupied(pieces, new Location(x+i, this.y)) == null) {
                    this.x = x + i; // king side castle
                    if (inCheck(pieces, king)) {kingSideCastle = false;}
                } else {kingSideCastle = false;}
                    
                if (isOccupied(pieces, new Location(x-i, this.y)) == null) {
                    this.x = x - i; // queen side castle
                    if (inCheck(pieces, king)) {queenSideCastle = false;} 
                } else {queenSideCastle = false;}
                    
            }
            this.x = x;
            this.y = y;
            if (this.color.equals("white")) {
                try {
                    if (isOccupied(pieces, new Location(7, 7)).hasMoved) {kingSideCastle = false;}
                } catch (NullPointerException e) {}
                try {
                    if (isOccupied(pieces, new Location(0, 7)).hasMoved && isOccupied(pieces, new Location(1, 7)) == null) {
                        queenSideCastle = false;
                    }
                } catch (NullPointerException e) {}

                if (kingSideCastle) {res.add(new Location(6, 7));}
                if (queenSideCastle) {res.add(new Location(2, 7));}
            }
            else { // color = "black"
                try {
                    if (isOccupied(pieces, new Location(7, 0)).hasMoved) {kingSideCastle = false;}
                } catch (NullPointerException e) {}
                try {
                    if (isOccupied(pieces, new Location(0, 0)).hasMoved && isOccupied(pieces, new Location(1, 7)) == null) {
                        queenSideCastle = false;
                    }
                } catch (NullPointerException e) {}
            
                if (kingSideCastle) {res.add(new Location(6, 0));}
                if (queenSideCastle) {res.add(new Location(2, 0));}
            }         
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
        
        //System.out.println(king.color + "LLL");
        for (Location location: res) {
            piece.x = location.x;
            piece.y = location.y;
            //System.out.println(king.color + " " + king.check);
            //if (piece.type.equals("bishop")) {
                //System.out.println("bishop " + piece.x + " " + piece.y);
            //}
            //for (Piece piece1: pieces) {
                //if (piece1 == piece) {
                    //System.out.println("piece reference seen");
                    //System.out.println(piece1.x + " " + piece1.y + "\n");
                //}
                //if (piece1.type.equals("bishop")){
                //System.out.println(piece1.type + " " + piece1.x + " " + piece1.y);}
            //}
            Piece occupiedPiece = isOccupied(pieces, location);
            if (occupiedPiece != null) {
                occupiedPiece.alive = false;
            }
            //TODO when looking for checks, cannot block with another piece
            System.out.println(piece.color + " " + piece.type + " " + piece.x + " " + piece.y);
            if (inCheck(pieces, king)) {
                System.out.println("HIHIHIHI" + inCheck(pieces, king));
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
        System.out.println("othercolor=" + otherColor);
        System.out.println(king.color + " king " + king.x + " " + king.y);
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor) && piece.alive) {
                //System.out.println(piece.color + " " + piece.type);
                piece.setMoves(pieces, piece, null, false);
                for (Location location: piece.getMoves()) {
                    if (piece.type.equals("queen")) {
                        System.out.println("queen move " + location.x + " " + location.y);
                    }
                    if (king.x == location.x && king.y == location.y) {
                        System.out.println("check, by piece" + piece.type + " " + piece.color + " " + piece.x + " " + piece.y + " \n");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // not needed?
    public boolean checkStatus(ArrayList<Piece> pieces) {
        /*
         * doing this.check = true and this.check = false did not work,
         * had to set as a boolean returnable function and change the value
         * that doesn't seem to prevent the stalemate bug
         */
        //System.out.println(this.color + " " + this.check);
        String otherColor = this.color.equals("white") ? "black": "white";
        for (Piece piece: pieces) {
            if (piece.color.equals(otherColor)) {
                piece.setMoves(pieces, piece, this, false);
                for (Location move: piece.getMoves()) {
                    if (this.x == move.x && this.y == move.y) {
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
