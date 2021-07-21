package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import org.json.simple.JSONObject;
import java.util.HashMap;

import processing.core.PImage;
import processing.core.PApplet;

public class ReadFileTest {
    @Test
    public void spriteFileTest() {
        App app = new App();
        PApplet.runSketch(new String[]{"string"}, app);
        HashMap<String,PImage> sprites = SpriteFiles.getSprites(app);
        assertNotNull(sprites);

    }

    @Test 
    public void JsonTest() {
        JSONObject config = ReadFile.readJSON("config.json");
        assertNotNull(config);
    }

    @Test
    public void strMapTest() {
        String[][] mapStr = ReadFile.readMapFile("maptest.txt");
        String[] line0 = new String[] {"6","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","5"};
        String[] line1 = new String[] {"2","7","7","7","7","7","8","7","7","7","9","7","7","7","7","7","7","c","2"};
        String[] line2 = new String[] {"4","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","1","3"};
        for(int i = 0; i< mapStr[0].length; i++) {
            assertEquals(line0[i],mapStr[0][i]);
            assertEquals(line1[i],mapStr[1][i]);
            assertEquals(line2[i],mapStr[2][i]);
        }
    }
    
    @Test
    public void cellMapTest() {
        String[][] mapStr = ReadFile.readMapFile("map1.txt");
        Cell[][] cells = ReadFile.giveMapCells(mapStr);
        String f = "fruit";
        String w = "wall";
        String e = "empty";
        String sf = "superfruit";
        String[] line26 = new String[] {w,e,f,f,f,f,f,f,sf,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,f,e,w};
        for(int i = 0; i< cells[0].length; i++) {
            assertEquals(line26[i],cells[26][i].getName());
        }
    }
    // @Test
    // public void exceptionTest() {
    //    ReadFile.readJSON("noFile");
    //    assertE
    // }
}
