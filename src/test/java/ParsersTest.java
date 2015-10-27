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
    public void testDDIParser() throws IOException
    {
        // path from root project dir
        Path path00010 = Paths.get("data/test/00010.xml");

        // kind of cheating since it's exactly what the parser does
        String raw00010 = new String(
            Files.readAllBytes(path00010)
        );

        NormalizedMetadata expectedNm00010 = new NormalizedMetadata(
                "Voter Turnout in the United States: A Data-Driven Learning Guide",
                new String[] {"United States Department of Commerce. Bureau of the Census"},
                raw00010,
                new DateTime(1984, 6, 20, 0, 0),
                new DateTime(1992, 2, 16, 0, 0)
        );

        NormalizedMetadata generatedNm00010 = Parsers.ddi(path00010.toFile());
        
        assertTrue(generatedNm00010.equals(expectedNm00010));
    }
}
