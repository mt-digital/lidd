package models;

import java.lang.StringBuilder;
import java.lang.Integer;

import java.io.IOException;
import java.io.File;

import java.nio.file.Path;
import java.nio.file.Files;

import java.time.YearMonth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.joda.time.DateTime;
import org.joda.time.IllegalFieldValueException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;


public class Parsers
{
    public Parsers () {};

    /**
     * 
     */
    public static NormalizedMetadata ddi(Path ddiPath) throws IOException
    {
        File ddiFile = ddiPath.toFile();

        String raw = new String(Files.readAllBytes(ddiPath));

        Document doc = Jsoup.parse(ddiFile, "UTF-8");
        
        // get relevant fields from DDI metadata using private methods below
        String title = ddiTitle(doc);
        
        List<AuthorAffiliation> authAffils = ddiAuthorAffiliations(doc);

        int i = 0;
        String[] authors = new String[authAffils.size()];
        for (AuthorAffiliation aa : authAffils)
        {
            authors[i++] = aa.author;
        }        

        DateTimeCoverage dtCoverage = ddiDateTimeCoverage(doc);

        // insert into new Normalized Metadata instance
        return new NormalizedMetadata(title, 
                                      authors,
                                      raw,
                                      dtCoverage.start,
                                      dtCoverage.end
                                      );
    }

    /**
     *
     */
    public static NormalizedMetadata eml(Path emlPath) throws IOException
    {
        File emlFile = emlPath.toFile();

        String raw = new String(Files.readAllBytes(emlPath));

        Document doc = Jsoup.parse(emlFile, "UTF-8");

        String title = emlTitle(doc);

        List<AuthorAffiliation> authAffils = emlAuthorAffiliations(doc);

        int i = 0;
        String[] authors = new String[authAffils.size()];
        for (AuthorAffiliation aa : authAffils)
        {
            authors[i++] = aa.author;
        }

        DateTimeCoverage dtCoverage = emlDateTimeCoverage(doc);

        return new NormalizedMetadata(
            "", 
            new String[]{""}, 
            raw, 
            dtCoverage.start,
            dtCoverage.end
        );
    }

    private static String ddiTitle(Document doc)
    {
        Elements titleElems = doc.getElementsByTag("titl");

        // if there is no title then title string exsits but is empty
        String title = "";

        title = titleElems.first().text();

        return title;
    }

    private static String emlTitle(Document doc)
    {
        String title = doc.select("dataset>title").text();

        return title;
    }

    private static List<AuthorAffiliation> ddiAuthorAffiliations(Document doc)
    {
        Elements authorElems = doc.getElementsByTag("AuthEnty");
        
        List<AuthorAffiliation> authorAffiliations = 
            new LinkedList<AuthorAffiliation>();

        for (Element el : authorElems)
        {
            authorAffiliations.add(
                new AuthorAffiliation(el.text(), el.attr("affiliation")));
        }

        return authorAffiliations;
    }

    /**
     * See https://goo.gl/TP84k9,  the docs for individualName, for more info 
     * on <contact> field.
     *
     * To summarize, a <contact> may have <individualName>, <organizationName>,
     * and <positionName>. If there is no <individualName> but there is a 
     * <positionName> then anyone at that organization with that position can
     * be considered a valid contact.
     *
     * Then for our AuthorAffiliation, if there is an <organizationName> but
     * no <individualName>, set the author to be the organization. If there is
     * an author but no organization, I'll set the organization to "None".
     */
    private static List<AuthorAffiliation> emlAuthorAffiliations(Document doc)
    {
        Elements creatorElems = doc.select("dataset>creator");

        List<AuthorAffiliation> authorAffiliations = 
            new LinkedList<AuthorAffiliation>();

        for (Element el : creatorElems)
        {
            // logic here to implement comments outline
        }

        return authorAffiliations;
    }

    // TODO use affiliation in the MongoDB
    private static class AuthorAffiliation
    {
        public final String author;
        public final String affiliation;

        public AuthorAffiliation(String author, String affiliation)
        {
            this.author = author;
            this.affiliation = affiliation;
        }
    }

