package com.rbtsb.lms.error;

import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.AbstractMap.*;

public interface ErrorStatus{

    static final Map<String, HttpStatus> codeMapResponse = Collections.unmodifiableMap(new HashMap<String, HttpStatus>() {
        {
            put("200", HttpStatus.OK);
            put("201", HttpStatus.CREATED);
            put("400", HttpStatus.BAD_REQUEST);
            put("401", HttpStatus.UNAUTHORIZED);
            put("404", HttpStatus.NOT_FOUND);
            put("405", HttpStatus.METHOD_NOT_ALLOWED);
            put("422", HttpStatus.UNPROCESSABLE_ENTITY);
            put("500", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    });

}
