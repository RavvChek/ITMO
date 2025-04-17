package part3;

import org.example.part3.Character;
import org.example.part3.Material;
import org.example.part3.Planet;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DomainModelTest {
    Character characterTest;
    Planet planet;
    Material material1;
    Planet.Surface surface;


    @BeforeAll
    static void setUpAll() {
        System.out.println("Running tests of domain model...\n");
    }

    @BeforeEach
    void setUp() {
        characterTest = new Character("Zofand", 30);
        planet = new Planet(10);
        material1 = new Material("gold", 25, 1000, true, 0.5);
        surface = planet.new Surface(material1);
    }

    @Test
    @DisplayName("Check constructor character")
    @Order(1)
    void checkConstructorCharacter() {
        assertEquals("Zofand", characterTest.getName());
    }

    @Test
    @DisplayName("Check availability look around character's")
    @Order(2)
    void checkLookAround() {
        assertFalse(characterTest.lookAround());
        characterTest.standUp();
        assertTrue(characterTest.lookAround());
    }

    @Test
    @DisplayName("Check calc volume materials")
    @Order(3)
    void checkCalcVolume() {
        assertEquals(0.025, material1.calcVolume());
    }

    @Test
    @DisplayName("Check brightness")
    @Order(5)
    void checkBrightness() {
        assertFalse(surface.shine());
        Material material2 = new Material("silver", 25, 1000, false, 0.3);
        surface.setMaterial(material2);
        assertFalse(surface.shine());
        Material material3 = new Material("gold", 25000, 1000, true, 1);
        surface.setMaterial(material3);
        assertTrue(surface.shine());
    }

    @Test
    @DisplayName("Check gets blinded")
    @Order(4)
    void checkGetsBlinded () {
        assertFalse(characterTest.getBlinded(surface));
        surface.setMaterial(new Material("gold_very_brightness", 300, 10, true, 10));
        assertTrue(characterTest.getBlinded(surface));
    }


    @AfterAll
    static void endAll() {
        System.out.println("Ending tests of domain model...");
    }

}
