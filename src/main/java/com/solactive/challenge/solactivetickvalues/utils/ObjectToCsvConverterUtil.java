package com.solactive.challenge.solactivetickvalues.utils;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.solactive.challenge.solactivetickvalues.model.TickValue;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ObjectToCsvConverterUtil {


    public static final String DELIMITER_UNDERSCORE = "-";

    /**
     * A utility method to convert java bean objects into csv file
     *
     * @param ric        Ric value
     * @param timestamp  Timestamp for the tick
     * @param tickValues List of tick values
     */
    public static File objectToCsvConverter(String ric, String timestamp, List<TickValue> tickValues) {

        String file_location = "TickValues" + DELIMITER_UNDERSCORE + ric + DELIMITER_UNDERSCORE + timestamp.replace(":", "-") + ".csv";
        File file = null;
        try {

            Writer writer = Files.newBufferedWriter(Paths.get(file_location));

            ColumnMappingStrategy mappingStrategy = new ColumnMappingStrategy();
            mappingStrategy.setType(TickValue.class);


            String[] columns = new String[]
                    {"epoch_timestamp", "price", "closePrice", "currency", "ric"};
            mappingStrategy.setColumnMapping(columns);


            StatefulBeanToCsvBuilder<TickValue> builder =
                    new StatefulBeanToCsvBuilder(writer);
            StatefulBeanToCsv beanWriter =
                    builder.withMappingStrategy(mappingStrategy).withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                            .build();


            beanWriter.write(tickValues);

            writer.close();

            file = new File(file_location);


        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to export csv for " + ric, e);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to export csv for " + ric, e);
        }

        return file;
    }
}

