package stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import freemarker.log.Logger;
import interactions.comunes.ValidarTextosQueContenganArrays;
import interactions.ussd.IngresarOpcionUSSD;
import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import tasks.CerrarErrorServidor;
import tasks.RealizarLlamada;
import utils.EvidenciaUtils;
import utils.WordAppium;

import static userinterfaces.USSDPage.*;
import static utils.PaquetesYOpcionesUSSD.*;

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

    @Then("^Cancelar")
    public void CancealrUSSD() {
        System.out.println("Se cancela flujo");
        theActorCalled(ACTOR_NAME).attemptsTo(
                Click.on(BTN_CANCELAR)
        );
    }

    //Step para realizar la llamada inicial al código USSD
    @Given("^Se realiza la llamada al numero (.*)$")
    public void realizaUnaLlamadaAlNumero(String numero) {
        System.out.println("📞 Iniciando llamada USSD al número: " + numero);

        theActorCalled(ACTOR_NAME).attemptsTo(
                CerrarErrorServidor.siEstaPresente(),
                RealizarLlamada.alNumero(numero)
        );

        System.out.println("✅ Llamada USSD iniciada exitosamente");
    }

    @When("^Valida Menu Inicio$")
    public void verificoQueInformacionPaquetesEstePresente() {
        System.out.println("🔍 Verificando Menu Inicio");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuInicioUSSD) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Ingreso la opcion \"(.*)\" para (.*)$")
    public void ingresoLaOpcionPara(String opcion, String descripcion) {
        System.out.println("⌨️ Ingresando opción: " + opcion + " para: " + descripcion);

        theActorCalled(ACTOR_NAME).attemptsTo(
                IngresarOpcionUSSD.laOpcion(opcion, descripcion)
        );

        System.out.println("✅ Opción ingresada y procesada exitosamente");
    }

    @And("^Valida Menu Compra De Paquetes$")
    public void ValidaMenuCompraDePaquetes() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuCompraDePaquetes) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Op De Paquetes De Datos$")
    public void ValidaMenuOpDePaquetesDeDatos() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesDatos) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Paquetes Todo Incluido$")
    public void ValidaMenuPaquetesTodoIncluido() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesTodoIncluido) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Mas Paquetes Todo Incluido$")
    public void ValidaMenuMasPaquetesTodoIncluido() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesLargaDuracion) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Mas Mas Paquetes Todo Incluido$")
    public void ValidaMenuMasMasPaquetesTodoIncluido() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesEspeciales) // ✅ SIN paréntesis
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Paquetes Especiales Comunidad$")
    public void ValidaMenuPaquetesEspecialesComunidad() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesEspecialesComunidad)
        );

        System.out.println("✅ Información de Menu Paquetes Especiales Comunidad correctamente");
    }

    @And("^Valida Menu Paquetes Relevo Comunidad$")
    public void ValidaMenuPaquetesRelevoComunidad() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesRelevoComunidad)
        );

        System.out.println("✅ Información de Menu Paquetes Relevo Comunidad correctamente");
    }

    @And("^Valida Menu Paquetes Apps$")
    public void ValidaMenuPaquetesApps() {
        System.out.println("🔍 Verificando Menu Paquetes Apps");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesApps)
        );

        System.out.println("✅ Información de Menu Paquetes Apps correctamente");
    }

    @And("^Valida Menu Paquetes Apps Mas$")
    public void ValidaMenuPaquetesAppsMas() {
        System.out.println("🔍 Verificando Menu Paquetes Apps Mas");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesAppsMas)
        );

        System.out.println("✅ Información de Menu Paquetes Apps Mas correctamente");
    }

    @And("^Valida Menu Recargas$")
    public void ValidaMenuRecargas() {
        System.out.println("🔍 Verificando Menu Recargas");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuRecargas)
        );

        System.out.println("✅ Información de Menu Recargas correctamente");
    }

    @And("^Valida Menu Recargas Mas$")
    public void ValidaMenuRecargasMas() {
        System.out.println("🔍 Verificando Menu Recargas Mas");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuRecargasMas)
        );

        System.out.println("✅ Información de Menu Recargas Mas correctamente");
    }

    @And("^Valida Menu Recarga Valores Altos$")
    public void ValidaMenuRecargaValoresAltos() {
        System.out.println("🔍 Verificando Menu Recarga Valores Altos");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuRecargaValoresAltos)
        );

        System.out.println("✅ Información de Menu Recarga Valores Altos correctamente");
    }

    @And("^Valida Menu Beneficios 3x1$")
    public void ValidaMenuBeneficios3x1() {
        System.out.println("🔍 Verificando Menu Beneficios 3x1");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuBeneficios3x1)
        );

        System.out.println("✅ Información de Menu Beneficios 3x1 correctamente");
    }

    @And("^Valida Menu Beneficios 3x1 Consulta$")
    public void ValidaMenuBeneficios3x1Consulta() {
        System.out.println("🔍 Verificando Menu Beneficios 3x1 Consulta");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuBeneficios3x1Consulta)
        );

        System.out.println("✅ Información de Menu Beneficios 3x1 Consulta correctamente");
    }

    @And("^Valida Menu Principal Paquetes$")
    public void ValidaMenuPrincipalPaquetes() {
        System.out.println("🔍 Verificando Menu Principal Paquetes");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPrincipalPaquetes),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Principal Paquetes correctamente");
    }

    @And("^Valida Menu Detalle Si no Hay Un Paquete$")
    public void ValidaMenuDetalleTodoIncluido() {
        System.out.println("🔍 Verificando Menu Detalle Todo Incluido");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(ValidaMenuDetalleSinoHayUnPaquete)
        );

        System.out.println("✅ Información de Menu Detalle Todo Incluido correctamente");
    }

    @And("^Valida Menu Detalle De Consumo$")
    public void ValidaMenuDetalleTodoIncluidoConsumo() {
        System.out.println("🔍 Verificando Menu Detalle Todo Incluido con Consumo");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuDetalleTodoIncluidoConsumo),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Detalle Todo Incluido con Consumo correctamente");
    }

    @And("^Valida Menu Autorizacion Datos$")
    public void ValidaMenuAutorizacionDatos() {
        System.out.println("🔍 Verificando Menu Autorización de Datos");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuAutorizacionDatos)
        );

        System.out.println("✅ Información de Menu Autorización de Datos correctamente");
    }

    @And("^Valida Menu Tipo Documento$")
    public void ValidaMenuTipoDocumento() {
        System.out.println("🔍 Verificando Menu Tipo de Documento");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuTipoDocumento),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Tipo de Documento correctamente");
    }


    //Medios De Pago
    @And("^Valida Menu Medios De Pago De Paquetes De Datos$")
    public void ValidaMenuMediosDePagoPaquetesDeDatos() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoDePaqueteDeDatos), // ✅ SIN paréntesis
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Medios De Pago De Paquetes Todo Incluido$")
    public void ValidaMenuMediosDePagoPaquetesTodoincluido() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoPaquetesTodoIncluido), // ✅ SIN paréntesis
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Inicio correctamente");

    }

    @And("^Valida Menu Medios De Pago De Paquetes El Mas vendido$")
    public void ValidaMenuMediosDePagoPaqueteselMasVendido() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoPaquetesElMasVendido), // ✅ SIN paréntesis
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Inicio correctamente");

    }

    @And("^Valida Menu Medios De Pago De Paquetes Todo Incluido Con PSE$")
    public void ValidaMenuMediosDePagoPaquetesTodoIncluidoConPSE() {
        System.out.println("🔍 Verificando Menu");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMedioDePagoPSE), // ✅ SIN paréntesis
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Inicio correctamente");
    }

    @And("^Valida Menu Paquetes Voz$")
    public void ValidaMenuPaquetesVoz() {
        System.out.println("🔍 Verificando Menu Paquetes Voz");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuPaquetesVoz)
        );

        System.out.println("✅ Información de Menu Paquetes Voz correctamente");
    }

    @And("^Valida Menu Medios De Pago Recarga$")
    public void ValidaMenuMediosDePagoRecarga() {
        System.out.println("🔍 Verificando Menu Medios De Pago Recarga");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoRecarga),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Medios De Pago Recarga correctamente");
    }

    @And("^Valida Menu Medios De Pago Recarga Con Tarjeta$")
    public void ValidaMenuMediosDePagoRecargaConTarjeta() {
        System.out.println("🔍 Verificando Menu Medios de Pago Recarga");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoRecargaConTarjeta),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Medios de Pago Recarga correctamente");
    }

    @And("^Valida Menu Medios De Pago Recarga Con PSE$")
    public void ValidaMenuMediosDePagoRecargaConPSE() {
        System.out.println("🔍 Verificando Menu Medios De Pago Recarga Con PSE");

        theActorCalled(ACTOR_NAME).attemptsTo(
                ValidarTextosQueContenganArrays.validarTexto(MenuMediosDePagoRecargaConPSE),
                Click.on(BTN_CANCELAR),
                WaitFor.aTime(30000)
        );

        System.out.println("✅ Información de Menu Medios De Pago Recarga Con PSE correctamente");
    }

    //Limpia la carpeta de capturas al inicio de cada escenario
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
