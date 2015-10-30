import models.Parsers;
import models.NormalizedMetadata;
import models.NormalizedMetadataRepository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import org.joda.time.DateTime;


public class ParsersTest
{
    @Test
    public void testDDIDateParser() throws Exception
    {
        String yearDate = "1990";
        String yearMoDate = "1976-11";
        String isoDate = "2001-11-01";
        String badDate = "2012-15-30"; 

        assertEquals(new DateTime(1990, 1, 1, 0, 0),
            Parsers.parseDate(yearDate, "start"));

        assertEquals(new DateTime(1990, 12, 31, 23, 59),
            Parsers.parseDate(yearDate, "end"));

        assertEquals(new DateTime(1976, 11, 1, 0, 0),
            Parsers.parseDate(yearMoDate, "start"));

        assertEquals(new DateTime(1976, 11, 30, 23, 59),
            Parsers.parseDate(yearMoDate, "end"));

        assertEquals(new DateTime(2001, 11, 1, 0, 0),
            Parsers.parseDate(isoDate, "start"));

        assertEquals(new DateTime(2001, 11, 1, 23, 59),
            Parsers.parseDate(isoDate, "end"));

        // TODO
        // Test (needs implementation) of handling bad datetimes
    }


    /**
     * Check expected parsing functionality of EML parser.
     */
    @Test
    public void testEMLParser() throws Exception
    {
        String title;
        String[] authors;
        DateTime startDateTime, endDateTime;
        
        // eml1.xml
        title = "88dehltg.txt";
        authors = new String[] {"DEEGAN, L.", "PETERSON, B.J."};
        startDateTime = new DateTime(1988, 1, 1, 0, 0);
        endDateTime = new DateTime(1988, 12, 31, 23, 59);

        emlTest("data/test/eml1.xml", title, authors, 
                startDateTime, endDateTime);

        // eml2.xml
        title = 
            "Fort Keogh site, station NWS COOP #245690, " +
            "Miles City-Frank Wiley Field, MT, study of precipitation in " + 
            "units of centimeter on a monthly timescale";
        authors = new String[] 
            {"National Climatic Data Center (NCDC)", "EcoTrends Project"};
        startDateTime = new DateTime(1937, 1, 1, 0, 0);
        endDateTime = new DateTime(2009, 9, 30, 23, 59);

        emlTest("data/test/eml2.xml", 
            title, authors, startDateTime, endDateTime);

        // eml3.xml
        title = "Santa Rita Experimental Range site, station Santa Rita Experimental Range pastures where the mesquite were killed and the pastures were burned: pasture 2S, study of plant cover of Krameria parvifolia in units of percent on a yearly timescale";
        authors = new String[] {"Santa Rita Experimental Range", 
            "EcoTrends Project"};
        startDateTime = new DateTime(1953, 1, 1, 0, 0);
        endDateTime = new DateTime(2006, 12, 31, 23, 59);

        emlTest("data/test/eml3.xml", 
            title, authors, startDateTime, endDateTime);

        // eml4.xml
        title = "Sevilleta site, station Rio Salado Grass Study Site, study of animal abundance of Rodentia in units of numberPerTrappingWeb on a yearly timescale";
        authors = new String[] {"Friggens, Mike", "Sevilleta", 
            "EcoTrends Project"};
        startDateTime = new DateTime(1989, 1, 1, 0, 0);
        endDateTime = new DateTime(1998, 12, 31, 23, 59);
        
        emlTest("data/test/eml4.xml",
            title, authors, startDateTime, endDateTime);
    }


    /**
     * Function to test parsing for a given EML file.
     */
    private void emlTest(String testFile, String title, String[] authors,
                         DateTime startDateTime, DateTime endDateTime)
        throws IOException, Exception
    {
        Path path = Paths.get(testFile);

        String raw = new String(Files.readAllBytes(path));
        
        NormalizedMetadata expectedNm = new NormalizedMetadata(
            title, authors, raw, startDateTime, endDateTime
        );

        NormalizedMetadata generatedNm = Parsers.eml(path);

        if (!expectedNm.equals(generatedNm))
        {
            System.out.println("\nExpected: " + expectedNm); 
            System.out.println("\nGenerated: " + generatedNm); 
        }

        assertTrue(expectedNm.equals(generatedNm));
    }

    /**
     * Check expected parsing functionality of DDI parser.
     */
    @Test
    public void testDDIParser() throws IOException
    {
        // path from root project dir
        Path path00010 = Paths.get("data/test/00010.xml");

        // kind of cheating since it's exactly what the parser does
        String raw00010 = new String(
            Files.readAllBytes(path00010)
        );

        NormalizedMetadata expectedNm00010 = new NormalizedMetadata(
            "United States Congressional District Data Books, 1961-1965",
            new String[] 
                {"United States Department of Commerce. Bureau of the Census"},
            raw00010,
            new DateTime(1961, 1, 1, 0, 0),
            new DateTime(1965, 12, 31, 23, 59)
        );

        NormalizedMetadata generatedNm00010 = Parsers.ddi(path00010);

        assertTrue(generatedNm00010.equals(expectedNm00010));
        
        
        /*
         * 13233.xml has <timePrd event="single" date="2000" cycle="P1"></timePrd>
         */ 
        Path path13233 = Paths.get("data/test/13233.xml");

        String raw13233 = new String(
            Files.readAllBytes(path13233)
        );

        NormalizedMetadata expectedNm13233 = new NormalizedMetadata(
            "Census of Population and Housing, 2000 [United States]: Summary File 2, Alabama",
            new String[] 
                {"United States Department of Commerce. Bureau of the Census"},
            raw13233,
            new DateTime(2000, 1, 1, 0, 0),
            new DateTime(2000, 12, 31, 23, 59)
        );

        NormalizedMetadata generatedNm13233 = Parsers.ddi(path13233);

        assertTrue(generatedNm13233.equals(expectedNm13233));

        /*
         * 09248.xml has shorter time period with full iso
         */
        Path path09248 = Paths.get("data/test/09248.xml");

        String raw09248 = new String(
            Files.readAllBytes(path09248)
        );

        NormalizedMetadata expectedNm09248 = new NormalizedMetadata(
            "ABC News West Germany Poll, May 1989",
            new String[] 
                {"ABC News"},
            raw09248,
            new DateTime(1989, 5, 12, 0, 0),
            new DateTime(1989, 5, 22, 23, 59)
        );

        NormalizedMetadata generatedNm09248 = Parsers.ddi(path09248);

        assertTrue(generatedNm09248.equals(expectedNm09248));

        /*
         * 36053.xml has three authors
         */
        Path path36053 = Paths.get("data/test/36053.xml");

        String raw36053 = new String(
            Files.readAllBytes(path36053)
        );

        NormalizedMetadata expectedNm36053 = new NormalizedMetadata(
            "Cognition and Aging in the USA (CogUSA) 2007-2009",
            new String[] 
                {"McArdle, John", "Rodgers, Willard", "Willis, Robert"},
            raw36053,
            new DateTime(2007, 1, 1, 0, 0),
            new DateTime(2009, 12, 31, 23, 59)
        );

        NormalizedMetadata generatedNm36053 = Parsers.ddi(path36053);

        assertTrue(generatedNm36053.equals(expectedNm36053));
    }
}
