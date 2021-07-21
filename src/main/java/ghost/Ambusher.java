package ghost;
import processing.core.PImage;
import processing.core.PApplet;

import org.json.simple.JSONArray;

/**
 * A type of ghost which targets four cells ahead of waka is chase mode.
 * In scatter mode it targets the top left corner.
 * The ambusher sprite is a pink ghost.
 */
public class Ambusher extends Ghost{
    /**
     * Ghost constructor
     * @param x The starting x coordinate of the ghost.
     * @param y The starting y coordinate of the ghost.
     * @param speed The ghost movement speed.
     * @param modeLengths The cycle of times between each chase and scatter phase.
     * @param frightenedLength The amount of time ghosts remained frightened and also invisible when corresponding items are collected.
     */
    public Ambusher(int x, int y, int speed, JSONArray modeLengths,int frightenedLength) {
        super(x, y, speed,"a", modeLengths,frightenedLength);
    }

    /**
     * Determines the target of chase mode.
     * @param waka The waka being targeted.
     * @return The new target coordinates.
     */
    public int[] getTarget(Waka waka) {
        int xTemp = waka.getCoord()[0];
        int yTemp = waka.getCoord()[1];

        switch(waka.getCurrentDirection()) {
            case "up":
                yTemp -= 64;
                break;
            case "down":
                yTemp += 64;
                break;
            case "right":
                xTemp += 64;
                break;
            case "left":
                xTemp -= 64;
                break;
        }
        return new int[] {xTemp,yTemp};
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
            int[] target = getTarget(waka);
            towardsTarget(map, target[0],target[1]);
        }
        else{
            towardsTarget(map, App.WIDTH,0);
        }
        timer();

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
    }
}