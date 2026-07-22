package hooks;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import utils.MyDriver;

import java.util.concurrent.TimeUnit;

public class PopupHandler {

    /**
     * Tiempo máximo de búsqueda de elementos (0 = sin espera, detección inmediata).
     * Evita que findElements espere el implicit wait completo cuando no hay popup.
     */
    private static final int IMPLICIT_WAIT_POPUP = 0;

    @Before
    @After
    public void cerrarPopups() {
        PopupGuardian.DRIVER_LOCK.lock();
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver == null) return;

            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            try {
                cerrarPopupSimClaro(driver);
                cerrarPopupIniciarExplorador(driver);
                cerrarPopupErrorConexion(driver);
                cerrarUSSDResidual(driver);
            } finally {
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            }
        } finally {
            PopupGuardian.DRIVER_LOCK.unlock();
        }
    }

    // ─────────────────────────────────────────────────────────────
    // 1. Popup "SIM Claro" / "Claro" → cuerpo identificable
    //    Botón: Cancelar
    // ─────────────────────────────────────────────────────────────
    private void cerrarPopupSimClaro(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Continua la compra de tus productos Claro')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("📌 [PopupHandler] Popup 'SIM Claro' cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }


    // ─────────────────────────────────────────────────────────────
    // 2. Popup "Iniciar el explorador" → nuevo popup detectado
    //    Botón: Cancelar
    // ─────────────────────────────────────────────────────────────
    private void cerrarPopupIniciarExplorador(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Iniciar el explorador')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("📌 [PopupHandler] Popup 'Iniciar el explorador' cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

    // ─────────────────────────────────────────────────────────────
    // 3. Error de conexión / MMI (todas las variantes)
    //    Botón: Aceptar
    // ─────────────────────────────────────────────────────────────
    private void cerrarPopupErrorConexion(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Problema de conexión o código')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
                System.out.println("📌 [PopupHandler] Popup error conexión/MMI cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

    // ─────────────────────────────────────────────────────────────
    // 4. Diálogo USSD residual abierto
    //    Botón: Cancelar
    // ─────────────────────────────────────────────────────────────
    private void cerrarUSSDResidual(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath("//*[@text='Cancelar']")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("📌 [PopupHandler] USSD residual cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

}