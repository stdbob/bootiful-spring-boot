package bootiful.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;

@SpringBootApplication
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}

}

sealed interface Loan permits SecuredLoan, UnsecuredLoan {}

final class SecuredLoan implements Loan {}

record UnsecuredLoan(float interest) implements Loan {}

class Message {
	String messageForLoan(Loan l){
		return switch (l) {
			case UnsecuredLoan (var interest) -> "oh! too bad. that interest rate is high! " + interest;
			case SecuredLoan sl -> "good job on getting a secured loan!";
		};
	}
}
record Customer(@Id Integer id, String name){}