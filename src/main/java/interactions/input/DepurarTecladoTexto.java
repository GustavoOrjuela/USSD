// Crear interacción para depurar el teclado
package interactions.input;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import utils.AndroidObject;

import java.util.List;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DepurarTecladoTexto implements Interaction {

  public static DepurarTecladoTexto explorar() {
    return instrumented(DepurarTecladoTexto.class);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    System.out.println("=== DEPURANDO TECLADO ===");

    try {
      // Buscar todos los elementos del teclado
      List<WebElement> teclas =
          AndroidObject.androidDriver(actor)
              .findElements(
                  By.xpath(
                      "//android.view.View[@resource-id='__react-content']/android.view.View[2]/android.view.View//android.widget.TextView"));

      System.out.println("Teclas encontradas: " + teclas.size());

      for (int i = 0; i < Math.min(teclas.size(), 10); i++) {
        try {
          String texto = teclas.get(i).getText();
          System.out.println("Tecla " + i + ": '" + texto + "'");
        } catch (Exception e) {
          System.out.println("Tecla " + i + ": Error al obtener texto");
        }
      }

      // Intentar encontrar "B" específicamente
      try {
        WebElement letraB =
            AndroidObject.androidDriver(actor)
                .findElement(By.xpath("//android.widget.TextView[contains(@text, 'B')]"));
        System.out.println("✅ Letra B encontrada: " + letraB.getText());
      } catch (Exception e) {
        System.out.println("❌ Letra B NO encontrada");
      }

    } catch (Exception e) {
      System.out.println("❌ Error general: " + e.getMessage());
    }
  }
}
