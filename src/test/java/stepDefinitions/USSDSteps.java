package stepDefinitions;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import freemarker.log.Logger;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;
import tasks.RealizarLlamada;
import utils.WordAppium;

import java.io.File;

import static net.serenitybdd.screenplay.actors.OnStage.theActorCalled;

public class USSDSteps {

    private static final Logger LOGGER = Logger.getLogger(WordAppium.class.getName());
    @Before
    public void initScenario(Scenario scenario) {
        OnStage.setTheStage(new OnlineCast());
        LOGGER.info("Limpiando carpeta de capturas...");
        File folder = new File("Capturas");

        if (folder.exists() && folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".jpg")) {
                    file.delete();
                }
            }
        }
    }
    @Given("^Se realiza la llamada al numero (.*)$")
    public void realizaUnaLlamadaAlNumero(String numero) {
        theActorCalled("").attemptsTo(RealizarLlamada.alNumero(numero));
    }
}