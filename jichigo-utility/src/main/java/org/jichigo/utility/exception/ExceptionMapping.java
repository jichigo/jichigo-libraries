package org.jichigo.utility.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ExceptionMapping<Value> {

    private Map<String, Value> mapping;

    public void setMapping(LinkedHashMap<String, Value> mapping) {
        this.mapping = mapping;
    }

    public Value getMappedValue(Class<? extends Exception> exceptionClass) {
        if (mapping == null || mapping.isEmpty()) {
            return null;
        }
        for (Entry<String, Value> entry : mapping.entrySet()) {
            Class<?> cls = exceptionClass;
            while (cls != Object.class) {
                if (cls.getName().contains(entry.getKey())) {
                    return entry.getValue();
                }
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

}
