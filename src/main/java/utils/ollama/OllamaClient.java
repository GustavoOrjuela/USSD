package utils.ollama;

import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Cliente HTTP para interactuar con Ollama API.
 *
 * Características:
 * - Configuración flexible de modelo y parámetros
 * - Manejo robusto de timeouts para modelos pesados
 * - Retry logic para fallos de red
 * - Logging detallado para debugging
 * - Soporte para streaming y non-streaming
 *
 * Configuración via system properties:
 * - ollama.url: URL del servidor Ollama (default: http://127.0.0.1:11434)
 * - ollama.model: Modelo a usar (default: mistral)
 * - ollama.timeout: Timeout en segundos (default: 120)
 * - ollama.enabled: Habilitar/deshabilitar análisis (default: true)
 *
 * Principios SOLID:
 * - SRP: Responsabilidad única de comunicación con Ollama
 * - OCP: Extensible para diferentes modelos y configuraciones
 * - DIP: Depende de abstracciones (OkHttp interfaces)
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class OllamaClient {

    // Configuración por defecto
    private static final String DEFAULT_OLLAMA_URL = "http://127.0.0.1:11434/api/generate";
    private static final String DEFAULT_MODEL = "mistral";
    private static final int DEFAULT_TIMEOUT_SECONDS = 120;
    private static final int DEFAULT_CONNECT_TIMEOUT = 30;
    private static final int DEFAULT_WRITE_TIMEOUT = 60;
    private static final int MAX_RETRIES = 2;

    private final String ollamaUrl;
    private final String model;
    private final int timeoutSeconds;
    private final boolean enabled;
    private final OkHttpClient client;

    /**
     * Constructor por defecto que lee configuración de system properties.
     */
    public OllamaClient() {
        this.ollamaUrl = System.getProperty("ollama.url", DEFAULT_OLLAMA_URL);
        this.model = System.getProperty("ollama.model", DEFAULT_MODEL);
        this.timeoutSeconds = Integer.parseInt(
                System.getProperty("ollama.timeout", String.valueOf(DEFAULT_TIMEOUT_SECONDS))
        );
        this.enabled = Boolean.parseBoolean(System.getProperty("ollama.enabled", "true"));

        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        logConfiguration();
    }

    /**
     * Constructor con configuración personalizada.
     *
     * @param ollamaUrl URL del servidor Ollama
     * @param model Modelo a utilizar
     * @param timeoutSeconds Timeout en segundos
     */
    public OllamaClient(String ollamaUrl, String model, int timeoutSeconds) {
        this.ollamaUrl = ollamaUrl;
        this.model = model;
        this.timeoutSeconds = timeoutSeconds;
        this.enabled = true;

        this.client = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();

        logConfiguration();
    }

    /**
     * Envía un prompt a Ollama y obtiene la respuesta.
     *
     * @param prompt Prompt para el modelo
     * @return Respuesta del modelo
     * @throws IOException Si hay error de comunicación
     */
    public String ask(String prompt) throws IOException {
        if (!enabled) {
            return "[Análisis Ollama deshabilitado via configuración]";
        }

        if (prompt == null || prompt.trim().isEmpty()) {
            throw new IllegalArgumentException("Prompt no puede ser nulo o vacío");
        }

        return askWithRetry(prompt, 0);
    }

    /**
     * Envía prompt con lógica de retry.
     *
     * @param prompt Prompt para enviar
     * @param attemptNumber Número de intento actual
     * @return Respuesta del modelo
     * @throws IOException Si falla después de todos los reintentos
     */
    private String askWithRetry(String prompt, int attemptNumber) throws IOException {
        try {
            return executeRequest(prompt);

        } catch (IOException e) {
            if (attemptNumber < MAX_RETRIES) {
                System.err.println(String.format(
                        "⚠️ [Ollama] Intento %d/%d falló: %s. Reintentando...",
                        attemptNumber + 1, MAX_RETRIES + 1, e.getMessage()
                ));

                // Espera exponencial entre reintentos
                try {
                    Thread.sleep((long) Math.pow(2, attemptNumber) * 1000);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                return askWithRetry(prompt, attemptNumber + 1);
            }

            throw new IOException(
                    String.format("Falló después de %d intentos: %s", MAX_RETRIES + 1, e.getMessage()),
                    e
            );
        }
    }

    /**
     * Ejecuta la petición HTTP a Ollama.
     *
     * @param prompt Prompt a enviar
     * @return Respuesta del modelo
     * @throws IOException Si hay error de comunicación
     */
    private String executeRequest(String prompt) throws IOException {
        // Construir body JSON
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("model", model);
        bodyJson.put("stream", false);
        bodyJson.put("prompt", prompt);

        // Opciones adicionales para mejor rendimiento
        JSONObject options = new JSONObject();
        options.put("temperature", 0.7); // Balance entre creatividad y precisión
        options.put("num_predict", 2000); // Máximo tokens de respuesta
        bodyJson.put("options", options);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                bodyJson.toString()
        );

        Request request = new Request.Builder()
                .url(ollamaUrl)
                .post(body)
                .build();

        System.out.println("🤖 [Ollama] Enviando análisis con modelo: " + model);
        long startTime = System.currentTimeMillis();

        try (Response response = client.newCall(request).execute()) {
            long duration = System.currentTimeMillis() - startTime;

            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "Sin detalles";
                throw new IOException(String.format(
                        "Error de Ollama (HTTP %d): %s",
                        response.code(), errorBody
                ));
            }

            String bodyString = response.body().string();

            // Extraer respuesta del JSON
            try {
                JSONObject json = new JSONObject(bodyString);
                String aiResponse = json.optString("response", bodyString);

                System.out.println(String.format(
                        "✅ [Ollama] Análisis completado en %.2f segundos",
                        duration / 1000.0
                ));

                return aiResponse;

            } catch (Exception e) {
                System.err.println("⚠️ [Ollama] Error parseando JSON, retornando raw response");
                return bodyString;
            }
        }
    }

    /**
     * Verifica si Ollama está disponible y el modelo configurado existe.
     *
     * @return true si Ollama está disponible
     */
    public boolean isAvailable() {
        if (!enabled) {
            return false;
        }

        try {
            // Hacer una petición simple para verificar disponibilidad
            String testPrompt = "Test connection";
            ask(testPrompt);
            return true;

        } catch (Exception e) {
            System.err.println("⚠️ [Ollama] No disponible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene información sobre el modelo configurado.
     *
     * @return Información del modelo
     */
    public String getModelInfo() {
        return String.format(
                "Modelo: %s | URL: %s | Timeout: %ds | Habilitado: %s",
                model, ollamaUrl, timeoutSeconds, enabled
        );
    }

    /**
     * Loggea la configuración actual.
     */
    private void logConfiguration() {
        System.out.println("🔧 [Ollama] Configuración:");
        System.out.println("   - URL: " + ollamaUrl);
        System.out.println("   - Modelo: " + model);
        System.out.println("   - Timeout: " + timeoutSeconds + "s");
        System.out.println("   - Habilitado: " + enabled);
    }

    // Getters
    public String getModel() {
        return model;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    /**
     * Cierra el cliente y libera recursos.
     */
    public void close() {
        if (client != null) {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
        }
    }
}