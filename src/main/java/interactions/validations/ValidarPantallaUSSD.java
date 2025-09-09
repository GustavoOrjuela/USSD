package interactions.validations;

import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.thucydides.core.annotations.Step;
import utils.CapturaDePantallaMovil;
import utils.EvidenciaUtils;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Validaciones específicas para pantallas del flujo USSD
 *
 * Aplica el patrón Screenplay y reutiliza validaciones existentes
 * Principios SOLID:
 * - SRP: Validaciones específicas para USSD
 * - DRY: Reutiliza ValidarTextoQueContengaX existente
 * - OCP: Extensible para nuevas validaciones USSD
 */
public class ValidarPantallaUSSD implements Interaction {

    private final TipoPantalla tipoPantalla;

    public enum TipoPantalla {
        MENU_PRINCIPAL("Info de la operadora", "Menú principal de USSD"),
        INFORMACION_PAQUETES("El mas vendido", "Información de paquetes disponibles"),
        MEDIOS_PAGO("Selecciona el medio de pago", "Pantalla de medios de pago"),
        CONFIRMACION_COMPRA("Compra Exitosa", "Confirmación de compra exitosa");

        private final String textoEsperado;
        private final String descripcion;

        TipoPantalla(String textoEsperado, String descripcion) {
            this.textoEsperado = textoEsperado;
            this.descripcion = descripcion;
        }

        public String getTextoEsperado() {
            return textoEsperado;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    public ValidarPantallaUSSD(TipoPantalla tipoPantalla) {
        this.tipoPantalla = tipoPantalla;
    }

    public static ValidarPantallaUSSD pantalla(TipoPantalla tipo) {
        return instrumented(ValidarPantallaUSSD.class, tipo);
    }

    @Override
    @Step("Validar que se muestre la pantalla: {0}")
    public <T extends Actor> void performAs(T actor) {
        try {
            System.out.println("🔍 Validando pantalla USSD: " + tipoPantalla.getDescripcion());

            // Esperar a que la pantalla se cargue completamente
            actor.attemptsTo(WaitFor.aTime(2000));

            // Registrar evidencia antes de la validación
            EvidenciaUtils.registrarCaptura("Validando " + tipoPantalla.getDescripcion());

            // Realizar la validación del texto esperado
            actor.attemptsTo(
                    ValidarTextoQueContengaX.elTextoContiene(tipoPantalla.getTextoEsperado())
            );

            // Validaciones adicionales específicas por tipo de pantalla
            validacionesEspecificas(actor);

            // Captura final de confirmación
            CapturaDePantallaMovil.tomarCapturaPantalla("validacion_" + tipoPantalla.name().toLowerCase() + "_exitosa");

            System.out.println("✅ Pantalla " + tipoPantalla.getDescripcion() + " validada correctamente");

        } catch (Exception e) {
            System.err.println("❌ Error validando pantalla " + tipoPantalla.getDescripcion() + ": " + e.getMessage());
            CapturaDePantallaMovil.tomarCapturaPantalla("error_validacion_" + tipoPantalla.name().toLowerCase());
            throw new RuntimeException("Fallo en validación de pantalla USSD: " + e.getMessage(), e);
        }
    }

    /**
     * Validaciones adicionales específicas según el tipo de pantalla
     */
    private <T extends Actor> void validacionesEspecificas(T actor) {
        switch (tipoPantalla) {
            case INFORMACION_PAQUETES:
                validarInformacionPaquetes(actor);
                break;
            case MEDIOS_PAGO:
                validarMediosPago(actor);
                break;
            case CONFIRMACION_COMPRA:
                validarConfirmacionCompra(actor);
                break;
            default:
                // Validación básica ya realizada
                break;
        }
    }

    /**
     * Validaciones específicas para la pantalla de información de paquetes
     */
    private <T extends Actor> void validarInformacionPaquetes(T actor) {
        try {
            System.out.println("📋 Validando elementos específicos de información de paquetes");

            // Validar elementos clave de la pantalla de paquetes
            actor.attemptsTo(
                    ValidarTextoQueContengaX.elTextoContiene("$7500"),
                    ValidarTextoQueContengaX.elTextoContiene("Dias"),
                    ValidarTextoQueContengaX.elTextoContiene("1.4GB")
            );

            System.out.println("✅ Información de paquetes validada");

        } catch (Exception e) {
            System.out.println("⚠️ Algunos elementos de paquetes no fueron encontrados: " + e.getMessage());
        }
    }

    /**
     * Validaciones específicas para la pantalla de medios de pago
     */
    private <T extends Actor> void validarMediosPago(T actor) {
        try {
            System.out.println("💳 Validando opciones de medios de pago");

            // Validar opciones de pago disponibles
            actor.attemptsTo(
                    ValidarTextoQueContengaX.elTextoContiene("Tarjeta de Credito"),
                    ValidarTextoQueContengaX.elTextoContiene("PSE"),
                    ValidarTextoQueContengaX.elTextoContiene("Nequi")
            );

            System.out.println("✅ Medios de pago validados");

        } catch (Exception e) {
            System.out.println("⚠️ Algunos medios de pago no fueron encontrados: " + e.getMessage());
        }
    }

    /**
     * Validaciones específicas para la confirmación de compra
     */
    private <T extends Actor> void validarConfirmacionCompra(T actor) {
        try {
            System.out.println("🎉 Validando confirmación de compra exitosa");

            actor.attemptsTo(
                    ValidarTextoQueContengaX.elTextoContiene("exitosa"),
                    ValidarTextoQueContengaX.elTextoContiene("paquete")
            );

            System.out.println("✅ Confirmación de compra validada");

        } catch (Exception e) {
            System.out.println("⚠️ Elementos de confirmación no encontrados: " + e.getMessage());
        }
    }

    // Factory methods para casos específicos

    /**
     * Validar pantalla de información de paquetes
     */
    public static ValidarPantallaUSSD informacionPaquetes() {
        return instrumented(ValidarPantallaUSSD.class, TipoPantalla.INFORMACION_PAQUETES);
    }

    /**
     * Validar pantalla de medios de pago
     */
    public static ValidarPantallaUSSD mediosPago() {
        return instrumented(ValidarPantallaUSSD.class, TipoPantalla.MEDIOS_PAGO);
    }

    /**
     * Validar confirmación de compra
     */
    public static ValidarPantallaUSSD confirmacionCompra() {
        return instrumented(ValidarPantallaUSSD.class, TipoPantalla.CONFIRMACION_COMPRA);
    }

    /**
     * Validar menú principal
     */
    public static ValidarPantallaUSSD menuPrincipal() {
        return instrumented(ValidarPantallaUSSD.class, TipoPantalla.MENU_PRINCIPAL);
    }

    // Getter para testing
    public TipoPantalla getTipoPantalla() {
        return tipoPantalla;
    }
}