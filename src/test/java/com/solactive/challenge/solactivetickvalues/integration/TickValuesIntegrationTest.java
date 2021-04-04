package com.solactive.challenge.solactivetickvalues.integration;


import com.solactive.challenge.solactivetickvalues.SolactiveTickValuesApplication;
import com.solactive.challenge.solactivetickvalues.service.TickValueService;
import org.assertj.core.util.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SolactiveTickValuesApplication.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = {"test"})
public class TickValuesIntegrationTest {

    private static String[] tickValues = new String[]{"TIMESTAMP=1616831761|PRICE=5.24|CLOSE_PRICE=|CURRENCY=EUR|RIC=AAPL.OQ",
            "TIMESTAMP=1616831762|PRICE=5.24|CLOSE_PRICE=|CURRENCY=EUR|RIC=IBM.N",
            "TIMESTAMP=1616831763|PRICE=|CLOSE_PRICE=7.5|CURRENCY=EUR|RIC=AAPL.OQ",
            "TIMESTAMP=1616831764|PRICE=5.24|CLOSE_PRICE=8.0|CURRENCY=EUR|RIC=IBM.N"};


    private static ExecutorService executor = Executors.newFixedThreadPool(4);

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private TestRestTemplate testRestTemplate;


    @Autowired
    private TickValueService tickValueService;

    @Test
    public void shouldSaveTickValues() {

        ResponseEntity<Object> exchange = this.testRestTemplate.exchange(
                UriComponentsBuilder.fromUriString("/solActive/tickValues").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
                createRequest(tickValues[(int) (Math.random() * tickValues.length - 1)], null
                ), Object.class);

        Matcher<HttpStatus> equalToOk = Matchers.equalTo(HttpStatus.OK);

        MatcherAssert.assertThat("Success", exchange.getStatusCode(), Matchers.anyOf(equalToOk));
    }

    @Test
    public void shouldNotReturnTickValuesAsNoTickValuesPosted() {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("ric", "AAPL.OQ");
        ResponseEntity<Object[]> exchange = this.testRestTemplate.exchange(
                UriComponentsBuilder.fromUriString("/solActive/tickValues/{ric}").buildAndExpand(uriVariables).toUri(), HttpMethod.GET,
                createRequest(null, null
                ), Object[].class);

        Matcher<HttpStatus> equalToOk = Matchers.equalTo(HttpStatus.OK);
        Matcher<HttpStatus> equalToNoContent = Matchers.equalTo(HttpStatus.NO_CONTENT);

        MatcherAssert.assertThat("Success", exchange.getStatusCode(), Matchers.anyOf(equalToOk, equalToNoContent));
    }

    @Test
    public void shouldReturnTickValuesAfterPost() {
        this.testRestTemplate.exchange(
                UriComponentsBuilder.fromUriString("/solActive/tickValues").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
                createRequest(tickValues[0], null
                ), Object.class);

        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("ric", "AAPL.OQ");
        ResponseEntity<Object[]> exchange = this.testRestTemplate.exchange(
                UriComponentsBuilder.fromUriString("/solActive/tickValues/{ric}").buildAndExpand(uriVariables).toUri(), HttpMethod.GET,
                createRequest(null, null
                ), Object[].class);

        Matcher<HttpStatus> equalToOk = Matchers.equalTo(HttpStatus.OK);

        MatcherAssert.assertThat("Success", exchange.getStatusCode(), Matchers.anyOf(equalToOk));
    }

    @Test
    public void shouldReturnCsvFileForARic() {
        for (int i = 0; i < tickValues.length; i++) {
            this.testRestTemplate.exchange(
                    UriComponentsBuilder.fromUriString("/solActive/tickValues").buildAndExpand(new HashMap<>()).toUri(), HttpMethod.POST,
                    createRequest(tickValues[i], null
                    ), Object.class);
        }


        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("ric", "AAPL.OQ");
        ResponseEntity exchange = this.testRestTemplate.exchange(
                UriComponentsBuilder.fromUriString("/solActive/tickValues/exportCsvFor/{ric}").buildAndExpand(uriVariables).toUri(), HttpMethod.GET,
                createRequest(null, "csv"
                ), String.class);

        Matcher<HttpStatus> equalToOk = Matchers.equalTo(HttpStatus.OK);

        MatcherAssert.assertThat("Success", exchange.getStatusCode(), Matchers.anyOf(equalToOk));
    }


    public static <T> HttpEntity<T> createRequest(T requestObject, String csv) {
        HttpHeaders headers = new HttpHeaders();

        if (csv != null && csv.equalsIgnoreCase("csv")) {
            headers.setAccept(Lists.newArrayList(MediaType.valueOf("text/csv"), MediaType.APPLICATION_JSON));
            headers.setContentDisposition(ContentDisposition.attachment().filename("Test.csv").build());
        }
        return requestObject != null ? new HttpEntity<>(requestObject, headers) : new HttpEntity<>(headers);
    }
}
