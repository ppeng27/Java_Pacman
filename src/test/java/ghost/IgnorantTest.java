package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class IgnorantTest {
    Waka waka;
    Cell empty;
    Cell fruit;
    Cell wall;
    Cell superfruit;
    Cell soda;
    Cell[][] map;
    JSONArray modeLengths;

    @Test 
    public void IgnorantConstructor() {
        //Testing the constructor and initial states after construction
        Ignorant ghost = new Ignorant(0,0,1,null,1);
        assertEquals("i",ghost.getType());
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
        // Checking correct target while in scatter mode and chase mode
        Ignorant ignorant = new Ignorant(16,16,1,modeLengths,1);
        int[] target = ignorant.getTarget(waka);
        assertArrayEquals(new int[]{16,16},target);

        ignorant = new Ignorant(16,144,1,modeLengths,1);
        target = ignorant.getTarget(waka);
        assertArrayEquals(new int[]{0,576},target);
    }

    @Test
    public void movementTest() {
         //Testing general movement with changing between mode, incrementing coordinate and direction
        Ignorant ignorant = new Ignorant(17,16,1,modeLengths,1);
        assertNull(ignorant.checkNextMove(map));
        assertEquals("left", ignorant.getDirection());

        ignorant.tick(map,waka);
        assertArrayEquals(new int[]{16,16},ignorant.getCoord());
        assertNull(ignorant.checkNextMove(map));
        assertEquals("down", ignorant.getDirection());

        ignorant.tick(map,waka);
        assertArrayEquals(new int[]{0,576},ignorant.getTargetCoord());
        ignorant.tick(map,waka);
        assertArrayEquals(new int[]{16,16},ignorant.getTargetCoord());

        ignorant = new Ignorant(16,17,1,modeLengths,1);
        ignorant.setDirection("up");
        ignorant.tick(map,waka);
        assertArrayEquals(new int[]{16,16},ignorant.getCoord());
        ignorant.tick(map,waka);
        assertArrayEquals(new int[]{17,16},ignorant.getCoord());
    }

    @Test
    public void itemStatesTest() {
        //Testing that items changes correct states and correct behaviour in each state
        Cell superfruit2 = new Cell(0,0,true,null,"superfruit");
        Ignorant ignorant = new Ignorant(16,16,1,modeLengths,1);
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(ignorant);

        waka.eat(superfruit,ghosts);
        assertTrue(ignorant.isFrightened());
        assertTrue(waka.collision(ghosts));
        ignorant.tick(map,waka);
        ignorant.revive();
        assertEquals("left", ignorant.getDirection());
        assertArrayEquals(new int[]{16,16},ignorant.getCoord());

        waka.eat(superfruit2,ghosts);
        ignorant.tick(map,waka);
        ignorant.tick(map,waka);
        assertFalse(ignorant.isFrightened());
        
        waka.eat(soda,ghosts);
        ignorant.tick(map,waka);
    }
}