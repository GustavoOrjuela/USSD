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

      // 1️⃣ Error MMI incorrecto
      try {
        if (!driver.findElements(By.xpath(
                "//*[@text='Problema de conexión o código MMI incorrecto.']")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
          System.out.println("📌 [BeforeHook] Error MMI cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) { /* Silencioso */ }

      // 2️⃣ Error USSD previo (sin MMI)
      try {
        if (!driver.findElements(By.xpath(
                "//*[@text='Problema de conexión o código incorrecto']")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
          System.out.println("📌 [BeforeHook] Error USSD cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) { /* Silencioso */ }

      // 2️⃣.1 Error USSD / MMI previo → cerrar con "Aceptar"
      try {
        if (!driver.findElements(By.xpath(
                "//*[contains(@text,'Problema de conexión o código')]")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
          System.out.println("📌 [BeforeHook] Error USSD/MMI cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) {
        // Silencioso
      }

      // 2️⃣.2 Error USSD / MMI previo → cerrar con "Aceptar"
      try {
        if (!driver.findElements(By.xpath(
                "//*[contains(@text,'Problema de conexión o código incorrecto de MMI.')]")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
          System.out.println("📌 [BeforeHook] Error USSD/MMI cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) {
        // Silencioso
      }

      // 3️⃣ Popup "SIM Claro" / "Claro" — portal web ← NUEVO
      try {
        if (!driver.findElements(By.xpath(
                "//*[contains(@text,'Continua la compra de tus productos Claro')]")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
          System.out.println("📌 [BeforeHook] Popup 'SIM Claro' portal web cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) { /* Silencioso */ }

      // 4️⃣ Popup genérico con "Aceptar" visible → cerrar con "Cancelar"
      try {
        if (!driver.findElements(By.xpath("//*[@text='Aceptar']")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
          System.out.println("📌 [BeforeHook] Popup de Claro cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) { /* Silencioso */ }

      // 5️⃣ USSD residual → cerrar con "Cancelar"
      try {
        if (!driver.findElements(By.xpath("//*[@text='Cancelar']")).isEmpty()) {
          driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
          System.out.println("📌 [BeforeHook] USSD previo cerrado");
          Thread.sleep(500);
        }
      } catch (Exception e) { /* Silencioso */ }

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
