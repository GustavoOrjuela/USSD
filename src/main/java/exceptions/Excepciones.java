package exceptions;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotPresent;
import static userinterfaces.USSDPage.*;

import interactions.wait.WaitFor;
import io.appium.java_client.android.AndroidDriver;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.serenitybdd.screenplay.questions.Presence;
import net.serenitybdd.screenplay.waits.WaitUntil;
import net.thucydides.core.webdriver.WebDriverFacade;

public class Excepciones {

  public void ExSwipeToElement(Actor actor, String label) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\""
                            + label
                            + "\"));")
            .click();
  }

  public void ExScrollHorizontalId(Actor actor, String id) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceIdMatches(\".*"
                            + id
                            + ".*\").scrollable(true)).setAsHorizontalList().scrollForward()");
  }

  public void ExScrollHorizontalIdElement(Actor actor, String id, String label) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceIdMatches(\".*"
                            + id
                            + ".*\").scrollable(true))"
                            + ".setAsHorizontalList().scrollIntoView(new UiSelector().text(\""
                            + label
                            + "\"));");
  }

  public void ExScrollHorizontal(Actor actor, String instance, String label) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceIdMatches(\".*com.clarocolombia.miclaro:id/rvCategories.*\")"
                            + ".childSelector(new UiSelector().instance("
                            + instance
                            + ").scrollable(true)))"
                            + ".setAsHorizontalList().scrollIntoView(new UiSelector().text(\""
                            + label
                            + "\"));")
            .click();
  }

  public void ExScrollElement(Actor actor, String instance, String label) {
    Serenity.takeScreenshot();
    int instancia = Integer.parseInt(instance);
    instancia = instancia + 1;
    String instancia2 = Integer.toString(instancia);
    if (!Presence.of(TXT_TE_GUSTARIA_CALIFICAR_MI_CLARO_APP)
            .viewedBy(actor)
            .resolveAll()
            .isEmpty()) {
      androidDriver(actor).findElementByAndroidUIAutomator("new UiSelector().text(\"No\")").click();
      actor.attemptsTo(WaitFor.aTime(1000));
    } else if (!Presence.of(TXT_ACTUALIZAR).viewedBy(actor).resolveAll().isEmpty()) {
      androidDriver(actor)
              .findElementByAndroidUIAutomator("new UiSelector().text(\"Actualizar\")")
              .click();
      actor.attemptsTo(WaitFor.aTime(5000));
    }
    try {
      androidDriver(actor)
              .findElementByAndroidUIAutomator("new UiSelector().text(\"" + label + "\")")
              .isDisplayed();
    } catch (Exception e) {
      androidDriver(actor)
              .findElementByAndroidUIAutomator(
                      "new UiScrollable(new UiSelector().resourceIdMatches(\".*com.clarocolombia.miclaro:id/containerRecycler.*\")"
                              + ".childSelector(new UiSelector().instance("
                              + instance
                              + ").scrollable(true)))"
                              + ".setAsHorizontalList().scrollIntoView(new UiSelector().text(\""
                              + label
                              + "\"));");
      /*try {
      	androidDriver(actor).findElementByAndroidUIAutomator("new UiSelector().text(\"" + label + "\")")
      			.isDisplayed();
      } catch (Exception ex) {
      	androidDriver(actor).findElementByAndroidUIAutomator(
      			"new UiScrollable(new UiSelector().resourceIdMatches(\".*com.clarocolombia.miclaro:id/rvCategories.*\")"
      					+ ".childSelector(new UiSelector().instance(" + instancia2 + ").scrollable(true)))"
      					+ ".setAsHorizontalList().scrollIntoView(new UiSelector().text(\"" + label + "\"));");
      }*/
    }
  }

  public void ExScrollHorizontalFinal(Actor actor, String instance) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().resourceIdMatches(\".*com.clarocolombia.miclaro:id/rvCategories.*\")"
                            + ".childSelector(new UiSelector().instance("
                            + instance
                            + ").scrollable(true)))"
                            + ".setAsHorizontalList().scrollForward()");
  }

  public void ExScrollVertical(Actor actor, String label) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\""
                            + label
                            + "\"));");
  }

  public void ExElTextoContiene(Actor actor, String text) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\")")
            .isDisplayed();
  }

  public void ExClickElTextoContiene(Actor actor, String text) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator("new UiSelector().textContains(\"" + text + "\")")
            .click();
  }

  public void ExClickByText(Actor actor, String text) {
    Serenity.takeScreenshot();
    if (!Presence.of(TXT_TE_GUSTARIA_CALIFICAR_MI_CLARO_APP)
            .viewedBy(actor)
            .resolveAll()
            .isEmpty()) {
      actor.attemptsTo(WaitFor.aTime(1000));
      androidDriver(actor).findElementByAndroidUIAutomator("new UiSelector().text(\"No\")").click();
    }
    actor.attemptsTo(WaitFor.aTime(1000));
    androidDriver(actor)
            .findElementByAndroidUIAutomator("new UiSelector().text(\"" + text + "\")")
            .click();
  }

  public void ExValidarTexto(Actor actor, String text) {
    Serenity.takeScreenshot();
    if (!Presence.of(IMG_LOADING).viewedBy(actor).resolveAll().isEmpty()) {
      actor.attemptsTo(WaitUntil.the(IMG_LOADING, isNotPresent()));
    }
    androidDriver(actor)
            .findElementByAndroidUIAutomator("new UiSelector().text(\"" + text + "\")")
            .isDisplayed();
  }

  public void ExScrollVerticalClass(Actor actor, String label) {
    Serenity.takeScreenshot();
    androidDriver(actor)
            .findElementByAndroidUIAutomator(
                    "new UiScrollable(new UiSelector().className(\"android.view.View\").scrollable(true)).scrollIntoView("
                            + "new UiSelector().text(\""
                            + label
                            + "\"));");
  }

  public AndroidDriver getAndroidDriver(Actor actor) {
    return androidDriver(actor);
  }

  @SuppressWarnings("unchecked")
  public static AndroidDriver androidDriver(Actor actor) {
    return (AndroidDriver) ((WebDriverFacade) getDriver(actor)).getProxiedDriver();
  }

  private static WebDriverFacade getDriver(Actor actor) {
    return ((WebDriverFacade) BrowseTheWeb.as(actor).getDriver());
  }
}
