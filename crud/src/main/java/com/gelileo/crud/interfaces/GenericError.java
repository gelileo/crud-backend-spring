package com.gelileo.crud.interfaces;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public interface GenericError {
    HttpStatus getStatus();
    String getDesc();

    default ResponseStatusException exception() {
        return new ResponseStatusException(getStatus(), getDesc());
    }

    default ResponseStatusException exception(String reason) {
        ResponseStatusException ret = exception();
        ret.setDetail(reason);
        return ret;
    }
}
