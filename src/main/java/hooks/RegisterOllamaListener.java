package hooks;

import net.thucydides.core.steps.StepEventBus;
import cucumber.api.java.Before;

/**
 * Hook de Cucumber para registrar el OllamaStepListener en el event bus de Serenity.
 *
 * Este hook se ejecuta ANTES de cada escenario pero registra el listener UNA SOLA VEZ
 * para toda la ejecución, evitando registros duplicados.
 *
 * Orden de ejecución:
 * - order = 0: Se ejecuta PRIMERO, antes de otros hooks
 * - Esto asegura que el listener esté activo desde el inicio
 *
 * Thread-safe:
 * - Usa volatile para visibilidad entre threads
 * - Sincronización para evitar race conditions en ejecución paralela
 *
 * Principios SOLID:
 * - SRP: Responsabilidad única de registrar el listener
 * - OCP: No necesita modificación para cambios en el listener
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class RegisterOllamaListener {

    private static volatile boolean registered = false;
    private static final Object lock = new Object();
    private static OllamaStepListener listenerInstance = null;

    /**
     * Registra el OllamaStepListener una sola vez al inicio de la ejecución.
     *
     * Se ejecuta antes de cada escenario pero el registro es único.
     */
    @Before(order = 0)
    public void registerListener() {
        if (!registered) {
            synchronized (lock) {
                // Double-check locking para thread safety
                if (!registered) {
                    try {
                        listenerInstance = new OllamaStepListener();
                        StepEventBus.getEventBus().registerListener(listenerInstance);

                        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
                        System.out.println("║  🔗 OLLAMA LISTENER REGISTRADO                               ║");
                        System.out.println("╚══════════════════════════════════════════════════════════════╝");
                        System.out.println("✅ Análisis IA activado para todos los fallos");
                        //System.out.println("📋 " + listenerInstance.getModelInfo());
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

    /**
     * Obtiene la instancia del listener (útil para testing).
     *
     * @return Instancia del listener o null si no está registrado
     */
    public static OllamaStepListener getListenerInstance() {
        return listenerInstance;
    }

    /**
     * Verifica si el listener está registrado.
     *
     * @return true si está registrado
     */
    public static boolean isRegistered() {
        return registered;
    }

    /**
     * Resetea el estado de registro (útil para testing).
     * ⚠️ Solo usar en tests unitarios, NO en producción.
     */
    public static void resetForTesting() {
        synchronized (lock) {
            registered = false;
            listenerInstance = null;
        }
    }
}