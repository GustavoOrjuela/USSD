package utils;

import io.appium.java_client.android.AndroidDriver;
import net.thucydides.core.webdriver.SerenityWebdriverManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilidadesAndroid {

  public static void abrirLinkEnNavegador(String url) {
    AndroidDriver driver = (AndroidDriver) SerenityWebdriverManager.inThisTestThread().getCurrentDriver();

    Map<String, Object> intentArgs = new HashMap<>();
    intentArgs.put("command", "am");
    intentArgs.put("args", List.of(
            "start",
            "-a", "android.intent.action.VIEW",
            "-d", url
    ));

    driver.executeScript("mobile: shell", intentArgs);
  }
}