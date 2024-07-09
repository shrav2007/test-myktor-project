package org.example.api.helpers;

import io.restassured.specification.RequestSpecification;
import org.jooq.tools.json.JSONObject;

public class SpecHelper {
    private final static String URL = "http://127.0.0.1:8082";

    public RequestSpecification specPostHelper(String key, JSONObject jsonObj) {
        return Specifications.specBuilder(URL + key)
                .setBody(jsonObj.toString())
                .build();
    }

    public RequestSpecification specPostHelperNoToken(String key, JSONObject jsonObj) {
        return Specifications.specBuilderNoToken(URL + key)
                .setBody(jsonObj.toString())
                .build();
    }

    public RequestSpecification specDeleteHelper(String key, JSONObject jsonObj) {
        return Specifications.specBuilder(URL + key)
                .setBody(jsonObj.toString())
                .build();
    }
}
