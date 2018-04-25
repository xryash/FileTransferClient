package com.woop.filetransferprototypeclient.api.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "data"
})


public class NewAccountResponse {

    @JsonProperty("data")
    private String data;

    /**
     * No args constructor for use in serialization
     *
     */
    public NewAccountResponse() {
    }

    /**
     *
     * @param data
     */
    public NewAccountResponse(String data) {
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
        return "NewAccountResponse{" +
                "data='" + data + '\'' +
                '}';
    }
}
