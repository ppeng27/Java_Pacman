package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;

public class MapTest {

    @Test 
    public void MapConstructor() {
        //Testing Map object can be initialised.
        Map map = new Map("map.txt");
        assertNotNull(map);
    }

    @Test 
    public void wakaStartTest() {
        //testing the starting waka postion is identified.
        Map map = new Map("map.txt");
        assertArrayEquals(new int[]{208,320}, map.wakaStart());
        map = new Map("maptest2.txt");
        assertNull(map.wakaStart());
    }

    @Test 
    public void fruitCount() {
        //Testing fruit is correctly counted
        Map map = new Map("map.txt");
        assertEquals(300, map.countFruit());
    }

    @Test

    public void getterTest() {
        //Testing getter methods are correct
        Map map = new Map("map1.txt");
        String[] str26 = new String[] {"2","p","7","7","7","7","7","7","8","7","7","7","7","7","7","7","7","7","7","7","7","7","7","7","7","7","c","2"};
        String[][] mapStr = map.getMapStr();
        for(int i = 0; i< mapStr[0].length; i++) {
            assertEquals(str26[i],mapStr[26][i]);
        }

        Cell[][] cells= map.getMapCells();
        String f = "fruit";
        String w = "wall";
        String e = "empty";
        String sf = "superfruit";
        String[] cells26 = new String[] {w,e,f,f,f,f,f,f,sf,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,e,w};
        for(int i = 0; i< cells[0].length; i++) {
            assertEquals(cells26[i],cells[26][i].getName());
        }
    }
}
