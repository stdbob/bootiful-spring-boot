package bootiful.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}

record Customer(@Id Integer id, String name){}

interface CustomerRepository extends ListCrudRepository<Customer,Integer> {
}

@Controller
@ResponseBody
class CustomerController {

	private final CustomerRepository repository;

	CustomerController(CustomerRepository repository){
		this.repository = repository;
	}

	@GetMapping("/customers")
	Collection<Customer> customers() {
		return  repository.findAll();
	}
}