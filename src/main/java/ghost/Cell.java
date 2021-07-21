package ghost;

import processing.core.PApplet;
import processing.core.PImage;

/**
 * Represent the type of cell on a map. Identifies cells by their string name.
 * Each cell has its own sprite, coordinates on the map and whether they are passable for moving entities.
 */
public class Cell{
    private int x;
    private int y;
    private boolean pass;
    private String code;
    private String name;

    /**
     * Cell constructor
     * @param x The horizontal x coordinate of the cell.
     * @param y The vertical y coordinate of the cell.
     * @param pass Whether the cell can be passed through.
     * @param code The image code of the cell.
     * @param name The string identifier for the type of cell.
     */

    public Cell(int x, int y, boolean pass, String code, String name) {
        this.x = x;
        this.y = y;
        this.pass = pass;
        this.code = code;
        this.name = name;
    }

    /**
     * Retrieves the coordinates of the cell.
     * @return The x and y coordinate.
     */
    public int[] getCoord() {
        return new int[]{x,y};
    }
    /**
     * Returns whether the cells is passable.
     * @return True if the cell can be passed, false otherwise.
     */
    public boolean passable() {
        return pass;
    }

    /**
     * Returns the sprite of the cell.
     * @return The PImage sprite of the cell.
     */
    public PImage getImage() {
        return Manager.sprites.get(code);
    }
    /**
     * Returns the name identifier of the cell.
     * @return The cell name.
     */

    public String getName() {
        return name;
    }
    /**
     * Sets the name of the cell.
     * @param name The name to be set.
     */

    public void setName(String name) {
        this.name = name;
    }
    /**
     * Sets the sprite of the cell.
     * @param code The sprite to be set.
     */
    public void setSprite(String code) {
        this.code = code;
    }
    /**
     * Determines if a string corresponds with a ghost.
     * @param cell The string to test.
     * @return Whether the cell is a ghost.
     */

    public static boolean isGhost(String cell) {
        if (cell.equals("c") ||cell.equals("a") ||cell.equals("i") ||cell.equals("w")) {
            return true;
        }
        return false;
    }
}