package parsers;

// jdk
import java.util.Arrays;
import java.io.File;
import java.util.stream.IntStream;

// spring
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// jsoup
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

@SpringBootApplication
public class Application implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String[] args) throws Exception {

        // parse arguments: which or all standards; where are they stored?
        String standard = 

        assert Arrays.asList({"icpsr", "eml"}).contains(standard);
        
        // iterate over directory 
        
        System.out.println("\nFirst the mongo stuff!!");
        System.out.println("----------------------------------\n");
        System.out.println(args.length);
        if (args.length > 0)
        {
            System.out.println("args: " + Arrays.toString(args));
        }

        mongoExample();

        System.out.println("\nNow printing some xml geog covers!!");
        System.out.println("----------------------------------\n");

        xmlExample();
    }

    /**
     *
     */
    private void mongoExample()
    {
        // for this example clear out customers
        repository.deleteAll();

        // save a couple customers
        repository.save(new Customer("Albert", "Pujols"));
        repository.save(new Customer("Albert", "Einstein"));
        repository.save(new Customer("Mary", "Magdalene"));
        repository.save(new Customer("John", "Magdalene"));

        // execute some queries and print results
        System.out.println("All customers found using findAll():");
        System.out.println("----------------------------------");
        for (Customer customer : repository.findAll())
        {
            System.out.println(customer);
        }

        System.out.println("First customer with first name 'Albert'");
        System.out.println("----------------------------------");
        System.out.println(repository.findByFirstName("Albert"));

        for (Customer customer : repository.findByLastName("Magdalene"))
        {
            System.out.println(customer);
        }
    }
    

    /**
     *
     */
    private void xmlExample()
    {
        try 
        {

        System.out.println("yo. example here...");    

        String path = 
            "/Users/mt/workspace/rda-lod/data/icpsr-ddi-metadata/36155.xml";

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

