package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

public class ChaserTest {
    Waka waka;
    Cell empty;
    Cell fruit;
    Cell wall;
    Cell superfruit;
    Cell soda;
    Cell[][] map;
    JSONArray modeLengths;

    @Test 
    public void ChaserConstructor() {
        //Testing the constructor and initial states after construction
        Chaser ghost = new Chaser(0,0,1,null,1);
        assertEquals("c",ghost.getType());
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
                {wall,wall,empty,wall},
                {wall,empty,empty,wall},
                {wall,wall,wall,wall}
            };
    }

    @Test
    public void scatterTest() {
        // Checking correct target while in scatter mode
        Chaser chaser = new Chaser(16,48,1,modeLengths,1);
        String direction = chaser.frightenedMove(map);
        assertEquals("right",direction);
        Chaser chaser2 = new Chaser(32,16,1,modeLengths,1);
        chaser2.frightenedMove(map);
        assertTrue(chaser2.getDirection().equals("left")||chaser2.getDirection().equals("down"));
    }

    @Test public void targetTest() {
        // Checking correct direction is correctly set
        Chaser chaser = new Chaser(16,32,1,modeLengths,1);
        chaser.towardsTarget(map,0,0);
        assertEquals("up",chaser.getDirection());
    }

    @Test
    public void nextMoveTest() {
        //Testing correct direction is chosen according to euclidian distance
        Chaser chaser = new Chaser(17,16,1,modeLengths,1);
        assertNull(chaser.checkNextMove(map));
        assertEquals("left", chaser.getDirection());

        chaser.tick(map,waka);
        assertArrayEquals(new int[]{16,16},chaser.getCoord());
        assertNull(chaser.checkNextMove(map));
        assertEquals("right", chaser.getDirection());

        chaser.tick(map,waka);
        chaser.tick(map,waka);
        assertArrayEquals(new int[]{16,16},chaser.getTargetCoord());
        chaser.tick(map,waka);
        chaser.tick(map,waka);
        assertArrayEquals(new int[]{0,0},chaser.getTargetCoord());

        chaser = new Chaser(32,31,1,modeLengths,1);
        chaser.setDirection("down");
        chaser.tick(map,waka);
        assertArrayEquals(new int[]{32,32},chaser.getCoord());
        chaser.setDirection("up");
        chaser.tick(map,waka);
        assertArrayEquals(new int[]{32,31},chaser.getCoord());
    }

    @Test
    public void turnAroundTest(){
        //Testing that ghost will turn around when no option is left.
        Chaser chaser = new Chaser(16,16,1,modeLengths,1);

        Cell[][] upDownMap = new Cell[][]{
            {wall,wall,wall},
            {wall,empty,wall},
            {wall,empty,wall},
            {wall,wall,wall}
        };

        chaser.setDirection("up");
        chaser.checkNextMove(upDownMap);
        assertEquals("down",chaser.getDirection());
        chaser = new Chaser(16,32,1,modeLengths,1);
        chaser.setDirection("down");
        chaser.checkNextMove(upDownMap);
        assertEquals("up",chaser.getDirection());

        Cell[][] leftRightnMap = new Cell[][]{
            {wall,wall,wall,wall},
            {wall,empty,empty,wall},
            {wall,wall,wall,wall}
        };

        chaser = new Chaser(16,16,1,modeLengths,1);
        chaser.checkNextMove(leftRightnMap);
        assertEquals("right",chaser.getDirection());
        chaser = new Chaser(32,16,1,modeLengths,1);
        chaser.setDirection("right");
        chaser.checkNextMove(leftRightnMap);
        assertEquals("left",chaser.getDirection());
    }
    @Test
    public void statesTest() {
        //Testing that items changes correct states and correct behaviour in each state
        Cell superfruit2 = new Cell(0,0,true,null,"superfruit");
        Chaser chaser = new Chaser(16,16,1,modeLengths,1);
        ArrayList<Ghost> ghosts = new ArrayList<Ghost>();
        ghosts.add(chaser);
        chaser.changeDebug();
        assertTrue(chaser.getDebugMode());
        chaser.changeDebug();
        assertFalse(chaser.getDebugMode());

        waka.eat(superfruit,ghosts);
        assertTrue(chaser.isFrightened());
        assertTrue(waka.collision(ghosts));
        chaser.tick(map,waka);
        chaser.revive();
        assertEquals("left", chaser.getDirection());
        assertArrayEquals(new int[]{16,16},chaser.getCoord());

        waka.eat(superfruit2,ghosts);
        chaser.tick(map,waka);
        waka.eat(soda,ghosts);
        chaser.tick(map,waka);
        assertFalse(chaser.isFrightened());

        chaser.revive();
        assertEquals("left", chaser.getDirection());
        assertArrayEquals(new int[]{16,16},chaser.getCoord());
    }
}