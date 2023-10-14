package com.r0ck1n70sh.csvanalyzer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
public class CsvAnalyzerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvAnalyzerApplication.class, args);
	}

}
