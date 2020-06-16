package de.unihildesheim.digilib;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        System.out.println(new File("./importfolder").mkdirs());
        SpringApplication.run(Application.class, args);
    }
}
