package ghost;
import processing.core.PImage;
import processing.core.PApplet;

import java.util.ArrayList;
import java.lang.Math;
import java.util.HashMap;
import org.json.simple.JSONArray;

import java.util.Random; 

/**
 * An abstract class which defines behaviours typical of all ghost types. 
 * Is the super class of the four ghost types in the game.
 * Ghosts contain coordinate x and y on the map, their speed, and different behaviours depending on their state.
 */
public abstract class Ghost{
    /**
     * x coordinate of the ghost.
     */
    protected int x;
    /**
     * y coordinate of the ghost.
     */
    protected int y;
    private int xStart;
    private int yStart;
    private int xTarget;
    private int yTarget;

    private String ghostType;
    private JSONArray modeLengths;
    /**
     * The movement speed of the ghost.
     */
    protected int speed;
    /**
     * Current direction the ghost is moving in.
     */
    protected String currentDirection;
    
    /**
     * Whether the ghost is frightened.
     */
    protected boolean frightened;
    /**
     * Whether the ghost is invisible.
     */
    protected boolean invisible;
    /**
     * Whether the ghost is active.
     */
    protected boolean active;
    /**
     * True for chase mode and false for scatter mode.
     */
    protected boolean mode; 
    private boolean debugMode;

    private int currentTimer;
    private int currentTimerIndex;
    private int itemLength;
    private int itemTimer;

    /**
     * Ghost constructor
     * @param x The starting x coordinate of the ghost.
     * @param y The starting y coordinate of the ghost.
     * @param speed The ghost movement speed.
     * @param ghostType The string representation of the ghost type.
     * @param modeLengths The cycle of times between each chase and scatter phase.
     * @param frightenedLength The amount of time ghosts remained frightened and also invisible when corresponding items are collected.
     */

    public Ghost(int x, int y, int speed, String ghostType, JSONArray modeLengths, int frightenedLength) {
        this.ghostType = ghostType;
        this.x = x;
        this.y = y;
        this.xStart = x;
        this.yStart = y;
        this.xTarget = 0;
        this.yTarget = 0;
        // this.sprite = sprite;
        this.modeLengths = modeLengths;
        this.speed = speed;
        this.currentDirection = "left";

        this.currentTimerIndex = 0;
        this.currentTimer = 0;
        this.itemLength = frightenedLength;
        this.active = true;
        this.itemTimer = 0;
    }
    /**
     * Handles logic of the ghost.
     * @param map The map to traverse.
     * @param waka The waka to target.
     */
    public abstract void tick(Cell[][] map, Waka waka);

    /**
     * Returns the type of the ghost.
     * @return The ghost type.
     */
    public String getType() {
        return ghostType;
    }
    /**
     * Returns the current x and y coordinate of the ghost.
     * @return Current coordinates.
     */
    public int[] getCoord() {
        return new int[]{x,y};
    }
    /**
     * Returns the current x and y coordinate of the target.
     * @return Current target coordinates.
     */
    public int[] getTargetCoord() {
        return new int[]{xTarget,yTarget};
    }
    /**
     * Returns whether ghost is in frightened mode.
     * @return Whether ghost is frightened.
     */
    public boolean isFrightened() {
        return frightened;
    }

    /**
     * Returns the current direction of the ghost.
     * @return The current direction.
     */
    public String getDirection() {
        return currentDirection;
    }

    /**
     * Deactivates the ghost so it is not put on the map.
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Revives the ghost and resets all modes to false and direction to default left.
     * Coordinates are set to the starting position.
     * All timers are reset.
     */

    public void revive() {
        this.active = true;
        this.x = xStart;
        this.y = yStart;
        frightened = false;
        invisible = false;
        currentDirection = "left";
        currentTimerIndex = 0;
        currentTimer = 0;
        mode = false;
    }

    /**
     * Handles the timing for changing between chase scatter mode.
     */
    public void timer() {
        if (currentTimer < (Long) modeLengths.get(currentTimerIndex)) {
            currentTimer++;
            return;
        }
        // System.out.println("current: " + currentTimer);
        // System.out.println("index: " + currentTimerIndex);
        // System.out.println("mode: " + mode);
        currentTimer = 0;
        if (currentTimerIndex < modeLengths.size()-1) {
            currentTimerIndex++;
        }
        else{
            currentTimerIndex = 0;
        }
        if (mode) {
            mode = false;
        }
        else{
            mode = true;
        }
    }

    /**
     * Sets the ghost to frightened mode. Also sets invisible mode to false.
     */
    public void frightened() {
        frightened = true;
        invisible = false;
        itemTimer = 0;
    }

    /**
     * Sets the ghost to invisible mode. Also sets frightened mode to false.
     */
    public void invisible() {
        invisible = true;
        frightened = false;
        itemTimer = 0;
    }
    /**
     * Handles the timing for how long an item is in effect for.
     */
    public void itemTimer() {
        if (itemTimer < itemLength) {
            itemTimer++;
            return;
        }
        itemTimer = 0;
        frightened = false;
        invisible = false;
    }

