package org.example.utils;

import lombok.experimental.UtilityClass;
import net.datafaker.Faker;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

@UtilityClass
public class DataFlowGenerate {

    private final Faker faker = new Faker();

    public static DataFlow generateDataFlow() {
        return DataFlow.builder()
                .open(faker.number().randomDouble(2, 90, 200))
                .close(faker.number().randomDouble(2, 90, 200))
                .volume(faker.number().randomDouble(2, 0, 1))
                .splitFactor(faker.number().randomDouble(2, 0, 1))
                .dividend(faker.number().randomDouble(2, 0, 1))
                .symbol(faker.money().currencyCode())
                .exchange(faker.money().currency())
                .date(LocalDateTime
                        .parse(faker.date().future(1, TimeUnit.DAYS, "YYYY-MM-dd HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
