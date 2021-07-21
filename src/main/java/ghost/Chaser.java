package ghost;
import processing.core.PImage;
import processing.core.PApplet;

import org.json.simple.JSONArray;
/**
 * A type of Ghost which targets the waka when in chase mode.
 * In scatter mode it targets the top left corner.
 * The chaser sprite is a red ghost.
 */
public class Chaser extends Ghost{

    /**
     * Chaser constructor
     * @param x The starting x coordinate of the ghost.
     * @param y The starting y coordinate of the ghost.
     * @param speed The ghost movement speed.
     * @param modeLengths The cycle of times between each chase and scatter phase.
     * @param frightenedLength The amount of time ghosts remained frightened and also invisible when corresponding items are collected.
     */
    public Chaser(int x, int y, int speed, JSONArray modeLengths,int frightenedLength) {
        super(x, y, speed, "c", modeLengths,frightenedLength);
    }
    /**
     * Logic handling for the ghost. Determines what mode the ghost is in and relevant action to perform.
     * Determines ghost movement behaviour.
     * @param map The map to traverse
     * @param waka The waka being targeted.
     */
    public void tick(Cell[][] map, Waka waka) {
        if (!active) {
            return;
        }
        if (invisible) {
            itemTimer();
        }
        if (frightened) {
            itemTimer();
            frightenedMove(map);
        }
        else if (mode) {
            towardsTarget(map, waka.getCoord()[0],waka.getCoord()[1]);
        }
        else{
            towardsTarget(map, 0,0);
        }

        switch(currentDirection) {
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
        timer();
    }
}