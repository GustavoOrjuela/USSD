package interactions.Scroll;

import interactions.validations.ValidarTextoQueContengaX;
import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Tasks;
import net.thucydides.core.annotations.Step;
import utils.AndroidObject;

/**
 * Interaction que ejecuta scroll horizontal sin validación inicial y después valida el texto
 *
 * <p>Casos de uso típicos: - Navegación por carruseles donde conoces la dirección pero necesitas
 * validar el resultado - Interfaces donde el scroll es determinístico pero quieres confirmar el
 * elemento final - Separación clara entre navegación y validación para mejor debugging
 *
 * <p>Principios SOLID aplicados: - SRP: Responsabilidad específica de scroll + validación
 * secuencial - OCP: Extensible para diferentes tipos de validación post-scroll - DIP: Depende de
 * abstracciones (AndroidObject, Validaciones)
 *
 * @author Senior Test Automation Engineer
 * @since 1.0
 */
public class ScrollHorizontalYValidar implements Interaction {

  private final String textoAValidar;
  private final DireccionScroll direccion;
  private final int numeroScrolls;
  private final double velocidad;
  private final int tiempoEsperaValidacion;

  /** Enum para definir la dirección del scroll */
  public enum DireccionScroll {
    DERECHA,
    IZQUIERDA,
    AMBAS_DIRECCIONES // Para casos donde no sabes la dirección exacta
  }

  /**
   * Constructor principal con parámetros por defecto
   *
   * @param textoAValidar Texto que se debe validar después del scroll
   * @param direccion Dirección del scroll horizontal
   */
  public ScrollHorizontalYValidar(String textoAValidar, DireccionScroll direccion) {
    this.textoAValidar = textoAValidar;
    this.direccion = direccion;
    this.numeroScrolls = 3; // Número por defecto de scrolls
    this.velocidad = 0.5; // Velocidad moderada
    this.tiempoEsperaValidacion = 1500; // Tiempo de espera antes de validar
  }

  /**
   * Constructor avanzado con parámetros personalizables
   *
   * @param textoAValidar Texto a validar
   * @param direccion Dirección del scroll
   * @param numeroScrolls Número de scrolls a realizar
   * @param velocidad Velocidad del scroll (0.1 - 1.0)
   * @param tiempoEsperaValidacion Tiempo en ms antes de validar
   */
  public ScrollHorizontalYValidar(
          String textoAValidar,
          DireccionScroll direccion,
          int numeroScrolls,
          double velocidad,
          int tiempoEsperaValidacion) {
    this.textoAValidar = textoAValidar;
    this.direccion = direccion;
    this.numeroScrolls = numeroScrolls;
    this.velocidad = velocidad;
    this.tiempoEsperaValidacion = tiempoEsperaValidacion;
  }

  @Override
  @Step("Realizar scroll horizontal hacia {1} y validar texto: '{0}'")
  public <T extends Actor> void performAs(T actor) {
    try {
      System.out.println("🔄 Iniciando scroll horizontal sin validación inicial");
      System.out.println("📋 Configuración:");
      System.out.println("   - Texto a validar: " + textoAValidar);
      System.out.println("   - Dirección: " + direccion);
      System.out.println("   - Número de scrolls: " + numeroScrolls);
      System.out.println("   - Velocidad: " + velocidad);

      // Validación de entrada
      validarParametros();

      // FASE 1: Ejecutar scroll horizontal sin validación
      ejecutarScrollHorizontal(actor);

      // FASE 2: Esperar antes de validar
      actor.attemptsTo(WaitFor.aTime(tiempoEsperaValidacion));

      // FASE 3: Validar que el texto esté presente
      validarTextoFinal(actor);

      System.out.println("✅ Scroll horizontal y validación completados exitosamente");

    } catch (Exception e) {
      System.err.println("❌ Error en ScrollHorizontalYValidar: " + e.getMessage());
      throw new RuntimeException("Fallo en scroll horizontal y validación: " + e.getMessage(), e);
    }
  }

  /** Ejecuta el scroll horizontal según la configuración especificada */
  private <T extends Actor> void ejecutarScrollHorizontal(T actor) {
    System.out.println("🔄 Ejecutando scroll horizontal...");

    switch (direccion) {
      case DERECHA:
        ejecutarScrollsDerecha(actor);
        break;
      case IZQUIERDA:
        ejecutarScrollsIzquierda(actor);
        break;
      case AMBAS_DIRECCIONES:
        ejecutarScrollsAmbos(actor);
        break;
      default:
        throw new IllegalArgumentException("Dirección de scroll no válida: " + direccion);
    }

    System.out.println("✅ Scroll horizontal completado");
  }

  /** Ejecuta múltiples scrolls hacia la derecha */
  private <T extends Actor> void ejecutarScrollsDerecha(T actor) {
    for (int i = 1; i <= numeroScrolls; i++) {
      System.out.println("   🔄 Scroll derecha " + i + "/" + numeroScrolls);
      AndroidObject.swipeHorizontal(actor, 0.2, 0.8, velocidad);
      esperarEntreScrolls();
    }
  }

  /** Ejecuta múltiples scrolls hacia la izquierda */
  private <T extends Actor> void ejecutarScrollsIzquierda(T actor) {
    for (int i = 1; i <= numeroScrolls; i++) {
      System.out.println("   🔄 Scroll izquierda " + i + "/" + numeroScrolls);
      AndroidObject.swipeHorizontal(actor, 0.8, 0.2, velocidad);
      esperarEntreScrolls();
    }
  }

