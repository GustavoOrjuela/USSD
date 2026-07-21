package utils;

import cucumber.api.Scenario;
import io.appium.java_client.android.AndroidDriver;
import jxl.common.Logger;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import net.serenitybdd.screenplay.rest.abiities.CallAnApi;
import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.By;
import utils.MyDriver;

import java.util.concurrent.TimeUnit;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class BeforeHook {

  private static final Logger LOGGER = Logger.getLogger(BeforeHook.class);

  @Before
  public void initScenario(Scenario scenario) {
    // ================================
    // 🔹 Cerrar popups al inicio
    // ================================
    cerrarPopupsIniciales();

    // ================================
    // 🔹 Logs de inicio
    // ================================
    LOGGER.info("************************************************************************************************");
    LOGGER.info("[ Start stage ] --> " + scenario.getName());
    LOGGER.info("************************************************************************************************");

    OnStage.setTheStage(new OnlineCast());
  }

  /**
   * Cierra todos los popups que puedan estar presentes al inicio del escenario.
   */
  private void cerrarPopupsIniciales() {
    try {
      AndroidDriver driver = MyDriver.get();
      if (driver == null) return;

      System.out.println("🔍 [BeforeHook] Verificando popups iniciales...");

      // Desactivar implicit wait para detección instantánea
      driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

      try {
        // 1. SIM Claro / Claro portal
        try {
          if (!driver.findElements(By.xpath(
                  "//*[contains(@text,'Continua la compra de tus productos Claro')]")).isEmpty()) {
            driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
            System.out.println("📌 [BeforeHook] Popup 'SIM Claro' cerrado");
            Thread.sleep(500);
          }
        } catch (Exception e) { /* Silencioso */ }

        // 2. Iniciar el explorador
        try {
          if (!driver.findElements(By.xpath(
                  "//*[contains(@text,'Iniciar el explorador')]")).isEmpty()) {
            driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
            System.out.println("📌 [BeforeHook] Popup 'Iniciar explorador' cerrado");
            Thread.sleep(500);
          }
        } catch (Exception e) { /* Silencioso */ }

        // 3. Error conexión / MMI (todas las variantes con contains)
        try {
          if (!driver.findElements(By.xpath(
                  "//*[contains(@text,'Problema de conexión o código')]")).isEmpty()) {
            driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
            System.out.println("📌 [BeforeHook] Error conexión/MMI cerrado");
            Thread.sleep(500);
          }
        } catch (Exception e) { /* Silencioso */ }

        // 4. USSD residual
        try {
          if (!driver.findElements(By.xpath("//*[@text='Cancelar']")).isEmpty()) {
            driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
            System.out.println("📌 [BeforeHook] USSD residual cerrado");
            Thread.sleep(500);
          }
        } catch (Exception e) { /* Silencioso */ }

      } finally {
        // Restaurar implicit wait original
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      }

      System.out.println("✅ [BeforeHook] Verificación de popups completada");

    } catch (Exception e) {
      System.err.println("⚠️ [BeforeHook] Error verificando popups: " + e.getMessage());
    }
  }

  public static void prepareStage(String urlBase) {
    OnStage.setTheStage(new OnlineCast());
    theActorCalled("Usuario").whoCan(CallAnApi.at(urlBase));
  }

  @After
  public void endScenario(Scenario scenario) {
    LOGGER.info("************************************************************************************************");
    LOGGER.info("[ End of stage ] --> " + scenario.getName());
    LOGGER.info("************************************************************************************************");
  }
}
