package epfl.sweng.test;


import epfl.sweng.globals.Globals;

import junit.framework.TestCase;

/**
 * Unit test for JSON methods in QuizQuestion 
 */
public class GlobalsTest extends TestCase {


    public void testGlobals() {
        assertNotNull(new Globals());
    }
}