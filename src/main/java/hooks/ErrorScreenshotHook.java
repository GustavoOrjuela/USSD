package hooks;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import io.appium.java_client.android.AndroidDriver;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import net.thucydides.core.model.*;
import net.thucydides.core.steps.ExecutedStepDescription;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import utils.MyDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;

/**
 * Hook independiente para captura de screenshots en fallos.
 *
 * Registra su propio StepListener en Serenity para capturar
 * la pantalla exacta del momento del fallo.
 *
 * Sin dependencia de Ollama ni de ninguna otra herramienta de análisis.
 */
public class ErrorScreenshotHook {

    private static final String ERROR_FOLDER = "Error";
    private static final String FILE_NAME = "ERROR.png";

    private static volatile boolean listenerRegistrado = false;

    @Before(order = 0)
    public void inicializar(Scenario scenario) {
        limpiarCarpetaError();
        registrarListenerSiNecesario();
    }

    // =========================================================
    // Registro del listener — una sola vez en toda la ejecución
    // =========================================================

    private void registrarListenerSiNecesario() {
        if (!listenerRegistrado) {
            synchronized (ErrorScreenshotHook.class) {
                if (!listenerRegistrado) {
                    StepEventBus.getEventBus().registerListener(new ErrorStepListener());
                    listenerRegistrado = true;
                    System.out.println("✅ [ErrorScreenshotHook] Listener de captura registrado");
                }
            }
        }
    }

    // =========================================================
    // Limpieza de carpeta al inicio de cada escenario
    // =========================================================

    private void limpiarCarpetaError() {
        try {
            Path errorPath = Paths.get(ERROR_FOLDER);
            if (!Files.exists(errorPath)) return;

            Files.walk(errorPath)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

            System.out.println("🧹 [ErrorScreenshotHook] Carpeta Error/ limpiada");

        } catch (IOException e) {
            System.err.println("⚠️ [ErrorScreenshotHook] No se pudo limpiar Error/: " + e.getMessage());
        }
    }

    // =========================================================
    // Listener interno — captura en el momento exacto del fallo
    // =========================================================

    private static class ErrorStepListener implements StepListener {

        @Override
        public void stepFailed(StepFailure failure) {
            try {
                AndroidDriver driver = MyDriver.get();
                if (driver == null) return;

                // Crear carpeta solo cuando hay fallo
                File errorFolder = new File(ERROR_FOLDER);
                if (!errorFolder.exists() && !errorFolder.mkdirs()) return;

                // Capturar pantalla en el momento exacto del fallo
                byte[] bytes = ((TakesScreenshot) driver)
                        .getScreenshotAs(OutputType.BYTES);

                // Nombre fijo — sobreescribe si hay múltiples fallos en cascada
                // Al terminar el escenario queda el último estado fallido
                File destino = new File(ERROR_FOLDER + File.separator + FILE_NAME);
                try (FileOutputStream fos = new FileOutputStream(destino)) {
                    fos.write(bytes);
                }

                System.out.println("📸 [ErrorScreenshotHook] Screenshot guardado: "
                        + destino.getAbsolutePath());

            } catch (Exception e) {
                System.err.println("⚠️ [ErrorScreenshotHook] Error capturando screenshot: "
                        + e.getMessage());
            }
        }

        // Métodos obligatorios del StepListener — sin implementación
        @Override public void testSuiteStarted(Class<?> testSuite) {}
        @Override public void testSuiteStarted(Story story) {}
        @Override public void testSuiteFinished() {}
        @Override public void testStarted(String testName) {}
        @Override public void testStarted(String s, String s1) {}
        @Override public void testFinished(TestOutcome outcome) {}
        @Override public void testRetried() {}
        @Override public void lastStepFailed(StepFailure stepFailure) {}
        @Override public void testFailed(TestOutcome outcome, Throwable cause) {}
        @Override public void testIgnored() {}
        @Override public void testSkipped() {}
        @Override public void testPending() {}
        @Override public void testIsManual() {}
        @Override public void stepStarted(ExecutedStepDescription description) {}
        @Override public void skippedStepStarted(ExecutedStepDescription description) {}
        @Override public void stepIgnored() {}
        @Override public void stepPending() {}
        @Override public void stepPending(String s) {}
        @Override public void stepFinished() {}
        @Override public void notifyScreenChange() {}
        @Override public void useExamplesFrom(DataTable dataTable) {}
        @Override public void addNewExamplesFrom(DataTable dataTable) {}
        @Override public void exampleStarted(Map<String, String> map) {}
        @Override public void exampleFinished() {}
        @Override public void assumptionViolated(String s) {}
        @Override public void testRunFinished() {}
    }
}