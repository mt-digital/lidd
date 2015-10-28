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
            Parsers.parseDdiDate(yearDate, "start"));

        assertEquals(new DateTime(1990, 12, 31, 23, 59),
            Parsers.parseDdiDate(yearDate, "end"));

        assertEquals(new DateTime(1976, 11, 1, 0, 0),
            Parsers.parseDdiDate(yearMoDate, "start"));

        assertEquals(new DateTime(1976, 11, 30, 23, 59),
            Parsers.parseDdiDate(yearMoDate, "end"));

        assertEquals(new DateTime(2001, 11, 1, 0, 0),
            Parsers.parseDdiDate(isoDate, "start"));

        assertEquals(new DateTime(2001, 11, 1, 23, 59),
            Parsers.parseDdiDate(isoDate, "end"));

        // TODO
        // Test (needs implementation) of handling bad datetimes
    }

    @Test
    public void testEMLParser() throws Exception
    {
        /*
         * eml1
         */
        Path pathEml1 = Paths.get("data/test/eml1.xml");

        String rawEml1 = new String(Files.readAllBytes(pathEml1));

        NormalizedMetadata expectedEml1 = new NormalizedMetadata(
            "88dehltg.txt",
            new String[]
                {"PETERSON, B.J", "DEEGAN, L."},
            rawEml1,
            new DateTime(1988, 1, 1, 0, 0),
            new DateTime(1988, 12, 31, 23, 59)
        );

        NormalizedMetadata generatedNmEml1 = Parsers.eml(pathEml1);

        assertTrue(expectectedEml1.equals(generatedNmEml1));

        /*
         * eml2 
         */
        Path pathEml2 = Paths.get("data/test/eml2.xml");

        String rawEml2 = new String(Files.readAllBytes(pathEml2));

        NormalizedMetadata expectedEml2 = new NormalizedMetadata(
            "88dehltg.txt",
            new String[]
                {"PETERSON, B.J", "DEEGAN, L."},
            rawEml2,
            new DateTime(1988, 1, 1, 0, 0),
            new DateTime(1988, 12, 31, 23, 59)
        );

        NormalizedMetadata generatedNmEml2 = Parsers.eml(pathEml2);

        assertTrue(expectectedEml2.equals(generatedNmEml2));
    }

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
