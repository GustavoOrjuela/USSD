package utils.ollama;

/**
 * VERSION 2.0 - ULTRA-ESPECÍFICO para detección de cambios de texto USSD
 *
 * MEJORAS CRÍTICAS:
 * 1. Prompt super-directo que fuerza búsqueda de elementos similares
 * 2. Instrucciones explícitas para comparar textos
 * 3. Búsqueda forzada de elementos con patrón "X."
 * 4. Formato de respuesta estructurado y conciso
 * 5. Ejemplo explícito de lo que se espera
 *
 * @author Senior Test Automation Engineer
 * @since 2.0
 */
public class OllamaAnalyzer {

    // Límite AUMENTADO para asegurar que capturamos todos los elementos de texto
    private static final int MAX_PAGE_SOURCE_LENGTH = 3000; // AUMENTADO de 1000

    /**
     * VERSION 2.0: Prompt ultra-específico que fuerza a phi3 a:
     * 1. Buscar EXACTAMENTE elementos con patrón similar
     * 2. Comparar textos explícitamente
     * 3. Listar TODOS los candidatos encontrados
     * 4. Proporcionar solución CONCRETA
     */
    public static String buildElementNotFoundPrompt(FailureContext context) {
        StringBuilder prompt = new StringBuilder();

        // ============================================
        // ENCABEZADO ULTRA-DIRECTO
        // ============================================
        prompt.append("ANÁLISIS RÁPIDO DE FALLO USSD ANDROID\n\n");

        // ============================================
        // CONTEXTO MÍNIMO
        // ============================================
        prompt.append("📍 ELEMENTO BUSCADO:\n");
        prompt.append("Localizador: ").append(context.getElementLocator()).append("\n");

        // Extraer texto esperado
        String expectedText = extractSearchText(context.getElementLocator());
        if (expectedText != null) {
            prompt.append("Texto esperado: \"").append(expectedText).append("\"\n");
        }
        prompt.append("\n");

        // ============================================
        // PAGE SOURCE COMPACTO PERO COMPLETO
        // ============================================
        prompt.append("📱 ELEMENTOS EN PANTALLA:\n");
        String pageSource = extractRelevantElements(context.getPageSource());

        // CRÍTICO: Validar que el page source tenga contenido
        if (pageSource == null || pageSource.length() < 100) {
            prompt.append("[ERROR: Page source vacío o corrupto. No se pueden analizar elementos.]\n\n");
            prompt.append("RESPUESTA REQUERIDA:\n");
            prompt.append("El page source está vacío. Verifica que:\n");
            prompt.append("1. El driver capturó el page source correctamente\n");
            prompt.append("2. La pantalla USSD estaba completamente cargada\n");
            prompt.append("3. No hay problemas de timing\n");
            return prompt.toString();
        }

        prompt.append(pageSource).append("\n\n");

        // ============================================
        // INSTRUCCIONES ULTRA-ESPECÍFICAS
        // ============================================
        prompt.append("🎯 TAREA:\n");
        prompt.append("1. Busca TODOS los elementos que contengan textos similares a \"").append(expectedText).append("\"\n");
        prompt.append("2. Busca elementos con el mismo PATRÓN (ejemplo: si buscas \"3. codensa\", busca \"3. XXXX\")\n");
        prompt.append("3. Lista CADA elemento encontrado con su texto COMPLETO\n");
        prompt.append("4. Proporciona el localizador UiSelector EXACTO que funcionaría\n\n");

        // ============================================
        // FORMATO DE RESPUESTA FORZADO
        // ============================================
        prompt.append("FORMATO DE RESPUESTA (usa EXACTAMENTE este formato):\n\n");
        prompt.append("## DIAGNÓSTICO\n");
        prompt.append("[Una línea explicando por qué falló]\n\n");

        prompt.append("## ELEMENTOS ENCONTRADOS\n");
        prompt.append("1. TextView text=\"[texto completo]\" → Candidato porque [razón]\n");
        prompt.append("2. TextView text=\"[texto completo]\" → Candidato porque [razón]\n");
        prompt.append("[Lista TODOS los elementos similares]\n\n");

        prompt.append("## SOLUCIÓN\n");
        prompt.append("Usar: `new UiSelector().textContains(\"[texto exacto]\")`\n");
        prompt.append("Alternativa: `new UiSelector().textContains(\"[texto alternativo]\")`\n\n");

        // ============================================
        // EJEMPLO EXPLÍCITO
        // ============================================
        prompt.append("EJEMPLO:\n");
        prompt.append("Si buscabas \"3. codensa\" y encuentras \"3. Nequi\", tu respuesta debe ser:\n\n");
        prompt.append("## DIAGNÓSTICO\n");
        prompt.append("El texto \"3. codensa\" cambió a \"3. Nequi\" en el menú USSD.\n\n");

        prompt.append("## ELEMENTOS ENCONTRADOS\n");
        prompt.append("1. TextView text=\"3. Nequi\" → Candidato porque tiene el mismo patrón \"3.\"\n");
        prompt.append("2. TextView text=\"3. PSE\" → Candidato alternativo con patrón \"3.\"\n\n");

        prompt.append("## SOLUCIÓN\n");
        prompt.append("Usar: `new UiSelector().textContains(\"3. Nequi\")`\n");
        prompt.append("Alternativa: `new UiSelector().textContains(\"3.\")` (más genérico)\n\n");

        prompt.append("---\n");
        prompt.append("AHORA ANALIZA EL PAGE SOURCE Y PROPORCIONA TU RESPUESTA:\n");

        return prompt.toString();
    }

