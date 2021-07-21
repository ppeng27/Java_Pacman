package ghost;

import processing.core.PApplet;
import processing.core.PImage;
import java.lang.Math;
import java.util.List;

/** 
 * Represents playable character Waka. Instances have their own coordinates, speed, direction, state and lives.
 * They take in a direction and evaluate whether it is possible to move in that direction according to a given map.
 */
public class Waka{
    private int x;
    private int y;
    private int startX;
    private int startY;
    private int speed;
    private String direction;
    private String futureDirection;
    private int animate; //open for 10 frames, close for 10 frames
    private boolean state;
    private int lives;

    /**
    * Waka Constuctor
    * @param x the initial x position of waka.
    * @param y the initial y position of waka.
    * @param speed the speed waka will move at.
    * @param lives number of lives waka has at start.
    */
    public Waka(int x, int y, int speed, int lives) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.speed = speed;
        this.lives = lives;
        this.direction = "left";
        this.futureDirection = "left";
        this.animate = 0;
        this.state = true; //moving or not
    }
    /**
     * Sets wakas current direction to given direction.
     * @param direction The direction to be set.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }
    /**
     * Sets wakas future direction to give direction.
     * @param direction The future direction to set
     */
    public void setFutureDirection(String direction) {
        this.futureDirection = direction;
    }
    /**
     * Returns the current direction that waka is moving in.
     * @return The current direction.
     */

    public String getCurrentDirection() {
        return direction;
    }
    /**
     * Returns the current coordinates of Waka as an int array with x then y.
     * @return The current coordinates.
     */
    public int[] getCoord() {
        return new int[] {x,y};
    }

    /**
     * Sets the position of waka.
     * @param x the new x coordinate.
     * @param y the new y coordinate.
     */
    public void setCoord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Evaluates whether Waka is dead.
     * 
     * If the lives are equal to zero, waka is considered dead and returns false.
     * Otherwise return true.
     * @return Whether waka is dead.
     */
    public boolean isDead() {
        if (lives <= 0) {
            return true;
        }
        return false;
    }
    /**
     * Checks whether a future direction is a direct turn pposite of current direction.
     * @return Whether the turn is direct.
     */
    public boolean directTurn() {
        if (direction.equals("right") && futureDirection.equals("left")) {
            return true;
        }
        if (direction.equals("left") && futureDirection.equals("right")) {
            return true;
        }
        if (direction.equals("up") && futureDirection.equals("down")) {
            return true;
        }
        if (direction.equals("down") && futureDirection.equals("up")) {
            return true;
        }
        return false;
    }

    /**
     * Checks whether a turn is valid based on the future direction.
     * 
     * If the future direction is opposite to the current direction, assumes valid and sets state to true.
     * If the waka is not at an x and y coordinate that is divisible by 16 (The pixels measurement of a cell).
     *
     * When the waka is at an x and y postion both divisible by 16, check the cell in the future direction using CheckNextMove method.
     * If it is passable, set state to true.
     * 
     * Otherwise, check the next cell in the current direction using CheckNextMove method. If passable set state to true, otherwise set state to false.
     * @param map The map to traverse and check next cells against.
     * @return Whether the turn is valid.
     */
    public boolean checkTurn(Cell[][] map) {
        
        if (x%16 != 0 || y%16 != 0) {
            if (directTurn()) {
                direction = futureDirection;
                state = true;
                return state;
            }
            return state;
        }

        int xTemp = this.x;
        int yTemp = this.y;

        switch(futureDirection) {
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
        if (map[yTemp/16][xTemp/16].passable()) {
            state = true;
            direction = futureDirection;
        }
        else{
            checkNextMove(map);
        }
        return state;
    }

    /**
     * Checks whether the next cell in the current direction is passable.
     * If the x and y position of waka is not both divisible by 16, assumes valid and sets state to true.
     * Otherwise, check cell in current direction and set state to true if passable, other set state to false.
     * @param map The map to traverse and check next cells against.
     * @return Whether next move is valid.
     */

    public boolean checkNextMove(Cell[][] map) {

        if (x%16 != 0 || y%16 != 0) {
            state = true;
            return true;
        }

        int xTemp = this.x;
        int yTemp = this.y;

        switch(direction) {
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
        if (map[yTemp/16][xTemp/16].passable()) {
            state = true;
        }
        else{
            state = false;
        }
        return state;
    }

    /**
     * Draws the number of lives on the bottom left on the map
     * @param app The app used to render images
     */

    public void drawLives(PApplet app) {
        for(int i = 0; i< lives; i++) {
            app.image(Manager.sprites.get("right"), i*28 +8, 544);
        }
    }
    
    /**
     * Handles the logic of waka.
     * Checks for collision with any of the ghosts on the map by calling collision method.
     * Check the next move and adjust the required x or y by incrementing/decrementing by the speed.
     * Calls eat method to check whether new cell is fruit or superfruit.
     * 
     * @param map The map to traverse.
     * @param ghosts A list of all the ghosts on the map.
     */

    public void tick(Cell[][] map, List<Ghost> ghosts) {
       collision(ghosts);
        if (direction.equals(futureDirection)) {
            checkNextMove(map);
        }
        else{
            checkTurn(map);
        }

        if (state) {
            switch(direction) {
                case "up":
                    this.y -= speed;
                    break;
                case "down":
                    this.y += speed;
                    break;
                case "right":
                    this.x += speed;
                    break;
                case "left":
                    this.x -= speed;
                    break;
            }
        }
        eat(map[y/16][x/16], ghosts);
    }

    /**
     * Renders the image of waka at the x and y position. 
     * Cycles through the open and close sprites, each lasting for 8 frames.
     * @param app The app to draw from.
     */

    public void draw(PApplet app) {
        drawLives(app);
        if (animate < 8) {
            app.image(Manager.sprites.get(direction), this.x-5, this.y-5);
            animate ++;
        }
        else if (animate < 16) {
            app.image(Manager.sprites.get("closed"), this.x-5, this.y-5);
            animate++;
        }
        else{
            app.image(Manager.sprites.get(direction), this.x-5, this.y-5);
            animate = 0;
        }
    }

    /**
     * Checks whether cell is a fruit or superfruit.
     * If the cell is a fruit, the cell name is set to empty and the sprite to null.
     * If the cell is a superfruit, cell name is set to empty, sprite set to null and every ghost made frightened.
     * Otherwise, not action is taken.
     * @param cell The cell being checked.
     * @param ghosts A list of all ghosts in the game.
     * @return Whether cell has been eaten.
     */

    public boolean eat(Cell cell, List<Ghost> ghosts) {
        if (cell.getName().equals("fruit")) {
            cell.setSprite("0");
            cell.setName("empty");
            return true;
        }
        else if (cell.getName().equals("superfruit")) {
            cell.setSprite("0");
            cell.setName("empty");
            ghosts.stream().forEach((e)-> e.frightened());
            return true;
        }
        else if (cell.getName().equals("soda")) {
            cell.setSprite("0");
            cell.setName("empty");
            ghosts.stream().forEach((e)-> e.invisible());
            return true;
        }
        return false;
    }

    /**
     * Checking for collision with ghosts. 
     * A ghost is considered to collide when the top left corner is within the top left quarter of the ghost.
     * If a collision occurs while ghosts are not frightened, waka loses a lives.
     * If a collision occurs while ghosts are frightened, the collided ghost becomes inactive.
     * @param ghosts A list of all the ghosts in the game.
     * @return Whether collision occurred.
     */

    public boolean collision(List<Ghost> ghosts) {
        for (Ghost ghost: ghosts) {
            int ghostLeft = ghost.getCoord()[0];
            int ghostRight = ghost.getCoord()[0] + 8;
            int ghostTop = ghost.getCoord()[1];
            int ghostBottom = ghost.getCoord()[1] + 8;
            // System.out.println(ghost.getType() + ghostLeft + " " + ghostRight +" "+ghostTop+" "+ghostBottom);
            // System.out.println(x +" "+ y);
            if (x >= ghostLeft && x <= ghostRight && y >= ghostTop && y <= ghostBottom) {
                if (ghost.isFrightened()) {
                    ghost.deactivate();
                    return true;
                }
                else{
                    ghosts.stream().forEach((e)-> e.revive());
                    lives--;
                    x = startX;
                    y = startY;
                    futureDirection = "left";
                    direction = "left";
                    return true;
                }
            }
        }
        return false;
    }
}