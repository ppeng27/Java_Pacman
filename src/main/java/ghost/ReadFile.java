package ghost;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import processing.core.PApplet;
import processing.core.PImage;
/**
 * An abstract class for reading files.
 * Reads in the JSON file "congig.json" and retrieves contents as a JSONObject.
 * Reads map file and also creates cell representation of map.
 */
public abstract class ReadFile{
    /**
     * Parses a JSON file and returns the JSONobject from the file. 
     * @param jsonFile The file to be parsed.
     * @return The JSONobject from the file that was read
     */
    public static JSONObject readJSON(String jsonFile) {

        JSONParser parser = new JSONParser();
        try{
            FileReader read = new FileReader(jsonFile);
            Object obj = parser.parse(read);
            JSONObject config = (JSONObject) obj;
            return config;
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a txt file containing the map for the game. 
     * @param mapName The file to be read.
     * @return A 2D string array representation of the map.
     */

    public static String[][] readMapFile(String mapName) {
        File f = new File(mapName);
        String[][] mapStr = new String[36][28];
        try{
            Scanner scan = new Scanner(f);
            int i = 0;
            while(scan.hasNext()) {
                String rowStr = scan.next();
                String[] row = rowStr.replace("\n","").split("");

                mapStr[i] = row;
                i++;
            }
        }
        catch(FileNotFoundException e) {
            System.out.println("Map file not found");
        }
        return mapStr;
    }
    /**
     * Reads 2D String array of a map and converts to Cells. 
     * @param mapStr The 2D string array version of the map.
     * @return A 2D Cell array representation of the map.
     */

    public static Cell[][] giveMapCells(String[][] mapStr) {
        Cell[][] mapCells = new Cell[App.HEIGHT/16][App.WIDTH/16];

        for(int i = 0; i < App.HEIGHT/16; i++) {
            for(int j = 0; j < App.WIDTH/16; j++) {
                Cell cell = null;
                
                if (mapStr[i][j].equals("p") || mapStr[i][j].equals("0")) {
                    cell = new Cell(j,i,true,"0","empty");
                }
                else if (mapStr[i][j].equals("a") || mapStr[i][j].equals("c") || mapStr[i][j].equals("i")|| mapStr[i][j].equals("w")) {
                    cell = new Cell(j,i,true,"0","empty");
                }
                else if (mapStr[i][j].equals("7")) {
                    cell = new Cell(j,i,true,"7","fruit");
                }
                else if (mapStr[i][j].equals("8")) {
                    cell = new Cell(j,i,true,"8","superfruit");
                }
                else if (mapStr[i][j].equals("9")) {
                    cell = new Cell(j,i,true,"9","soda");
                }
                else{
                    cell = new Cell(j,i,false,mapStr[i][j],"wall");
                }
                mapCells[i][j] = cell;
            }
        }
        return mapCells;
    }
}