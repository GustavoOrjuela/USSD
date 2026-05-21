package hooks;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import utils.MyDriver;

public class PopupHandler {

    @Before
    @After
    public void cerrarPopupClaro() {

        // Popup genérico con botón "Aceptar" visible → cerrar con "Cancelar"
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null) {
                if (driver.findElements(By.xpath("//*[@text='Aceptar']")).size() > 0) {
                    driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                    System.out.println("📌 Popup de Claro detectado y cerrado automáticamente (post-step).");
                }
            }
        } catch (Exception e) {
            // Silencioso: no debe interrumpir la ejecución
        }

        // Error MMI incorrecto → cerrar con "Aceptar"
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null) {
                if (driver.findElements(By.xpath("//*[@text='Problema de conexión o código MMI incorrecto.']")).size() > 0) {
                    driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
                    System.out.println("📌 Popup de Claro MMI detectado y cerrado automáticamente (post-step).");
                }
            }
        } catch (Exception e) {
            // Silencioso: no debe interrumpir la ejecución
        }

        // Popup "SIM Claro" o "Claro" → identificado por su cuerpo de texto, cerrar con "Cancelar"
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null) {
                if (!driver.findElements(By.xpath(
                        "//*[contains(@text,'Continua la compra de tus productos Claro')]")).isEmpty()) {
                    driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                    System.out.println("📌 Popup 'SIM Claro' / 'Claro' detectado y cerrado automáticamente.");
                }
            }
        } catch (Exception e) {
            // Silencioso: no debe interrumpir la ejecución
        }

        // Diálogo USSD abierto → cerrar con "Cancelar"
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null && !driver.findElements(By.xpath("//*[@text='Cancelar']")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("📌 Pantalla USSD cerrada automáticamente al finalizar el escenario");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cerrar el USSD: " + e.getMessage());
        }

        // Error de código incorrecto (sin MMI) → cerrar con "Aceptar"
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null && !driver.findElements(
                    By.xpath("//*[@text='Problema de conexión o código incorrecto']")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
                System.out.println("📌 Pantalla de error de código USSD cerrada automáticamente al finalizar el escenario");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cerrar el USSD: " + e.getMessage());
        }
    }
}