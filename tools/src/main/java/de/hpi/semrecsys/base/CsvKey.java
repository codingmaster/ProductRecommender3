package de.hpi.semrecsys.base;

import java.util.stream.Stream;

public enum CsvKey {
    _id,
    _title,
    _description;

    public static boolean hasKey(String key){
        return Stream.of(CsvKey.values()).anyMatch(csvKey -> csvKey.name().equals(key));
    }

    public static CsvKey UNIQUE_KEY = _id;
}
