package com.practice.job.practice_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.InvalidPathException;

@SpringBootApplication
public class PracticeRestApplication {

    public static void main(String[] args) throws InvalidPathException {
        SpringApplication.run(PracticeRestApplication.class, args);
    }

}
