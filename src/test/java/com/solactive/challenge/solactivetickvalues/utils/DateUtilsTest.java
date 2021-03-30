package com.solactive.challenge.solactivetickvalues.utils;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class DateUtilsTest {

    @Test
    public void shouldConvertEpochTimestampToLocaleDateTime(){

        String epochTimeStamp="1616958853";

        LocalDateTime dateTime=DateUtils.convertEpochToDate(epochTimeStamp);

        Assert.assertEquals(dateTime.toString(), "2021-03-29T00:44:13");



    }

}
