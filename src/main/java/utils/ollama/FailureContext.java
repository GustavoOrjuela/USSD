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
 * Captura toda la información relevante en el momento del fallo:
 * - Page source completo
 * - Screenshot en base64
 * - Elemento que se buscaba
 * - Stack trace del error
 * - Timestamp
 *
 * Principios SOLID:
 * - SRP: Responsabilidad única de encapsular contexto de fallo
 * - OCP: Abierto para extensión (agregar más contexto)
 * - ISP: Interfaz clara y específica para análisis de fallos
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class FailureContext {

    private final String testName;
    private final String stepDescription;
    private final String errorMessage;
    private final String stackTrace;
    private final String pageSource;
    private final String screenshotBase64;
    private final String elementLocator;
    private final String elementType; // xpath, text, id, etc.
    private final LocalDateTime timestamp;
    private final String ussdCode; // Código USSD que se estaba probando

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
     * El análisis completo usa el page source completo.
     */
    public String getTruncatedPageSource(int maxLength) {
        if (pageSource == null || pageSource.length() <= maxLength) {
            return pageSource;
        }
        return pageSource.substring(0, maxLength) + "\n... [truncado para log]";
    }

    /**
     * Captura screenshot del driver actual.
     *
     * @param driver WebDriver actual
     * @return Screenshot en base64 o null si falla
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

        public Builder withDriver(WebDriver driver) {
            if (driver != null) {
                try {
                    this.pageSource = driver.getPageSource();
                    this.screenshotBase64 = captureScreenshot(driver);
                } catch (Exception e) {
                    System.err.println("⚠️ Error capturando contexto del driver: " + e.getMessage());
                }
            }
            return this;
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

                // Limitar stack trace a 10 líneas para no saturar el prompt
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
                "FailureContext{test='%s', step='%s', error='%s', timestamp='%s'}",
                testName, stepDescription, errorMessage, getFormattedTimestamp()
        );
    }
}