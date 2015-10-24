package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    
    @Autowired
    private CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        
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
}
