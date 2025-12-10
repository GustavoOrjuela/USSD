package utils.ollama;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Contexto completo de un fallo de prueba para análisis con IA.
 *
 * VERSION 2.0 - MEJORAS:
 * - Captura robusta de page source con retry y validación
 * - Detección de page source vacío/corrupto
 * - Logging detallado del tamaño capturado
 * - Fallback con múltiples intentos
 *
 * @author Senior Test Automation Engineer
 * @since 2.0
 */
public class FailureContext {

    private final String testName;
    private final String stepDescription;
    private final String errorMessage;
    private final String stackTrace;
    private final String pageSource;
    private final String screenshotBase64;
    private final String elementLocator;
    private final String elementType;
    private final LocalDateTime timestamp;
    private final String ussdCode;

    private FailureContext(Builder builder) {
        this.testName = builder.testName;
        this.stepDescription = builder.stepDescription;
        this.errorMessage = builder.errorMessage;
        this.stackTrace = builder.stackTrace;
        this.pageSource = builder.pageSource;
        this.screenshotBase64 = builder.screenshotBase64;
        this.elementLocator = builder.elementLocator;
        this.elementType = builder.elementType;
        this.timestamp = LocalDateTime.now();
        this.ussdCode = builder.ussdCode;
    }

    // Getters
    public String getTestName() { return testName; }
    public String getStepDescription() { return stepDescription; }
    public String getErrorMessage() { return errorMessage; }
    public String getStackTrace() { return stackTrace; }
    public String getPageSource() { return pageSource; }
    public String getScreenshotBase64() { return screenshotBase64; }
    public String getElementLocator() { return elementLocator; }
    public String getElementType() { return elementType; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getUssdCode() { return ussdCode; }

    public String getFormattedTimestamp() {
        return timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * Obtiene un resumen truncado del page source para logging.
     */
    public String getTruncatedPageSource(int maxLength) {
        if (pageSource == null || pageSource.length() <= maxLength) {
            return pageSource;
        }
        return pageSource.substring(0, maxLength) + "\n... [truncado para log, total: " + pageSource.length() + " chars]";
    }

    /**
     * Captura screenshot del driver actual.
     */
    private static String captureScreenshot(WebDriver driver) {
        try {
            if (driver instanceof TakesScreenshot) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                return Base64.getEncoder().encodeToString(screenshot);
            }
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo capturar screenshot: " + e.getMessage());
        }
        return null;
    }

    /**
     * Builder pattern para construcción flexible de FailureContext.
     */
    public static class Builder {
        private String testName;
        private String stepDescription;
        private String errorMessage;
        private String stackTrace;
        private String pageSource;
        private String screenshotBase64;
        private String elementLocator;
        private String elementType = "unknown";
        private String ussdCode;

        public Builder withTestName(String testName) {
            this.testName = testName;
            return this;
        }

        public Builder withStepDescription(String stepDescription) {
            this.stepDescription = stepDescription;
            return this;
        }

        public Builder withError(Throwable error) {
            if (error != null) {
                this.errorMessage = error.getMessage();
                this.stackTrace = getStackTraceAsString(error);
            }
            return this;
        }

        public Builder withPageSource(String pageSource) {
            this.pageSource = pageSource;
            return this;
        }

        /**
         * VERSION 2.0: Captura robusta de page source con retry y validación.
         *
         * CAMBIOS:
         * - Múltiples intentos con esperas entre reintentos
         * - Validación de contenido útil (debe tener TextView, text=, etc.)
         * - Logging detallado del tamaño capturado
         * - Detección de page source vacío/corrupto
         */
        public Builder withDriver(WebDriver driver) {
            if (driver != null) {
                try {
                    System.out.println("📸 Capturando contexto del driver...");

                    // Capturar page source con retry inteligente
                    this.pageSource = capturePageSourceWithRetry(driver, 3);

                    // Validar que el page source tenga contenido útil
                    if (this.pageSource == null || this.pageSource.length() < 1000) {
                        System.err.println("⚠️ [FailureContext] Page source VACÍO o CORRUPTO detectado!");
                        System.err.println("   Tamaño: " + (this.pageSource != null ? this.pageSource.length() : 0) + " caracteres");
                        System.err.println("   🔄 Realizando captura de emergencia...");

                        // Esperar 1 segundo extra y capturar nuevamente
                        Thread.sleep(1000);
                        String emergencyCapture = driver.getPageSource();

                        if (emergencyCapture != null && emergencyCapture.length() > this.pageSource.length()) {
                            System.out.println("✅ Captura de emergencia exitosa: " + emergencyCapture.length() + " chars");
                            this.pageSource = emergencyCapture;
                        } else {
                            System.err.println("❌ Captura de emergencia también falló");
                        }
                    }

                    // Log final del tamaño capturado
                    int finalSize = this.pageSource != null ? this.pageSource.length() : 0;
                    if (finalSize < 1000) {
                        System.err.println("⚠️ [CRÍTICO] Page source final MUY PEQUEÑO: " + finalSize + " chars");
                        System.err.println("   Esto causará análisis genéricos de IA sin elementos reales.");
                    } else if (finalSize < 5000) {
                        System.out.println("⚠️ Page source capturado (tamaño moderado): " + finalSize + " chars");
                    } else {
                        System.out.println("✅ Page source capturado correctamente: " + finalSize + " chars");
                    }

                    // Capturar screenshot
                    this.screenshotBase64 = captureScreenshot(driver);

                } catch (Exception e) {
                    System.err.println("❌ Error capturando contexto del driver: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.err.println("⚠️ Driver es NULL, no se puede capturar contexto");
            }
            return this;
        }

        /**
         * Captura page source con retry para asegurar que tenga contenido.
         *
         * Validaciones:
         * - Tamaño mínimo: 1000 caracteres
         * - Debe contener: TextView, Button, text=, resource-id=
         * - Retry con esperas incrementales
         */
        private String capturePageSourceWithRetry(WebDriver driver, int maxRetries) {
            for (int i = 0; i < maxRetries; i++) {
                try {
                    String ps = driver.getPageSource();

                    // Validar que tenga contenido útil
                    boolean hasUsefulContent = ps != null && ps.length() > 1000 &&
                            (ps.contains("TextView") || ps.contains("Button") ||
                                    ps.contains("text=") || ps.contains("resource-id="));

                    if (hasUsefulContent) {
                        System.out.println("✅ Intento " + (i+1) + "/" + maxRetries +
                                ": Page source válido capturado (" + ps.length() + " chars)");
                        return ps;
                    }

                    System.err.println("⚠️ Intento " + (i+1) + "/" + maxRetries +
                            ": Page source sin contenido útil (" +
                            (ps != null ? ps.length() : 0) + " chars)");

                    // Espera incremental entre reintentos
                    if (i < maxRetries - 1) {
                        int waitTime = (i + 1) * 500; // 500ms, 1000ms, 1500ms
                        System.out.println("   ⏳ Esperando " + waitTime + "ms antes del siguiente intento...");
                        Thread.sleep(waitTime);
                    }

                } catch (Exception e) {
                    System.err.println("❌ Error en intento " + (i+1) + ": " + e.getMessage());
                }
            }

            // Último intento sin validación (devolver lo que sea)
            System.err.println("⚠️ Todos los reintentos fallaron, devolviendo último page source capturado");
            try {
                return driver.getPageSource();
            } catch (Exception e) {
                System.err.println("❌ No se pudo capturar page source: " + e.getMessage());
                return "[ERROR: Page source no disponible - " + e.getMessage() + "]";
            }
        }

        public Builder withElementLocator(String locator, String type) {
            this.elementLocator = locator;
            this.elementType = type;
            return this;
        }

        public Builder withUssdCode(String ussdCode) {
            this.ussdCode = ussdCode;
            return this;
        }

        public FailureContext build() {
            return new FailureContext(this);
        }

        private String getStackTraceAsString(Throwable error) {
            StringBuilder sb = new StringBuilder();
            sb.append(error.toString()).append("\n");

            for (StackTraceElement element : error.getStackTrace()) {
                sb.append("\tat ").append(element.toString()).append("\n");

                // Limitar stack trace a 10 líneas
                if (sb.length() > 2000) {
                    sb.append("\t... [resto del stack trace omitido]");
                    break;
                }
            }

            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return String.format(
                "FailureContext{test='%s', step='%s', error='%s', pageSourceSize=%d, timestamp='%s'}",
                testName, stepDescription, errorMessage,
                (pageSource != null ? pageSource.length() : 0),
                getFormattedTimestamp()
        );
    }
}