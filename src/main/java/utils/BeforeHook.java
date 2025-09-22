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

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class BeforeHook {

  /********** Log Attribute **********/
  private static final Logger LOGGER = Logger.getLogger(BeforeHook.class);

  @Before
  public void initScenario(Scenario scenario) {

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

    LOGGER.info(
            "************************************************************************************************");
    LOGGER.info("[ Start stage ] --> " + scenario.getName());
    LOGGER.info(
            "************************************************************************************************");

    OnStage.setTheStage(new OnlineCast()); // ← esto evita el error
  }

  public static void prepareStage(String urlBase) {
    OnStage.setTheStage(new OnlineCast());
    theActorCalled("Usuario").whoCan(CallAnApi.at(urlBase));
  }

  @After
  public void endScenario(Scenario scenario) {
    LOGGER.info(
            "************************************************************************************************");
    LOGGER.info("[ End of stage ] --> " + scenario.getName());
    LOGGER.info(
            "************************************************************************************************");
  }
}