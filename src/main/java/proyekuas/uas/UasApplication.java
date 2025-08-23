package proyekuas.uas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class UasApplication {

	public static void main(String[] args) {
		SpringApplication.run(UasApplication.class, args);
	}

}
