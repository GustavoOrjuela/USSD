package interactions.comunes;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.targets.Target;
import net.serenitybdd.screenplay.waits.WaitUntil;

import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isClickable;

public class Clickable implements Interaction {

  Target element;

  public Clickable(Target element) {
    this.element = element;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {

    actor.attemptsTo(WaitUntil.the(element, isClickable()).forNoMoreThan(8).seconds());
  }
}
