package utils;

import io.appium.java_client.android.AndroidDriver;
import net.thucydides.core.webdriver.DriverSource;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MyDriver implements DriverSource {

    private static AndroidDriver driver;

    @Override
    public WebDriver newDriver() {
        try {
            // Permite sobreescribir por línea de comandos: -Dappium.hub, -Dappium.udid, -Dappium.deviceName
            String hub = System.getProperty("appium.hub", "http://127.0.0.1:4723/wd/hub");
            String udid = System.getProperty("appium.udid", System.getenv("ANDROID_UDID"));
            String deviceName = System.getProperty("appium.deviceName", System.getenv("ANDROID_DEVICE_NAME"));

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("platformName", "Android");
            caps.setCapability("automationName", "UiAutomator2");
            caps.setCapability("deviceName", (deviceName != null && !deviceName.isEmpty()) ? deviceName : "AndroidDevice");
            if (udid != null && !udid.isEmpty()) {
                caps.setCapability("udid", udid);
            }

            // Dialer nativo (según tu dumpsys)
            caps.setCapability("appPackage", "com.google.android.dialer");
            caps.setCapability("appActivity", "com.google.android.dialer.extensions.GoogleDialtactsActivity");

            // Calidad de vida
            caps.setCapability("noReset", true);
            caps.setCapability("fullReset", false);
            caps.setCapability("autoGrantPermissions", true);
            caps.setCapability("newCommandTimeout", 120);
            caps.setCapability("dontStopAppOnReset", true);

            driver = new AndroidDriver(new URL(hub), caps);
            driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            return driver;

        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar el AndroidDriver para el Dialer", e);
        }
    }

    @Override
    public boolean takesScreenshots() {
        return true;
    }

    public static AndroidDriver get() {
        return driver;
    }
}
