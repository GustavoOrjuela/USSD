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
     * VERSION 2.1: Formato de tarjetas apiladas para máxima legibilidad.
     *
     * DISEÑO:
     * - Estructura de tarjetas con bordes claros
     * - Separación visual entre secciones
     * - Headers con iconos y fondo de color
     * - Contenido organizado en bloques
     * - Código destacado con fondo gris
     */
    private String formatOllamaAnalysisAsHtml(String analysis, FailureContext context, long durationMs) {
        StringBuilder html = new StringBuilder();

        // ═══════════════════════════════════════════════════════════
        // CONTENEDOR PRINCIPAL
        // ═══════════════════════════════════════════════════════════
        html.append("<div style='font-family: Arial, sans-serif; max-width: 900px; margin: 10px 0;'>");

        // ═══════════════════════════════════════════════════════════
        // TARJETA 1: HEADER CON GRADIENTE
        // ═══════════════════════════════════════════════════════════
        html.append("<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); ")
                .append("padding: 15px; border-radius: 8px 8px 0 0; color: white; ")
                .append("box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>");
        html.append("<h3 style='margin: 0 0 5px 0; font-size: 18px; font-weight: 600;'>")
                .append("🧠 Análisis Inteligente con IA</h3>");
        html.append("<div style='font-size: 12px; opacity: 0.9;'>")
                .append("📊 Modelo: <strong>").append(ollamaClient.getModel()).append("</strong> | ")
                .append("⏱️ Duración: <strong>").append(String.format("%.1f", durationMs / 1000.0)).append("s</strong> | ")
                .append("🕐 ").append(context.getFormattedTimestamp())
                .append("</div>");
        html.append("</div>");

        // ═══════════════════════════════════════════════════════════
        // TARJETA 2: CONTEXTO DEL FALLO
        // ═══════════════════════════════════════════════════════════
        html.append("<div style='background: #fff3cd; border-left: 5px solid #ffc107; ")
                .append("padding: 12px 15px; margin-top: 2px;'>");
        html.append("<div style='font-weight: 600; color: #856404; margin-bottom: 8px; font-size: 14px;'>")
                .append("📍 CONTEXTO DEL FALLO</div>");

        html.append("<div style='color: #666; font-size: 13px; margin-bottom: 6px;'>")
                .append("<strong>Error:</strong> ").append(getSimpleErrorType(context.getErrorMessage()))
                .append("</div>");

        if (context.getElementLocator() != null) {
            html.append("<div style='color: #666; font-size: 13px;'>")
                    .append("<strong>Buscando:</strong> ")
                    .append("<code style='background: #fff; padding: 2px 6px; border: 1px solid #ddd; ")
                    .append("border-radius: 3px; font-size: 12px;'>")
                    .append(escapeHtml(shortenLocator(context.getElementLocator())))
                    .append("</code>")
                    .append("</div>");
        }
        html.append("</div>");

        // ═══════════════════════════════════════════════════════════
        // TARJETA 3: CONTENIDO DEL ANÁLISIS (PARSEADO EN SECCIONES)
        // ═══════════════════════════════════════════════════════════
        html.append(parseAnalysisIntoCards(analysis));

        // Cerrar contenedor principal
        html.append("</div>");

        return html.toString();
    }

    /**
     * Parsea el análisis de Ollama en tarjetas separadas por sección.
     */
    private String parseAnalysisIntoCards(String analysis) {
        if (analysis == null || analysis.trim().isEmpty()) {
            return "<div style='background: #f8f9fa; padding: 15px; text-align: center; color: #999;'>" +
                    "Sin análisis disponible</div>";
        }

        StringBuilder cards = new StringBuilder();
        String[] sections = analysis.split("##\\s+");

        for (String section : sections) {
            if (section.trim().isEmpty()) continue;

            String[] lines = section.split("\n", 2);
            if (lines.length < 2) continue;

            String title = lines[0].trim();
            String content = lines.length > 1 ? lines[1].trim() : "";

            // Determinar icono y color según el título
            String icon = "📄";
            String borderColor = "#dee2e6";
            String bgColor = "#ffffff";

            if (title.toUpperCase().contains("DIAGNÓSTICO") || title.toUpperCase().contains("DIAGNOSTICO")) {
                icon = "🔍";
                borderColor = "#0d6efd";
                bgColor = "#e7f3ff";
            } else if (title.toUpperCase().contains("ELEMENTOS")) {
                icon = "📋";
                borderColor = "#198754";
                bgColor = "#e8f5e9";
            } else if (title.toUpperCase().contains("SOLUCIÓN") || title.toUpperCase().contains("SOLUCION")) {
                icon = "✅";
                borderColor = "#28a745";
                bgColor = "#d4edda";
            }

            // Crear tarjeta para esta sección
            cards.append("<div style='background: ").append(bgColor).append("; ")
                    .append("border-left: 5px solid ").append(borderColor).append("; ")
                    .append("padding: 15px; margin-top: 2px; box-shadow: 0 1px 3px rgba(0,0,0,0.05);'>");

            // Header de la sección
            cards.append("<div style='font-weight: 600; color: #333; margin-bottom: 10px; ")
                    .append("font-size: 14px; text-transform: uppercase; letter-spacing: 0.5px;'>")
                    .append(icon).append(" ").append(escapeHtml(title))
                    .append("</div>");

            // Contenido formateado
            cards.append("<div style='color: #495057; font-size: 13px; line-height: 1.6;'>")
                    .append(formatSectionContent(content))
                    .append("</div>");

            cards.append("</div>");
        }

        return cards.toString();
    }

    /**
     * Formatea el contenido de cada sección con highlights y estructura.
     */
    private String formatSectionContent(String content) {
        if (content == null) return "";

        String formatted = escapeHtml(content);

        // Listas numeradas con mejor formato
        formatted = formatted.replaceAll("(\\d+)\\. ",
                "<br><strong style='color: #495057;'>$1.</strong> ");

        // Highlights en textos entre comillas
        formatted = formatted.replaceAll("&quot;([^&]+?)&quot;",
                "<mark style='background-color: #fff3cd; padding: 1px 4px; font-weight: 500; " +
                        "border-radius: 2px;'>\"$1\"</mark>");

        // Code blocks
        formatted = formatted.replaceAll("`([^`]+)`",
                "<code style='background-color: #f8f9fa; color: #e83e8c; padding: 2px 6px; " +
                        "border-radius: 3px; font-family: Consolas, monospace; font-size: 12px; " +
                        "border: 1px solid #e9ecef;'>$1</code>");

        // Flechas indicadoras
        formatted = formatted.replace("→",
                "<span style='color: #0d6efd; font-weight: bold; margin: 0 4px;'>→</span>");

        // Bold
        formatted = formatted.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");

        // Line breaks
        formatted = formatted.replace("\n", "<br>");

        // Limpiar múltiples <br> consecutivos
        formatted = formatted.replaceAll("(<br>\\s*){3,}", "<br><br>");

        return formatted;
    }

    /**
     * Obtiene tipo de error simplificado.
     */
    private String getSimpleErrorType(String errorMessage) {
        if (errorMessage == null) return "Error desconocido";

        if (errorMessage.contains("NoSuchElement")) {
            return "Elemento no encontrado";
        } else if (errorMessage.contains("Timeout")) {
            return "Timeout esperando elemento";
        } else if (errorMessage.contains("StaleElement")) {
            return "Elemento obsoleto";
        }

        return "Error de automatización";
    }

    /**
     * Acorta localizadores muy largos para mejor visualización.
     */
    private String shortenLocator(String locator) {
        if (locator == null || locator.length() <= 80) {
            return locator;
        }
        return locator.substring(0, 77) + "...";
    }


    /**
     * VERSION 2.1: Conversión SIMPLIFICADA para mejor legibilidad y consistencia con log.
     *
     * CAMBIOS v2.1:
     * - Headers simples con color y underline limpio
     * - Highlights amarillos suaves solo en textos clave
     * - Code blocks con fondo gris (sin gradientes)
     * - Listas numeradas en texto plano (sin círculos decorativos)
     * - Formato consistente con salida de consola
     */
    private String convertMarkdownToHtmlEnhanced(String markdown) {
        if (markdown == null) return "";

        String html = escapeHtml(markdown);

        // Headers simples con color (sin bordes gruesos)
        html = html.replaceAll("### (.*?)(&lt;br&gt;|\\n)",
                "<h4 style='color: #424242; margin-top: 12px; margin-bottom: 8px; " +
                        "border-bottom: 1px solid #e0e0e0; padding-bottom: 4px;'>$1</h4>");

        html = html.replaceAll("## (.*?)(&lt;br&gt;|\\n)",
                "<h3 style='color: #1565c0; margin-top: 15px; margin-bottom: 8px; " +
                        "border-bottom: 2px solid #e3f2fd; padding-bottom: 6px;'>$1</h3>");

        // Bold simple (sin color adicional)
        html = html.replaceAll("\\*\\*(.*?)\\*\\*", "<strong>$1</strong>");

        // Code blocks simples con fondo gris
        html = html.replaceAll("`([^`]+)`",
                "<code style='background-color: #f5f5f5; color: #333; " +
                        "padding: 2px 6px; border-radius: 3px; font-family: Consolas, monospace; " +
                        "border: 1px solid #e0e0e0;'>$1</code>");

        // Highlights SIMPLES en textos entre comillas (elementos encontrados)
        html = html.replaceAll("&quot;([^&]+)&quot;",
                "<mark style='background-color: #fff9c4; padding: 2px 4px; " +
                        "border-radius: 2px; font-weight: 500;'>\"$1\"</mark>");

        // Listas numeradas SIMPLES (sin círculos, solo bold)
        html = html.replaceAll("(\\d+)\\. ", "<strong>$1.</strong> ");

        // Flechas para indicadores
        html = html.replace("→", "<span style='color: #1976d2; font-weight: bold;'>→</span>");

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