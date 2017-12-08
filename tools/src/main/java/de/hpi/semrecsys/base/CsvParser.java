package de.hpi.semrecsys.base;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.validation.constraints.NotNull;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@SpringBootApplication
public class CsvParser implements CommandLineRunner {

    private Logger log = LoggerFactory.getLogger(CsvParser.class);
    private static final Charset CVS_CHARSET = StandardCharsets.ISO_8859_1;
    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final int PRINT_OUTPUT = 10;
    private int BUFFER_SIZE = 1;

    private int errorCount = 0;
    private long productCount = 0;

    private Path errorFile;

    @Value("${input-folder}")
    private String inputFolder;

    @Value("${output-folder}")
    private String outputFolder;

    @Override
    public void run(String... strings) throws Exception {
        clearOutput(Paths.get(outputFolder));
        convertCsv(Paths.get(inputFolder));
    }

    public static void main(String[] args) {
        SpringApplication.run(CsvParser.class, args);
    }

    private void clearOutput(Path path) throws IOException {
        Files.newDirectoryStream(path, Files::deleteIfExists);
    }

    public void convertCsv(@NotNull Path folder) throws IOException {

        Map<String, JsonProduct> dataMap = new LinkedHashMap<>();

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(folder,
                (p) -> Files.isRegularFile(p) && p.toString().toLowerCase().endsWith(".csv"))) {
            for (Path file : stream) {
                initForFile(file);
                createJSONproductsFromCSV(file, dataMap);
            }
        }
    }

    private void initForFile(Path file) throws IOException {
        log.info("reading CSV file {}", file);
        this.errorFile = Paths.get(file.toAbsolutePath() + "_errors");
        Files.deleteIfExists(errorFile);

        this.errorCount = 0;
        this.productCount = 0;
    }

    private void createJSONproductsFromCSV(Path path, Map<String, JsonProduct> dataMap) throws IOException {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withHeader().withIgnoreEmptyLines();
        CSVParser parser = CSVParser.parse(path.toFile(), CVS_CHARSET, format);
        Map<String, Integer> headers = parser.getHeaderMap();
        log.info("Found headers: ");
        headers.keySet().forEach(header -> log.info(header));
        Map<CsvKey, Integer> columns = new HashMap<>();
                headers.keySet().stream()
                        .filter(CsvKey::hasKey)
                        .forEach(name -> columns.put(CsvKey.valueOf(name), headers.get(name)));

        // Iterator is needed to catch exceptions by iterator.hasNext()
        Iterator<CSVRecord> parserIterator = parser.iterator();

        boolean hasNext = parserIterator.hasNext();
        long lineNr = 0;

        while (hasNext) {
            String key = "UNKNOWN_KEY";
            try {
                hasNext = parserIterator.hasNext();

                if (!hasNext) break;

                CSVRecord csvRecord = parserIterator.next();

                lineNr = parser.getCurrentLineNumber();
                key = csvRecord.get(columns.get(CsvKey.UNIQUE_KEY));
                JsonProduct product = createProduct(columns, csvRecord);
                dataMap.put(product.getId(), product);

                if (lineNr % PRINT_OUTPUT == 0) {
                    log.info("CSV: Processing line: " + lineNr);
                }

                if( lineNr > 0 && lineNr % BUFFER_SIZE == 0){
                    createJsonProducts(dataMap);
                    dataMap.clear();
                }


            } catch (Exception ex) {
                dataMap.remove(key);
                processError(ErrorType.CSVError, lineNr, key, ex);
            }
        }

        log.info("Creating JSON products for products up to " + lineNr);
        createJsonProducts(dataMap);
        log.info("Created JSON products: " + productCount);
        log.info("Errors Occurrences: " + errorCount);
        dataMap.clear();

    }

    private JsonProduct createProduct(Map<CsvKey, Integer> columns, CSVRecord csvRecord) {
        JsonProduct product = new JsonProduct();
        for(Map.Entry<CsvKey, Integer> column : columns.entrySet()){
            Integer columnIdx = column.getValue();
            product.getValues().put(column.getKey(), csvRecord.get(columnIdx));
        }

        return product;
    }


    private void createJsonProducts(Map<String, JsonProduct> dataMap) throws IOException {
        log.info("Creating JSON file for " + dataMap.size() + " products");
        Instant timestamp = Instant.now();

        for (JsonProduct product : dataMap.values()) {
            String jsonFileName = createFileName(product);
            try {

                JsonNode jsonNode = createJsonProduct(product, timestamp);
                Path jsonFile = Paths.get(jsonFileName);

                OBJECT_MAPPER.writeValue(jsonFile.toFile(), jsonNode);

                if (productCount % PRINT_OUTPUT == 0) {
                    log.info("JSON: Processing product:  " + productCount);
                }
            } catch (Exception ex) {
                log.error("Couldn't write to the file " + jsonFileName);
                processError(ErrorType.JSONError, productCount, product.getId(), ex);
            }
            productCount++;
        }
    }

    private JsonNode createJsonProduct(JsonProduct product, Instant timestamp) {

        ObjectNode rootNode = OBJECT_MAPPER.createObjectNode();

        List<JsonNode> attributes = new ArrayList<>();
        for (CsvKey csvKey : CsvKey.values()) {
            Collection<String> values = product.getValues().get(csvKey);

            if (values != null && !values.isEmpty()) {

                if(csvKey.equals(CsvKey.UNIQUE_KEY)){
                    JsonNode node  = toJson(values.iterator().next());
                    rootNode.set("id", node);
                }
                else{
                    ObjectNode node = OBJECT_MAPPER.createObjectNode();
                    node.set("key", toJson(csvKey.name()));
                    node.set("value", toJson(values.iterator().next()));
                    attributes.add(node);
                }
            }
        }
        rootNode.set("attributes", OBJECT_MAPPER.valueToTree(attributes));

        return rootNode;
    }

    private JsonNode toJson(String value) {

        if (value.equals("NULL")) {
            return null;
        }

        return OBJECT_MAPPER.valueToTree(value);
    }


    private String createFileName(JsonProduct product) {
        return outputFolder + "/" + product.getId() + ".json";
    }

    private void processError(ErrorType errorType, Long lineNr, String key, Exception ex) throws IOException {
        log.error("Exception occured for line " + lineNr + " : " + ex.getMessage());
        errorCount++;
        try (BufferedWriter errorWriter = Files.newBufferedWriter(this.errorFile, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            errorWriter.write(errorType.name() + "\t" + lineNr + "\t" + key + "\t" + ex.getMessage() + "\n");
            errorWriter.flush();
        }
    }

    enum ErrorType {CSVError, JSONError}
}
