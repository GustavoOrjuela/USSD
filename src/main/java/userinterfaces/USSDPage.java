package userinterfaces;

import io.appium.java_client.MobileBy;
import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;

public class USSDPage extends PageObject {

    public static final Target TECLADO_TELEFONO =
            Target.the("Teclado del telefono")
                    .located(MobileBy.id("com.google.android.dialer:id/tab_dialpad"));
    public static final Target TECLADO_TELEFONO2 =
            Target.the("Teclado del telefono")
                    .located(MobileBy.id("com.google.android.dialer:id/dialpad_fab"));
    public static final Target BTN_LLAMAR =
            Target.the("Boton de marcar")
                    .located(MobileBy.id("com.google.android.dialer:id/dialpad_voice_call_button"));

    public static final Target TXT_APP_PREDETERMINADA =
            Target.the("Texto Establecer Teléfono como app predeterminada")
                    .located(MobileBy.id("com.google.android.dialer:id/default_request_text"));

    public static final Target BTN_HACER_PREDETERMINADA =
            Target.the("Boton Hacer predeterminada")
                    .located(MobileBy.id("com.google.android.dialer:id/set_default_phone_app_button"));


    public static final Target IMG_ESPERA =
            Target.the("Ejecutando código USSD…")
                    .located(MobileBy.id("android:id/progress"));

    public static final Target CJA_INGRESAR_OPCION =
            Target.the("Caja para ingresar la opción seleccionada")
                    .located(MobileBy.id("com.android.phone:id/input_field"));

    public static final Target BTN_ENVIAR =
            Target.the("Boton de enviar")
                    .located(MobileBy.id("android:id/button1"));

    public static final Target TXT_TE_GUSTARIA_CALIFICAR_MI_CLARO_APP =
            Target.the("Texto de te gustaria calificar mi claro App")
                    .located(MobileBy.xpath("//*[@text='¿Te gustaría calificar tu experiencia con el App Mi Claro?']"));

    public static final Target TXT_MENSAJES =
            Target.the("Texto Mensajes")
                    .located(MobileBy.xpath("//*[@text='Mensajes']"));

    public static final Target TXT_MENSAJE_SALDO_INSUFICIENTE =
            Target.the("Texto Mensajes")
                    .located(MobileBy.xpath("//*[@text='Tu saldo es insuficiente, intenta con otro medio de pago.']"));
    public static final Target TXT_MENSAJE_COMPRA_EXITOSA =
            Target.the("Texto Mensajes")
                    .located(MobileBy.xpath("//*[@text='Tu paquete se activo exitosamente, en un momento recibiras un mensaje de texto con los detalles de tu compra. Gracias']"));

    public static final Target TXT_ACEPTAR =
            Target.the("Texto Aceptar")
                    .located(MobileBy.xpath("//*[@text='Aceptar']"));


    public static final Target TXT_779 =
            Target.the("Mensaje 779")
                    .located(MobileBy.xpath("//*[@text='779']"));

    public static final Target TXT_CLARO =
            Target.the("Mensaje CLARO")
                    .located(MobileBy.xpath("//*[@text='CLARO']"));

    public static final Target TXT_ACTUALIZAR =
            Target.the("Texto de Actualizar")
                    .located(MobileBy.xpath("//*[@text='Activar']"));

    public static final Target MENSAJE_CON_LINK =
            Target.the("Texto de Actualizar")
                    .located(MobileBy.xpath("//*[@text='Compra Exitosa. Ya puedes disfrutar de tu paquete de 200 MB con Vig de 1 dias por $1500. Info clro.co/paquetes']"));

    public static final Target IMG_LOADING =
            Target.the("Imagen de espera de CLaro")
                    .located(MobileBy.id("com.clarocolombia.miclaro:id/imgLoading"));

    public static final Target TECLADO =
            Target.the("Imagen de espera de CLaro")
                    .located(MobileBy.id("com.google.android.dialer:id/dialpad_view"));

    public static final Target BTN_CANCELAR =
            Target.the("Botón Cancelar")
                    .located(MobileBy.id("android:id/button2"));

}

