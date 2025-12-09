package hooks;

import io.appium.java_client.android.AndroidDriver;
import net.serenitybdd.core.Serenity;
import net.thucydides.core.model.DataTable;
import net.thucydides.core.model.Story;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.steps.ExecutedStepDescription;
import net.thucydides.core.steps.StepFailure;
import net.thucydides.core.steps.StepListener;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import utils.MyDriver;
import utils.ollama.FailureContext;
import utils.ollama.OllamaAnalyzer;
import utils.ollama.OllamaClient;

import java.util.Map;

/**
 * Listener de Serenity BDD que intercepta fallos y los analiza con Ollama IA.
 *
 * Funcionalidad:
 * - Detecta fallos en steps de Serenity
 * - Captura contexto completo (page source, screenshot, localizador)
 * - Envía a Ollama para análisis inteligente
 * - Integra resultados en reportes Serenity como HTML enriquecido
 * - Logs detallados en consola para debugging en tiempo real
 *
 * Tipos de fallos analizados:
 * - NoSuchElementException (el más común en USSD)
 * - TimeoutException
 * - StaleElementReferenceException
 * - Cualquier otro fallo (análisis general)
 *
 * Configuración via system properties:
 * - ollama.enabled=false para deshabilitar análisis
 * - ollama.model=llama2 para cambiar modelo
 * - ollama.timeout=180 para aumentar timeout
 *
 * Principios SOLID:
 * - SRP: Responsabilidad única de análisis de fallos con IA
 * - OCP: Extensible para nuevos tipos de análisis
 * - DIP: Depende de abstracciones (OllamaClient, FailureContext)
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class OllamaStepListener implements StepListener {

    private final OllamaClient ollamaClient;
    private boolean initialized = false;
    private String currentTestName;
    private String currentUssdCode;

    public OllamaStepListener() {
        this.ollamaClient = new OllamaClient();
        System.out.println("🔗 [OllamaListener] Inicializado");
        System.out.println("   " + ollamaClient.getModelInfo());
    }

    @Override
    public void testStarted(String testName) {
        if (!initialized) {
            verifyOllamaAvailability();
            initialized = true;
        }
        this.currentTestName = testName;
        System.out.println("🧪 [OllamaListener] Test iniciado: " + testName);
    }

    @Override
    public void stepFailed(StepFailure failure) {
        if (!ollamaClient.isEnabled()) {
            System.out.println("⏭️ [OllamaListener] Análisis deshabilitado, saltando...");
            return;
        }

        Throwable cause = failure.getException();
        String stepDescription = failure.getMessage();

        System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║  🚨 FALLO DETECTADO - INICIANDO ANÁLISIS OLLAMA             ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println("📍 Step: " + stepDescription);
        System.out.println("⚠️  Error: " + (cause != null ? cause.getClass().getSimpleName() : "Unknown"));

        // Registrar detección del fallo en Serenity
        Serenity.recordReportData()
                .withTitle("🚨 Fallo Detectado")
                .andContents(formatFailureDetection(stepDescription, cause));

        // Determinar tipo de análisis
        OllamaAnalyzer.AnalysisType analysisType = OllamaAnalyzer.determineAnalysisType(cause);
        System.out.println("🔍 Tipo de análisis: " + analysisType);

        // Construir contexto del fallo
        FailureContext context = buildFailureContext(stepDescription, cause);

        if (context == null) {
            System.err.println("❌ [OllamaListener] No se pudo construir contexto, abortando análisis");
            return;
        }

        // Ejecutar análisis con Ollama
        analyzeWithOllama(context, analysisType);

        System.out.println("╚══════════════════════════════════════════════════════════════╝\n");
    }

    /**
     * Construye el contexto completo del fallo capturando toda la información relevante.
     */
    private FailureContext buildFailureContext(String stepDescription, Throwable cause) {
        try {
            AndroidDriver driver = MyDriver.get();

            if (driver == null) {
                System.err.println("⚠️ [OllamaListener] Driver no disponible, contexto limitado");
            }

            FailureContext.Builder contextBuilder = new FailureContext.Builder()
                    .withTestName(currentTestName != null ? currentTestName : "Unknown Test")
                    .withStepDescription(stepDescription)
                    .withError(cause);

            // Capturar page source y screenshot del driver
            if (driver != null) {
                System.out.println("📸 Capturando page source y screenshot...");
                contextBuilder.withDriver(driver);
            }

            // Extraer información del localizador si es NoSuchElementException
            if (cause instanceof NoSuchElementException) {
                extractLocatorInfo(cause.getMessage(), contextBuilder);
            }

            // Agregar código USSD si está disponible
            if (currentUssdCode != null) {
                contextBuilder.withUssdCode(currentUssdCode);
            }

            FailureContext context = contextBuilder.build();
            System.out.println("✅ Contexto construido: " + context);

            // Log muestra del page source
            if (context.getPageSource() != null) {
                System.out.println("📄 Page source capturado: " +
                        context.getTruncatedPageSource(500).length() + " chars");
            }

            return context;

        } catch (Exception e) {
            System.err.println("❌ [OllamaListener] Error construyendo contexto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Extrae información del localizador del mensaje de error.
     */
    private void extractLocatorInfo(String errorMessage, FailureContext.Builder contextBuilder) {
        try {
            // Ejemplos de mensajes:
            // "An element could not be located... Using=-android uiautomator, value=new UiSelector().text(\"Teclado\")"
            // "no such element: Unable to locate element: {\"method\":\"xpath\",\"selector\":\"//...\"]

            if (errorMessage.contains("uiautomator")) {
                // Extraer el localizador UiAutomator
                int valueStart = errorMessage.indexOf("value=");
                if (valueStart != -1) {
                    String locator = errorMessage.substring(valueStart + 6);
                    // Limpiar hasta el siguiente paréntesis o fin
                    int endIndex = locator.indexOf(")");
                    if (endIndex != -1) {
                        locator = locator.substring(0, endIndex + 1);
                    }
                    contextBuilder.withElementLocator(locator.trim(), "uiautomator");
                    System.out.println("🎯 Localizador extraído: " + locator.trim());
                }
            } else if (errorMessage.contains("xpath")) {
                // Extraer xpath
                int selectorStart = errorMessage.indexOf("selector\":\"");
                if (selectorStart != -1) {
                    String xpath = errorMessage.substring(selectorStart + 11);
                    int endIndex = xpath.indexOf("\"");
                    if (endIndex != -1) {
                        xpath = xpath.substring(0, endIndex);
                    }
                    contextBuilder.withElementLocator(xpath, "xpath");
                    System.out.println("🎯 XPath extraído: " + xpath);
                }
            }

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo extraer localizador: " + e.getMessage());
        }
    }

    /**
     * Ejecuta el análisis con Ollama y registra resultados.
     */
    private void analyzeWithOllama(FailureContext context, OllamaAnalyzer.AnalysisType analysisType) {
        try {
            System.out.println("🤖 Preparando prompt para Ollama...");

            // Construir prompt según tipo de análisis
            String prompt;
            if (analysisType == OllamaAnalyzer.AnalysisType.ELEMENT_NOT_FOUND) {
                prompt = OllamaAnalyzer.buildElementNotFoundPrompt(context);
            } else {
                prompt = OllamaAnalyzer.buildGeneralAnalysisPrompt(context);
            }

            System.out.println("📤 Enviando a Ollama (esto puede tomar 30-120 segundos)...");

            // Registrar que el análisis está en progreso
            Serenity.recordReportData()
                    .withTitle("🤖 Análisis Ollama en Progreso")
                    .andContents("Modelo: " + ollamaClient.getModel() +
                            "<br>Tipo: " + analysisType +
                            "<br>⏳ Esperando respuesta...");

            long startTime = System.currentTimeMillis();
            String aiAnalysis = ollamaClient.ask(prompt);
            long duration = System.currentTimeMillis() - startTime;

            System.out.println(String.format("✅ Análisis recibido en %.2f segundos", duration / 1000.0));
            System.out.println("\n╔══════════════════════════════════════════════════════════════╗");
            System.out.println("║  🧠 ANÁLISIS OLLAMA                                          ║");
            System.out.println("╚══════════════════════════════════════════════════════════════╝");
            System.out.println(aiAnalysis);
            System.out.println("╚══════════════════════════════════════════════════════════════╝\n");

            // Registrar análisis completo en reporte Serenity con HTML enriquecido
            String htmlReport = formatOllamaAnalysisAsHtml(aiAnalysis, context, duration);

            Serenity.recordReportData()
                    .withTitle("🧠 Análisis Inteligente Ollama (" + ollamaClient.getModel() + ")")
                    .andContents(htmlReport);

            System.out.println("✅ Análisis registrado en reporte Serenity");

        } catch (Exception e) {
            System.err.println("❌ [OllamaListener] Error en análisis: " + e.getMessage());
            e.printStackTrace();

            // Registrar el error en Serenity
            Serenity.recordReportData()
                    .withTitle("❌ Error en Análisis Ollama")
                    .andContents(formatError(e));
        }
    }

    /**
     * Formatea el análisis de Ollama como HTML enriquecido para el reporte.
     */
    private String formatOllamaAnalysisAsHtml(String analysis, FailureContext context, long durationMs) {
        StringBuilder html = new StringBuilder();

        html.append("<div style='font-family: Arial, sans-serif; padding: 15px; ")
                .append("background-color: #f8f9fa; border-left: 4px solid #007bff; margin: 10px 0;'>");

        // Header
        html.append("<div style='background-color: #007bff; color: white; padding: 10px; ")
                .append("margin: -15px -15px 15px -15px; border-radius: 4px 4px 0 0;'>");
        html.append("<h3 style='margin: 0;'>🧠 Análisis Inteligente con IA</h3>");
        html.append("<small>Modelo: ").append(ollamaClient.getModel())
                .append(" | Duración: ").append(String.format("%.2f", durationMs / 1000.0))
                .append("s | ").append(context.getFormattedTimestamp()).append("</small>");
        html.append("</div>");

        // Contexto del fallo
        html.append("<div style='background-color: #fff3cd; padding: 10px; ")
                .append("border-left: 3px solid #ffc107; margin-bottom: 15px;'>");
        html.append("<strong>📍 Contexto:</strong><br>");
        html.append("<code>").append(escapeHtml(context.getStepDescription())).append("</code><br>");
        if (context.getElementLocator() != null) {
            html.append("<strong>🎯 Elemento buscado:</strong> ")
                    .append("<code>").append(escapeHtml(context.getElementLocator())).append("</code>");
        }
        html.append("</div>");

        // Análisis (convertir markdown a HTML básico)
        html.append("<div style='background-color: white; padding: 15px; ")
                .append("border: 1px solid #dee2e6; border-radius: 4px;'>");
        html.append(convertMarkdownToHtml(analysis));
        html.append("</div>");

        html.append("</div>");

        return html.toString();
    }

    /**
     * Convierte markdown básico a HTML para mejor visualización.
     */
    private String convertMarkdownToHtml(String markdown) {
        if (markdown == null) return "";

        String html = escapeHtml(markdown);

        // Headers
        html = html.replaceAll("### (.*?)\\n", "<h4 style='color: #495057; margin-top: 15px;'>$1</h4>\n");
        html = html.replaceAll("## (.*?)\\n", "<h3 style='color: #343a40;'>$1</h3>\n");
        html = html.replaceAll("# (.*?)\\n", "<h2 style='color: #212529;'>$1</h2>\n");

        // Bold
        html = html.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");

        // Code blocks
        html = html.replaceAll("`([^`]+)`", "<code style='background-color: #f8f9fa; padding: 2px 5px; border-radius: 3px;'>$1</code>");

        // Line breaks
        html = html.replace("\n", "<br>");

        return html;
    }

    /**
     * Escapa HTML para prevenir XSS.
     */
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    /**
     * Formatea la detección del fallo para Serenity.
     */
    private String formatFailureDetection(String step, Throwable cause) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div style='padding: 10px; background-color: #f8d7da; border-left: 4px solid #dc3545;'>");
        sb.append("<strong>Step:</strong> ").append(escapeHtml(step)).append("<br>");
        sb.append("<strong>Error:</strong> ").append(cause != null ? cause.getClass().getSimpleName() : "Unknown").append("<br>");
        if (cause != null && cause.getMessage() != null) {
            sb.append("<strong>Mensaje:</strong> <code>").append(escapeHtml(cause.getMessage())).append("</code>");
        }
        sb.append("</div>");
        return sb.toString();
    }

    /**
     * Formatea errores para Serenity.
     */
    private String formatError(Exception e) {
        return String.format(
                "<div style='padding: 10px; background-color: #f8d7da; color: #721c24;'>" +
                        "<strong>Error:</strong> %s<br>" +
                        "<strong>Mensaje:</strong> %s<br>" +
                        "<em>Verifica que Ollama esté ejecutándose y el modelo '%s' esté disponible.</em>" +
                        "</div>",
                e.getClass().getSimpleName(),
                escapeHtml(e.getMessage()),
                ollamaClient.getModel()
        );
    }

    /**
     * Verifica disponibilidad de Ollama al inicio.
     */
    private void verifyOllamaAvailability() {
        if (!ollamaClient.isEnabled()) {
            System.out.println("⏭️ [OllamaListener] Análisis deshabilitado via configuración");
            return;
        }

        System.out.println("🔍 [OllamaListener] Verificando disponibilidad de Ollama...");

        try {
            // Hacer una verificación rápida sin bloquear el test
            new Thread(() -> {
                boolean available = ollamaClient.isAvailable();
                if (available) {
                    System.out.println("✅ [OllamaListener] Ollama disponible y listo");
                } else {
                    System.err.println("⚠️ [OllamaListener] Ollama no responde. El análisis puede fallar.");
                    System.err.println("   Verifica: ollama serve");
                    System.err.println("   Modelo: ollama pull " + ollamaClient.getModel());
                }
            }).start();

        } catch (Exception e) {
            System.err.println("⚠️ [OllamaListener] Error verificando Ollama: " + e.getMessage());
        }
    }

    /**
     * Permite configurar el código USSD actual para contexto.
     */
    public void setCurrentUssdCode(String ussdCode) {
        this.currentUssdCode = ussdCode;
    }

    // ===========================
    // MÉTODOS BASE OBLIGATORIOS DE StepListener
    // ===========================

    @Override public void testSuiteStarted(Class<?> testSuite) {}
    @Override public void testSuiteStarted(Story story) {}
    @Override public void testSuiteFinished() {}
    @Override public void testStarted(String s, String s1) {}
    @Override public void testFinished(TestOutcome outcome) {
        currentTestName = null;
        currentUssdCode = null;
    }
    @Override public void testRetried() {}
    @Override public void lastStepFailed(StepFailure stepFailure) {}
    @Override public void testFailed(TestOutcome outcome, Throwable cause) {}
    @Override public void testIgnored() {}
    @Override public void testSkipped() {}
    @Override public void testPending() {}
    @Override public void testIsManual() {}
    @Override public void stepStarted(ExecutedStepDescription description) {}
    @Override public void skippedStepStarted(ExecutedStepDescription executedStepDescription) {}
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
    @Override public void testRunFinished() {
        // Cerrar cliente al finalizar toda la ejecución
        ollamaClient.close();
    }
}