package com.solactive.challenge.solactivetickvalues.utils;

import com.opencsv.bean.ColumnPositionMappingStrategy;



public class ColumnMappingStrategy extends ColumnPositionMappingStrategy {
        private static final String[] HEADER = new String[]{"TIMESTAMP", "PRICE", "CLOSE_PRICE", "CURRENCY", "RIC"};

        @Override
        public String[] generateHeader() {
            return HEADER;
        }
    }

