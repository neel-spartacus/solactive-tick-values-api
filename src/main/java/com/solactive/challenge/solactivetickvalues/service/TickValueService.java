package com.solactive.challenge.solactivetickvalues.service;

import com.solactive.challenge.solactivetickvalues.model.TickValue;
import com.solactive.challenge.solactivetickvalues.utils.ObjectToCsvConverterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@Slf4j
public class TickValueService {


    private ConcurrentMap<String, List<TickValue>> tickValuesMultiMap = new ConcurrentHashMap<String, List<TickValue>>();

    private ConcurrentMap<String, File> exportFilesMultimap = new ConcurrentHashMap<String, File>();


    @Async(value = "tickValueTaskExecutor")
    public void saveTickValues(TickValue tickValue) {

        tickValuesMultiMap.computeIfAbsent(tickValue.getRic(), k -> new CopyOnWriteArrayList<TickValue>()).add(tickValue);

    }


    public List<TickValue> getTickValuesForRic(String ric) {
        return tickValuesMultiMap.getOrDefault(ric, null);

    }

    public File downloadTickValuesForRic(String ric) {
        Optional<List<TickValue>> tickValues = Optional.ofNullable(tickValuesMultiMap.getOrDefault(ric, null));
        Optional<TickValue> optional = tickValues.isPresent() ? tickValues.get().stream().filter(tick -> tick.getClosePrice() != null && !String.valueOf(tick.getClosePrice()).isEmpty()).findAny() : Optional.empty();
        if (optional.isPresent()) {
            try {
                String timestamp = optional.get().getEpoch_timestamp().toString();
                return ObjectToCsvConverterUtil.objectToCsvConverter(ric, timestamp, tickValues.get());

            } catch (Exception
                    e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void exportTickValues(String ric, String timestamp, List<TickValue> tickValues) {

        File exportedCsv = ObjectToCsvConverterUtil.objectToCsvConverter(ric, timestamp, tickValues);
        exportFilesMultimap.put(ric, exportedCsv);
    }

    public File retrieveTickValueCsvForRic(String ric) {
        return exportFilesMultimap.get(ric);
    }
}


