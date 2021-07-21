package ghost;
import processing.core.PImage;
import processing.core.PApplet;

import org.json.simple.JSONArray;
import java.util.List;

/**
 * A ghost type which targets double the vector from Chaser to 2 grid spaces ahead of Waka in chase mode.
 * In scatter mode it targets the bottom right.
 * The whim sprite is a blue ghost.
 */
public class Whim extends Ghost{
    private Ghost chaser;

    /**
     * Ghost constructor
     * @param x The starting x coordinate of the ghost.
     * @param y The starting y coordinate of the ghost.
     * @param speed The ghost movement speed.
     * @param modeLengths The cycle of times between each chase and scatter phase.
     * @param frightenedLength The amount of time ghosts remained frightened and also invisible when corresponding items are collected.
     */
    public Whim(int x, int y, int speed, JSONArray modeLengths,int frightenedLength) {
        super(x, y, speed,"w", modeLengths,frightenedLength);
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
            towardsTarget(map, App.WIDTH,App.HEIGHT);
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
    /**
     * Assigns the chaser which whim bases its movement pattern on.
     * @param chaser The chaser to be assigned.
     */

    public void setChaser(Ghost chaser) {
        this.chaser = chaser;
    }

    /**
     * Returns the assigned chaser.
     * @return The assigned chaser.
     */

    public Ghost getChaser() {
        return this.chaser;
    }

     /**
     * Assigns a chaser for any Whim type ghosts in the game. 
     * If no whim is found, no action is taken.
     * Assumes existence of a Chaser is Whim is found.
     * If there are multiple Whims or chasers, all whims are assigned the first Chaser in the list.
     * @param ghosts The lists of ghosts to go through.
     */

    public static void assignChaserForWhim(List<Ghost> ghosts) {
        boolean foundWhim;
        Ghost chaser = null;
        for (Ghost g: ghosts) {
            if (g.getType().equals("w")) {
                foundWhim = true;
            }
        }

        for (Ghost g: ghosts) {
            if (g.getType().equals("c")) {
                chaser = g;
            }
        }

        for (Ghost g: ghosts) {
            if (g.getType().equals("w")) {
                Whim w = (Whim) g;
                w.setChaser(chaser);
            }
        }
    }

    /**
     * Determines the target of chase mode.
     * @param waka The waka being targeted.
     * @return The new target coordinates.
     */

    public int[] getTarget(Waka waka) {
        int[] targetPosition = waka.getCoord();
        int[] chaserPosition = chaser.getCoord();
        String wakaDirection = waka.getCurrentDirection();
        switch(wakaDirection) {
            case "up":
                targetPosition[1] -= 32;
                break;
            case "down":
                targetPosition[1] += 32;
                break;
            case "right":
                targetPosition[0] += 32;
                break;
            case "left":
                targetPosition[0]-= 32;
                break;
        }
        //System.out.println("Before " + targetPosition[0] +" "+ targetPosition[1]+ " " + chaserPosition[0] + " " + chaserPosition[1]);
        targetPosition[0] += (targetPosition[0] - chaserPosition[0]);
        targetPosition[1] += (targetPosition[1] - chaserPosition[1]);
        
        //System.out.println(targetPosition[0] +" "+ targetPosition[1]+ " " + chaserPosition[0] + " " + chaserPosition[1]);
        return targetPosition;
    }
}