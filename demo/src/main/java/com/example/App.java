package com.example;

import javafx.application.Application;
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
    private static Group group;
    private static Pane pane;
    
    // all the file paths for the pieces
    String whiteKingStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKing.png";
    String blackKingStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKing.png";
    String whiteQueenStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteQueen.png";
    String blackQueenStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackQueen.png";
    String whiteRookStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteRook.png";
    String blackRookStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackRook.png";
    String whiteBishopStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteBishop.png";
    String blackBishopStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackBishop.png";
    String whiteKnightStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whiteKnight.png";
    String blackKnightStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackKnight.png";
    String whitePawnStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\whitePawn.png";
    String blackPawnStr = "C:\\Users\\kvsha\\Documents\\VSCode\\Java\\Chess\\demo\\src\\main\\resources\\com\\example\\PiecePics\\blackPawn.png";
    
    // for adding the pieces to the screen
    InputStream stream;
    Image image;
    ImageView imageView;
    int[] coordinates;
    ArrayList<Piece> pieces = new ArrayList<Piece>();
    King whiteKing;
    King blackKing;
    String turn;
    boolean toMove;
    Piece curPieceSelected;

    @Override
    public void start(Stage stage) throws IOException {
        group = new Group();
        pane = new Pane(group);

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
                group.getChildren().add(r);
                count++;
            }
            count++;
        }
        
        setUpPieces();
        setUpBoard();
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
                    if (i == 4) {piece = new King(i, j, "black");} // add black king
                }
                if (j == 1) {piece = new Pawn(i, j, "black");} // add black pawns
                if (j == 6) {piece = new Pawn(i, j, "white");} // add white pawns
                if (j == 7) { // 1st rank
                    if (i == 0 || i == 7) {piece = new Rook(i, j, "white");} // add black rook
                    if (i == 1 || i == 6) {piece = new Knight(i, j, "knight");} // add black knight
                    if (i == 2 || i == 5) {piece = new Bishop(i, j, "black");} // add black bishop
                    if (i == 3) {piece = new Queen(i, j, "white");} // add black queen
                    if (i == 4) {piece = new King(i, j, "white");} // add black king
                }
                if (piece != null) {pieces.add(piece);}
            }   
        }
        for (Piece piece: pieces) {
            piece.setMoves(pieces);
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
                if (!toMove) {
                    for (Piece piece: pieces) {
                        // define all possible moves for correpsonding color turn
                        if (piece.getColor().equals(turn) && piece.isAlive()) {  
                            piece.setMoves(pieces);
                        }
                        if (piece.getX() == square[0] && piece.getY() == square[1]) {
                            curPieceSelected = piece;
                            toMove = true;
                        }
                    }
                }
                if (toMove) {
                    for (Location location: curPieceSelected.getMoves()) {
                        if (location.getX() == square[0] && location.getY() == square[1]) {
                            movePiece(curPieceSelected, square[0], square[1]);
                        }
                    }
                }
            }
        });
    }

    private void movePiece(Piece curPieceSelected, int x, int y) {
        curPieceSelected.setX(x);
        curPieceSelected.setY(y);
    }

    private int[] findSquare(int x, int y) { // returns the square coordinates given the clicked mouse coords
        int[] res = new int[2];
        res[0] = (x-25)/100;
        res[1] = (y-25)/100;
        return res;
    }

    private void setUpBoard() throws FileNotFoundException {
        //TODO this should be ahcnged so that
        //pieces should be changed after every move
        for (int i = 0; i < 8; i++) {
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
        }
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
            group.getChildren().add(imageView);
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

/* additional notes
 *
 * https://www.geeksforgeeks.org/class-type-casting-in-java/#:~:text=Typecasting%20is%20the%20assessment%20of,direction%20to%20the%20inheritance%20tree.
 * shows how to Override functions in parent classes to properly call functions of subclasses
 * just use the same name
 * 
 */