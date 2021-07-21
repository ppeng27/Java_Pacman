package ghost;

import processing.core.PImage;
import processing.core.PApplet;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

import java.util.HashMap;
import org.json.simple.JSONArray;
/**
 * Holds all cell coordinates in the game. Used to determine movement allowance of waka and ghosts.
 */
public class Map{
    private String mapName;
    private String[][] mapStr;
    private Cell[][] mapCells;

    /**
     * Map constructor
     * @param mapName The name of the the map file that should be read.
     */
    public Map(String mapName) {

        this.mapName = mapName;
        mapStr = ReadFile.readMapFile(mapName);
        mapCells = ReadFile.giveMapCells(mapStr);
    }

    /**
     * Renders the image of cells at their respective x and y position. 
     * If the image of the cell in null, this indicates an empty cell so no image is drawn.
     * @param app The app to draw from.
     */
    public void draw(PApplet app) {

        int x = 0;
        int y = 0;

        for(Cell[] r : mapCells) {
            for (Cell cell: r) {
                if (cell.getImage() != null) {
                    app.image(cell.getImage(), x, y);
                }
                x += 16;
            }
            y += 16;
            x = 0;
        }
    }

    /**
     * Returns the Cell representation of the map.
     * @return The 2D Cell array of the map.
     */
    public Cell[][] getMapCells() {
        return this.mapCells;
    }

    /**
     * Returns the String representation of the map.
     * @return The 2D String array of the map.
     */
    public String[][] getMapStr() {
        return this.mapStr;
    }

    /**
     * Finds the waka starting postion on the map.
     * @return coordinates of the waka start postion.
     */
    public int[] wakaStart() {
        for(int i = 0; i < mapStr.length; i++) {
            for(int j = 0; j < mapStr[0].length; j++) {
                if (mapStr[i][j].equals("p")) {
                    return new int[]{j*16,i*16};
                }
            }
        }
        return null;
    }

    /**
     * Counts the number of fruit and superfruit still on the map.
     * @return number of fruit and superfruit.
     */

    public int countFruit() {
        int fruitNum = 0;
        for(Cell[] row: mapCells) {
            for(Cell cell: row) {
                if (cell.getName().equals("fruit") || cell.getName().equals("superfruit")) {
                    fruitNum++;
                }
            }
        }
        return fruitNum;
    }
}
