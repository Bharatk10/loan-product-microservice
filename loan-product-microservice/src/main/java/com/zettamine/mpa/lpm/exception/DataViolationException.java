package com.zettamine.mpa.lpm.exception;

import java.util.Map;

public class DataViolationException extends RuntimeException {
    
    private Map<String, String> conflicts;

    public DataViolationException(Map<String, String> conflicts) {
        super();
        this.conflicts = conflicts;
    }

    public DataViolationException(String message) {
		super(message);
	}

	@Override
    public String getMessage() {
        return mapToString(conflicts);
    }

    private String mapToString(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n{\n   ");
        int size = map.size();
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey()).append(" : ").append(entry.getValue());
            if (++i < size) {
                sb.append(",\n   ");
            }
        }
        sb.append("\n}");
        return sb.toString();
    }

}
