package tasks;


import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Click;
import net.serenitybdd.screenplay.actions.Enter;
import net.serenitybdd.screenplay.questions.Presence;
import net.serenitybdd.screenplay.waits.WaitUntil;
import org.openqa.selenium.By;
import utils.CapturaDePantallaMovil;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.matchers.WebElementStateMatchers.isNotPresent;
import static userinterfaces.USSDPage.*;

public class RealizarLlamada implements Task {

    private final String numero;

    public RealizarLlamada(String numero) {
        this.numero = numero;
    }

    public static Performable alNumero(String numero) {
        return instrumented(RealizarLlamada.class, numero);
    }


    @Override
    public <T extends Actor> void performAs(T actor) {

        if (!Presence.of(TXT_APP_PREDETERMINADA).viewedBy(actor).resolveAll().isEmpty()) {
            actor.attemptsTo(
                    Click.on(BTN_HACER_PREDETERMINADA)

            );
        } else {
            actor.attemptsTo(
                    WaitFor.aTime(1000));
        }

        actor.attemptsTo(
                Click.on(TECLADO_TELEFONO),
                Enter.theValue(numero).into(By.id("digits")));
        CapturaDePantallaMovil.tomarCapturaPantalla("captura_pantalla");


        actor.attemptsTo(
                Click.on(BTN_LLAMAR),
                WaitUntil.the(IMG_ESPERA, isNotPresent()));


    }

}