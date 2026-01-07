package hooks;

import cucumber.api.java.After;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import utils.MyDriver;

public class PopupHandler {

    @After
    public void cerrarPopupClaro() {
        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null) {
                // Verificar si está visible el botón "Aceptar" del popup
                if (driver.findElements(By.xpath("//*[@text='Aceptar']")).size() > 0) {
                    // Presionar "Cancelar" para cerrarlo
                    driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                    System.out.println("📌 Popup de Claro detectado y cerrado automáticamente (post-step).");
                }
            }
        } catch (Exception e) {
            // Silencioso: no debe interrumpir la ejecución
        }

        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null && !driver.findElements(By.xpath("//*[@text='Cancelar']")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("📌 Pantalla USSD cerrada automáticamente al finalizar el escenario");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cerrar el USSD: " + e.getMessage());
        }

        try {
            AndroidDriver driver = MyDriver.get();
            if (driver != null && !driver.findElements(By.xpath("//*[@text='Problema de conexión o código incorrecto']")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
                System.out.println("📌 Pantalla de error de código USSD cerrada automáticamente al finalizar el escenario");
            }
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo cerrar el USSD: " + e.getMessage());
        }
    }
}