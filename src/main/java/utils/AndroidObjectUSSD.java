package utils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Extensión de AndroidObject con métodos específicos para USSD
 *
 * Esta clase extiende las capacidades base de AndroidObject añadiendo
 * funcionalidades específicas para el manejo de interfaces USSD.
 *
 * Principios aplicados:
 * - SRP: Métodos específicos para USSD separados de funcionalidad general
 * - OCP: Extiende AndroidObject sin modificar su código base
 * - DRY: Reutiliza métodos base de AndroidObject
 */
public class AndroidObjectUSSD extends AndroidObject {

    /**
     * Valida que un campo de entrada USSD esté disponible y listo para uso
     */
    public boolean validarCampoEntradaUSSD(Actor actor) {
        try {
            actor.attemptsTo(WaitFor.aTime(2000));

            AndroidDriver driver = androidDriver(actor);
            WebElement campoEntrada = driver.findElement(
                    MobileBy.id("com.android.phone:id/input_field")
            );

            return campoEntrada.isEnabled() && campoEntrada.isDisplayed();

        } catch (Exception e) {
            System.out.println("⚠️ Campo de entrada USSD no disponible: " + e.getMessage());
            return false;
        }
    }

    /**
     * Limpia el campo de entrada USSD si contiene texto previo
     */
    public void limpiarCampoEntradaUSSD(Actor actor) {
        try {
            AndroidDriver driver = androidDriver(actor);
            WebElement campoEntrada = driver.findElement(
                    MobileBy.id("com.android.phone:id/input_field")
            );

            if (!campoEntrada.getText().isEmpty()) {
                campoEntrada.clear();
                System.out.println("🧹 Campo USSD limpiado");
            }

        } catch (Exception e) {
            System.out.println("ℹ️ No fue necesario limpiar campo USSD");
        }
    }

