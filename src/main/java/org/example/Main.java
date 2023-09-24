package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sound.sampled.*;
import java.io.IOException;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, LineUnavailableException {
        SpringApplication.run(Main.class);
    }
}