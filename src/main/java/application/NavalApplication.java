package application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"application", "entities", "services", "exceptions"})
public class NavalApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(NavalApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Delega a execução para o método da tua consola interativa
        Main.executarConsola(args);
    }
}