    /**
     * No equivalent to Python Dateutil, so doing some manual string parsing
     */
    public static DateTime parseDate(String dateStr, String startOrEnd)
        throws IllegalFieldValueException, Exception
    {
        int year, month, day, hour, minute;
        if (startOrEnd == "start")
        {
            hour = 0; 
            minute = 0; 
        }
        else if (startOrEnd == "end")
        {
            hour = 23;
            minute = 59;
        }
        else
        {
            throw new Exception("startOrEnd must be 'start' or 'end'");            
        }
        
        // compile regex for year only and iso date
        Pattern yearPattern = 
            Pattern.compile("[12][0-9]{3}");

        Pattern yearMoPattern = 
            Pattern.compile("[12][0-9]{3}-[01][0-9]");

        Pattern isoDatePattern = 
            Pattern.compile("[12][0-9]{3}-[01][0-9]-[0-3][0-9]");

        // see if the date str matches year or iso patterns
        Matcher yearMatcher = yearPattern.matcher(dateStr);
        Matcher yearMoMatcher = yearMoPattern.matcher(dateStr);
        Matcher isoMatcher = isoDatePattern.matcher(dateStr);

        if (isoMatcher.find())
        {
            String[] yrMoDay = dateStr.split("-");
            year = Integer.valueOf(yrMoDay[0]);
            month = Integer.valueOf(yrMoDay[1]);
            day = Integer.valueOf(yrMoDay[2]);
        }
        else if (yearMoMatcher.find())
        {
            String[] yrMo = dateStr.split("-");
            year = Integer.valueOf(yrMo[0]);
            month = Integer.valueOf(yrMo[1]);

            if (startOrEnd == "start")
                day = 1;
            else
            {
                // see SO question; ttp://goo.gl/Mk0nVS
                YearMonth ym = YearMonth.of(year, month);
                day = ym.lengthOfMonth();                
            }
        }
        else if (yearMatcher.find())
        {
            year = Integer.valueOf(dateStr);
            if (startOrEnd == "start")
            {
                month = 1;
                day = 1;
            }
            else
            {
                month = 12;
                day = 31;
            }
        }
        else  // if neither match, put in "no value" dates
        {
            if (startOrEnd == "start")
            {
                year = 1000;
                month = 1;
                day = 1;
            }
            else
            {
                year = 3000;
                month = 12;
                day = 31;
            }
        }

        return new DateTime(year, month, day, hour, minute);
    }

    private static class DateTimeCoverage
    {
        DateTime start;
        DateTime end;

        public DateTimeCoverage(DateTime start, DateTime end) 
        {
            // TODO throw IllegalFieldValueException if start > end
            this.start = start;
            this.end = end;
        }
    }

    /**
     * Time periods for DDI be like...
     * 
     * <timePrd event="start" date="1961" cycle="P1"> </timePrd>
     * <timePrd event="end"   date="1965" cycle="P1"> </timePrd>
     * 
     * So get the event attribute and date attribute, then pass to 
     * parseDate. There are some records with more than one start and and
     * date. If len of startDates or endDates > 1, put all dates in a list,
     * sort, and take the first and last as start and end.
     */
    private static DateTimeCoverage ddiDateTimeCoverage(Document doc)
    {
        // if bad date values are found, catch it and output default
        // 1000 - 3000AD "no data" DateTimeCoverage
        DateTimeCoverage ret = 
            new DateTimeCoverage(
                new DateTime(1000, 1, 1, 0, 0),
                new DateTime(3000, 12, 31, 23, 59)
            );

        DateTime startDateTime, endDateTime;
        try
        {
            // find all timePrd elements
            Elements timePrdElems = doc.getElementsByTag("timePrd");

            // filter into start and end elements by attribute
            Elements startElems = timePrdElems.select("[event=start]");
            Elements endElems = timePrdElems.select("[event=end]");
            Elements singleElems = timePrdElems.select("[event=single]");

            // if n start or n end > 1, put into list, sort ascending, take 
            // first and last as start and end
            if (startElems.size() == 1 && endElems.size() == 1)
            {
                ret = new DateTimeCoverage(
                    parseDate(startElems.first().attr("date"), "start"),
                    parseDate(endElems.last().attr("date"), "end")
                );
            }
            else if (singleElems.size() == 1)
            {
                String dateStr = singleElems.first().attr("date");

                ret = new DateTimeCoverage(
                    parseDate(dateStr, "start"),
                    parseDate(dateStr, "end")
                );
            }
            else
            {
                Elements fullList = new Elements();
                
                fullList.addAll(startElems);
                fullList.addAll(endElems);
                fullList.addAll(singleElems);

                if (fullList.size() > 0)  // if no els found, default return
                {
                    List<DateTime> dts = new ArrayList<DateTime>();
                    for (Element el : startElems)
                    {
                        dts.add(parseDate(el.attr("date"), "start"));
                    }
                    for (Element el : endElems)
                    {
                        dts.add(parseDate(el.attr("date"), "end"));
                    }

                    DateTime start = new DateTime(2000, 1, 1, 0, 0);
                    DateTime end = new DateTime(2000, 1, 1, 0, 0);

                    int i = 0;
                    for (DateTime dt : dts)
                    {
                        if (i++ == 0)
                        {
                            start = dt;
                            end = dt;
                        }
                        else
                        {
                            if (dt.isBefore(start))
                            {
                                start = dt;
                            }
                            else if (dt.isAfter(end))
                            {
                                end = dt;
                            }

                            i++;
                        }
                    }

                    ret = 
                        new DateTimeCoverage(dts.get(0), dts.get(dts.size() - 1));
                }
            } 
        }
        catch (Exception e)  // TODO restrict to IllegalFieldValueException?
        {
            // default ret will be returned with warning message
            System.out.println(
                "Illegal Value Found for Dates in " + doc.location() + ":\n" +
                "Traceback:\n"
            );

            e.printStackTrace();
        }

        return ret;
    }
}
