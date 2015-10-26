package models;

import java.nio.file.Path;

import org.joda.time.DateTime;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;


public class Parsers
{
    public Parsers () {};

    public static NormalizedMetadata ddi(Path ddiPath)
    {
        // extract relevant fields from DDI metadata
        
        // insert into new Normalized Metadata instance
        return new NormalizedMetadata("test", 
                                      new String[]{"Some", "Authors"},
                                      "<xml>yo</xml>",
                                      "xx8442",
                                      new DateTime(1996, 1, 1, 14, 47),
                                      new DateTime(2006, 12, 22, 0, 0)
                                      );
    }
}
