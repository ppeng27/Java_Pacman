package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class WhimTest {
    Waka waka;
    Cell empty;
    Cell fruit;
    Cell wall;
    Cell superfruit;
    Cell soda;
    Cell[][] map;
    JSONArray modeLengths;
    Chaser chaser;

    @Test 
    public void WhimConstructor() {
        //Testing the constructor and initial states after construction
        Whim ghost = new Whim(0,0,1,null,1);
        assertEquals("w",ghost.getType());
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
        chaser = new Chaser(16,32,1,null,1);
    }

    @Test
    public void settingChaser(){
        //Testing that chaser is correctly assigned
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(chaser);
        Whim whim = new Whim(16,16,1,modeLengths,1);
        Ignorant ignorant = new Ignorant(16,16,1,modeLengths,1);
        assertNull(whim.getChaser());
        ghosts.add(whim);
        ghosts.add(ignorant);
        Whim.assignChaserForWhim(ghosts);
        assertNotNull(whim.getChaser());
        assertEquals("c",whim.getChaser().getType());
    }

    @Test 
    public void targetTest() {
        // Checking correct target while in chase mode
        Whim whim = new Whim(16,16,1,modeLengths,1);
        whim.setChaser(chaser);
        int[] target = whim.getTarget(waka);
        assertArrayEquals(new int[]{-48,0},target);

        waka.setDirection("right");
        target = whim.getTarget(waka);
        assertArrayEquals(new int[]{80,0},target);

        waka.setDirection("up");
        target = whim.getTarget(waka);
        assertArrayEquals(new int[]{16,-64},target);

        waka.setDirection("down");
        target = whim.getTarget(waka);
        assertArrayEquals(new int[]{16,64},target);
    }

    @Test
    public void movementTest() {
         //Testing general movement with changing between mode, incrementing coordinate and direction
        Whim whim = new Whim(17,16,1,modeLengths,1);
        whim.setChaser(chaser);
        assertNull(whim.checkNextMove(map));
        assertEquals("left", whim.getDirection());

        whim.tick(map,waka);
        assertArrayEquals(new int[]{16,16},whim.getCoord());
        assertNull(whim.checkNextMove(map));
        assertEquals("down", whim.getDirection());

        whim.tick(map,waka);
        assertArrayEquals(new int[]{448,576},whim.getTargetCoord());

        whim = new Whim(16,48,1,modeLengths,1);
        whim.setChaser(chaser);
        whim.tick(map,waka);
        assertArrayEquals(new int[]{16,47},whim.getCoord());

        whim.setDirection("down");
        whim.tick(map,waka);
        assertArrayEquals(new int[]{16,48},whim.getCoord());
        whim.tick(map,waka);
        assertArrayEquals(new int[]{17,48},whim.getCoord());

        
    }

    @Test
    public void itemStatesTest() {
        //Testing that items changes correct states and correct behaviour in each state
        Cell superfruit2 = new Cell(0,0,true,null,"superfruit");
        Whim whim = new Whim(16,16,1,modeLengths,1);
        whim.setChaser(chaser);
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(whim);

        waka.eat(superfruit,ghosts);
        assertTrue(whim.isFrightened());
        assertTrue(waka.collision(ghosts));
        whim.tick(map,waka);
        whim.revive();
        assertEquals("left", whim.getDirection());
        assertArrayEquals(new int[]{16,16},whim.getCoord());

        waka.eat(superfruit2,ghosts);
        whim.tick(map,waka);
        whim.tick(map,waka);
        assertFalse(whim.isFrightened());
        
        waka.eat(soda,ghosts);
        whim.tick(map,waka);
    }
}