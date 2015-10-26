/**
 * From the rest-service tutorial at http://spring.io/guides/gs/rest-service.
 *
 * By wrapping Application in @SpringBootApplication there are many individual
 * "convenience annotations" including "@ComponentScan (that) tells Spring to 
 * look for other components, configurations, and services in the `hello` 
 * package, allowing it to find the `GreetingController`". Awesome!
 *
 * So new controllers can be added at will as long as they are wrapped in
 * @RestController and the routes wrapped in @RequestMapping.  See the more
 * advanced http://spring.io/guides/tutorials/bookmarks/ REST API to see how
 * these must be broken apart to, e.g., enable CORS. 
 */
package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;

@SpringBootApplication
public class Application {
    
    // some definitions
    static String personURI    = "http://somewhere/JohnSmith";
    static String fullName     = "John Smith";

    public static void main(String[] args) {
        // XXX Here is the Spring web app, commented out to play with Jena

        // create an empty Model
        Model model = ModelFactory.createDefaultModel();

        // create the resource
        Resource person = model.createResource(personURI);

        // add the property
        person.addProperty(VCARD.FN, fullName);

        Statement stmt = model.listStatements().nextStatement();

        System.out.print("Subject: " + stmt.getSubject().toString() + "\n");
        System.out.print("Predicate: " + stmt.getPredicate().toString() + "\n");
        System.out.print("Object: " + stmt.getObject().toString() + "\n");
        System.out.print("YEEAAAH!" + "\n");
        System.out.print("JSON-LD:" + "\n");
        model.write(System.out, "JSON-LD");

        SpringApplication.run(Application.class, args);
    }
}
