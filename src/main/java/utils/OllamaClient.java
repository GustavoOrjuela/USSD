package utils;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class OllamaClient {

    private static final String OLLAMA_URL = "http://127.0.0.1:11434/api/generate";

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    public String ask(String prompt) throws IOException {

        // Construir JSON correcto
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("model", "mistral");
        bodyJson.put("stream", false);
        bodyJson.put("prompt", prompt);

        // ✔ OkHttp 3.x usa este formato
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                bodyJson.toString()
        );

        Request request = new Request.Builder()
                .url(OLLAMA_URL)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {

            if (!response.isSuccessful()) {
                throw new IOException("Error de Ollama: " + response);
            }

            String bodyString = response.body().string();

            // Extraer solo el texto de respuesta
            try {
                JSONObject json = new JSONObject(bodyString);
                return json.optString("response", bodyString);
            } catch (Exception e) {
                return bodyString;
            }
        }
    }
}
