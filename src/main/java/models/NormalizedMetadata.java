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

    private String identifier;

    @Indexed
    private DateTime startDateTime;

    @Indexed
    private DateTime endDateTime;

    public NormalizedMetadata() {};

    public NormalizedMetadata(String title, 
                              String[] authors, 
                              String raw, 
                              String identifier,
                              DateTime startDateTime,
                              DateTime endDateTime)
    {
        this.title = title;
        this.authors = authors;
        this.raw = raw;
        this.identifier = identifier;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }


    @Override
    public String toString() 
    {
        return String.format(
           "NormalizedMetadata:\n\t" +
               "%s\n\t%s\n\t%s\n\t%s\n\t%s",
           title, Arrays.toString(authors), identifier, 
           startDateTime.toString(), endDateTime.toString()
       );
    }
}