    /**
     * Switches between debug and normal mode.
     */
    public void changeDebug() {
        if (debugMode) {
            debugMode = false;
        }
        else{
            debugMode = true;
        }
    }

    /**
     * Set the direction of the ghost.
     * @param direction The direction to be set.
     */
    public void setDirection(String direction) {
        this.currentDirection = direction;
    }

    /**
     * Returns the current debug mode.
     * @return The current state of debug.
     */
    public boolean getDebugMode() {
        return debugMode;
    }

    public void checkIntersection(ArrayList<String> possibleMoves, int targetX, int targetY) {
        double shortest = 10000;
        String nextMove = currentDirection;
        int yTemp;
        int xTemp;

        for(String move: possibleMoves) {
            yTemp = y;
            xTemp = x;
            switch(move) {
                case "up":
                    yTemp -= 16;
                    break;
                case "down":
                    yTemp += 16;
                    break;
                case "right":
                    xTemp += 16;
                    break;
                case "left":
                    xTemp -= 16;
                    break;
            }
            if (Math.hypot(yTemp-targetY,xTemp-targetX) < shortest) {
                shortest = Math.hypot(yTemp-targetY,xTemp-targetX);
                nextMove = move;
            }
        }
        currentDirection = nextMove;
    }
    /**
     * Checks the surrounding four cells around ghost for possible nextMoves.
     * @param map The map to traverse.
     * @return A list of possible moves.
     */

    public ArrayList<String> checkNextMove(Cell[][] map) {

        if (x%16 != 0 || y%16 != 0) {
            return null;
        }

        int xIndex = x/16;
        int yIndex = y/16;
        ArrayList<String> possibleMoves = new ArrayList<String>();

        if (yIndex != 0 && !currentDirection.equals("down")) {
            if (map[yIndex-1][xIndex].passable()) {
                possibleMoves.add("up");
            }
        }
        if (yIndex != 35 && !currentDirection.equals("up")) {
            if (map[yIndex+1][xIndex].passable()) {
                possibleMoves.add("down");
            }
        }
        if (xIndex != 0 && !currentDirection.equals("right")) {
            if (map[yIndex][xIndex-1].passable()) {
                possibleMoves.add("left");
            }
        }
        if (yIndex != 0 && !currentDirection.equals("left")) {
            if (map[yIndex][xIndex+1].passable()) {
                possibleMoves.add("right");
            }
        }

        if (possibleMoves.size() == 0) {
            switch(currentDirection) {
                case "up":
                    currentDirection = "down";
                    return null;
                case "down":
                    currentDirection ="up";
                    return null;
                case "right":
                    currentDirection = "left";
                    return null;
                case "left":
                    currentDirection = "right";
                    return null;
            }
        }
        else if (possibleMoves.size() == 1) {
            currentDirection = possibleMoves.get(0);
            return null;
        }
        else{
            return possibleMoves;
        }
        return null;
    }
    /**
     * Set the next move based on closest cell to to target position based on euclidian distance.
     * @param map The map to traverse.
     * @param targetX The x coordinate of the target position.
     * @param targetY The y coordinate of the target position.
     */

    public void towardsTarget(Cell[][] map, int targetX, int targetY) {
        xTarget = targetX;
        yTarget = targetY;
        ArrayList<String> possibleMoves = checkNextMove(map);
        if (possibleMoves != null) {
            checkIntersection(possibleMoves, targetX, targetY);
        }  
    }

    /**
     * Sets the next move for frightened move;
     * @param map The map to be traversed.
     * @return The current direction.
     */
    public String frightenedMove(Cell[][] map) {
        ArrayList<String> possibleMoves = checkNextMove(map);
        if (possibleMoves != null) {
            Random ran = new Random();
            int randomIndex = ran.nextInt(possibleMoves.size());
            currentDirection = possibleMoves.get(randomIndex);
        }  
        return currentDirection;
    }

    /**
     * Renders the image of ghost at the x and y position. 
     * Image displayed depends on the state of variables active, debugMode, frightened and invisible.
     * @param app The app to draw from.
     */
    public void draw(PApplet app) {
        if (!active) {
            return;
        }

        if (debugMode) {
            app.stroke(255);
            app.strokeWeight(2); 
            app.line(x+8,y+8, xTarget, yTarget);
        }
        if (frightened) {
            app.image(Manager.sprites.get("frightened"), this.x-4, this.y-4);
        }
        else if (invisible) {
            if (x%32 == 0 && y%32 == 0) {
                app.image(Manager.sprites.get(ghostType), this.x-4, this.y-4);
            }
        }
        else{
            app.image(Manager.sprites.get(ghostType), this.x-4, this.y-4);
        }
    }

     
}