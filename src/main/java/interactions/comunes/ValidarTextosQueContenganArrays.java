package interactions.comunes;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ValidarTextosQueContenganArrays extends AndroidObject implements Interaction {

    private List<String> textos;

    public ValidarTextosQueContenganArrays(List<String> textos) {
        this.textos = textos;
    }

    @Override
    @Step("Valida que todos los textos de la lista estén contenidos en otro texto visible.")
    public <T extends Actor> void performAs(T actor) {
        for (String texto : textos) {
            ElTextoContiene(actor, texto); // misma validación que ya tienes
        }
    }

    public static Interaction validarTexto(List<String> textos) {
        return instrumented(ValidarTextosQueContenganArrays.class, textos);
    }
}
