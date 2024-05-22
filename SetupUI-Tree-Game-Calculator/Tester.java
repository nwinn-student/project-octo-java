

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class Tester.
 *
 * @author  Noah Winn
 * @version (a version number or a date)
 */
public class Tester
{
    /**
     * Default constructor for test class Tester
     */
    public Tester()
    {
    }

    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
    }

    @Test
    public void canFrameShow(){
        Frame fram = new Frame();
        fram.initializeFrame();
        assert(fram.isVisible() == true);
    }
    
    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown()
    {
    }
}
