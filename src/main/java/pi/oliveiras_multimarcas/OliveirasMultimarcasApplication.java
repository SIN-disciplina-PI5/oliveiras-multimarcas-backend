package pi.oliveiras_multimarcas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableJpaAuditing
public class OliveirasMultimarcasApplication {

    public static void main(String[] args) {
        SpringApplication.run(OliveirasMultimarcasApplication.class, args);
    }

}
