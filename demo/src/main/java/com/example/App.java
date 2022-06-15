package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.example.Pieces.*;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Group gridGroup;
    private static Group pieceGroup;
    private static Pane pane;
    
    // for adding the pieces to the screen
    private static InputStream stream;
    private static Image image;
    private static ImageView imageView;
    private static int[] coordinates;
    private static ArrayList<Piece> pieces = new ArrayList<Piece>();
    private static King whiteKing;
    private static King blackKing;
    private static String turn;
    private static boolean toMove;
    private static Piece curPieceSelected;

    @Override
    public void start(Stage stage) throws IOException {
        gridGroup = new Group();
        pieceGroup = new Group();
        pane = new Pane(gridGroup, pieceGroup);

        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // make the grid with alternating colors white and grey
                Rectangle r = new Rectangle();
                
                coordinates = coordinateFormula(i, j);
                
                r.setX(coordinates[0]);
                r.setY(coordinates[1]);
                r.setWidth(100);
                r.setHeight(100);
                
                if (count % 2 == 0) r.setFill(Color.WHITE);
                else r.setFill(Color.GREY);
                gridGroup.getChildren().add(r);
                count++;
            }
            count++;
        }
        
        setUpPieces();
        drawBoard();
        scene = new Scene(pane, 850, 850);
        stage.setScene(scene);
        stage.setTitle("Chess");
        stage.setResizable(false);
        stage.show();
        
        startGame();
    }

    private void setUpPieces() {
        /* purpose
         * set up all the pieces with coordinates, 
         * color, and set the available moves
         */
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = null;
                if (j == 0) { // 8th rank
                    if (i == 0 || i == 7) {piece = new Rook(i, j, "black");} // add black rook
                    if (i == 1 || i == 6) {piece = new Knight(i, j, "black");} // add black knight
                    if (i == 2 || i == 5) {piece = new Bishop(i, j, "black");} // add black bishop
                    if (i == 3) {piece = new Queen(i, j, "black");} // add black queen
                    if (i == 4) { // add black king
                        piece = new King(i, j, "black"); 
                        blackKing = new King(i, j, "black");
                    } 
                }
                if (j == 1) {piece = new Pawn(i, j, "black");} // add black pawns
                if (j == 6) {piece = new Pawn(i, j, "white");} // add white pawns
                if (j == 7) { // 1st rank
                    if (i == 0 || i == 7) {piece = new Rook(i, j, "white");} // add white rook
                    if (i == 1 || i == 6) {piece = new Knight(i, j, "white");} // add white knight
                    if (i == 2 || i == 5) {piece = new Bishop(i, j, "white");} // add white bishop
                    if (i == 3) {piece = new Queen(i, j, "white");} // add white queen
                    if (i == 4) { // add white king
                        piece = new King(i, j, "white");
                        whiteKing = new King(i, j, "white");
                    } 
                }
                if (piece != null) {pieces.add(piece);}
            }   
        }        
    }

    private void startGame() {
        turn = "white";

        scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // getting x and y coordinates
                int x = (int)event.getX();
                int y = (int)event.getY();
                int[] square = findSquare(x, y);
                x = square[0];
                y = square[1];
                System.out.println(turn);
                if (!toMove) {
                    for (Piece piece: pieces) {
                        // define all possible moves for correpsonding color turn
                        if (piece.getColor().equals(turn) && piece.isAlive()) {  
                            King king = turn.equals("white") ? whiteKing: blackKing;
                            piece.setMoves(pieces, piece, king, true); // TODO dont need piece arg in set moves func
                        }
                        if (piece.getX() == x && piece.getY() == y && piece.getColor().equals(turn)) {
                            curPieceSelected = piece;
                            toMove = true;
                        }
                    }
                }
                else {
                    toMove = false;
                    for (Location location: curPieceSelected.getMoves()) {
                        System.out.println(location.getX() + " " + location.getY());
                        if (location.getX() == x && location.getY() == y) {
                            Piece occupiedPiece = curPieceSelected.isOccupied(pieces, new Location(x, y));
                            if (occupiedPiece != null) {
                                // if a piece is took
                                System.out.println("Took");
                                occupiedPiece.setAlive(false);
                                pieces.remove(occupiedPiece);
                            }                            
                            curPieceSelected.setX(x);
                            curPieceSelected.setY(y);
                            try {
                                pieceGroup.getChildren().clear();
                                drawBoard();
                                // if turn is white, then turn is black and vice versa
                                turn = turn.equals("white") ? "black": "white";
                            } catch (FileNotFoundException e) {e.printStackTrace();}
                        }
                    }
                }
            }
        });
    }


    private int[] findSquare(int x, int y) { // returns the square coordinates given the clicked mouse coords
        int[] res = new int[2];
        res[0] = (x-25)/100;
        res[1] = (y-25)/100;
        return res;
    }

    private void drawBoard() throws FileNotFoundException {
        //TODO this should be ahcnged so that
        //pieces should be changed after every move
        for (Piece piece: pieces) {
            setImage(piece.getFileString(), piece.getX(), piece.getY());
        }
        /*for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (j == 0) { // 8th rank
                    if (i == 0 || i == 7) {setImage(blackRookStr, i, j);} // add black rook
                    if (i == 1 || i == 6) {setImage(blackKnightStr, i, j);} // add black knight
                    if (i == 2 || i == 5) {setImage(blackBishopStr, i, j);} // add black bishop
                    if (i == 3) {setImage(blackQueenStr, i, j);} // add black queen
                    if (i == 4) {setImage(blackKingStr, i, j);} // add black king
                }
                if (j == 1) {setImage(blackPawnStr, i, j);} // add black pawns
                if (j == 6) {setImage(whitePawnStr, i, j);} // add white pawns
                if (j == 7) { // 1st rank
                    if (i == 0 || i == 7) {setImage(whiteRookStr, i, j);} // add black rook
                    if (i == 1 || i == 6) {setImage(whiteKnightStr, i, j);} // add black knight
                    if (i == 2 || i == 5) {setImage(whiteBishopStr, i, j);} // add black bishop
                    if (i == 3) {setImage(whiteQueenStr, i, j);} // add black queen
                    if (i == 4) {setImage(whiteKingStr, i, j);} // add black king
                }
            }   
        }*/
    }

    private void setImage(String pathname, int i, int j) throws FileNotFoundException {
        try {
            stream = new FileInputStream(pathname);
            image = new Image(stream);
            imageView = new ImageView();
            imageView.setImage(image);
            coordinates = coordinateFormula(i, j);
            imageView.setX(coordinates[0]);
            imageView.setY(coordinates[1]); 
            pieceGroup.getChildren().add(imageView);
        } catch (FileNotFoundException e) {System.out.println("cannot find file"); e.printStackTrace();}
    }

    private int[] coordinateFormula(int i, int j) {
        int[] res = new int[2];
        res[0] = 25+100*i;
        res[1] = 25+100*j;
        return res;
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}

// TODO delete the piece argument for the setMoves() functions?
// TODO the blocking of checks doesn't work as intended, maybe the checking mechanism doesn't in general

/* additional notes
 *
 * https://www.geeksforgeeks.org/class-type-casting-in-java/#:~:text=Typecasting%20is%20the%20assessment%20of,direction%20to%20the%20inheritance%20tree.
 * shows how to Override functions in parent classes to properly call functions of subclasses
 * just use the same name and arguments with @Override
 * 
 * java platform se binary was telling me that my program was in an infinite loop
 * 
 * 
 * 
 */