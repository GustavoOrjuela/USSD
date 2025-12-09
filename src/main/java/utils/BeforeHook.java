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
import hooks.OllamaStepListener;
import net.thucydides.core.steps.StepEventBus;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class BeforeHook {

  private static final Logger LOGGER = Logger.getLogger(BeforeHook.class);

  @Before
  public void initScenario(Scenario scenario) {
    // ================================
    // 🔹 Cerrar popup automático
    // ================================
    try {
      AndroidDriver driver = MyDriver.get();

      if (driver != null) {

        if (driver.findElements(By.xpath("//*[@text='Aceptar']")).size() > 0) {
          driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
          System.out.println("📌 Popup de Claro detectado y cerrado automáticamente.");
        }
      }

    } catch (Exception e) {
      // Silencioso: no debe interrumpir la ejecución
    }

    // ================================
    // 🔹 Logs de inicio
    // ================================
    LOGGER.info("************************************************************************************************");
    LOGGER.info("[ Start stage ] --> " + scenario.getName());
    LOGGER.info("************************************************************************************************");

    // ================================
    // 🔹 Inicializar Screenplay
    // ================================
    OnStage.setTheStage(new OnlineCast());
  }

  // ================================
  // 🔹 Config API (si se usa)
  // ================================
  public static void prepareStage(String urlBase) {
    OnStage.setTheStage(new OnlineCast());
    theActorCalled("Usuario").whoCan(CallAnApi.at(urlBase));
  }

  // ================================
  // 🔹 Logs final escenario
  // ================================
  @After
  public void endScenario(Scenario scenario) {
    LOGGER.info("************************************************************************************************");
    LOGGER.info("[ End of stage ] --> " + scenario.getName());
    LOGGER.info("************************************************************************************************");
  }
}
