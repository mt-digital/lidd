package models;

import java.util.Arrays;

import org.joda.time.DateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.index.Indexed;


@Document
public class NormalizedMetadata 
{
    @Id
    private String id;

    @TextIndexed
    private String title;

    private String[] authors;

    private String raw;

    @Indexed
    private DateTime startDateTime;

    @Indexed
    private DateTime endDateTime;

    public NormalizedMetadata() {};

    public NormalizedMetadata(String title, 
                              String[] authors, 
                              String raw, 
                              DateTime startDateTime,
                              DateTime endDateTime)
    {
        this.title = title;
        this.authors = authors;
        this.raw = raw;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public boolean equals(NormalizedMetadata other)
    {
        boolean ret = (
            other.title.equals(this.title) &&
            Arrays.equals(other.authors, this.authors) &&  // ok; 1D array
            other.raw.equals(this.raw) &&
            other.startDateTime.equals(this.startDateTime) &&
            other.endDateTime.equals(this.endDateTime)
        );

        return ret;
    }

    @Override
    public String toString() 
    {
        return String.format(
           "NormalizedMetadata:\n\t" +
               "%s\n\t%s\n\t%s\n\t%s",
           title, Arrays.toString(authors),
           startDateTime.toString(), endDateTime.toString()
       );
    }
}
