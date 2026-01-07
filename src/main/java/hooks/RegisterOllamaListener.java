package hooks;

import net.thucydides.core.steps.StepEventBus;
import cucumber.api.java.Before;

public class RegisterOllamaListener {

    private static volatile boolean registered = false;
    private static final Object lock = new Object();
    private static OllamaStepListener listenerInstance = null;

    @Before(order = 0)
    public void registerListener() {
        if (!registered) {
            synchronized (lock) {
                if (!registered) {
                    try {
                        listenerInstance = new OllamaStepListener();
                        StepEventBus.getEventBus().registerListener(listenerInstance);

                        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
                        System.out.println("║  🔗 OLLAMA LISTENER REGISTRADO                               ║");
                        System.out.println("╚══════════════════════════════════════════════════════════════╝");
                        System.out.println("✅ Análisis IA activado para todos los fallos");

                        // CAMBIO: Usar configuración directamente, no método inexistente
                        String model = System.getProperty("ollama.model", "phi3");
                        String url = System.getProperty("ollama.url", "http://127.0.0.1:11434");
                        int timeout = Integer.parseInt(System.getProperty("ollama.timeout", "240"));

                        System.out.println("📋 Configuración Ollama:");
                        System.out.println("   - Modelo: " + model);
                        System.out.println("   - URL: " + url);
                        System.out.println("   - Timeout: " + timeout + "s");
                        System.out.println("═══════════════════════════════════════════════════════════════\n");

                        registered = true;

                    } catch (Exception e) {
                        System.err.println("❌ [RegisterOllamaListener] Error registrando listener: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static OllamaStepListener getListenerInstance() {
        return listenerInstance;
    }

    public static boolean isRegistered() {
        return registered;
    }

    public static void resetForTesting() {
        synchronized (lock) {
            registered = false;
            listenerInstance = null;
        }
    }
}