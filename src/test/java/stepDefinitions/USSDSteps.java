package stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import freemarker.log.Logger;
import interactions.ussd.IngresarOpcionUSSD;
import interactions.validations.ValidarPantallaUSSD;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import tasks.RealizarLlamada;
import utils.EvidenciaUtils;
import utils.WordAppium;

import java.io.File;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

/**
 * Step Definitions para el flujo completo de USSD
 *
 * Implementa los pasos del caso de prueba @USSD001 siguiendo el patrón Screenplay
 * y manteniendo la consistencia con la arquitectura existente del proyecto.
 *
 * @author Senior Test Automation Engineer
 */
public class USSDSteps {

    private static final Logger LOGGER = Logger.getLogger(WordAppium.class.getName());
    private static final String ACTOR_NAME = "UsuarioUSSD";

    @Before
    public void initScenario(Scenario scenario) {
        OnStage.setTheStage(new OnlineCast());

        // Limpiar evidencias previas
        limpiarCarpetaCapturas();

        // Reiniciar contador de evidencias
        EvidenciaUtils.reiniciarContador();

        LOGGER.info("🚀 Iniciando escenario USSD: " + scenario.getName());
    }

    /**
     * Step para realizar la llamada inicial al código USSD
     */
    @Given("^Se realiza la llamada al numero (.*)$")
    public void realizaUnaLlamadaAlNumero(String numero) {
        System.out.println("📞 Iniciando llamada USSD al número: " + numero);

        theActorCalled(ACTOR_NAME).attemptsTo(
                RealizarLlamada.alNumero(numero)
        );

        System.out.println("✅ Llamada USSD iniciada exitosamente");
    }

    /**
     * Step para ingresar una opción específica en el menú USSD
     */
    @When("^Ingreso la opcion \"(.*)\" para (.*)$")
    public void ingresoLaOpcionPara(String opcion, String descripcion) {
        System.out.println("⌨️ Ingresando opción: " + opcion + " para: " + descripcion);

        theActorCalled(ACTOR_NAME).attemptsTo(
                IngresarOpcionUSSD.laOpcion(opcion, descripcion)
        );

        System.out.println("✅ Opción ingresada y procesada exitosamente");
    }

    /**
     * Step para verificar que la información de paquetes esté presente
     */
    @Then("^Verifico que la informacion de paquetes este presente en pantalla$")
    public void verificoQueInformacionPaquetesEstePresente() {
        System.out.println("🔍 Verificando información de paquetes en pantalla");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarPantallaUSSD.informacionPaquetes()
        );

        System.out.println("✅ Información de paquetes verificada correctamente");
    }

    /**
     * Step para verificar que la información de medios de pago esté presente
     */
    @Then("^Verifico que la informacion de medios de pago este presente en pantalla$")
    public void verificoQueInformacionMediosPagoEstePresente() {
        System.out.println("💳 Verificando información de medios de pago en pantalla");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarPantallaUSSD.mediosPago()
        );

        System.out.println("✅ Información de medios de pago verificada correctamente");
    }

    /**
     * Step genérico para verificar cualquier pantalla USSD
     */
    @Then("^Verifico que se muestre la pantalla de \"(.*)\"$")
    public void verificoQueSeMuestreLaPantallaDe(String tipoPantalla) {
        System.out.println("🔍 Verificando pantalla: " + tipoPantalla);

        ValidarPantallaUSSD.TipoPantalla tipo = mapearTipoPantalla(tipoPantalla);

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarPantallaUSSD.pantalla(tipo)
        );

        System.out.println("✅ Pantalla " + tipoPantalla + " verificada correctamente");
    }

    /**
     * Step para validar texto específico en pantalla
     */
    @Then("^Verifico que el texto \"(.*)\" este presente en pantalla$")
    public void verificoQueElTextoEstePresente(String texto) {
        System.out.println("📝 Verificando presencia del texto: " + texto);

        theActorCalled(ACTOR_NAME).attemptsTo(
                interactions.validations.ValidarTextoQueContengaX.elTextoContiene(texto)
        );

        System.out.println("✅ Texto verificado correctamente");
    }

    // Métodos auxiliares

    /**
     * Mapea strings a enum TipoPantalla para flexibilidad en los steps
     */
    private ValidarPantallaUSSD.TipoPantalla mapearTipoPantalla(String tipoPantalla) {
        switch (tipoPantalla.toLowerCase()) {
            case "informacion de paquetes":
            case "paquetes":
                return ValidarPantallaUSSD.TipoPantalla.INFORMACION_PAQUETES;
            case "medios de pago":
            case "pago":
                return ValidarPantallaUSSD.TipoPantalla.MEDIOS_PAGO;
            case "confirmacion":
            case "confirmacion de compra":
                return ValidarPantallaUSSD.TipoPantalla.CONFIRMACION_COMPRA;
            case "menu principal":
            case "principal":
                return ValidarPantallaUSSD.TipoPantalla.MENU_PRINCIPAL;
            default:
                throw new IllegalArgumentException("Tipo de pantalla no reconocido: " + tipoPantalla);
        }
    }

    /**
     * Limpia la carpeta de capturas al inicio de cada escenario
     */
    private void limpiarCarpetaCapturas() {
        try {
            LOGGER.info("🧹 Limpiando carpeta de capturas...");
            File folder = new File("Capturas");

            if (folder.exists() && folder.isDirectory()) {
                File[] archivos = folder.listFiles();
                if (archivos != null) {
                    for (File file : archivos) {
                        if (file.isFile() && (file.getName().endsWith(".png") || file.getName().endsWith(".jpg"))) {
                            boolean eliminado = file.delete();
                            if (!eliminado) {
                                LOGGER.info("⚠️ No se pudo eliminar: " + file.getName());
                            }
                        }
                    }
                }
            } else {
                folder.mkdirs();
                LOGGER.info("📁 Carpeta Capturas creada");
            }

            LOGGER.info("✅ Limpieza de capturas completada");

        } catch (Exception e) {
            LOGGER.info("⚠️ Error durante limpieza de capturas: " + e.getMessage());
        }
    }
}