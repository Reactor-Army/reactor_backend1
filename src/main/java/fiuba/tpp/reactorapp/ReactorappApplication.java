package fiuba.tpp.reactorapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReactorappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactorappApplication.class, args);
	}

}
