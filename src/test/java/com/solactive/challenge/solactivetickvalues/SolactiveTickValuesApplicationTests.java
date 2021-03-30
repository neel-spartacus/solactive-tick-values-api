package com.solactive.challenge.solactivetickvalues;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.boot.test.context.SpringBootTest.*;

@SpringBootTest(classes = { SolactiveTickValuesApplication.class }, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = { "test" })
class SolactiveTickValuesApplicationTests {


	@LocalServerPort
	int randomServerPort;


	@Test
	void contextLoads() {
	}

}