    /**
     * VERSION 2.0: Extracción mejorada que preserva MÁS información
     * pero elimina ruido (Views vacíos, Layouts sin contenido)
     */
    private static String extractRelevantElements(String pageSource) {
        if (pageSource == null || pageSource.isEmpty()) {
            return "[Page source no disponible]";
        }

        if (pageSource.length() <= MAX_PAGE_SOURCE_LENGTH) {
            return pageSource;
        }

        StringBuilder relevant = new StringBuilder();
        String[] lines = pageSource.split("\n");
        int charCount = 0;
        int elementCount = 0;

        for (String line : lines) {
            // Filtrar solo líneas con información útil
            boolean isRelevant =
                    line.contains("TextView") ||
                            line.contains("EditText") ||
                            line.contains("Button") ||
                            line.contains("text=") ||
                            line.contains("resource-id=") ||
                            line.contains("content-desc=");

            if (isRelevant && charCount < MAX_PAGE_SOURCE_LENGTH) {
                // Limpiar línea (remover espacios excesivos)
                String cleanLine = line.trim().replaceAll("\\s+", " ");
                relevant.append(cleanLine).append("\n");
                charCount += cleanLine.length();
                elementCount++;
            }

            if (charCount >= MAX_PAGE_SOURCE_LENGTH) {
                break;
            }
        }

        if (elementCount == 0) {
            // Si no encontramos NADA relevante, devolver las primeras líneas raw
            relevant.append("[No se encontraron elementos de texto. Page source raw:]\n");
            relevant.append(pageSource.substring(0, Math.min(1000, pageSource.length())));
        } else {
            relevant.append("\n[Total: ").append(elementCount).append(" elementos relevantes]");
        }

        return relevant.toString();
    }

    /**
     * Extrae el texto esperado del localizador UiSelector.
     *
     * Ejemplos:
     * - textContains("3. codensa") → "3. codensa"
     * - text("Aceptar") → "Aceptar"
     * - resourceId("btn_ok") → "btn_ok"
     */
    private static String extractSearchText(String locator) {
        if (locator == null || locator.isEmpty()) {
            return null;
        }

        // Buscar textContains("...")
        if (locator.contains("textContains")) {
            int start = locator.indexOf("textContains(\"") + 14;
            int end = locator.indexOf("\")", start);
            if (start > 13 && end > start) {
                return locator.substring(start, end);
            }
        }

        // Buscar text("...")
        if (locator.contains("text(\"")) {
            int start = locator.indexOf("text(\"") + 6;
            int end = locator.indexOf("\")", start);
            if (start > 5 && end > start) {
                return locator.substring(start, end);
            }
        }

        // Buscar resourceId("...")
        if (locator.contains("resourceId(\"")) {
            int start = locator.indexOf("resourceId(\"") + 12;
            int end = locator.indexOf("\")", start);
            if (start > 11 && end > start) {
                return locator.substring(start, end);
            }
        }

        return null;
    }

    /**
     * Construye prompt para análisis general (no específico de localización).
     */
    public static String buildGeneralAnalysisPrompt(FailureContext context) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("ANÁLISIS RÁPIDO DE FALLO:\n\n");
        prompt.append("ERROR: ").append(context.getErrorMessage()).append("\n\n");
        prompt.append("Proporciona en 3 líneas:\n");
        prompt.append("1. Causa probable\n");
        prompt.append("2. Solución recomendada\n");
        prompt.append("3. Cómo evitarlo\n");

        return prompt.toString();
    }

    /**
     * Determina el tipo de análisis apropiado según el tipo de excepción.
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