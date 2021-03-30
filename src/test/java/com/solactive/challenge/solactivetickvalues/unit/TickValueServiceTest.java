package com.solactive.challenge.solactivetickvalues.unit;


import com.solactive.challenge.solactivetickvalues.model.TickValue;
import com.solactive.challenge.solactivetickvalues.service.TickValueService;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class TickValueServiceTest {


    @InjectMocks
    private TickValueService tickValueService;


    private ConcurrentHashMap tickValuesMultiMap;


    private ConcurrentHashMap exportFilesMultimap;


    @Before
    public void setup() {
        tickValuesMultiMap = Mockito.mock(ConcurrentHashMap.class);
        exportFilesMultimap = Mockito.mock(ConcurrentHashMap.class);
        ReflectionTestUtils.setField(tickValueService, "tickValuesMultiMap", tickValuesMultiMap);
        ReflectionTestUtils.setField(tickValueService, "exportFilesMultimap", exportFilesMultimap);
    }


    @Test
    public void shouldGetTickValue() {
        TickValue tickValue = TickValue.builder().closePrice(Double.valueOf("17.5")).price(Double.valueOf("8.5"))
                .epoch_timestamp(LocalDateTime.now()).currency("EUR").ric("IBM.N").build();


        Mockito.when(tickValuesMultiMap.getOrDefault(anyString(), any())).thenReturn(Lists.newArrayList(tickValue));

        List<TickValue> tickValueLists = tickValueService.getTickValuesForRic("IBM.N");

        Assert.assertEquals(1, tickValueLists.size());

    }

    @Test
    public void shouldGetExportedCsvForTickValue() {

        String filePath = "Test.csv";
        Mockito.when(exportFilesMultimap.get(anyString())).thenReturn(new File(filePath));

        File exported = tickValueService.retrieveTickValueCsvForRic("IBM.N");

        Assert.assertEquals("Test.csv", exported.getName());


    }


}
