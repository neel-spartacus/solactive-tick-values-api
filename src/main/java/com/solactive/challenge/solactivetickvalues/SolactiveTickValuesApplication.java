package com.solactive.challenge.solactivetickvalues;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class
, HibernateJpaAutoConfiguration.class})
@ComponentScan({"com.solactive.challenge.solactivetickvalues"})
public class SolactiveTickValuesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SolactiveTickValuesApplication.class, args);
	}

}
