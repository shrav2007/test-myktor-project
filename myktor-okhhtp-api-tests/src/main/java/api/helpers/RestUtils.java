package api.helpers;

import okhttp3.*;

import java.io.IOException;

public class RestUtils {
    private final static String BASE_URL = "http://127.0.0.1:8082";
    private final static String TOKEN = "e13180e0-2da2-4acb-97f4-dcca2b3d174b";

    static OkHttpClient client = new OkHttpClient();

    public static Response postResponseNoToken(String json, String path) throws IOException {

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .post(body)
                .build();
        Call call = client.newCall(request);
        return call.execute();
    }

    public static Response postResponse(String json, String path) throws IOException {

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .addHeader("Bearer-Authorization", TOKEN)
                .post(body)
                .build();
        Call call = client.newCall(request);
        return call.execute();
    }

    public static Response deleteResponse(String json, String path) throws IOException {
        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(BASE_URL + path)
                .addHeader("Bearer-Authorization", TOKEN)
                .delete(body)
                .build();
        Call call = client.newCall(request);
        return call.execute();
    }
}