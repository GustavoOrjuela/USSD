package interactions.comunes;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class Atras extends AndroidObject implements Interaction {

  @Override
  @Step("Regresa atrás en la aplicacion.")
  public <T extends Actor> void performAs(T actor) {
    Atras(actor);
  }

  public static Interaction irAtras() {
    return instrumented(Atras.class);
  }
}