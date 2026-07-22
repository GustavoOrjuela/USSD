package interactions.ussd;

import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.thucydides.core.annotations.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.CapturaDePantallaMovil;
import utils.EvidenciaUtils;

import java.util.List;

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
            EvidenciaUtils.registrarCaptura("Antes de ingresar opción " + opcion + " - " + descripcionPaso);

            escribirOpcionDirecto(actor);
            CapturaDePantallaMovil.tomarCapturaPantalla("opcion_" + opcion + "_ingresada");
            clickEnviarDirecto(actor);

            actor.attemptsTo(WaitFor.aTime(1200));

            EvidenciaUtils.registrarCaptura("Resultado después de enviar opción " + opcion + " - " + descripcionPaso);
            System.out.println("✅ Opción USSD " + opcion + " procesada exitosamente");

        } catch (Exception e) {
            System.err.println("❌ Error al procesar opción USSD " + opcion + ": " + e.getMessage());
            CapturaDePantallaMovil.tomarCapturaPantalla("error_opcion_" + opcion);
            throw new RuntimeException("Fallo al ingresar opción USSD: " + e.getMessage(), e);
        }
    }

    private <T extends Actor> void limpiarCampoSiEsNecesario(T actor) {
        try {
            actor.attemptsTo(
                    net.serenitybdd.screenplay.actions.Click.on(CJA_INGRESAR_OPCION)
            );
            actor.attemptsTo(WaitFor.aTime(500));
        } catch (Exception e) {
            System.out.println("ℹ️ Campo de entrada no necesita limpieza o no está disponible");
        }
    }

    public static IngresarOpcionUSSD compraDePaquetes() {
        return instrumented(IngresarOpcionUSSD.class, "1", "Compra de paquetes");
    }

    public static IngresarOpcionUSSD paqueteMasVendido() {
        return instrumented(IngresarOpcionUSSD.class, "1", "El más vendido");
    }

    public static IngresarOpcionUSSD recargas() {
        return instrumented(IngresarOpcionUSSD.class, "2", "Recargas");
    }

    public static IngresarOpcionUSSD consultaSaldo() {
        return instrumented(IngresarOpcionUSSD.class, "3", "Consulta de saldo y consumos");
    }

    public static IngresarOpcionUSSD opcionPersonalizada(String numero, String descripcion) {
        return instrumented(IngresarOpcionUSSD.class, numero, descripcion);
    }

    /**
     * Escribe la opción directamente con el driver, usando espera reactiva
     * (polling cada 200ms) en vez de implicit wait bloqueante. Ignora
     * StaleElementReferenceException para reintentar la búsqueda si el DOM
     * cambió justo antes de localizar el campo.
     *
     * Nota: se usa la firma (driver, long timeOutInSeconds, long sleepInMillis)
     * porque el proyecto está en Selenium 3.141.59, donde WebDriverWait no
     * tiene overloads con Duration (esos llegaron en Selenium 4).
     */
    private <T extends Actor> void escribirOpcionDirecto(T actor) {
        io.appium.java_client.android.AndroidDriver driver = utils.AndroidObject.androidDriver(actor);
        try {
            WebElement campo = new WebDriverWait(driver, 5L, 200L)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(d -> {
                        List<WebElement> campos = d.findElements(By.id("com.android.phone:id/input_field"));
                        return campos.isEmpty() ? null : campos.get(0);
                    });
            campo.sendKeys(opcion);
            System.out.println("⌨️ [IngresarOpcionUSSD] Opción '" + opcion + "' escrita directamente");
        } catch (Exception e) {
            throw new RuntimeException("Campo input_field no encontrado en pantalla: " + e.getMessage(), e);
        }
    }

    /**
     * Hace click en el botón Enviar con espera reactiva y tolerancia a
     * StaleElementReferenceException. Timeout corto porque el botón puede
     * legítimamente no existir en todos los menús (comportamiento silencioso
     * se conserva).
     */
    private <T extends Actor> void clickEnviarDirecto(T actor) {
        io.appium.java_client.android.AndroidDriver driver = utils.AndroidObject.androidDriver(actor);
        try {
            WebElement boton = new WebDriverWait(driver, 4L, 200L)
                    .ignoring(StaleElementReferenceException.class)
                    .ignoring(NoSuchElementException.class)
                    .until(d -> {
                        List<WebElement> botones = d.findElements(By.id("android:id/button1"));
                        return botones.isEmpty() ? null : botones.get(0);
                    });
            boton.click();
            System.out.println("✅ [IngresarOpcionUSSD] Botón Enviar clickeado");
        } catch (Exception e) {
            System.out.println("ℹ️ [IngresarOpcionUSSD] Botón Enviar no encontrado: " + e.getMessage());
        }
    }

    public String getOpcion() {
        return opcion;
    }

    public String getDescripcionPaso() {
        return descripcionPaso;
    }
}