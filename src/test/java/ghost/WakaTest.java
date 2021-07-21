package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import processing.core.PApplet;

class WakaTest {
    Waka waka;
    Cell empty;
    Cell fruit;
    Cell wall;
    Cell superfruit;
    Cell soda;
    Cell[][] map;
    Ghost g1;
    ArrayList<Ghost> ghosts;
    
    @BeforeEach
    public void setUpObjects() {
        waka = new Waka(16,16,1,1);
        empty = new Cell(160,80,true,null,"empty");
        fruit = new Cell(160,80,true,null,"fruit");
        wall = new Cell(160,80,false,null,"wall");
        superfruit = new Cell(160,80,true,null,"superfruit");
        soda = new Cell(160,80,true,null,"soda");
        map = new Cell[][]{
                {wall,wall,wall,wall},
                {wall,empty,wall,wall},
                {wall,fruit,empty,wall},
                {wall,wall,wall,wall}
            };
        ghosts = new ArrayList<Ghost>();
        g1 = new Chaser(32,32,1,null,300);
        ghosts.add(g1);
    }

    @Test
    public void constructorTest() {
        //testing Waka object can be initialised with correct initial states.
        assertArrayEquals(new int[]{16,16}, waka.getCoord());
        assertEquals("left",waka.getCurrentDirection());
        assertFalse(waka.isDead());
    }

    @Test
    public void changeDirectionTest() {
        //Testing setter methods for direction
        waka.setDirection("up");
        assertEquals("up",waka.getCurrentDirection());
    }

    @Test
    public void eatTest() {
        //Testing eat method identifies cells correctly.
        assertTrue(waka.eat(fruit,null));
        assertFalse(waka.eat(wall,null));
        assertTrue(waka.eat(superfruit,ghosts));
        assertTrue(waka.eat(soda,ghosts));
    }

    @Test
    public void NextMoveTest() {
        //Testing walls are identified and appropriate movement.
        assertFalse(waka.checkNextMove(map));

        waka.setDirection("up");
        assertFalse(waka.checkNextMove(map));

        waka.setDirection("right");
        assertFalse(waka.checkNextMove(map));

        waka.setDirection("down");
        assertTrue(waka.checkNextMove(map));
    
        waka.setCoord(16,17);
        assertTrue(waka.checkNextMove(map));
    }

    @Test
    public void TurnTest() {
         //testing all directions of turning including direct turning.
        waka.setFutureDirection("up");
        assertFalse(waka.directTurn());
        assertFalse(waka.checkTurn(map));

        waka.setFutureDirection("right");
        assertTrue(waka.directTurn());
        assertFalse(waka.checkTurn(map));

        waka.setFutureDirection("left");
        assertFalse(waka.directTurn());
        assertFalse(waka.checkTurn(map));

        waka.setFutureDirection("down");
        assertFalse(waka.directTurn());
        assertTrue(waka.checkTurn(map));

        waka.setCoord(16,17);
        waka.setFutureDirection("up");
        assertTrue(waka.directTurn());
        assertTrue(waka.checkTurn(map));

        waka.setFutureDirection("up");
        assertTrue(waka.checkTurn(map));
        waka.setFutureDirection("down");
        assertTrue(waka.directTurn());
    }

    @Test
    public void CollisionTest() {
        //Testing collision works properly for both frightened and not frightened states of ghost.
        assertFalse(waka.collision(ghosts));
        assertFalse(waka.isDead());

        waka.setCoord(32,32);
        waka.eat(superfruit,ghosts);
        assertTrue(waka.collision(ghosts));
        assertFalse(waka.isDead());

        g1.revive();
        assertTrue(waka.collision(ghosts));
        assertTrue(waka.isDead());
    }
    @Test
    public void tickTest() {
         //Testing combined methods works correctly with intended function..
        waka.setFutureDirection("down");
        waka.tick(map, ghosts);
        assertArrayEquals(new int[]{16,17},waka.getCoord());
        assertEquals("down",waka.getCurrentDirection());

        waka.tick(map, ghosts);
        assertArrayEquals(new int[]{16,18},waka.getCoord());
        assertEquals("down",waka.getCurrentDirection());

        waka.setCoord(16,32);
        waka.setFutureDirection("right");
        waka.tick(map, ghosts);
        assertArrayEquals(new int[]{17,32},waka.getCoord());
        assertEquals("right",waka.getCurrentDirection());

        waka.setFutureDirection("left");
        waka.tick(map, ghosts);
        assertArrayEquals(new int[]{16,32},waka.getCoord());
        assertEquals("left",waka.getCurrentDirection());

        waka.setFutureDirection("up");
        waka.tick(map, ghosts);
        assertArrayEquals(new int[]{16,31},waka.getCoord());
        assertEquals("up",waka.getCurrentDirection());
    }
}