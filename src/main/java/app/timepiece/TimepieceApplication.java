package app.timepiece;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TimepieceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimepieceApplication.class, args);
    }

}
