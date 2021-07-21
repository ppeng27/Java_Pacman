package ghost;
import processing.core.PImage;
import processing.core.PApplet;
import java.lang.Math;

import org.json.simple.JSONArray;
/**
 * A ghost type which targets waka in chase mode when it is less than 8 cells away.
 * In scatter mode it targets the bottom left.
 * The ignorant sprite is an orange ghost.
 */

public class Ignorant extends Ghost{

    /**
     * Ghost constructor
     * @param x The starting x coordinate of the ghost.
     * @param y The starting y coordinate of the ghost.
     * @param speed The ghost movement speed.
     * @param modeLengths The cycle of times between each chase and scatter phase.
     * @param frightenedLength The amount of time ghosts remained frightened and also invisible when corresponding items are collected.
     */

    public Ignorant(int x, int y,int speed, JSONArray modeLengths,int frightenedLength) {
        super(x, y, speed, "i", modeLengths,frightenedLength);
    }

    /**
     * Determines the target of chase mode.
     * @param waka The waka being targeted.
     * @return The new target coordinates.
     */
    public int[] getTarget(Waka waka) {
        double distance = Math.hypot(y-waka.getCoord()[1],x-waka.getCoord()[0]);
        if (distance < 128) {
            return new int[]{waka.getCoord()[0],waka.getCoord()[1]};
        }
        else{
            return new int[]{0,App.HEIGHT};
        }
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
            towardsTarget(map, 0,App.HEIGHT);
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