package com.alpha.system.util;

import java.util.function.Consumer;

import org.openapitools.jackson.nullable.JsonNullable;

public class JsonUtil {
	
	public static <T> void changeIfPresent(JsonNullable<T> nullable, Consumer<T> consumer) {
        if (nullable.isPresent()) {
            consumer.accept(nullable.get());
        }
    }
}
