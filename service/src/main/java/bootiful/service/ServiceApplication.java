package bootiful.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.annotation.Id;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

	@Bean
	ApplicationRunner loomDemo() {
		return args -> {
			var threads = new ArrayList<Thread>();
			var names = new ConcurrentSkipListSet<String>();

			for (var i = 0; i < 1000; i++) {
				var first = i == 0;
//				var thread = Thread.ofPlatform().unstarted(() -> {
				var thread = Thread.ofVirtual().unstarted(() -> {
					//InputStream.read()
					//OutputStream.write()
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					if (first) {
						names.add(Thread.currentThread().toString());
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					if (first) {
						names.add(Thread.currentThread().toString());
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					if (first) {
						names.add(Thread.currentThread().toString());
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
					if (first) {
						names.add(Thread.currentThread().toString());
					}
				});
				threads.add(thread);
			}
			for (var t : threads) t.start();
			for (var t : threads) t.join();

			System.out.println(names.toString());
		};
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

@Controller
@ResponseBody
class StoryTimeController {

	private final ChatClient singularity;

    StoryTimeController(ChatClient.Builder chatClientBuilder) {
        this.singularity = chatClientBuilder.build();
    }

	@GetMapping("/story")
	Map<String,String> story(){
		var prompt = """
				Dear Singularity,
				
				Please write a story about the amazing Java and Spring developers of
				Bucharest, Romania.
				
				And, please write it in the style of famed children's book author Ion Creanga.
				
				Cordially,
				Alin
				""";
		var request = this.singularity.prompt(new Prompt(prompt));

		return Map.of("story", request.call().content());
	}
}