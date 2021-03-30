package com.solactive.challenge.solactivetickvalues.service;

import com.solactive.challenge.solactivetickvalues.model.TickValue;
import com.solactive.challenge.solactivetickvalues.utils.ObjectToCsvConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class TickValueService {


    private ConcurrentMap<String, List<TickValue>> tickValuesMultiMap = new ConcurrentHashMap<String, List<TickValue>>();

    private ConcurrentMap<String, File> exportFilesMultimap = new ConcurrentHashMap<String, File>();


    @Async(value = "tickValueTaskExecutor")
    /**
     * Method to save tick values to in memory data structure.
     *
     * @param tickValue TickValue object to be saved
     */
    public void saveTickValues(TickValue tickValue) {

        tickValuesMultiMap.computeIfAbsent(tickValue.getRic(), k -> new CopyOnWriteArrayList<TickValue>()).add(tickValue);

    }

    /**
     * Method to retrieve tick values for a particular ric
     *
     * @param ric ric value
     */
    public List<TickValue> getTickValuesForRic(String ric) {
        return tickValuesMultiMap.getOrDefault(ric, null);

    }


    /**
     * Method to export tick values to csv.
     *
     * @param ric        Ric value
     * @param timestamp  Timestamp for the tick
     * @param tickValues List of tick values
     */
    public void exportTickValues(String ric, String timestamp, List<TickValue> tickValues) {

        File exportedCsv = ObjectToCsvConverterUtil.objectToCsvConverter(ric, timestamp, tickValues);
        exportFilesMultimap.put(ric, exportedCsv);
    }

    public File retrieveTickValueCsvForRic(String ric) {
        return exportFilesMultimap.get(ric);
    }
}


