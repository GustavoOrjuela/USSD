package tasks;

import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;
import utils.CapturaDePantallaMovil;
import utils.EvidenciaUtils;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task que valida la respuesta del sistema USSD tras la selección de paquetes.
 *
 * Existen dos respuestas válidas del sistema:
 *   - Camino 1: El sistema muestra el menú de selección de paquetes disponibles
 *               (texto: "Selecciona para ver detalle")
 *   - Camino 2: El sistema informa que no hay paquetes vigentes
 *               (texto: "No cuentas con paquetes vigentes")
 *
 * La tarea se considera exitosa si CUALQUIERA de los dos textos está presente en pantalla.
 *
 * Principios SOLID aplicados:
 *   - SRP: Responsabilidad única de validar la respuesta USSD post-selección
 *   - OCP: Extensible para agregar más caminos válidos sin modificar la lógica base
 *   - DIP: Depende de abstracciones de AndroidObject y Screenplay
 *
 * @author Senior Test Automation Engineer
 */
public class ValidarRespuesta extends AndroidObject implements Task {

    // ── Textos identificadores de cada camino válido ─────────────────────────
    private static final String TEXTO_CAMINO_1 = "Selecciona para ver detalle";
    private static final String TEXTO_CAMINO_2 = "No cuentas con paquetes vigentes";

    // ── Factory method (patrón Screenplay) ───────────────────────────────────
    public static ValidarRespuesta enPantalla() {
        return instrumented(ValidarRespuesta.class);
    }

    @Override
    @Step("Validar respuesta USSD: menú de paquetes o aviso sin paquetes vigentes")
    public <T extends Actor> void performAs(T actor) {

        // Pequeña espera para que el diálogo USSD se estabilice
        actor.attemptsTo(WaitFor.aTime(2000));

        EvidenciaUtils.registrarCaptura("Antes de validar respuesta USSD");

        System.out.println("🔍 Validando respuesta USSD — verificando caminos válidos...");

        // ── Camino 1: menú con paquetes disponibles ───────────────────────────
        try {
            ElTextoContiene(actor, TEXTO_CAMINO_1);
            System.out.println("✅ Camino 1 detectado: menú de selección de paquetes visible");
            CapturaDePantallaMovil.tomarCapturaPantalla("validar_respuesta_camino1_exitoso");
            return; // Validación superada — salir inmediatamente
        } catch (Exception e) {
            System.out.println("ℹ️ Camino 1 no presente, verificando camino 2...");
        }

        // ── Camino 2: aviso sin paquetes vigentes ─────────────────────────────
        try {
            ElTextoContiene(actor, TEXTO_CAMINO_2);
            System.out.println("✅ Camino 2 detectado: aviso de sin paquetes vigentes visible");
            CapturaDePantallaMovil.tomarCapturaPantalla("validar_respuesta_camino2_exitoso");
        } catch (Exception e) {
            // Ninguno de los dos textos está en pantalla — fallo real
            CapturaDePantallaMovil.tomarCapturaPantalla("validar_respuesta_fallo");
            throw new AssertionError(
                    "❌ ValidarRespuesta falló: ninguno de los textos esperados está visible en pantalla.\n" +
                            "   Esperado (camino 1): \"" + TEXTO_CAMINO_1 + "\"\n" +
                            "   Esperado (camino 2): \"" + TEXTO_CAMINO_2 + "\""
            );
        }
    }
}