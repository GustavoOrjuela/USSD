package interactions.input;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.targets.Target;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IngresarMontoTeclado implements Interaction {
  private final String monto;

  // Targets para el teclado numérico
  private static final Target BTN_1 =
      Target.the("Botón 1").locatedBy("//android.widget.Button[@text='1']");
  private static final Target BTN_2 =
      Target.the("Botón 2").locatedBy("//android.widget.Button[@text='2']");
  private static final Target BTN_3 =
      Target.the("Botón 3").locatedBy("//android.widget.Button[@text='3']");
  private static final Target BTN_4 =
      Target.the("Botón 4").locatedBy("//android.widget.Button[@text='4']");
  private static final Target BTN_5 =
      Target.the("Botón 5").locatedBy("//android.widget.Button[@text='5']");
  private static final Target BTN_6 =
      Target.the("Botón 6").locatedBy("//android.widget.Button[@text='6']");
  private static final Target BTN_7 =
      Target.the("Botón 7").locatedBy("//android.widget.Button[@text='7']");
  private static final Target BTN_8 =
      Target.the("Botón 8").locatedBy("//android.widget.Button[@text='8']");
  private static final Target BTN_9 =
      Target.the("Botón 9").locatedBy("//android.widget.Button[@text='9']");
  private static final Target BTN_0 =
      Target.the("Botón 0").locatedBy("//android.widget.Button[@text='0']");
  private static final Target BTN_CONFIRMAR =
      Target.the("Confirmar").locatedBy("//android.widget.Button[contains(@text,'✓')]");

  private IngresarMontoTeclado(String monto) {
    this.monto = monto;
  }

  public static IngresarMontoTeclado conValor(String monto) {
    return instrumented(IngresarMontoTeclado.class, monto);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    for (char digito : monto.toCharArray()) {
      switch (digito) {
        case '0':
          actor.attemptsTo(Click.on(BTN_0));
          break;
        case '1':
          actor.attemptsTo(Click.on(BTN_1));
          break;
        case '2':
          actor.attemptsTo(Click.on(BTN_2));
          break;
        case '3':
          actor.attemptsTo(Click.on(BTN_3));
          break;
        case '4':
          actor.attemptsTo(Click.on(BTN_4));
          break;
        case '5':
          actor.attemptsTo(Click.on(BTN_5));
          break;
        case '6':
          actor.attemptsTo(Click.on(BTN_6));
          break;
        case '7':
          actor.attemptsTo(Click.on(BTN_7));
          break;
        case '8':
          actor.attemptsTo(Click.on(BTN_8));
          break;
        case '9':
          actor.attemptsTo(Click.on(BTN_9));
          break;
      }
    }

    // Confirmar entrada
    actor.attemptsTo(Click.on(BTN_CONFIRMAR));
  }
}
