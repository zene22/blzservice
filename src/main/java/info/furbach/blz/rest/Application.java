package info.furbach.blz.rest;

import info.furbach.blz.rest.blz.Bankleitzahl;
import info.furbach.blz.rest.blz.BankleitzahlRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Configuration
@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(Application.class);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.run(args);
    }

    @Bean
    public CommandLineRunner demo(BankleitzahlRepository repository) {
        return (args) -> {
            Stream<String> stream = null;
            try {
                //aus den Ressourcen, geht nur bei Windows, nicht im Docker
                stream = Files.lines(Paths.get(getClass().getClassLoader().getResource("blz_2018_06_04_txt.txt").toURI()));
            } catch (Exception e) {
                e.printStackTrace();
                //aus dem import Verzeichnis
                stream = Files.lines(Paths.get("/import/blz.txt"));
            }
            stream.filter(line -> !StringUtils.isEmpty(line.substring(139, 150).trim()) && (repository.findByBlz(Long.parseLong(line.substring(0, 8))).isEmpty()))
                    .forEach(line -> repository.save(Bankleitzahl.builder()
                            .bic(line.substring(139, 150))
                            .blz(Long.parseLong(line.substring(0, 8)))
                            .plz(line.substring(67, 72))
                            .name(line.substring(9, 67).trim())
                            .ort(line.substring(72, 107).trim())
                            .build()));
        };
    }
}
