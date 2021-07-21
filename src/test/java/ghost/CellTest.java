package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CellTest {

    @Test
    public void getterTest() {
        //Testing all getter methods
        Cell cell = new Cell(160,80,true,null,"fruit");
        assertArrayEquals(new int[]{160,80}, cell.getCoord());
        assertTrue(cell.passable());
        assertNull(cell.getImage());
        assertEquals("fruit",cell.getName());
    }

    @Test
    public void setterTest() {
        //Testing the setter methods
        Cell cell = new Cell(160,80,true,"7","fruit");
        cell.setName("empty");
        assertEquals("empty",cell.getName());
    }

    @Test
    public void isGhostTest() {
        //Testing ghost cells correctly identified.
        assertTrue(Cell.isGhost("c"));
        assertTrue(Cell.isGhost("w"));
        assertTrue(Cell.isGhost("i"));
        assertTrue(Cell.isGhost("a"));
        assertFalse(Cell.isGhost("7"));
    }
}