package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class AmbusherTest {
    Waka waka;
    Cell empty;
    Cell fruit;
    Cell wall;
    Cell superfruit;
    Cell soda;
    Cell[][] map;
    JSONArray modeLengths;

    @Test 
    public void AmbusherConstructor() {
        //Testing the constructor and initial states after construction
        Ambusher ghost = new Ambusher(0,0,1,null,1);
        assertEquals("a",ghost.getType());
        assertArrayEquals(new int[]{0,0},ghost.getCoord());
        assertArrayEquals(new int[]{0,0},ghost.getTargetCoord());
        assertFalse(ghost.isFrightened());
    }

    @BeforeEach
    public void setBefore() {
        JSONObject config = ReadFile.readJSON("configTest.json");
        modeLengths = (JSONArray) config.get("modeLengths");
        waka = new Waka(16,16,1,1);
        empty = new Cell(160,80,true,null,"empty");
        fruit = new Cell(160,80,true,null,"fruit");
        wall = new Cell(160,80,false,null,"wall");
        superfruit = new Cell(160,80,true,null,"superfruit");
        soda = new Cell(160,80,true,null,"soda");
        map = new Cell[][]{
                {wall,wall,wall,wall},
                {wall,empty,empty,wall},
                {wall,empty,wall,wall},
                {wall,empty,empty,wall},
                {wall,wall,wall,wall}
            };
    }

    @Test 
    public void targetTest() {
        // Checking correct target while in chase mode
        Ambusher ambusher = new Ambusher(32,16,1,modeLengths,1);
        int[] target = ambusher.getTarget(waka);
        assertArrayEquals(new int[]{-48,16},target);
        
        waka.setDirection("up");
        target = ambusher.getTarget(waka);
        assertArrayEquals(new int[]{16,-48},target);

        waka.setDirection("down");
        target = ambusher.getTarget(waka);
        assertArrayEquals(new int[]{16,80},target);

        waka.setDirection("right");
        target = ambusher.getTarget(waka);
        assertArrayEquals(new int[]{80,16},target);
    }

    @Test
    public void movementTest() {
        //Testing general movement with changing between mode, incrementing coordinate and direction
        Ambusher ambusher = new Ambusher(16,48,1,modeLengths,1);
        ambusher.tick(map,waka);
        assertArrayEquals(new int[]{16,47},ambusher.getCoord());
        assertArrayEquals(new int[]{448,0},ambusher.getTargetCoord());
        assertEquals("up", ambusher.getDirection());

        ambusher.tick(map,waka);
        ambusher.tick(map,waka);
        assertArrayEquals(new int[]{-48,16},ambusher.getTargetCoord());

        ambusher = new Ambusher(17,16,1,modeLengths,1);
        ambusher.tick(map,waka);
        assertArrayEquals(new int[]{16,16},ambusher.getCoord());
        ambusher.setDirection("right");
        ambusher.tick(map,waka);
        assertArrayEquals(new int[]{17,16},ambusher.getCoord());

    }
    @Test
    public void itemStatesTest() {
        //Testing that items changes correct states and correct behaviour in each state
        Cell superfruit2 = new Cell(0,0,true,null,"superfruit");
        Ambusher ambusher = new Ambusher(16,16,1,modeLengths,1);
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(ambusher);

        waka.eat(superfruit,ghosts);
        assertTrue(ambusher.isFrightened());
        assertTrue(waka.collision(ghosts));
        ambusher.tick(map,waka);
        ambusher.revive();
        assertEquals("left", ambusher.getDirection());
        assertArrayEquals(new int[]{16,16},ambusher.getCoord());

        waka.eat(superfruit2,ghosts);
        ambusher.tick(map,waka);
        ambusher.tick(map,waka);
        assertFalse(ambusher.isFrightened());
        
        waka.eat(soda,ghosts);
        ambusher.tick(map,waka);
    }
}