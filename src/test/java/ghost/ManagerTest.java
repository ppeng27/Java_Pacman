package ghost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import processing.core.PApplet;

class ManagerTest {

    @Test 
    public void ManagerSetup() {
        //Testing setup has correct functionality and correctly initialises objects.
        Manager manager = new Manager();
        assertNotNull(manager);
        assertNotNull(manager.sprites);
        App app = new App();
        PApplet.runSketch(new String[]{"string"}, app);
        app.delay(100);
        manager.loadImages(app);
        manager.setObjects();
        assertNotNull(manager.getWaka());
        assertEquals(4,manager.getGhosts().size());
        
    }

    @Test
    public void debugTest() {
        //Testing the debugMode works properly
        Manager manager = new Manager();
        App app = new App();
        PApplet.runSketch(new String[]{"string"}, app);
        app.noLoop();
        manager.loadImages(app);
        manager.setObjects();
        manager.debugMode();
        for(Ghost ghost: manager.getGhosts()) {
            assertTrue(ghost.getDebugMode());
        }
    }

    @Test
    public void tickTest() {
        //Testing combined logic.
        Manager manager = new Manager();
        App app = new App();
        PApplet.runSketch(new String[]{"string"}, app);
        app.noLoop();
        manager.loadImages(app);
        manager.setObjects();
        manager.tick();
        assertArrayEquals(new int[]{207,320},manager.getWaka().getCoord());
    }
}