  /** Ejecuta scrolls en ambas direcciones (útil para exploración) */
  private <T extends Actor> void ejecutarScrollsAmbos(T actor) {
    int scrollsPorDireccion = numeroScrolls / 2;

    // Primero hacia la derecha
    for (int i = 1; i <= scrollsPorDireccion; i++) {
      System.out.println("   🔄 Scroll derecha " + i + "/" + scrollsPorDireccion);
      AndroidObject.swipeHorizontal(actor, 0.2, 0.8, velocidad);
      esperarEntreScrolls();
    }

    // Luego hacia la izquierda
    for (int i = 1; i <= scrollsPorDireccion; i++) {
      System.out.println("   🔄 Scroll izquierda " + i + "/" + scrollsPorDireccion);
      AndroidObject.swipeHorizontal(actor, 0.8, 0.2, velocidad);
      esperarEntreScrolls();
    }
  }

  /** Valida que el texto esté presente después del scroll */
  private <T extends Actor> void validarTextoFinal(T actor) {
    System.out.println("🔍 Validando presencia del texto: " + textoAValidar);

    actor.attemptsTo(ValidarTextoQueContengaX.elTextoContiene(textoAValidar));

    System.out.println("✅ Texto validado exitosamente: " + textoAValidar);
  }

  /** Pausa entre scrolls para mejor estabilidad */
  private void esperarEntreScrolls() {
    try {
      Thread.sleep(300); // Pausa corta entre scrolls
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  /** Valida los parámetros de entrada */
  private void validarParametros() {
    if (textoAValidar == null || textoAValidar.trim().isEmpty()) {
      throw new IllegalArgumentException("El texto a validar no puede ser nulo o vacío");
    }

    if (numeroScrolls <= 0) {
      throw new IllegalArgumentException("El número de scrolls debe ser mayor a 0");
    }

    if (velocidad < 0.1 || velocidad > 1.0) {
      throw new IllegalArgumentException("La velocidad debe estar entre 0.1 y 1.0");
    }

    if (tiempoEsperaValidacion < 0) {
      throw new IllegalArgumentException("El tiempo de espera no puede ser negativo");
    }
  }

  // Factory methods siguiendo el patrón del proyecto

  /**
   * Scroll hacia la derecha y validar texto (configuración por defecto)
   *
   * @param texto Texto a validar después del scroll
   * @return Interaction configurada
   */
  public static ScrollHorizontalYValidar scrollDerechaYValidar(String texto) {
    return Tasks.instrumented(ScrollHorizontalYValidar.class, texto, DireccionScroll.DERECHA);
  }

  /**
   * Scroll hacia la izquierda y validar texto (configuración por defecto)
   *
   * @param texto Texto a validar después del scroll
   * @return Interaction configurada
   */
  public static ScrollHorizontalYValidar scrollIzquierdaYValidar(String texto) {
    return Tasks.instrumented(ScrollHorizontalYValidar.class, texto, DireccionScroll.IZQUIERDA);
  }

  /**
   * Scroll en ambas direcciones y validar texto
   *
   * @param texto Texto a validar después del scroll
   * @return Interaction configurada
   */
  public static ScrollHorizontalYValidar scrollAmbasDireccionesYValidar(String texto) {
    return Tasks.instrumented(
            ScrollHorizontalYValidar.class, texto, DireccionScroll.AMBAS_DIRECCIONES);
  }

  /**
   * Configuración personalizada completa
   *
   * @param texto Texto a validar
   * @param direccion Dirección del scroll
   * @param numeroScrolls Cantidad de scrolls
   * @param velocidad Velocidad del scroll
   * @param tiempoEspera Tiempo de espera antes de validar
   * @return Interaction configurada
   */
  public static ScrollHorizontalYValidar scrollPersonalizadoYValidar(
          String texto,
          DireccionScroll direccion,
          int numeroScrolls,
          double velocidad,
          int tiempoEspera) {
    return Tasks.instrumented(
            ScrollHorizontalYValidar.class, texto, direccion, numeroScrolls, velocidad, tiempoEspera);
  }

  /**
   * Configuración rápida - más scrolls, mayor velocidad
   *
   * @param texto Texto a validar
   * @param direccion Dirección del scroll
   * @return Interaction configurada para ejecución rápida
   */
  public static ScrollHorizontalYValidar scrollRapidoYValidar(
          String texto, DireccionScroll direccion) {
    return Tasks.instrumented(ScrollHorizontalYValidar.class, texto, direccion, 5, 0.8, 800);
  }

  /**
   * Configuración lenta - menos scrolls, menor velocidad (para interfaces delicadas)
   *
   * @param texto Texto a validar
   * @param direccion Dirección del scroll
   * @return Interaction configurada para ejecución lenta
   */
  public static ScrollHorizontalYValidar scrollLentoYValidar(
          String texto, DireccionScroll direccion) {
    return Tasks.instrumented(ScrollHorizontalYValidar.class, texto, direccion, 2, 0.3, 2000);
  }

  // Getters para testing y debugging
  public String getTextoAValidar() {
    return textoAValidar;
  }

  public DireccionScroll getDireccion() {
    return direccion;
  }

  public int getNumeroScrolls() {
    return numeroScrolls;
  }

  public double getVelocidad() {
    return velocidad;
  }

  public int getTiempoEsperaValidacion() {
    return tiempoEsperaValidacion;
  }
}