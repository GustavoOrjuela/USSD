package interactions.ussd;

import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;
import utils.CapturaDePantallaMovil;
import utils.EvidenciaUtils;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static userinterfaces.USSDPage.*;

/**
 * Interacción para ingresar una opción en el menú USSD y confirmar la selección
 *
 * Principios SOLID aplicados:
 * - SRP: Responsabilidad única de manejar la entrada de opciones USSD
 * - OCP: Extensible para diferentes tipos de validaciones
 * - DIP: Depende de abstracciones del framework Screenplay
 *
 * @author Senior Test Automation Engineer
 */
public class IngresarOpcionUSSD implements Interaction {

    private final String opcion;
    private final String descripcionPaso;

    public IngresarOpcionUSSD(String opcion, String descripcionPaso) {
        this.opcion = opcion;
        this.descripcionPaso = descripcionPaso;
    }

    public static IngresarOpcionUSSD laOpcion(String opcion, String descripcionPaso) {
        return instrumented(IngresarOpcionUSSD.class, opcion, descripcionPaso);
    }

    @Override
    @Step("Ingresar la opción '{0}' en el menú USSD para: {1}")
    public <T extends Actor> void performAs(T actor) {
        try {
            System.out.println("📱 Ingresando opción USSD: " + opcion + " - " + descripcionPaso);

            // Registrar el paso para evidencia
            EvidenciaUtils.registrarCaptura("Antes de ingresar opción " + opcion + " - " + descripcionPaso);

            // Esperar a que el campo de entrada esté disponible
            actor.attemptsTo(WaitFor.aTime(2000));

            // Limpiar campo si hay contenido previo
            limpiarCampoSiEsNecesario(actor);

            // Ingresar la opción seleccionada
            actor.attemptsTo(
                    Enter.theValue(opcion).into(CJA_INGRESAR_OPCION)
            );

            // Captura después de ingresar la opción
            CapturaDePantallaMovil.tomarCapturaPantalla("opcion_" + opcion + "_ingresada");

            // Confirmar la selección con el botón Enviar
            actor.attemptsTo(
                    Click.on(BTN_ENVIAR)
            );

            // Esperar procesamiento de la solicitud
            actor.attemptsTo(WaitFor.aTime(3000));

            // Captura final del resultado
            EvidenciaUtils.registrarCaptura("Resultado después de enviar opción " + opcion + " - " + descripcionPaso);

            System.out.println("✅ Opción USSD " + opcion + " procesada exitosamente");

        } catch (Exception e) {
            System.err.println("❌ Error al procesar opción USSD " + opcion + ": " + e.getMessage());
            CapturaDePantallaMovil.tomarCapturaPantalla("error_opcion_" + opcion);
            throw new RuntimeException("Fallo al ingresar opción USSD: " + e.getMessage(), e);
        }
    }

    /**
     * Limpia el campo de entrada si contiene texto previo
     */
    private <T extends Actor> void limpiarCampoSiEsNecesario(T actor) {
        try {
            // Seleccionar todo el texto del campo y limpiarlo
            actor.attemptsTo(
                    Click.on(CJA_INGRESAR_OPCION)
            );

            // Pausa para asegurar que el campo esté seleccionado
            actor.attemptsTo(WaitFor.aTime(500));

        } catch (Exception e) {
            System.out.println("ℹ️ Campo de entrada no necesita limpieza o no está disponible");
        }
    }

    // Factory methods para casos específicos del flujo USSD

    /**
     * Método específico para seleccionar compra de paquetes (opción 1)
     */
    public static IngresarOpcionUSSD compraDePaquetes() {
        return instrumented(IngresarOpcionUSSD.class, "1", "Compra de paquetes");
    }

    /**
     * Método específico para seleccionar el paquete más vendido (opción 1)
     */
    public static IngresarOpcionUSSD paqueteMasVendido() {
        return instrumented(IngresarOpcionUSSD.class, "1", "El más vendido");
    }

    /**
     * Método específico para seleccionar recargas (opción 2)
     */
    public static IngresarOpcionUSSD recargas() {
        return instrumented(IngresarOpcionUSSD.class, "2", "Recargas");
    }

    /**
     * Método específico para consulta de saldo (opción 3)
     */
    public static IngresarOpcionUSSD consultaSaldo() {
        return instrumented(IngresarOpcionUSSD.class, "3", "Consulta de saldo y consumos");
    }

    /**
     * Método genérico para cualquier opción personalizada
     */
    public static IngresarOpcionUSSD opcionPersonalizada(String numero, String descripcion) {
        return instrumented(IngresarOpcionUSSD.class, numero, descripcion);
    }

    // Getters para testing y debugging
    public String getOpcion() {
        return opcion;
    }

    public String getDescripcionPaso() {
        return descripcionPaso;
    }
}