    /**
     * Espera a que aparezca el diálogo de respuesta USSD
     */
    public boolean esperarRespuestaUSSD(Actor actor, int timeoutSeconds) {
        try {
            AndroidDriver driver = androidDriver(actor);

            for (int i = 0; i < timeoutSeconds; i++) {
                try {
                    // Buscar elementos típicos de respuesta USSD
                    WebElement respuesta = driver.findElement(
                            MobileBy.xpath("//android.widget.TextView[contains(@text, 'Info de la operadora') or " +
                                    "contains(@text, 'El mas vendido') or " +
                                    "contains(@text, 'Selecciona el medio')]")
                    );

                    if (respuesta.isDisplayed()) {
                        System.out.println("📱 Respuesta USSD recibida");
                        return true;
                    }

                } catch (Exception ignored) {
                    // Continuar esperando
                }

                Thread.sleep(1000);
            }

            System.out.println("⏰ Timeout esperando respuesta USSD");
            return false;

        } catch (Exception e) {
            System.err.println("❌ Error esperando respuesta USSD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene todo el texto visible en la pantalla USSD actual
     */
    public String obtenerTextoCompletoUSSD(Actor actor) {
        try {
            AndroidDriver driver = androidDriver(actor);
            StringBuilder textoCompleto = new StringBuilder();

            // Obtener todos los elementos de texto visibles
            List<WebElement> elementosTexto = driver.findElements(
                    MobileBy.xpath("//android.widget.TextView[@text != '']")
            );

            for (WebElement elemento : elementosTexto) {
                if (elemento.isDisplayed()) {
                    String texto = elemento.getText().trim();
                    if (!texto.isEmpty()) {
                        textoCompleto.append(texto).append("\n");
                    }
                }
            }

            String resultado = textoCompleto.toString();
            System.out.println("📄 Texto completo USSD capturado:\n" + resultado);

            return resultado;

        } catch (Exception e) {
            System.err.println("❌ Error obteniendo texto USSD: " + e.getMessage());
            return "";
        }
    }

    /**
     * Valida que específicos elementos de menú USSD estén presentes
     */
    public boolean validarElementosMenuUSSD(Actor actor, String... textosEsperados) {
        try {
            AndroidDriver driver = androidDriver(actor);
            boolean todosEncontrados = true;

            for (String texto : textosEsperados) {
                try {
                    WebElement elemento = driver.findElement(
                            new MobileBy.ByAndroidUIAutomator(
                                    "new UiSelector().textContains(\"" + texto + "\")"
                            )
                    );

                    if (!elemento.isDisplayed()) {
                        System.out.println("❌ Elemento no visible: " + texto);
                        todosEncontrados = false;
                    } else {
                        System.out.println("✅ Elemento encontrado: " + texto);
                    }

                } catch (Exception e) {
                    System.out.println("❌ Elemento no encontrado: " + texto);
                    todosEncontrados = false;
                }
            }

            return todosEncontrados;

        } catch (Exception e) {
            System.err.println("❌ Error validando elementos menú USSD: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si hay un diálogo de error o mensaje de sistema
     */
    public boolean verificarMensajeErrorUSSD(Actor actor) {
        try {
            AndroidDriver driver = androidDriver(actor);

            // Buscar indicadores comunes de error
            String[] mensajesError = {
                    "Error", "error", "Saldo insuficiente",
                    "No disponible", "Servicio no disponible",
                    "Intente más tarde", "Fallo en la operación"
            };

            for (String mensajeError : mensajesError) {
                try {
                    WebElement elemento = driver.findElement(
                            new MobileBy.ByAndroidUIAutomator(
                                    "new UiSelector().textContains(\"" + mensajeError + "\")"
                            )
                    );

                    if (elemento.isDisplayed()) {
                        System.out.println("⚠️ Mensaje de error detectado: " + mensajeError);
                        return true;
                    }

                } catch (Exception ignored) {
                    // Continuar buscando otros mensajes
                }
            }

            return false;

        } catch (Exception e) {
            System.err.println("❌ Error verificando mensajes de error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cierra cualquier diálogo o popup que pueda interferir con USSD
     */
    public void cerrarDialogosInterferentes(Actor actor) {
        try {
            AndroidDriver driver = androidDriver(actor);

            // Lista de textos de botones comunes para cerrar diálogos
            String[] botonesCerrar = {"Aceptar", "OK", "Cerrar", "Cancelar", "No"};

            for (String boton : botonesCerrar) {
                try {
                    WebElement elemento = driver.findElement(
                            new MobileBy.ByAndroidUIAutomator(
                                    "new UiSelector().text(\"" + boton + "\")"
                            )
                    );

                    if (elemento.isDisplayed() && elemento.isEnabled()) {
                        elemento.click();
                        System.out.println("🔘 Diálogo cerrado con: " + boton);
                        actor.attemptsTo(WaitFor.aTime(1000));
                        break;
                    }

                } catch (Exception ignored) {
                    // Continuar con el siguiente botón
                }
            }

        } catch (Exception e) {
            System.out.println("ℹ️ No hay diálogos interferentes para cerrar");
        }
    }

    /**
     * Método específico para manejar timeouts en respuestas USSD
     */
    public void manejarTimeoutUSSD(Actor actor, int timeoutMs) {
        try {
            System.out.println("⏳ Esperando respuesta USSD - Timeout: " + timeoutMs + "ms");

            long inicio = System.currentTimeMillis();
            boolean respuestaRecibida = false;

            while ((System.currentTimeMillis() - inicio) < timeoutMs && !respuestaRecibida) {
                try {
                    // Verificar si hay nueva información en pantalla
                    AndroidDriver driver = androidDriver(actor);
                    List<WebElement> elementos = driver.findElements(
                            MobileBy.xpath("//android.widget.TextView")
                    );

                    if (elementos.size() > 2) { // Al menos algunos elementos de respuesta
                        respuestaRecibida = true;
                        System.out.println("📱 Respuesta USSD detectada");
                    }

                    Thread.sleep(500);

                } catch (Exception e) {
                    Thread.sleep(1000);
                }
            }

            if (!respuestaRecibida) {
                System.out.println("⚠️ Timeout alcanzado esperando respuesta USSD");
            }

        } catch (Exception e) {
            System.err.println("❌ Error en manejo de timeout USSD: " + e.getMessage());
        }
    }
}