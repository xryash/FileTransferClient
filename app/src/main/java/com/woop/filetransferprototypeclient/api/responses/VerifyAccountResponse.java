package com.woop.filetransferprototypeclient.api.responses;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NoID on 24.02.2018.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})

public class VerifyAccountResponse {
    @JsonProperty("data")
    private String data;

    /**
     * No args constructor for use in serialization
     *
     */
    public VerifyAccountResponse() {
    }

    /**
     *
     * @param data
     */
    public VerifyAccountResponse(String data) {
        super();
        this.data = data;
    }



    @JsonProperty("data")
    public String getData() {
        return data;
    }

    @JsonProperty("data")
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "VerifyAccountResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}
