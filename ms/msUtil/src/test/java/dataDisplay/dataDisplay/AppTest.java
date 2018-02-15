package dataDisplay.dataDisplay;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.Arrays;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public static void main(String[] args) {
//        String str = "123";
//
//        String[] allRank = new String[]{
//                "A","B"
//        };
//
//        String letter = "A";
//
//        boolean success = Arrays.asList("A","B","C","D").contains(letter);
//        System.out.println(success);

        String str = "";

        String es= StringEscapeUtils.escapeHtml("!@#$%^&*()_+\"\"");

        System.out.println(es);
    }
}
