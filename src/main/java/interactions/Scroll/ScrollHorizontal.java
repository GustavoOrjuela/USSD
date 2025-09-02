package interactions.Scroll;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Interaction para realizar scroll horizontal simple Complementa a ScrollHorizontalHastaTexto para
 * casos donde solo se necesita scroll sin búsqueda
 *
 * <p>Sigue el mismo patrón que la clase Scroll existente en el proyecto
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class ScrollHorizontal extends AndroidObject implements Interaction {

  private final DireccionHorizontal direccion;
  private final double velocidad;

  /** Enum para definir la dirección del scroll horizontal */
  public enum DireccionHorizontal {
    IZQUIERDA,
    DERECHA
  }

  /**
   * Constructor privado para el patrón factory
   *
   * @param direccion Dirección del scroll
   * @param velocidad Velocidad del scroll (0.1 a 1.0)
   */
  private ScrollHorizontal(DireccionHorizontal direccion, double velocidad) {
    this.direccion = direccion;
    this.velocidad = velocidad;
  }

  /** Constructor por defecto (scroll hacia la derecha, velocidad moderada) */
  private ScrollHorizontal() {
    this.direccion = DireccionHorizontal.DERECHA;
    this.velocidad = 0.5;
  }

  @Override
  @Step("Realizar scroll horizontal hacia {0} con velocidad {1}")
  public <T extends Actor> void performAs(T actor) {
    try {
      System.out.println(
              "🔄 Ejecutando scroll horizontal hacia: " + direccion + " con velocidad: " + velocidad);

      switch (direccion) {
        case IZQUIERDA:
          swipeHorizontal(actor, 0.8, 0.2, velocidad);
          break;
        case DERECHA:
          swipeHorizontal(actor, 0.2, 0.8, velocidad);
          break;
        default:
          throw new IllegalArgumentException("Dirección de scroll no válida: " + direccion);
      }

      System.out.println("✅ Scroll horizontal completado");

    } catch (Exception e) {
      System.err.println("❌ Error en scroll horizontal: " + e.getMessage());
      throw new RuntimeException("Fallo en scroll horizontal", e);
    }
  }

  // Factory methods siguiendo el patrón del proyecto

  /**
   * Scroll horizontal simple hacia la derecha (método por defecto)
   *
   * @return Interaction configurada para scroll derecha
   */
  public static Interaction scrollHorizontalDerecha() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.DERECHA, 0.5);
  }

  /**
   * Scroll horizontal simple hacia la izquierda
   *
   * @return Interaction configurada para scroll izquierda
   */
  public static Interaction scrollHorizontalIzquierda() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.IZQUIERDA, 0.5);
  }

  /**
   * Scroll horizontal con dirección y velocidad personalizadas
   *
   * @param direccion Dirección del scroll
   * @param velocidad Velocidad (0.1 = muy lento, 1.0 = muy rápido)
   * @return Interaction configurada
   */
  public static Interaction scrollHorizontalPersonalizado(
          DireccionHorizontal direccion, double velocidad) {
    // Validar velocidad
    if (velocidad < 0.1 || velocidad > 1.0) {
      throw new IllegalArgumentException("La velocidad debe estar entre 0.1 y 1.0");
    }
    return instrumented(ScrollHorizontal.class, direccion, velocidad);
  }

  /**
   * Scroll horizontal rápido hacia la derecha
   *
   * @return Interaction configurada para scroll rápido
   */
  public static Interaction scrollHorizontalDerechaRapido() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.DERECHA, 0.8);
  }

  /**
   * Scroll horizontal rápido hacia la izquierda
   *
   * @return Interaction configurada para scroll rápido
   */
  public static Interaction scrollHorizontalIzquierdaRapido() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.IZQUIERDA, 0.8);
  }

  /**
   * Scroll horizontal lento hacia la derecha (útil para interfaces delicadas)
   *
   * @return Interaction configurada para scroll lento
   */
  public static Interaction scrollHorizontalDerechaLento() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.DERECHA, 0.2);
  }

  /**
   * Scroll horizontal lento hacia la izquierda (útil para interfaces delicadas)
   *
   * @return Interaction configurada para scroll lento
   */
  public static Interaction scrollHorizontalIzquierdaLento() {
    return instrumented(ScrollHorizontal.class, DireccionHorizontal.IZQUIERDA, 0.2);
  }

  /**
   * Método de conveniencia para mantener compatibilidad con el patrón existente Equivalente a
   * scrollHorizontalDerecha()
   *
   * @return Performable para scroll horizontal
   */
  public static Performable scrollHorizontalUnaVista() {
    return instrumented(ScrollHorizontal.class);
  }

  // Getters para testing y debugging
  public DireccionHorizontal getDireccion() {
    return direccion;
  }

  public double getVelocidad() {
    return velocidad;
  }
}
