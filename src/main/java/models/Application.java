package models;

// jdk
import java.util.Arrays;
import java.nio.file.Files;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

import org.joda.time.DateTime;

// spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextQuery;

// jsoup
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;


@SpringBootApplication
public class Application implements CommandLineRunner {
    
    @Autowired
    private NormalizedMetadataRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    private static String[] standards = {"ddi", "eml"};

    @Override
    public void run(String[] args) throws Exception {

        // parse arguments: which or all standards; where are they stored?
        String standard = args[0];

        if (standard.equals("help"))
        {
            System.out.println(
                "\n\nUsage:\n\tjava -jar build/libs/mongo-test-0.1.0.jar (ddi,eml) {metadata directory}\n\tExample:\n\t\tjava -jar build/libs/mongo-test-0.1.0.jar icpsr metadata/icpsr\n\n"
            );
            System.exit(0);
        }
        
        String metadataDirectory = args[1];
        
        // iterate over directory if a directory
        Path path = Paths.get(metadataDirectory);
        if (!Files.exists(path))
        {
            throw new BadInputDirectory(metadataDirectory);
        }

        try 
        {
            Files.walk(path).forEach(
                f -> 
                {
                    if (f.toString().contains("xml"))
                    {
                        NormalizedMetadata nm = Parsers.ddi(f);

                        repository.save(nm);
                    }
                }
            );
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }



            //System.out.println("Parallel:\n");
            //Files.walk(path).parallel().forEach(System.out::println);
            //*** As expected, prints the numbers out of order in parallel **/
            //***************************************************************/
            //IntStream.range(0, 100).parallel().forEach(System.out::println);

            //System.out.println("Serial:\n");
            //Files.walk(path).forEach(System.out::println);
            //IntStream.range(0, 100).forEach(System.out::println);

            //Files.walk(path).parallel().forEach(

        
        //System.out.println("\nFirst the mongo stuff!!");
        //System.out.println("----------------------------------\n");
        //System.out.println(args.length);
        //if (args.length > 0)
        //{
            //System.out.println("args: " + Arrays.toString(args));
        //}

        //mongoExample();

        //System.out.println("\nNow printing some xml geog covers!!");
        //System.out.println("----------------------------------\n");

        //xmlExample();
    }

    /**
     *
     */
    private void mongoExample()
    {
        // for this example clear out customers
        repository.deleteAll();

        // Create new norm metadata records
        repository.save(new NormalizedMetadata(
            "yo mama", 
            new String[] {"Henry IV", "Lupe", "Muffin"},
            "<xml>yeah!</xml",
            "x0xxdfj114",
            new DateTime(1982, 4, 27, 14, 22),
            new DateTime(1986, 3, 25, 19, 17)
            )
        );

        repository.save(new NormalizedMetadata(
            "mama", 
            new String[] {"Henry IV", "Lupe", "Muffin"},
            "<xml>yeah!</xml",
            "x0xxdfj114",
            new DateTime(1982, 4, 27, 14, 22),
            new DateTime(1986, 3, 25, 19, 17)
            )
        );

        repository.save(new NormalizedMetadata(
            "hoochie", 
            new String[] {"Giorno", "Muffin"},
            "<xml>ohh yeah!</xml",
            "x0xdfj1",
            new DateTime(1982, 4, 27, 14, 22),
            new DateTime(1986, 3, 25, 19, 17)
            )
        );

        System.out.println(
            "\n\nFinding by LIKE title 'mama' using findByTitleLike:\n\n");

        repository.findByTitleLike("mama").forEach(rec ->
            System.out.println(rec)
        );

        System.out.println(
            "\n\nFinding by title exactly matching 'mama' using findByTitle:\n\n");

        repository.findByTitle("mama").forEach(rec ->
            System.out.println(rec)
        );

        System.out.println("\n\nPrinting all:\n\n");

        repository.findAll().forEach(rec ->
            System.out.println(rec)
        );
    }


    /**
     * Parse DDI metadata, create NormalizedMetadata record.
     *
     * Can be used alone or as part of a stream
     * 
     * @param path path to XML file
     */
    //static NormalizedMetadata parseDDI(Path path) throws Exception
    //{
        //// 
        //if (path.toString().contains("xml"))
        //{
            //System.out.println(
                //"I would be parsing XML for file " + path + "!"
            //);
            ////NormalizedMetadata.fromDDI(path);
        //}   
        //else
        //{
            //throw new Exception(
                //String.format("We only parse XML files, not %s", path)
            //);
        //}
    //}
    

    /**
     *
     */
    private void xmlExample()
    {
        try 
        {

        System.out.println("yo. example here...");    

        String path = 
            "/Users/mturner/workspace/rda-lod/data/icpsr-ddi-metadata/36155.xml";

        File file = new File(path);

        Document doc = Jsoup.parse(file, "UTF-8");

        Elements geogCovers = doc.getElementsByTag("geogCover");

        for (int i = 0; i < geogCovers.size(); i++)
        {
            System.out.println(
                "geogCover " + Integer.toString(i) + 
                    ": " + geogCovers.get(i).text()
            );
        }

        System.out.println("\n/*** Now for the IntStream version ***/\n");

        // use a stream; this can be automatically parallelized for, say,
        // parsing metadata into the database
        IntStream.range(0, geogCovers.size()).forEach(
            (i) ->
            {
                System.out.println(
                    "geogCover " + Integer.toString(i) +
                    ": " + geogCovers.get(i).text()
                );
            } 
        );

        System.out.println("\n/*************************************/");
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}


class ParserNotFoundException extends Exception
{
      //Parameterless Constructor
      public ParserNotFoundException() {}

      //Constructor that accepts a message
      public ParserNotFoundException(String userParser)
      {
         super("Parser " + userParser + " could not be found");
      }
}


class BadInputDirectory extends Exception
{
      //Parameterless Constructor
      public BadInputDirectory() {}

      //Constructor that accepts a message
      public BadInputDirectory(String userDirectory)
      {
         super("Directory " + userDirectory + " could not be found or is not a directory");
      }
}
