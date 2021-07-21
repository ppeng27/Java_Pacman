package ghost;
import java.util.HashMap;
import processing.core.PApplet;
import processing.core.PImage;

/**
 * Loads all necessary sprites and stores them in a HashMap with a sting code as the key.
 */
public abstract class SpriteFiles{
    /**
     * Returns the a HashMap with images and their corresponding string code.
     * @param app The app to load images from.
     * @return sprite HashMap.
     */
    public static HashMap<String,PImage> getSprites(PApplet app) {
        HashMap<String,PImage> sprites = new HashMap<String,PImage>();
        sprites.put("1",app.loadImage("src/main/resources/horizontal.png"));
        sprites.put("2",app.loadImage("src/main/resources/vertical.png"));
        sprites.put("3",app.loadImage("src/main/resources/upLeft.png"));
        sprites.put("4",app.loadImage("src/main/resources/upRight.png"));
        sprites.put("5",app.loadImage("src/main/resources/downLeft.png"));
        sprites.put("6",app.loadImage("src/main/resources/downRight.png"));
        sprites.put("7",app.loadImage("src/main/resources/fruit.png"));
        sprites.put("8",app.loadImage("src/main/resources/superfruit.png"));
        sprites.put("9",app.loadImage("src/main/resources/soda.png"));

        sprites.put("a",app.loadImage("src/main/resources/ambusher.png"));
        sprites.put("c",app.loadImage("src/main/resources/chaser.png"));
        sprites.put("i",app.loadImage("src/main/resources/ignorant.png"));
        sprites.put("w",app.loadImage("src/main/resources/whim.png"));
        sprites.put("frightened",app.loadImage("src/main/resources/frightened.png"));

        sprites.put("closed",app.loadImage("src/main/resources/playerClosed.png"));
        sprites.put("up",app.loadImage("src/main/resources/playerUp.png"));
        sprites.put("down",app.loadImage("src/main/resources/playerDown.png"));
        sprites.put("left",app.loadImage("src/main/resources/playerLeft.png"));
        sprites.put("right",app.loadImage("src/main/resources/playerRight.png"));
        return sprites;
    }
}