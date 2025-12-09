package hooks;

import net.serenitybdd.core.Serenity;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.openqa.selenium.NoSuchElementException;
import utils.OllamaClient;

import java.util.Map;

public class OllamaStepListener implements StepListener {

    private final OllamaClient ollama = new OllamaClient();

    // ===========================
    // ANALIZAR ERROR DE STEPS
    // ===========================

    @Override
    public void stepFailed(StepFailure failure) {

        Throwable cause = failure.getException();

        System.out.println("⚠️ [OllamaListener] Error detectado: " + failure.getMessage());

        Serenity.recordReportData()
                .withTitle("⚠️ Error detectado")
                .andContents(failure.getMessage());

        if (cause instanceof NoSuchElementException) {

            System.out.println("🤖 [Ollama] Ejecutando análisis IA...");

            Serenity.recordReportData()
                    .withTitle("🤖 Ollama ejecutado")
                    .andContents("Tipo: NoSuchElementException");

            try {
                String prompt = "Analiza este error de Selenium y explica por qué no se encontró el elemento:\n\n"
                        + cause.getMessage();

                String respuesta = ollama.ask(prompt);

                Serenity.recordReportData()
                        .withTitle("🧠 Respuesta de Ollama")
                        .andContents(respuesta);

                System.out.println("🧠 [OLLAMA] " + respuesta);

            } catch (Exception e) {
                Serenity.recordReportData()
                        .withTitle("❌ Error al llamar a Ollama")
                        .andContents(e.getMessage());

                System.out.println("❌ [OLLAMA ERROR] " + e.getMessage());
            }
        }
    }

    @Override public void lastStepFailed(StepFailure stepFailure) {}

    // ===========================
    // MÉTODOS BASE OBLIGATORIOS
    // ===========================

    @Override public void testSuiteStarted(Class<?> testSuite) {}
    @Override public void testSuiteStarted(Story story) {}
    @Override public void testSuiteFinished() {}
    @Override public void testStarted(String testName) {}
    @Override public void testStarted(String s, String s1) {}
    @Override public void testFinished(TestOutcome outcome) {}
    @Override public void testRetried() {}
    @Override public void testFailed(TestOutcome outcome, Throwable cause) {}
    @Override public void testIgnored() {}
    @Override public void testSkipped() {}
    @Override public void testPending() {}
    @Override public void testIsManual() {}
    @Override public void stepStarted(ExecutedStepDescription description) {}
    @Override public void skippedStepStarted(ExecutedStepDescription executedStepDescription) {}
    @Override public void stepIgnored() {}
    @Override public void stepPending() {}
    @Override public void stepPending(String s) {    }
    @Override public void stepFinished() {}
    @Override public void notifyScreenChange() {}
    @Override public void useExamplesFrom(DataTable dataTable) {}
    @Override public void addNewExamplesFrom(DataTable dataTable) {}
    @Override public void exampleStarted(Map<String, String> map) {}
    @Override public void exampleFinished() {}
    @Override public void assumptionViolated(String s) {}
    @Override public void testRunFinished() {
    }
}
