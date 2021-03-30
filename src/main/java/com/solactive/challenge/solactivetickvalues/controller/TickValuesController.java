package com.solactive.challenge.solactivetickvalues.controller;


import com.solactive.challenge.solactivetickvalues.model.TickValue;
import com.solactive.challenge.solactivetickvalues.service.TickValueService;
import com.solactive.challenge.solactivetickvalues.utils.ObjectToCsvConverterUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.solactive.challenge.solactivetickvalues.utils.DateUtils.convertEpochToDate;

@RestController
@RequestMapping("/")
@Slf4j
@Api(tags = "TickValues")
public class TickValuesController {


    @Autowired
    private TickValueService tickValueService;

    @PostMapping(value = "solActive/tickValues")
    @ApiOperation(value = "Insert/Update tick value for a ric")
    @ApiResponses(value = {
            @ApiResponse(code=200,message = "Success"),
            @ApiResponse(code=401,message = "Unauthorized"),
            @ApiResponse(code=500,message = "Failed to insert/update tick value",response = ResponseEntity.class)
    })
    public ResponseEntity saveTickValues(@RequestBody(required = true) String tickValue) throws IOException {


        String[] tickParams = tickValue.split("\\|");
        if (tickParams.length > 5) {
            log.error("Incorrect number of TickValues in the uploaded file");
            return new ResponseEntity("Incorrect number of TickValues in the uploaded file", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<String> tickValues = Arrays.asList(tickParams);
            Map<String, String> valueMap = tickValues.stream().map(tick -> tick.split("=", -1)).collect(Collectors.toMap(value -> value[0], value -> value.length > 1 ? value[1] : null));

            TickValue tickValueObj = TickValue.builder().epoch_timestamp(valueMap.get("TIMESTAMP") != null ? convertEpochToDate(valueMap.get("TIMESTAMP")) : LocalDateTime.now(ZoneId.systemDefault()))
                    .price(valueMap.get("PRICE") != null && !valueMap.get("PRICE").isEmpty() ? Double.valueOf(valueMap.get("PRICE")) : null)
                    .closePrice(valueMap.get("CLOSE_PRICE") != null && !valueMap.get("CLOSE_PRICE").isEmpty() ? Double.valueOf(valueMap.get("CLOSE_PRICE")) : null)
                    .currency(valueMap.get("CURRENCY")).ric(valueMap.get("RIC")).build();

             tickValueService.saveTickValues(tickValueObj);

            if(valueMap.get("CLOSE_PRICE") != null && !valueMap.get("CLOSE_PRICE").isEmpty() ){
               List<TickValue> tickValuesForRic=tickValueService.getTickValuesForRic(valueMap.get("RIC"));
               String timestamp=convertEpochToDate(valueMap.get("TIMESTAMP")).toString();
               String ric=valueMap.get("RIC");
               tickValueService.exportTickValues(ric, timestamp, tickValuesForRic);


            }

            return new ResponseEntity(tickValueObj,HttpStatus.OK);




        }

    }

    @GetMapping(value = "solActive/tickValues/{ric}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "To look up for tick values for a particular ric", response = TickValue.class)
    @ApiResponses(value = {
            @ApiResponse(code=200,message = "Success"),
            @ApiResponse(code=401,message = "Unauthorized"),
            @ApiResponse(code=500,message = "Failed to retrieve tick values for a ric")
    })
    public ResponseEntity<List<TickValue>> getTickValuesForRic(@PathVariable(value = "ric") String ric) {
        List<TickValue> tickValues = tickValueService.getTickValuesForRic(ric);
        return tickValues!=null && !tickValues.isEmpty()? new ResponseEntity(tickValueService.getTickValuesForRic(ric),HttpStatus.OK)
                :new ResponseEntity("No Tick Values found for ric : " + ric,HttpStatus.NO_CONTENT);

    }

   /* @GetMapping(value = "solActive/tickValues/export/{ric}",produces = "text/csv")
    @ApiOperation(value = "Download tick values for a ric ")
    @ApiResponses(value = {
            @ApiResponse(code=200,message = "Success"),
            @ApiResponse(code=401,message = "Unauthorized"),
            @ApiResponse(code=500,message = "Failed to download the csv file",response = ResponseEntity.class)
    })
    public ResponseEntity downloadTickValuesForRic(@PathVariable(value = "ric") String ric, HttpServletResponse response) {

        File file = tickValueService.downloadTickValuesForRic(ric);

        return file != null ? ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new FileSystemResource(file)) : new ResponseEntity("No file with tick values for " + ric,HttpStatus.NO_CONTENT);

    }*/

    @GetMapping(value = "solActive/tickValues/exportCsvFor/{ric}", produces = "text/csv")
    @ApiOperation(value = "Download tick values for a ric ")
    @ApiResponses(value ={
        @ApiResponse(code=200,message = "Success"),
        @ApiResponse(code=401,message = "Unauthorized"),
        @ApiResponse(code=500,message = "Failed to download the csv file",response = ResponseEntity.class)
    })
    public ResponseEntity downloadTickValuesvCSForRic(@PathVariable(value = "ric") String ric) {

        File file = tickValueService.retrieveTickValueCsvForRic(ric);

        return file != null ? ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new FileSystemResource(file)) : new ResponseEntity("No file with tick values for " + ric,HttpStatus.NO_CONTENT);

    }
}
