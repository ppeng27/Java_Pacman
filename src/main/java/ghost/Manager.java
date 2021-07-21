package ghost;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PFont;

import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * The game manager class which stores all relevant objects in the game and consolodates all tick and draw methods.
 */
public class Manager{

    /**
     * The sprite images and their corresponding string codes.
     */
    public static HashMap<String,PImage> sprites;
    private Map map;
    private ArrayList<Ghost> ghosts;
    private JSONObject config;
    private Waka waka;
    private PFont font;

    /**
     * Manager constructor
     * Initialises the sprites and reads in the JSON file named "config.json"
     */
    public Manager() {
        sprites = new HashMap<String,PImage>();
        config = ReadFile.readJSON("config.json");
    }

    /**
     * Loads all images and fonts.
     * @param app The PApplet used to load images.
     */
    public void loadImages(PApplet app) {
        sprites = SpriteFiles.getSprites(app);
        font = app.createFont("src/main/resources/PressStart2P-Regular.ttf", 32);
    }
    /**
     * Creates all objects in the game including sprites, font, map, ghosts and the waka.
     */
    public void setObjects() {
        this.map = new Map((String) config.get("map"));
        this.ghosts = new ArrayList<Ghost>();
        int speed = ((Long) config.get("speed")).intValue();
        int lives = ((Long) config.get("lives")).intValue();

        this.waka = new Waka(this.map.wakaStart()[0],this.map.wakaStart()[1],speed,lives);
        setGhosts(map.getMapStr());
        Whim.assignChaserForWhim(ghosts);
    }

    /**
     * Draws all sprites of the map, ghosts and waka.
     * @param app The PApplet used to draw images.
     */

    public void draw(PApplet app) {
        app.background(194, 183, 167);
        if (waka.isDead()) {
            app.textFont(font);
            app.text("GAME OVER",224,288);
            app.textAlign(app.CENTER, app.CENTER);
        }
        else if (map.countFruit() != 0) {
            tick();
            map.draw(app);
            waka.draw(app);
            ghosts.stream().forEach((e)-> e.draw(app));
        }
        else{
            app.textFont(font);
            app.text("YOU WIN",224,288);
            app.textAlign(app.CENTER, app.CENTER);
        }
    }

    public void tick() {
        waka.tick(this.map.getMapCells(), ghosts);
        ghosts.stream().forEach((e)-> e.tick(this.map.getMapCells(),this.waka));
    }
    /**
     * Returns the game's waka object.
     * @return The game's waka.
     */
    public Waka getWaka() {
        return waka;
    }

    /**
     * Returns a list of the ghosts in the game.
     * @return The ghosts in the game.
     */
    public ArrayList<Ghost> getGhosts() {
        return ghosts;
    }

    /**
     * Adds ghosts into the list of ghosts according to what types appear in the map.
     * @param mapStr The map to determine number and types of ghosts from.
     */
    public void setGhosts(String[][] mapStr) {

        for (int i = 0; i < mapStr.length; i++) {
            for (int j = 0; j< mapStr[0].length; j++) {
                if (Cell.isGhost(mapStr[i][j])) {
                    addGhost(j*16,i*16,mapStr[i][j]);
                }
            }

        }
    }

    /**
     * Adds a single ghost based on ghost type and coordinates.
     * @param x The starting x postion of the ghost.
     * @param y The starting y postion of the ghost.
     * @param ghostType The string representation of the ghost type.
     */
    public void addGhost(int x, int y, String ghostType) {
        JSONArray modeLengths = (JSONArray) config.get("modeLengths");
        int speed = ((Long) config.get("speed")).intValue();
        int frightenedLength = ((Long) config.get("frightenedLength")).intValue();
        switch(ghostType) {
            case "c":
                    ghosts.add(new Chaser(x,y, speed, modeLengths, frightenedLength));
                    break;
            case "a":
                ghosts.add(new Ambusher(x,y,speed, modeLengths,frightenedLength));
                break;
            case "i":
                ghosts.add(new Ignorant(x,y,speed,modeLengths,frightenedLength));
                break;
            case "w":
                ghosts.add(new Whim(x,y,speed, modeLengths,frightenedLength));
                break;
        }
    }

    /**
     * Turns all ghosts in the game to debug mode.
     */
    public void debugMode() {
        ghosts.stream().forEach((e)-> e.changeDebug());
    }
}