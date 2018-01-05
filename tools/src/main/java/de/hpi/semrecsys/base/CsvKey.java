package de.hpi.semrecsys.base;

import java.util.stream.Stream;

public enum CsvKey {
    id,
    name,
    description,
    material,
    category,
    tags;

    public static boolean hasKey(String key){
        return Stream.of(CsvKey.values()).anyMatch(csvKey -> csvKey.name().equals(key));
    }

    public static CsvKey UNIQUE_KEY = id;
    public static CsvKey NAME_KEY = name;
}
