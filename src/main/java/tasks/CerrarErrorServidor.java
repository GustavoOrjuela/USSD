package tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.questions.Presence;
import net.serenitybdd.screenplay.actions.Click;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static userinterfaces.USSDPage.*;

public class CerrarErrorServidor implements Task {

    public static CerrarErrorServidor siEstaPresente() {
        return instrumented(CerrarErrorServidor.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {

        if (!Presence.of(TXT_SERVER_ERROR)
                .viewedBy(actor)
                .resolveAll()
                .isEmpty()) {

            actor.attemptsTo(
                    Click.on(BTN_ACEPTAR_ERROR)
            );
        }
    }
}