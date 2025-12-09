package utils.ollama;

/**
 * Construye prompts inteligentes y contextuales para análisis de fallos con IA.
 *
 * Especializado en automatización USSD con análisis comparativo entre:
 * - Lo que se esperaba encontrar (elemento buscado)
 * - Lo que realmente existe en el page source
 *
 * Genera prompts estructurados que guían al modelo IA para proporcionar:
 * 1. Diagnóstico preciso del problema
 * 2. Elementos similares que podrían ser el objetivo
 * 3. Recomendaciones concretas de solución
 * 4. Contexto específico de USSD/Android
 *
 * Principios SOLID:
 * - SRP: Responsabilidad única de construir prompts de análisis
 * - OCP: Extensible para diferentes tipos de análisis
 * - DIP: No depende de implementaciones concretas de IA
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class OllamaAnalyzer {

    // Límite de caracteres del page source para el análisis
    private static final int MAX_PAGE_SOURCE_LENGTH = 8000;

    /**
     * Construye un prompt especializado para analizar fallos de localización de elementos.
     *
     * @param context Contexto completo del fallo
     * @return Prompt optimizado para análisis de IA
     */
    public static String buildElementNotFoundPrompt(FailureContext context) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Eres un experto en automatización de pruebas Android y análisis de fallos USSD.\n\n");

        // CONTEXTO DEL FALLO
        prompt.append("=== CONTEXTO DEL FALLO ===\n");
        prompt.append("Test: ").append(context.getTestName()).append("\n");
        prompt.append("Step: ").append(context.getStepDescription()).append("\n");
        prompt.append("Timestamp: ").append(context.getFormattedTimestamp()).append("\n");

        if (context.getUssdCode() != null) {
            prompt.append("Código USSD: ").append(context.getUssdCode()).append("\n");
        }

        prompt.append("\n");

        // ELEMENTO BUSCADO
        prompt.append("=== ELEMENTO QUE SE BUSCABA ===\n");
        prompt.append("Tipo de localizador: ").append(context.getElementType()).append("\n");
        prompt.append("Localizador: ").append(context.getElementLocator()).append("\n");
        prompt.append("\n");

        // ERROR
        prompt.append("=== ERROR REPORTADO ===\n");
        prompt.append(context.getErrorMessage()).append("\n\n");

        // PAGE SOURCE (truncado inteligentemente)
        prompt.append("=== PAGE SOURCE ACTUAL ===\n");
        String pageSource = truncatePageSourceIntelligently(context.getPageSource());
        prompt.append(pageSource).append("\n\n");

        // INSTRUCCIONES DE ANÁLISIS
        prompt.append("=== ANÁLISIS REQUERIDO ===\n");
        prompt.append("Proporciona un análisis estructurado en las siguientes secciones:\n\n");

        prompt.append("1. DIAGNÓSTICO:\n");
        prompt.append("   - ¿Por qué no se encontró el elemento?\n");
        prompt.append("   - ¿El elemento existe pero con diferente localizador?\n");
        prompt.append("   - ¿Hay problemas de timing (elemento no cargado aún)?\n");
        prompt.append("   - ¿Es un cambio en la UI/UX de la aplicación?\n\n");

        prompt.append("2. ELEMENTOS SIMILARES ENCONTRADOS:\n");
        prompt.append("   - Lista elementos en el page source que podrían ser el objetivo\n");
        prompt.append("   - Incluye sus localizadores exactos (text, resource-id, content-desc)\n");
        prompt.append("   - Explica por qué cada uno podría ser el elemento buscado\n\n");

        prompt.append("3. RECOMENDACIÓN DE SOLUCIÓN:\n");
        prompt.append("   - Localizador específico que debería funcionar\n");
        prompt.append("   - Estrategia de espera si es problema de timing\n");
        prompt.append("   - Fallback alternativo si el elemento cambió\n\n");

        prompt.append("4. CONTEXTO USSD:\n");
        prompt.append("   - ¿Es un problema común en flujos USSD?\n");
        prompt.append("   - ¿Podría ser específico del fabricante del dispositivo?\n");
        prompt.append("   - ¿Hay diferencias entre menús USSD de diferentes operadores?\n\n");

        prompt.append("Formato: Usa markdown para mejor legibilidad en el reporte HTML.");

        return prompt.toString();
    }

    /**
     * Construye un prompt para análisis general de fallos (no específicos de localización).
     *
     * @param context Contexto del fallo
     * @return Prompt para análisis general
     */
    public static String buildGeneralAnalysisPrompt(FailureContext context) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("Analiza este fallo de automatización USSD y proporciona:\n\n");

        prompt.append("ERROR:\n");
        prompt.append(context.getErrorMessage()).append("\n\n");

        if (context.getStackTrace() != null) {
            prompt.append("STACK TRACE:\n");
            prompt.append(context.getStackTrace()).append("\n\n");
        }

        prompt.append("CONTEXTO:\n");
        prompt.append("- Test: ").append(context.getTestName()).append("\n");
        prompt.append("- Step: ").append(context.getStepDescription()).append("\n\n");

        prompt.append("Proporciona:\n");
        prompt.append("1. Causa probable del fallo\n");
        prompt.append("2. Recomendaciones para resolverlo\n");
        prompt.append("3. Mejores prácticas para evitar este tipo de fallos\n");

        return prompt.toString();
    }

    /**
     * Trunca el page source de forma inteligente, preservando información relevante.
     *
     * Estrategia:
     * - Mantiene elementos interactivos (TextView, EditText, Button)
     * - Elimina Views genéricos sin contenido
     * - Preserva atributos importantes (text, resource-id, content-desc)
     *
     * @param pageSource Page source completo
     * @return Page source optimizado para análisis
     */
    private static String truncatePageSourceIntelligently(String pageSource) {
        if (pageSource == null) {
            return "[Page source no disponible]";
        }

        if (pageSource.length() <= MAX_PAGE_SOURCE_LENGTH) {
            return pageSource;
        }

        // Extraer solo nodos relevantes usando regex simple
        StringBuilder relevantNodes = new StringBuilder();

        // Patrones de elementos relevantes en USSD
        String[] relevantPatterns = {
                "android.widget.TextView",
                "android.widget.EditText",
                "android.widget.Button",
                "text=\"[^\"]+\"",
                "resource-id=\"[^\"]+\"",
                "content-desc=\"[^\"]+\""
        };

        // Dividir en líneas y filtrar las relevantes
        String[] lines = pageSource.split("\n");
        int charCount = 0;
        boolean truncated = false;

        for (String line : lines) {
            // Verificar si la línea contiene información relevante
            boolean isRelevant = false;
            for (String pattern : relevantPatterns) {
                if (line.contains("TextView") || line.contains("EditText") ||
                        line.contains("Button") || line.contains("text=") ||
                        line.contains("resource-id=") || line.contains("content-desc=")) {
                    isRelevant = true;
                    break;
                }
            }

            if (isRelevant && charCount < MAX_PAGE_SOURCE_LENGTH) {
                relevantNodes.append(line).append("\n");
                charCount += line.length();
            }

            if (charCount >= MAX_PAGE_SOURCE_LENGTH) {
                truncated = true;
                break;
            }
        }

        if (truncated) {
            relevantNodes.append("\n... [Page source truncado. Mostrando solo elementos interactivos relevantes]");
        }

        String result = relevantNodes.toString();

        // Si después del filtrado queda muy poco, usar truncado simple
        if (result.length() < 1000) {
            return pageSource.substring(0, Math.min(MAX_PAGE_SOURCE_LENGTH, pageSource.length())) +
                    "\n... [truncado]";
        }

        return result;
    }

    /**
     * Determina el tipo de análisis apropiado según el tipo de excepción.
     *
     * @param error Excepción lanzada
     * @return Tipo de análisis recomendado
     */
    public static AnalysisType determineAnalysisType(Throwable error) {
        if (error == null) {
            return AnalysisType.GENERAL;
        }

        String errorClass = error.getClass().getSimpleName().toLowerCase();
        String errorMessage = error.getMessage() != null ? error.getMessage().toLowerCase() : "";

        if (errorClass.contains("nosuchelement") || errorMessage.contains("could not be located")) {
            return AnalysisType.ELEMENT_NOT_FOUND;
        }

        if (errorClass.contains("timeout") || errorMessage.contains("timeout")) {
            return AnalysisType.TIMEOUT;
        }

        if (errorClass.contains("stale") || errorMessage.contains("stale")) {
            return AnalysisType.STALE_ELEMENT;
        }

        return AnalysisType.GENERAL;
    }

    /**
     * Tipos de análisis disponibles.
     */
    public enum AnalysisType {
        ELEMENT_NOT_FOUND,
        TIMEOUT,
        STALE_ELEMENT,
        GENERAL
    }
}