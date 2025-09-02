package interactions.input;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import org.openqa.selenium.By;
import utils.AndroidObject;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class IngresarMontoConTecladoNumerico implements Interaction {
  private final String valor;

  // Constructor requerido por Serenity
  public IngresarMontoConTecladoNumerico(String valor) {
    this.valor = valor;
  }

  public static IngresarMontoConTecladoNumerico elValor(String valor) {
    return instrumented(IngresarMontoConTecladoNumerico.class, valor);
  }

  // Solución con los localizadores reales del teclado
  @Override
  public <T extends Actor> void performAs(T actor) {
    System.out.println("Ingresando monto: " + valor);

    // Limpiar campo primero si es necesario
    try {
      AndroidObject.androidDriver(actor)
          .findElement(By.xpath("//android.widget.TextView[@resource-id='delete']"))
          .click();
      Thread.sleep(500);
      System.out.println("Campo limpiado");
    } catch (Exception e) {
      System.out.println("No necesita limpiar campo");
    }

    // Ingresar cada dígito del valor usando resource-id
    for (char digito : valor.toCharArray()) {
      if (Character.isDigit(digito)) {
        try {
          System.out.println("Haciendo click en dígito: " + digito);
          AndroidObject.androidDriver(actor)
              .findElement(By.xpath("//android.widget.TextView[@resource-id='" + digito + "']"))
              .click();

          Thread.sleep(300);
          System.out.println("✅ Click exitoso en: " + digito);

        } catch (Exception e) {
          System.out.println(
              "❌ Error al hacer click en dígito: " + digito + " - " + e.getMessage());
        }
      }
    }

    // Confirmar entrada con el botón check
    try {
      System.out.println("Confirmando entrada...");
      AndroidObject.androidDriver(actor)
          .findElement(By.xpath("//android.widget.TextView[@resource-id='check']"))
          .click();

      System.out.println("✅ Entrada confirmada");
      Thread.sleep(1000);

    } catch (Exception e) {
      System.out.println("❌ Error al confirmar: " + e.getMessage());
    }
  }
}
