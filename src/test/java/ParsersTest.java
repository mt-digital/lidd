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
        String isoDate = "2001-11-01";
        String badDate = "2012-15-30"; 

        assertEquals(new DateTime(1990, 1, 1, 0, 0),
            Parsers.parseDdiDate(yearDate, "start"));

        assertEquals(new DateTime(1990, 12, 31, 23, 59),
            Parsers.parseDdiDate(yearDate, "end"));

        assertEquals(new DateTime(2001, 11, 1, 0, 0),
            Parsers.parseDdiDate(isoDate, "start"));

        assertEquals(new DateTime(2001, 11, 1, 23, 59),
            Parsers.parseDdiDate(isoDate, "end"));

        // TODO
        // Test (needs implementation) of handling bad datetimes
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
    }
}
