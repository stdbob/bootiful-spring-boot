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

interface Loan {}

class SecuredLoan implements Loan {}

class UnsecuredLoan implements Loan {}

class Message {
	String messageForLoan(Loan l){
		if(l instanceof SecuredLoan) {
			var sl = (SecuredLoan)l;
			return "good job on getting a secured loan!";
		}
		if(l instanceof UnsecuredLoan) {
			var usl = (UnsecuredLoan)l;
			return "oh! too bad";
		}

		throw new IllegalArgumentException("ooops!");
	}
}
record Customer(@Id Integer id, String name){}