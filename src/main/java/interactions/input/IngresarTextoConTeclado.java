package interactions.input;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import org.openqa.selenium.By;
import utils.AndroidObject;

import static net.serenitybdd.screenplay.Tasks.instrumented;

// Actualizar IngresarTextoConTeclado
public class IngresarTextoConTeclado implements Interaction {
  private final String texto;

  public IngresarTextoConTeclado(String texto) {
    this.texto = texto;
  }

  public static IngresarTextoConTeclado elTexto(String texto) {
    return instrumented(IngresarTextoConTeclado.class, texto);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    System.out.println("Ingresando texto: " + texto);

    for (char letra : texto.toCharArray()) {
      try {
        System.out.println("Buscando letra: " + letra);

        // Probar diferentes variaciones de la letra
        String[] variaciones = {
          String.valueOf(letra), // B
          String.valueOf(letra).toLowerCase(), // b
          String.valueOf(letra).toUpperCase() // B
        };

        boolean encontrada = false;
        for (String variacion : variaciones) {
          try {
            AndroidObject.androidDriver(actor)
                .findElement(By.xpath("//android.widget.TextView[@text='" + variacion + "']"))
                .click();

            System.out.println("✅ Click exitoso en: " + variacion);
            encontrada = true;
            break;

          } catch (Exception e) {
            // Continuar con la siguiente variación
          }
        }

        if (!encontrada) {
          System.out.println("❌ No se encontró la letra: " + letra);
        }

        Thread.sleep(300);

      } catch (Exception e) {
        System.out.println("❌ Error general con letra: " + letra);
      }
    }
  }
}
