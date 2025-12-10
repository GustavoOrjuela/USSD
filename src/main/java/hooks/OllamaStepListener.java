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
import utils.MyDriver;
import utils.ollama.FailureContext;
import utils.ollama.OllamaAnalyzer;
import utils.ollama.OllamaClient;

import java.util.Map;

/**
 * VERSION 2.0 - Listener de Serenity BDD con análisis IA mejorado
 *
 * MEJORAS:
 * - Validación de page source capturado
 * - Logging detallado del tamaño de contexto
 * - Alertas cuando el page source está vacío
 * - HTML enriquecido con highlights específicos
 *
 * @author Senior Test Automation Engineer
 * @since 2.0
 */
public class OllamaStepListener implements StepListener {

    private final OllamaClient ollamaClient;
    private boolean initialized = false;
    private String currentTestName;
    private String currentUssdCode;

    public OllamaStepListener() {
        this.ollamaClient = new OllamaClient();
        System.out.println("🔗 [OllamaListener v2.0] Inicializado");
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
        System.out.println("║  🚨 FALLO DETECTADO - INICIANDO ANÁLISIS OLLAMA v2.0        ║");
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

        // VALIDACIÓN CRÍTICA: Verificar tamaño del page source
        int pageSourceSize = context.getPageSource() != null ? context.getPageSource().length() : 0;
        System.out.println("📊 Tamaño del page source capturado: " + pageSourceSize + " caracteres");

        if (pageSourceSize < 1000) {
            System.err.println("⚠️⚠️⚠️ [CRÍTICO] PAGE SOURCE VACÍO O CORRUPTO ⚠️⚠️⚠️");
            System.err.println("   El análisis de IA será genérico sin elementos reales.");
            System.err.println("   Causas posibles:");
            System.err.println("   - Driver no capturó el page source correctamente");
            System.err.println("   - Timing: página no cargada completamente");
            System.err.println("   - Problema con Appium/UiAutomator");

            // Registrar alerta en Serenity
            Serenity.recordReportData()
                    .withTitle("⚠️ Alerta: Page Source Vacío")
                    .andContents(formatPageSourceWarning(pageSourceSize));
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

            // Capturar page source y screenshot del driver (CON RETRY ROBUSTO)
            if (driver != null) {
                contextBuilder.withDriver(driver); // Ahora tiene retry inteligente
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

            return context;

        } catch (Exception e) {
            System.err.println("❌ [OllamaListener] Error construyendo contexto: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * VERSION 2.0: Extracción mejorada de localizador completo.
     */
    private void extractLocatorInfo(String errorMessage, FailureContext.Builder contextBuilder) {
        try {
            System.out.println("🔍 Extrayendo localizador de: " + errorMessage.substring(0, Math.min(200, errorMessage.length())) + "...");

            if (errorMessage.contains("uiautomator")) {
                // Buscar "value=" y capturar TODO hasta el final del UiSelector
                int valueStart = errorMessage.indexOf("value=");
                if (valueStart != -1) {
                    String fullLocator = errorMessage.substring(valueStart + 6);

                    // Limpiar hasta encontrar el delimitador correcto
                    // Delimitadores: "}", "\n", "Session ID"
                    int endIndex = -1;

                    if (fullLocator.contains("}")) {
                        endIndex = fullLocator.indexOf("}");
                    }
                    if (endIndex == -1 && fullLocator.contains("\n")) {
                        endIndex = fullLocator.indexOf("\n");
                    }
                    if (endIndex == -1 && fullLocator.contains("Session ID")) {
                        endIndex = fullLocator.indexOf("Session ID");
                    }

                    String cleanLocator = endIndex != -1 ?
                            fullLocator.substring(0, endIndex).trim() :
                            fullLocator.trim();

                    contextBuilder.withElementLocator(cleanLocator, "uiautomator");
                    System.out.println("🎯 Localizador completo extraído: " + cleanLocator);
                }
            } else if (errorMessage.contains("xpath")) {
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
     * VERSION 2.0: HTML enriquecido con highlights específicos para textos encontrados.
     */
    private String formatOllamaAnalysisAsHtml(String analysis, FailureContext context, long durationMs) {
        StringBuilder html = new StringBuilder();

        html.append("<div style='font-family: Arial, sans-serif; padding: 15px; ")
                .append("background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); ")
                .append("border-radius: 8px; margin: 10px 0; color: white;'>");

        // Header con gradiente
        html.append("<div style='padding: 15px; margin: -15px -15px 15px -15px;'>");
        html.append("<h3 style='margin: 0; font-size: 20px;'>🧠 Análisis Inteligente con IA</h3>");
        html.append("<small style='opacity: 0.9;'>Modelo: ").append(ollamaClient.getModel())
                .append(" | Duración: ").append(String.format("%.2f", durationMs / 1000.0))
                .append("s | ").append(context.getFormattedTimestamp()).append("</small>");
        html.append("</div>");

        // Contexto del fallo
        html.append("<div style='background-color: rgba(255,255,255,0.95); padding: 12px; ")
                .append("border-left: 4px solid #ffc107; margin-bottom: 15px; ")
                .append("border-radius: 4px; color: #333;'>");
        html.append("<strong style='color: #d32f2f;'>📍 Contexto:</strong><br>");
        html.append("<code style='background: #f5f5f5; padding: 3px 6px; border-radius: 3px;'>")
                .append(escapeHtml(context.getStepDescription().substring(0, Math.min(150, context.getStepDescription().length()))))
                .append("</code><br>");
        if (context.getElementLocator() != null) {
            html.append("<strong style='color: #1976d2;'>🎯 Elemento buscado:</strong> ")
                    .append("<code style='background: #e3f2fd; padding: 3px 6px; border-radius: 3px; ")
                    .append("font-weight: 600;'>").append(escapeHtml(context.getElementLocator())).append("</code>");
        }
        html.append("</div>");

        // Análisis (convertir markdown a HTML con highlights)
        html.append("<div style='background-color: white; padding: 15px; ")
                .append("border-radius: 6px; color: #333; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>");
        html.append(convertMarkdownToHtmlEnhanced(analysis));
        html.append("</div>");

        html.append("</div>");

        return html.toString();
    }

    /**
     * VERSION 2.0: Conversión mejorada con highlights específicos para textos entre comillas.
     */
    private String convertMarkdownToHtmlEnhanced(String markdown) {
        if (markdown == null) return "";

        String html = escapeHtml(markdown);

        // Headers con colores
        html = html.replaceAll("### (.*?)(&lt;br&gt;|\\n)",
                "<h4 style='color: #d32f2f; margin-top: 15px; border-bottom: 2px solid #ffcdd2; padding-bottom: 5px;'>$1</h4>");
        html = html.replaceAll("## (.*?)(&lt;br&gt;|\\n)",
                "<h3 style='color: #1976d2; margin-top: 20px; border-bottom: 3px solid #bbdefb; padding-bottom: 8px;'>$1</h3>");

        // Bold
        html = html.replaceAll("\\*\\*(.*?)\\*\\*", "<strong style='color: #d32f2f;'>$1</strong>");

        // Code blocks con gradiente
        html = html.replaceAll("`([^`]+)`",
                "<code style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); " +
                        "color: white; padding: 3px 8px; border-radius: 4px; font-weight: 500;'>$1</code>");

        // ⭐ HIGHLIGHT ESPECÍFICO: Textos entre comillas (probables elementos encontrados)
        html = html.replaceAll("&quot;([^&]+)&quot;",
                "<span style='background: linear-gradient(135deg, #fff9c4 0%, #ffeb3b 100%); " +
                        "padding: 4px 8px; border-radius: 4px; border: 2px solid #fbc02d; " +
                        "font-weight: 600; color: #000; display: inline-block; margin: 2px;'>\"$1\"</span>");

        // Listas numeradas con íconos
        html = html.replaceAll("(\\d+)\\. ",
                "<span style='display: inline-block; width: 24px; height: 24px; background: #4caf50; " +
                        "color: white; border-radius: 50%; text-align: center; line-height: 24px; " +
                        "margin-right: 8px; font-size: 14px;'>$1</span>");

        // Line breaks
        html = html.replace("\n", "<br>");

        return html;
    }

    /**
     * Formatea alerta de page source vacío.
     */
    private String formatPageSourceWarning(int size) {
        return String.format(
                "<div style='padding: 15px; background: linear-gradient(135deg, #ff6b6b 0%, #c92a2a 100%); " +
                        "color: white; border-radius: 6px; border-left: 5px solid #fff;'>" +
                        "<h4 style='margin: 0 0 10px 0;'>⚠️ Page Source Vacío Detectado</h4>" +
                        "<p style='margin: 5px 0;'><strong>Tamaño capturado:</strong> %d caracteres (mínimo esperado: 1000)</p>" +
                        "<p style='margin: 5px 0;'><strong>Impacto:</strong> El análisis de IA será genérico sin elementos reales de la pantalla.</p>" +
                        "<p style='margin: 5px 0;'><strong>Causas posibles:</strong></p>" +
                        "<ul style='margin: 5px 0 0 20px;'>" +
                        "<li>Driver no capturó el page source correctamente</li>" +
                        "<li>Timing: Pantalla USSD no cargada completamente</li>" +
                        "<li>Problema con Appium/UiAutomator en el dispositivo</li>" +
                        "</ul>" +
                        "</div>",
                size
        );
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
        sb.append("<strong>Step:</strong> ").append(escapeHtml(step.substring(0, Math.min(200, step.length())))).append("<br>");
        sb.append("<strong>Error:</strong> ").append(cause != null ? cause.getClass().getSimpleName() : "Unknown").append("<br>");
        if (cause != null && cause.getMessage() != null) {
            sb.append("<strong>Mensaje:</strong> <code>").append(escapeHtml(cause.getMessage().substring(0, Math.min(300, cause.getMessage().length())))).append("</code>");
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
        ollamaClient.close();
    }
}