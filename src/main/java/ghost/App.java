package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
/**
 * The GUI system which runs the game. 
 * Handles user input from standard input for player movement and debugging mode.
 */
public class App extends PApplet {

    public static final int WIDTH = 448;
    public static final int HEIGHT = 576;
    private Manager game;
    
    /**
     * Set up the game manager object.
     */
    public App() {
        game = new Manager();
    }
    /**
     * Setup frame rate and loads in all images.
     */
    public void setup() {
        frameRate(60);
        game.loadImages(this);
        game.setObjects();
    }

    /**
     * Determines the size of the GUI screen.
     */
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Handles user input and passes valid inputs to the game object.
     */

    public void keyPressed() { //move to waka
        if (key == ' ') {
            game.debugMode();
        }
        if (key == CODED) {
            switch (keyCode) {
                case UP:
                    game.getWaka().setFutureDirection("up");
                    break;
                case DOWN:
                    game.getWaka().setFutureDirection("down");
                    break;
                case LEFT:
                    game.getWaka().setFutureDirection("left");
                    break;
                case RIGHT:
                    game.getWaka().setFutureDirection("right");
                    break;
            }
        }
    }
    /**
     * Renders the images of the map, player and ghosts.
     */
    public void draw() { 
        game.draw(this);
    }
    /**
     * The main method of the entire program.
     * @param args Command line arguments.
     */

    public static void main(String[] args) {
        PApplet.main("ghost.App");
    }
}