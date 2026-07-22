package tasks;

import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.questions.Presence;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import utils.CapturaDePantallaMovil;
import utils.MyDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotPresent;
import static userinterfaces.USSDPage.*;

public class RealizarLlamada implements Task {

    private final String numero;
    private static final int MAX_REINTENTOS = 4;

    public RealizarLlamada(String numero) {
        this.numero = numero;
    }

    public static Performable alNumero(String numero) {
        return instrumented(RealizarLlamada.class, numero);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        // 1. App predeterminada (sin cambios, es el primer step y raramente falla)
        if (!Presence.of(TXT_APP_PREDETERMINADA).viewedBy(actor).resolveAll().isEmpty()) {
            actor.attemptsTo(Click.on(BTN_HACER_PREDETERMINADA));
        } else {
            actor.attemptsTo(WaitFor.aTime(1000));
        }

        // 2. Abrir teclado → con retry
        clickConRetry("com.google.android.dialer:id/tab_dialpad",
                "com.google.android.dialer:id/dialpad_fab",
                "Teclado");
        pausa(600);

        // 3. Escribir número → con retry
        escribirConRetry();

        CapturaDePantallaMovil.tomarCapturaPantalla("captura_pantalla");

        // 4. Botón Llamar → con retry
        clickConRetry("com.google.android.dialer:id/dialpad_voice_call_button",
                null,
                "Llamar");

        // 5. Esperar que el spinner desaparezca usando el driver directamente
        esperarSpinnerDirecto();
    }

    // ─── Click con retry sobre cualquier ID de elemento ──────────────────────

    private void clickConRetry(String idPrincipal, String idAlternativo, String nombre) {
        for (int i = 1; i <= MAX_REINTENTOS; i++) {
            cerrarPopupSiPresente();
            try {
                var driver = MyDriver.get();
                driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
                try {
                    List<?> elementos = driver.findElements(By.id(idPrincipal));
                    if (!elementos.isEmpty()) {
                        ((org.openqa.selenium.WebElement) elementos.get(0)).click();
                        System.out.println("✅ [RealizarLlamada] " + nombre + " — click OK (intento " + i + ")");
                        return;
                    }
                    if (idAlternativo != null) {
                        List<?> alt = driver.findElements(By.id(idAlternativo));
                        if (!alt.isEmpty()) {
                            ((org.openqa.selenium.WebElement) alt.get(0)).click();
                            System.out.println("✅ [RealizarLlamada] " + nombre + " alternativo — click OK (intento " + i + ")");
                            return;
                        }
                    }
                    throw new RuntimeException("Elemento '" + nombre + "' no encontrado en pantalla");
                } finally {
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ [RealizarLlamada] StaleElement en '" + nombre + "' intento " + i + " — reintentando");
                if (i == MAX_REINTENTOS) throw e;
                pausa(1000);
            } catch (RuntimeException e) {
                if (i == MAX_REINTENTOS) throw e;
                pausa(1000);
            }
        }
    }

    // ─── Escritura del número con retry ──────────────────────────────────────

    private void escribirConRetry() {
        for (int i = 1; i <= MAX_REINTENTOS; i++) {
            cerrarPopupSiPresente();
            try {
                var driver = MyDriver.get();
                driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
                try {
                    List<?> campos = driver.findElements(By.id("digits"));
                    if (!campos.isEmpty()) {
                        ((org.openqa.selenium.WebElement) campos.get(0)).sendKeys(numero);
                        System.out.println("✅ [RealizarLlamada] Número escrito: " + numero + " (intento " + i + ")");
                        return;
                    }
                    throw new RuntimeException("Campo 'digits' no encontrado");
                } finally {
                    driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
                }
            } catch (StaleElementReferenceException e) {
                System.out.println("⚠️ [RealizarLlamada] StaleElement en 'digits' intento " + i + " — reintentando");
                if (i == MAX_REINTENTOS) throw e;
                pausa(1000);
                clickConRetry("com.google.android.dialer:id/tab_dialpad",
                        "com.google.android.dialer:id/dialpad_fab",
                        "Teclado (reapertura)");
            } catch (RuntimeException e) {
                if (i == MAX_REINTENTOS) throw e;
                pausa(1000);
            }
        }
    }

    // ─── Cierre de popup sin lock (mismo thread) ─────────────────────────────

    private void cerrarPopupSiPresente() {
        var driver = MyDriver.get();
        if (driver == null) return;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            List<?> simClaro = driver.findElements(By.xpath(
                    "//*[contains(@text,'Continua la compra de tus productos Claro')]"));
            if (!simClaro.isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("🛡️ [RealizarLlamada] Popup SIM Claro cerrado");
                pausa(500);
            }
            List<?> explorador = driver.findElements(By.xpath(
                    "//*[contains(@text,'Iniciar el explorador')]"));
            if (!explorador.isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("🛡️ [RealizarLlamada] Popup Iniciar explorador cerrado");
                pausa(500);
            }
        } catch (Exception ignored) {
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }
    /**
     * Espera que el spinner USSD (android:id/progress) desaparezca.
     * Usa el driver directamente para evitar que Serenity cachee el elemento
     * y falle con StaleElement cuando el popup invalida el DOM.
     */
    private void esperarSpinnerDirecto() {
        var driver = MyDriver.get();
        if (driver == null) return;

        long inicio = System.currentTimeMillis();
        long timeout = 30_000; // 30 segundos máximo

        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            while (System.currentTimeMillis() - inicio < timeout) {
                try {
                    List<?> spinners = driver.findElements(By.id("android:id/progress"));
                    if (spinners.isEmpty()) {
                        System.out.println("✅ [RealizarLlamada] Spinner desapareció — menú USSD listo");
                        return;
                    }
                    Thread.sleep(300);
                } catch (Exception e) {
                    // StaleElement u otro error = el spinner ya no existe
                    System.out.println("✅ [RealizarLlamada] Spinner ya no existe en DOM — continuando");
                    return;
                }
            }
            System.out.println("⚠️ [RealizarLlamada] Timeout esperando spinner — continuando de todas formas");
        } finally {
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        }
    }

    private void pausa(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}