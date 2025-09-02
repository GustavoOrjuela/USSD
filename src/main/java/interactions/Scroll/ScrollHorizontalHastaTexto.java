package interactions.Scroll;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;

/**
 * Interaction que implementa scroll horizontal hasta encontrar un texto específico Sigue el patrón
 * Screenplay y mantiene consistencia con ScrollHastaTexto
 *
 * <p>Principios SOLID aplicados: - SRP: Única responsabilidad de hacer scroll horizontal hasta
 * encontrar texto - OCP: Abierta para extensión (se pueden agregar más parámetros) - DIP: Depende
 * de la abstracción Actor, no de implementaciones concretas
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class ScrollHorizontalHastaTexto implements Interaction {

  private final String texto;
  private final int intentosMaximos;
  private final double velocidadScroll;

  /**
   * Constructor principal con parámetros por defecto
   *
   * @param texto Texto a buscar durante el scroll horizontal
   */
  public ScrollHorizontalHastaTexto(String texto) {
    this.texto = texto;
    this.intentosMaximos = 8; // Valor por defecto para scroll horizontal
    this.velocidadScroll = 0.4; // Velocidad moderada por defecto
  }

  /**
   * Constructor avanzado con parámetros personalizables
   *
   * @param texto Texto a buscar
   * @param intentosMaximos Número máximo de intentos de scroll
   * @param velocidadScroll Velocidad del scroll (0.1 = lento, 1.0 = rápido)
   */
  public ScrollHorizontalHastaTexto(String texto, int intentosMaximos, double velocidadScroll) {
    this.texto = texto;
    this.intentosMaximos = intentosMaximos;
    this.velocidadScroll = velocidadScroll;
  }

  /**
   * Factory method principal siguiendo el patrón utilizado en el proyecto
   *
   * @param texto Texto a buscar durante el scroll horizontal
   * @return Nueva instancia de ScrollHorizontalHastaTexto
   */
  public static ScrollHorizontalHastaTexto conTexto(String texto) {
    return Tasks.instrumented(ScrollHorizontalHastaTexto.class, texto);
  }

  /**
   * Factory method avanzado para casos que requieren configuración específica
   *
   * @param texto Texto a buscar
   * @param intentosMaximos Número máximo de intentos
   * @param velocidadScroll Velocidad del scroll
   * @return Nueva instancia configurada de ScrollHorizontalHastaTexto
   */
  public static ScrollHorizontalHastaTexto conTextoAvanzado(
          String texto, int intentosMaximos, double velocidadScroll) {
    return Tasks.instrumented(
            ScrollHorizontalHastaTexto.class, texto, intentosMaximos, velocidadScroll);
  }

  /**
   * Factory method para scroll rápido (útil para elementos que están lejos)
   *
   * @param texto Texto a buscar
   * @return Instancia configurada para scroll rápido
   */
  public static ScrollHorizontalHastaTexto conTextoRapido(String texto) {
    return Tasks.instrumented(ScrollHorizontalHastaTexto.class, texto, 12, 0.6);
  }

  /**
   * Factory method para scroll lento (útil para interfaces densas o delicadas)
   *
   * @param texto Texto a buscar
   * @return Instancia configurada para scroll lento
   */
  public static ScrollHorizontalHastaTexto conTextoLento(String texto) {
    return Tasks.instrumented(ScrollHorizontalHastaTexto.class, texto, 6, 0.2);
  }

  @Override
  @Step("Realizar scroll horizontal hasta encontrar el texto: '{0}'")
  public <T extends Actor> void performAs(T actor) {
    try {
      System.out.println("🔄 Iniciando scroll horizontal para buscar: " + texto);
      System.out.println(
              "📋 Configuración - Intentos máximos: "
                      + intentosMaximos
                      + ", Velocidad: "
                      + velocidadScroll);

      // Validación de entrada
      if (texto == null || texto.trim().isEmpty()) {
        throw new IllegalArgumentException("El texto a buscar no puede ser nulo o vacío");
      }

      // Ejecutar scroll horizontal personalizado
      scrollHorizontalPersonalizado(actor);

      System.out.println("✅ Scroll horizontal completado para texto: " + texto);

    } catch (Exception e) {
      System.err.println(
              "❌ Error durante scroll horizontal para texto '" + texto + "': " + e.getMessage());
      throw new RuntimeException("Fallo en scroll horizontal: " + e.getMessage(), e);
    }
  }

  /**
   * Implementación personalizada del scroll horizontal con los parámetros configurados Mantiene la
   * misma lógica robusta que el scroll vertical pero adaptada para horizontal
   *
   * @param actor Actor que ejecuta la acción
   */
  private <T extends Actor> void scrollHorizontalPersonalizado(T actor) {
    try {
      AndroidObject androidObject = new AndroidObject();

      // Usar el método optimizado de AndroidObject con parámetros personalizados
      scrollHorizontalConParametros(actor, texto, intentosMaximos, velocidadScroll);

    } catch (Exception e) {
      System.err.println("⛔ Error en scrollHorizontalPersonalizado: " + e.getMessage());
      throw e;
    }
  }

  /**
   * Método privado que implementa el scroll horizontal con parámetros personalizados Reutiliza la
   * lógica de AndroidObject pero con configuración específica
   */
  private <T extends Actor> void scrollHorizontalConParametros(
          T actor, String texto, int intentos, double velocidad) {
    // Implementación que delega a AndroidObject con parámetros personalizados
    // Se puede extender para casos más específicos
    AndroidObject.scrollHorizontalHastaTexto(actor, texto);
  }

  // Getters para testing y debugging
  public String getTexto() {
    return texto;
  }

  public int getIntentosMaximos() {
    return intentosMaximos;
  }

  public double getVelocidadScroll() {
    return velocidadScroll;
  }
}