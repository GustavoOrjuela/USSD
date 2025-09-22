package utils;

import java.util.*;

public class Constants {

    // ==================== CÓDIGOS USSD ====================

    /**
     * Código USSD principal para compra de paquetes
     */
    public static final String CODIGO_USSD_611 = "*611#";

    /**
     * Código USSD para consulta de saldo
     */
    public static final String CODIGO_USSD_SALDO = "*444#";

    /**
     * Código USSD para recargas
     */
    public static final String CODIGO_USSD_RECARGAS = "*555#";

    /**
     * Código USSD para consulta de consumos
     */
    public static final String CODIGO_USSD_CONSUMOS = "*777#";

    /**
     * Código USSD para paquetes de datos específicos
     */
    public static final String CODIGO_USSD_PAQUETES_DATOS = "*133#";

    /**
     * Código USSD para servicio al cliente
     */
    public static final String CODIGO_USSD_SERVICIO_CLIENTE = "*123#";

    // ==================== TEXTOS DE PANTALLAS PRINCIPALES ====================

    /**
     * Texto identificador del menú principal USSD
     */
    public static final String INFO_OPERADORA = "Info de la operadora";

    /**
     * Título alternativo del menú principal
     */
    public static final String MENU_PRINCIPAL_USSD = "Menú principal USSD";

    /**
     * Mensaje promocional 3x1
     */
    public static final String RECIBE_3X1_DATOS = "Recibe 3x1 en datos y minutos comprando con Nequi, Daviplata PSE o Tarjeta";

    /**
     * Mensaje de bienvenida del operador
     */
    public static final String MENSAJE_BIENVENIDA_CLARO = "Bienvenido a Claro";

    /**
     * Indicador de carga del sistema
     */
    public static final String CARGANDO_INFORMACION = "Cargando información";

    // ==================== OPCIONES DE MENÚ PRINCIPAL ====================

    /**
     * Opción 1: Compra de paquetes
     */
    public static final String COMPRA_PAQUETES_MENU = "1. Compra de paquetes";

    /**
     * Opción 2: Recargas
     */
    public static final String RECARGAS_MENU = "2. Recargas";

    /**
     * Opción 3: Consulta de saldo y consumos
     */
    public static final String CONSULTA_SALDO_MENU = "3. Consulta de saldo y consumos";

    /**
     * Opción 9: Más opciones
     */
    public static final String MAS_OPCIONES = "9. Más";

    /**
     * Opción 0: Atrás
     */
    public static final String ATRAS_MENU = "0. Atrás";

    /**
     * Texto descriptivo de consulta de saldo
     */
    public static final String CONSULTA_SALDO_DESCRIPCION = "Consulta de saldo y consumos";

    // ==================== INFORMACIÓN DE PAQUETES ====================

    /**
     * Identificador del paquete más vendido
     */
    public static final String EL_MAS_VENDIDO = "El mas vendido";

    /**
     * Precio del paquete más vendido
     */
    public static final String PAQUETE_MAS_VENDIDO_PRECIO = "$7500";

    /**
     * Vigencia del paquete más vendido
     */
    public static final String PAQUETE_MAS_VENDIDO_DIAS = "6 Dias";

    /**
     * Datos incluidos en el paquete más vendido
     */
    public static final String PAQUETE_MAS_VENDIDO_DATOS = "1.4GB+WFX+Voz";

    /**
     * Datos alternativos del paquete
     */
    public static final String PAQUETE_DATOS_ALTERNATIVOS = "1.4GB";

    /**
     * Título genérico para paquetes de datos
     */
    public static final String PAQUETE_DATOS_TITULO = "Paquete de Datos";

    /**
     * Título para paquetes todo incluido
     */
    public static final String PAQUETE_TODO_INCLUIDO_TITULO = "Paquete Todo Incluido";

    // Opciones de paquetes numeradas (formato completo)
    /**
     * Opción completa del paquete más vendido
     */
    public static final String OPCION_PAQUETE_MAS_VENDIDO = "1. El mas vendido: $7500 6 Dias 1.4GB+WFX+Voz";

    /**
     * Opción de paquete de datos
     */
    public static final String OPCION_PAQUETE_DATOS = "2. Paquete de Datos";

    /**
     * Opción de paquete todo incluido
     */
    public static final String OPCION_PAQUETE_TODO_INCLUIDO = "3. Paquete Todo Incluido";

    // Información adicional de paquetes
    /**
     * Texto de servicios incluidos WFX
     */
    public static final String INFO_WFX = "WFX";

    /**
     * Texto de servicios de voz incluidos
     */
    public static final String INFO_VOZ = "Voz";

    /**
     * Texto de servicios SMS incluidos
     */
    public static final String INFO_SMS = "SMS";

    /**
     * Texto genérico de datos
     */
    public static final String INFO_DATOS = "Datos";

    /**
     * Texto de minutos incluidos
     */
    public static final String INFO_MINUTOS = "Minutos";

    // ==================== MEDIOS DE PAGO ====================

    /**
     * Encabezado de selección de medios de pago
     */
    public static final String SELECCIONA_MEDIO_PAGO = "Selecciona el medio de pago para tu paquete:";

    /**
     * Opción 1: Tarjeta de crédito
     */
    public static final String TARJETA_CREDITO = "1. Tarjeta de Credito";

    /**
     * Opción 2: PSE
     */
    public static final String PSE_PAGO = "2. PSE";

    /**
     * Opción 3: Nequi
     */
    public static final String NEQUI_PAGO = "3. Nequi";

    /**
     * Opción 4: Descuento de saldo
     */
    public static final String DESCUENTO_SALDO = "4. Descuento de saldo";

    /**
     * Opción 0: Atrás en medios de pago
     */
    public static final String ATRAS_MEDIOS_PAGO = "0. Atrás";

    // Medios de pago adicionales
    /**
     * Opción de Daviplata
     */
    public static final String DAVIPLATA_PAGO = "Daviplata";

    /**
     * Opción de PayU
     */
    public static final String PAYU_PAGO = "PayU";

    /**
     * Opción de efectivo
     */
    public static final String EFECTIVO_PAGO = "Efectivo";

    // ==================== OPCIONES NUMÉRICAS ====================

    public static final String OPCION_1 = "1";
    public static final String OPCION_2 = "2";
    public static final String OPCION_3 = "3";
    public static final String OPCION_4 = "4";
    public static final String OPCION_5 = "5";
    public static final String OPCION_6 = "6";
    public static final String OPCION_7 = "7";
    public static final String OPCION_8 = "8";
    public static final String OPCION_9 = "9";
    public static final String OPCION_0 = "0";

    /**
     * Símbolo de asterisco
     */
    public static final String ASTERISCO = "*";

    /**
     * Símbolo de numeral
     */
    public static final String NUMERAL = "#";

    // ==================== BOTONES Y ACCIONES ====================

    /**
     * Botón principal de envío
     */
    public static final String BTN_ENVIAR = "Enviar";

    /**
     * Botón de cancelación
     */
    public static final String BTN_CANCELAR = "Cancelar";

    /**
     * Botón de aceptación
     */
    public static final String BTN_ACEPTAR = "Aceptar";

    /**
     * Botón OK estándar
     */
    public static final String BTN_OK = "OK";

    /**
     * Botón de cierre
     */
    public static final String BTN_CERRAR = "Cerrar";

    /**
     * Botón de navegación atrás
     */
    public static final String BTN_ATRAS = "Atrás";

    /**
     * Botón para continuar
     */
    public static final String BTN_CONTINUAR = "Continuar";

    /**
     * Botón de confirmación
     */
    public static final String BTN_CONFIRMAR = "Confirmar";

    /**
     * Botón de rechazo
     */
    public static final String BTN_NO = "No";

    /**
     * Botón de salida
     */
    public static final String BTN_SALIR = "Salir";

    // ==================== MENSAJES DE ÉXITO ====================

    /**
     * Mensaje de compra exitosa
     */
    public static final String COMPRA_EXITOSA_USSD = "Compra Exitosa.";

    /**
     * Mensaje de paquete activado exitosamente
     */
    public static final String PAQUETE_ACTIVADO_EXITOSAMENTE = "Tu paquete se activo exitosamente";

    /**
     * Mensaje sobre recepción de SMS con detalles
     */
    public static final String MENSAJE_DETALLES_SMS = "en un momento recibiras un mensaje de texto con los detalles de tu compra";

    /**
     * Mensaje de agradecimiento
     */
    public static final String GRACIAS_MENSAJE = "Gracias";

    /**
     * Mensaje de operación exitosa genérica
     */
    public static final String OPERACION_EXITOSA_USSD = "Operación exitosa.";

    /**
     * Mensaje de transacción completada
     */
    public static final String TRANSACCION_COMPLETADA = "Transacción completada exitosamente";

    /**
     * Mensaje de confirmación de compra
     */
    public static final String COMPRA_CONFIRMADA = "Compra confirmada";

    /**
     * Mensaje de activación inmediata
     */
    public static final String ACTIVACION_INMEDIATA = "Tu paquete está activo de inmediato";

    // ==================== MENSAJES DE ERROR ====================

    /**
     * Mensaje de saldo insuficiente (corto)
     */
    public static final String SALDO_INSUFICIENTE_USSD = "Tu saldo es insuficiente";

    /**
     * Mensaje de saldo insuficiente (completo)
     */
    public static final String SALDO_INSUFICIENTE_COMPLETO = "Tu saldo es insuficiente, intenta con otro medio de pago.";

    /**
     * Mensaje de servicio no disponible
     */
    public static final String SERVICIO_NO_DISPONIBLE = "Servicio no disponible";

    /**
     * Mensaje de error en procesamiento
     */
    public static final String ERROR_PROCESAMIENTO = "Error en el procesamiento";

    /**
     * Mensaje para reintentar más tarde
     */
    public static final String INTENTE_MAS_TARDE = "Intente más tarde";

    /**
     * Mensaje de fallo en operación
     */
    public static final String FALLO_OPERACION = "Fallo en la operación";

    /**
     * Mensaje de sesión expirada
     */
    public static final String SESION_EXPIRADA = "Sesión expirada";

    /**
     * Mensaje de conexión perdida
     */
    public static final String CONEXION_PERDIDA = "Conexión perdida";

    /**
     * Mensaje de timeout del sistema
     */
    public static final String TIMEOUT_SISTEMA = "Tiempo de espera agotado";

    /**
     * Mensaje de opción inválida
     */
    public static final String OPCION_INVALIDA = "Opción inválida";

    /**
     * Mensaje de formato incorrecto
     */
    public static final String FORMATO_INCORRECTO = "Formato incorrecto";

    // ==================== PATRONES DE VALIDACIÓN ====================

    /**
     * Patrón regex para validar precios ($1234)
     */
    public static final String PATRON_PRECIO = "\\$\\d+";

    /**
     * Patrón regex para datos en GB (1.4GB, 2GB)
     */
    public static final String PATRON_DATOS_GB = "\\d+(\\.\\d+)?\\s*GB";

    /**
     * Patrón regex para datos en MB (500MB, 1500MB)
     */
    public static final String PATRON_DATOS_MB = "\\d+(\\.\\d+)?\\s*MB";

    /**
     * Patrón regex para vigencia en días (7 Dias, 30 días)
     */
    public static final String PATRON_VIGENCIA_DIAS = "\\d+\\s+(Dias?|días?)";

    /**
     * Patrón regex para números telefónicos (310 263 2840)
     */
    public static final String PATRON_TELEFONO = "\\d{3}\\s\\d{3}\\s\\d{4}";

    /**
     * Patrón regex para códigos USSD (*611#, *444#)
     */
    public static final String PATRON_NUMERO_CORTO = "\\*\\d{3}#";

    /**
     * Patrón regex para montos monetarios
     */
    public static final String PATRON_MONTO = "\\$?\\d{1,3}(,\\d{3})*(\\.\\d{2})?";

    /**
     * Patrón regex para fechas DD/MM/YYYY
     */
    public static final String PATRON_FECHA = "\\d{2}/\\d{2}/\\d{4}";

    /**
     * Patrón regex para horas HH:MM
     */
    public static final String PATRON_HORA = "\\d{2}:\\d{2}";

    // ==================== TIMEOUTS (en milisegundos) ====================

    /**
     * Timeout principal para respuestas USSD (15 segundos)
     */
    public static final int TIMEOUT_USSD_RESPONSE = 15000;

    /**
     * Timeout para carga de menús (8 segundos)
     */
    public static final int TIMEOUT_MENU_LOAD = 8000;

    /**
     * Timeout para confirmaciones de compra (20 segundos)
     */
    public static final int TIMEOUT_CONFIRMATION = 20000;

    /**
     * Timeout para llegada de SMS (30 segundos)
     */
    public static final int TIMEOUT_SMS_ARRIVAL = 30000;

    /**
     * Timeout para procesamiento de pagos (45 segundos)
     */
    public static final int TIMEOUT_PAYMENT_PROCESS = 45000;

    /**
     * Timeout para conexión inicial (10 segundos)
     */
    public static final int TIMEOUT_CONNECTION = 10000;

    /**
     * Timeout para validaciones rápidas (5 segundos)
     */
    public static final int TIMEOUT_QUICK_VALIDATION = 5000;

    /**
     * Timeout extendido para operaciones lentas (60 segundos)
     */
    public static final int TIMEOUT_EXTENDED = 60000;

    // ==================== WAITS ESTÁNDAR (en milisegundos) ====================

    /**
     * Espera corta (1 segundo)
     */
    public static final int WAIT_SHORT = 1000;

    /**
     * Espera media (2 segundos)
     */
    public static final int WAIT_MEDIUM = 2000;

    /**
     * Espera larga (3 segundos)
     */
    public static final int WAIT_LONG = 3000;

    /**
     * Espera extra larga (5 segundos)
     */
    public static final int WAIT_EXTRA_LONG = 5000;

    /**
     * Espera mínima (500 ms)
     */
    public static final int WAIT_MINIMAL = 500;

    /**
     * Espera para animaciones (800 ms)
     */
    public static final int WAIT_ANIMATION = 800;

    /**
     * Espera entre pasos (1.5 segundos)
     */
    public static final int WAIT_BETWEEN_STEPS = 1500;

    // ==================== DESCRIPCIONES PARA EVIDENCIAS ====================

    /**
     * Descripción: Llamada inicial USSD
     */
    public static final String DESC_LLAMADA_INICIAL = "Llamada inicial USSD";

    /**
     * Descripción: Menú principal USSD
     */
    public static final String DESC_MENU_PRINCIPAL = "Menú principal USSD";

    /**
     * Descripción: Selección de compra de paquetes
     */
    public static final String DESC_SELECCION_PAQUETES = "Selección de compra de paquetes";

    /**
     * Descripción: Información de paquetes disponibles
     */
    public static final String DESC_INFO_PAQUETES = "Información de paquetes disponibles";

    /**
     * Descripción: Selección paquete más vendido
     */
    public static final String DESC_SELECCION_MAS_VENDIDO = "Selección paquete más vendido";

    /**
     * Descripción: Pantalla de medios de pago
     */
    public static final String DESC_MEDIOS_PAGO = "Pantalla de medios de pago";

    /**
     * Descripción: Selección de medio de pago
     */
    public static final String DESC_SELECCION_MEDIO_PAGO = "Selección de medio de pago";

    /**
     * Descripción: Confirmación de compra
     */
    public static final String DESC_CONFIRMACION = "Confirmación de compra";

    /**
     * Descripción: Mensaje SMS de confirmación
     */
    public static final String DESC_MENSAJE_SMS = "Mensaje SMS de confirmación";

    /**
     * Descripción: Error de saldo insuficiente
     */
    public static final String DESC_ERROR_SALDO = "Error de saldo insuficiente";

    /**
     * Descripción: Procesamiento de pago
     */
    public static final String DESC_PROCESAMIENTO_PAGO = "Procesamiento de pago";

    /**
     * Descripción: Validación de elementos UI
     */
    public static final String DESC_VALIDACION_UI = "Validación de elementos UI";

    // ==================== MENSAJES DE LOG ====================

    /**
     * Log: Inicio del flujo USSD
     */
    public static final String LOG_INICIO_FLUJO = "🚀 Iniciando flujo USSD";

    /**
     * Log: Fin exitoso del flujo
     */
    public static final String LOG_FIN_FLUJO_EXITOSO = "✅ Flujo USSD completado exitosamente";

    /**
     * Log: Opción ingresada
     */
    public static final String LOG_OPCION_INGRESADA = "⌨️ Opción ingresada: ";

    /**
     * Log: Validación exitosa
     */
    public static final String LOG_VALIDACION_EXITOSA = "✅ Validación exitosa: ";

    /**
     * Log: Error en validación
     */
    public static final String LOG_ERROR_VALIDACION = "❌ Error en validación: ";

    /**
     * Log: Pantalla cargada
     */
    public static final String LOG_PANTALLA_CARGADA = "📱 Pantalla cargada: ";

    /**
     * Log: Procesando pago
     */
    public static final String LOG_PROCESANDO_PAGO = "💳 Procesando pago: ";

    /**
     * Log: Esperando SMS
     */
    public static final String LOG_ESPERANDO_SMS = "📩 Esperando SMS de confirmación";

    /**
     * Log: Timeout alcanzado
     */
    public static final String LOG_TIMEOUT_ALCANZADO = "⏰ Timeout alcanzado";

    /**
     * Log: Error inesperado
     */
    public static final String LOG_ERROR_INESPERADO = "🔥 Error inesperado: ";

    /**
     * Log: Reintento de operación
     */
    public static final String LOG_REINTENTANDO = "🔄 Reintentando operación: ";

    /**
     * Log: Configuración aplicada
     */
    public static final String LOG_CONFIGURACION = "⚙️ Configuración aplicada: ";

    /**
     * Log: Evidencia capturada
     */
    public static final String LOG_EVIDENCIA_CAPTURADA = "📸 Evidencia capturada: ";

    // ==================== NOMBRES DE ARCHIVOS DE CAPTURA ====================

    /**
     * Archivo: Llamada inicial USSD
     */
    public static final String CAPTURA_LLAMADA_INICIAL = "llamada_inicial_ussd";

    /**
     * Archivo: Menú principal USSD
     */
    public static final String CAPTURA_MENU_PRINCIPAL = "menu_principal_ussd";

    /**
     * Archivo: Información de paquetes
     */
    public static final String CAPTURA_INFO_PAQUETES = "informacion_paquetes";

    /**
     * Archivo: Selección de paquete
     */
    public static final String CAPTURA_SELECCION_PAQUETE = "seleccion_paquete";

    /**
     * Archivo: Medios de pago
     */
    public static final String CAPTURA_MEDIOS_PAGO = "medios_pago";

    /**
     * Archivo: Selección de medio de pago
     */
    public static final String CAPTURA_SELECCION_PAGO = "seleccion_medio_pago";

    /**
     * Archivo: Confirmación de compra
     */
    public static final String CAPTURA_CONFIRMACION = "confirmacion_compra";

    /**
     * Archivo: SMS de confirmación
     */
    public static final String CAPTURA_SMS_CONFIRMACION = "sms_confirmacion";

    /**
     * Archivo: Error USSD
     */
    public static final String CAPTURA_ERROR = "error_ussd";

    /**
     * Archivo: Timeout USSD
     */
    public static final String CAPTURA_TIMEOUT = "timeout_ussd";

    /**
     * Archivo: Estado del sistema
     */
    public static final String CAPTURA_ESTADO_SISTEMA = "estado_sistema";

    /**
     * Archivo: Debug detallado
     */
    public static final String CAPTURA_DEBUG = "debug_ussd";

    // ==================== CONFIGURACIONES DE ENTORNO ====================

    /**
     * Ambiente de producción
     */
    public static final String ENV_PROD = "PROD";

    /**
     * Ambiente de pruebas QA
     */
    public static final String ENV_QA = "QA";

    /**
     * Ambiente de desarrollo
     */
    public static final String ENV_DEV = "DEV";

    /**
     * Ambiente de staging
     */
    public static final String ENV_STAGING = "STAGING";

    /**
     * Ambiente local
     */
    public static final String ENV_LOCAL = "LOCAL";

    // Códigos USSD específicos por ambiente
    /**
     * USSD para producción
     */
    public static final String USSD_PROD = "*611#";

    /**
     * USSD para QA
     */
    public static final String USSD_QA = "*611#";

    /**
     * USSD para desarrollo
     */
    public static final String USSD_DEV = "*611#";

    // ==================== PARÁMETROS DE RETRY Y RECUPERACIÓN ====================

    /**
     * Máximo número de reintentos para operaciones USSD
     */
    public static final int MAX_REINTENTOS_USSD = 3;

    /**
     * Máximo número de reintentos para validaciones
     */
    public static final int MAX_REINTENTOS_VALIDACION = 5;

    /**
     * Intervalo entre reintentos (2 segundos)
     */
    public static final int INTERVALO_REINTENTO = 2000;

    /**
     * Intervalo extendido para reintentos (5 segundos)
     */
    public static final int INTERVALO_REINTENTO_EXTENDIDO = 5000;

    /**
     * Factor multiplicador para backoff exponencial
     */
    public static final double FACTOR_BACKOFF = 1.5;

    // ==================== LÍMITES Y UMBRALES ====================

    /**
     * Máximo de caracteres permitidos en entrada USSD
     */
    public static final int MAX_CARACTERES_ENTRADA = 10;

    /**
     * Mínimo saldo requerido (en pesos)
     */
    public static final int MIN_SALDO_REQUERIDO = 1000;

    /**
     * Máximo tiempo de respuesta aceptable (10 segundos)
     */
    public static final int MAX_TIEMPO_RESPUESTA_ACEPTABLE = 10000;

    /**
     * Máximo número de opciones en un menú
     */
    public static final int MAX_OPCIONES_MENU = 10;

    /**
     * Longitud mínima de mensaje SMS
     */
    public static final int MIN_LONGITUD_SMS = 10;

    /**
     * Longitud máxima de mensaje SMS
     */
    public static final int MAX_LONGITUD_SMS = 160;

    // ==================== MENSAJES DEL SISTEMA ====================

    /**
     * Estado: Ejecutando código USSD
     */
    public static final String EJECUTANDO_CODIGO_USSD = "Ejecutando código USSD…";

    /**
     * Estado: Procesando solicitud
     */
    public static final String PROCESANDO_SOLICITUD = "Procesando solicitud...";

    /**
     * Estado: Conectando con el servicio
     */
    public static final String CONECTANDO_SERVICIO = "Conectando con el servicio";

    /**
     * Estado: Esperando entrada del usuario
     */
    public static final String ESTADO_ESPERANDO_ENTRADA = "Esperando entrada del usuario";

    /**
     * Estado: Procesando
     */
    public static final String ESTADO_PROCESANDO = "Procesando solicitud";

    /**
     * Estado: Completado
     */
    public static final String ESTADO_COMPLETADO = "Operación completada";

    /**
     * Estado: Error
     */
    public static final String ESTADO_ERROR = "Error en operación";

    /**
     * Estado: Timeout
     */
    public static final String ESTADO_TIMEOUT = "Timeout en operación";

    /**
     * Estado: Cancelado
     */
    public static final String ESTADO_CANCELADO = "Operación cancelada";

    // ==================== TIPOS DE PAQUETES Y SERVICIOS ====================

    /**
     * Información promocional
     */
    public static final String INFO_PROMOCIONAL = "Promocional";

    /**
     * Información de vigencia
     */
    public static final String INFO_VIGENCIA = "Vigencia";

    /**
     * Información de precio
     */
    public static final String INFO_PRECIO = "Precio";

    /**
     * Información de servicios incluidos
     */
    public static final String INFO_INCLUYE = "Incluye";

    /**
     * Servicio de WhatsApp
     */
    public static final String SERVICIO_WHATSAPP = "WhatsApp";

    /**
     * Servicio de Facebook
     */
    public static final String SERVICIO_FACEBOOK = "Facebook";

    /**
     * Servicio de Instagram
     */
    public static final String SERVICIO_INSTAGRAM = "Instagram";

    /**
     * Servicio de YouTube
     */
    public static final String SERVICIO_YOUTUBE = "YouTube";

    /**
     * Servicio de Netflix
     */
    public static final String SERVICIO_NETFLIX = "Netflix";

    /**
     * Redes sociales ilimitadas
     */
    public static final String REDES_SOCIALES_ILIMITADAS = "Redes sociales ilimitadas";

    // ==================== VALIDACIONES ESPECÍFICAS ====================

    /**
     * Validación: Precio válido encontrado
     */
    public static final String VALIDACION_PRECIO_VALIDO = "Precio válido encontrado";

    /**
     * Validación: Datos válidos encontrados
     */
    public static final String VALIDACION_DATOS_VALIDOS = "Datos válidos encontrados";

    /**
     * Validación: Vigencia válida encontrada
     */
    public static final String VALIDACION_VIGENCIA_VALIDA = "Vigencia válida encontrada";

    /**
     * Validación: Menú completo validado
     */
    public static final String VALIDACION_MENU_COMPLETO = "Menú completo validado";

    /**
     * Validación: Medios de pago disponibles
     */
    public static final String VALIDACION_PAGO_DISPONIBLE = "Medios de pago disponibles";

    /**
     * Validación: Elementos UI correctos
     */
    public static final String VALIDACION_UI_CORRECTA = "Elementos UI validados correctamente";

    /**
     * Validación: Flujo completado
     */
    public static final String VALIDACION_FLUJO_COMPLETADO = "Flujo completado exitosamente";

    // ==================== MAPAS DE CONFIGURACIÓN ====================

    /**
     * Mapa de códigos USSD por ambiente
     */
    private static final Map<String, String> CODIGOS_POR_AMBIENTE;

    /**
     * Mapa de timeouts por tipo de operación
     */
    private static final Map<String, Integer> TIMEOUTS_POR_OPERACION;

    /**
     * Lista de textos de error conocidos
     */
    private static final List<String> TEXTOS_ERROR_CONOCIDOS;

    /**
     * Lista de textos de éxito conocidos
     */
    private static final List<String> TEXTOS_EXITO_CONOCIDOS;

    static {
        // Inicializar mapa de códigos USSD por ambiente
        Map<String, String> codigosTemp = new HashMap<>();
        codigosTemp.put(ENV_PROD, USSD_PROD);
        codigosTemp.put(ENV_QA, USSD_QA);
        codigosTemp.put(ENV_DEV, USSD_DEV);
        codigosTemp.put(ENV_STAGING, USSD_QA);
        codigosTemp.put(ENV_LOCAL, USSD_DEV);
        CODIGOS_POR_AMBIENTE = Collections.unmodifiableMap(codigosTemp);

        // Inicializar mapa de timeouts por operación
        Map<String, Integer> timeoutsTemp = new HashMap<>();
        timeoutsTemp.put("ussd_response", TIMEOUT_USSD_RESPONSE);
        timeoutsTemp.put("menu_load", TIMEOUT_MENU_LOAD);
        timeoutsTemp.put("confirmation", TIMEOUT_CONFIRMATION);
        timeoutsTemp.put("sms_arrival", TIMEOUT_SMS_ARRIVAL);
        timeoutsTemp.put("payment_process", TIMEOUT_PAYMENT_PROCESS);
        timeoutsTemp.put("connection", TIMEOUT_CONNECTION);
        timeoutsTemp.put("validation", TIMEOUT_QUICK_VALIDATION);
        timeoutsTemp.put("extended", TIMEOUT_EXTENDED);
        TIMEOUTS_POR_OPERACION = Collections.unmodifiableMap(timeoutsTemp);

        // Inicializar lista de textos de error conocidos
        TEXTOS_ERROR_CONOCIDOS = Collections.unmodifiableList(Arrays.asList(
                SALDO_INSUFICIENTE_USSD,
                SALDO_INSUFICIENTE_COMPLETO,
                SERVICIO_NO_DISPONIBLE,
                ERROR_PROCESAMIENTO,
                INTENTE_MAS_TARDE,
                FALLO_OPERACION,
                SESION_EXPIRADA,
                CONEXION_PERDIDA,
                TIMEOUT_SISTEMA,
                OPCION_INVALIDA,
                FORMATO_INCORRECTO
        ));

        // Inicializar lista de textos de éxito conocidos
        TEXTOS_EXITO_CONOCIDOS = Collections.unmodifiableList(Arrays.asList(
                COMPRA_EXITOSA_USSD,
                PAQUETE_ACTIVADO_EXITOSAMENTE,
                OPERACION_EXITOSA_USSD,
                TRANSACCION_COMPLETADA,
                COMPRA_CONFIRMADA,
                ACTIVACION_INMEDIATA,
                GRACIAS_MENSAJE
        ));
    }

    // ==================== CONSTRUCTOR PRIVADO ====================

    /**
     * Constructor privado para prevenir instanciación.
     * Esta clase solo debe usarse para acceder a constantes estáticas.
     */
    private Constants() {
        throw new UnsupportedOperationException(
                "ConstantesUSSD es una clase de utilidades y no debe ser instanciada"
        );
    }

    // ==================== MÉTODOS UTILITARIOS ESTÁTICOS ====================

    /**
     * Obtiene el código USSD correspondiente al ambiente especificado.
     *
     * @param ambiente Ambiente actual (PROD, QA, DEV, STAGING, LOCAL)
     * @return Código USSD correspondiente al ambiente
     * @throws IllegalArgumentException si el ambiente no es válido
     */
    public static String obtenerCodigoUSSDPorAmbiente(String ambiente) {
        if (ambiente == null || ambiente.trim().isEmpty()) {
            return CODIGO_USSD_611; // Valor por defecto
        }

        String codigoUSSD = CODIGOS_POR_AMBIENTE.get(ambiente.toUpperCase());
        return codigoUSSD != null ? codigoUSSD : CODIGO_USSD_611;
    }

    /**
     * Obtiene el timeout correspondiente al tipo de operación especificada.
     *
     * @param tipoOperacion Tipo de operación (ussd_response, menu_load, etc.)
     * @return Timeout en milisegundos para la operación
     */
    public static int obtenerTimeoutPorOperacion(String tipoOperacion) {
        if (tipoOperacion == null || tipoOperacion.trim().isEmpty()) {
            return TIMEOUT_USSD_RESPONSE; // Valor por defecto
        }

        Integer timeout = TIMEOUTS_POR_OPERACION.get(tipoOperacion.toLowerCase());
        return timeout != null ? timeout : TIMEOUT_USSD_RESPONSE;
    }

    /**
     * Valida si un texto corresponde a un precio válido usando regex.
     *
     * @param texto Texto a validar
     * @return true si el texto representa un precio válido ($1234, $5678)
     */
    public static boolean esPrecioValido(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return texto.trim().matches(PATRON_PRECIO);
    }

    /**
     * Valida si un texto corresponde a datos válidos (GB o MB).
     *
     * @param texto Texto a validar
     * @return true si el texto representa datos válidos (1.4GB, 500MB)
     */
    public static boolean sonDatosValidos(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        String textoLimpio = texto.trim();
        return textoLimpio.matches(PATRON_DATOS_GB) || textoLimpio.matches(PATRON_DATOS_MB);
    }

    /**
     * Valida si un texto corresponde a vigencia válida en días.
     *
     * @param texto Texto a validar
     * @return true si el texto representa vigencia válida (7 Dias, 30 días)
     */
    public static boolean esVigenciaValida(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }
        return texto.trim().matches(PATRON_VIGENCIA_DIAS);
    }

    /**
     * Valida si un código corresponde a un código USSD válido.
     *
     * @param codigo Código a validar
     * @return true si el código es un USSD válido (*611#, *444#)
     */
    public static boolean esCodigoUSSDValido(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        return codigo.trim().matches(PATRON_NUMERO_CORTO);
    }

    /**
     * Determina si un texto representa un mensaje de error conocido.
     *
     * @param texto Texto a evaluar
     * @return true si el texto contiene algún mensaje de error conocido
     */
    public static boolean esMensajeDeError(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        String textoMinuscula = texto.toLowerCase().trim();
        return TEXTOS_ERROR_CONOCIDOS.stream()
                .anyMatch(error -> textoMinuscula.contains(error.toLowerCase()));
    }

    /**
     * Determina si un texto representa un mensaje de éxito conocido.
     *
     * @param texto Texto a evaluar
     * @return true si el texto contiene algún mensaje de éxito conocido
     */
    public static boolean esMensajeDeExito(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return false;
        }

        String textoMinuscula = texto.toLowerCase().trim();
        return TEXTOS_EXITO_CONOCIDOS.stream()
                .anyMatch(exito -> textoMinuscula.contains(exito.toLowerCase()));
    }

    /**
     * Obtiene el wait apropiado basado en el tipo de operación.
     *
     * @param tipoOperacion Tipo de operación (short, medium, long, extra_long)
     * @return Tiempo de espera en milisegundos
     */
    public static int obtenerWaitPorTipo(String tipoOperacion) {
        if (tipoOperacion == null) {
            return WAIT_MEDIUM; // Valor por defecto
        }

        switch (tipoOperacion.toLowerCase()) {
            case "minimal":
            case "min":
                return WAIT_MINIMAL;
            case "short":
            case "corto":
                return WAIT_SHORT;
            case "medium":
            case "medio":
                return WAIT_MEDIUM;
            case "long":
            case "largo":
                return WAIT_LONG;
            case "extra_long":
            case "extra":
                return WAIT_EXTRA_LONG;
            case "animation":
            case "animacion":
                return WAIT_ANIMATION;
            case "between_steps":
            case "entre_pasos":
                return WAIT_BETWEEN_STEPS;
            default:
                return WAIT_MEDIUM;
        }
    }

    /**
     * Extrae el valor numérico de un precio ($7500 -> 7500).
     *
     * @param textoPrecio Texto que contiene el precio
     * @return Valor numérico del precio, o -1 si no se puede extraer
     */
    public static int extraerValorPrecio(String textoPrecio) {
        if (!esPrecioValido(textoPrecio)) {
            return -1;
        }

        try {
            // Remover el símbolo $ y espacios, luego convertir a entero
            String numeroStr = textoPrecio.replaceAll("[^\\d]", "");
            return Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Extrae el valor numérico de datos (1.4GB -> 1.4).
     *
     * @param textoDatos Texto que contiene los datos
     * @return Valor numérico de los datos, o -1.0 si no se puede extraer
     */
    public static double extraerValorDatos(String textoDatos) {
        if (!sonDatosValidos(textoDatos)) {
            return -1.0;
        }

        try {
            // Extraer solo la parte numérica (antes de GB/MB)
            String numeroStr = textoDatos.replaceAll("[^\\d.]", "");
            return Double.parseDouble(numeroStr);
        } catch (NumberFormatException e) {
            return -1.0;
        }
    }

    /**
     * Extrae el número de días de vigencia (6 Dias -> 6).
     *
     * @param textoVigencia Texto que contiene la vigencia
     * @return Número de días, o -1 si no se puede extraer
     */
    public static int extraerDiasVigencia(String textoVigencia) {
        if (!esVigenciaValida(textoVigencia)) {
            return -1;
        }

        try {
            // Extraer solo los números
            String numeroStr = textoVigencia.replaceAll("[^\\d]", "");
            return Integer.parseInt(numeroStr);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Valida si una opción numérica está en el rango válido (0-9).
     *
     * @param opcion Opción a validar
     * @return true si la opción es válida
     */
    public static boolean esOpcionValida(String opcion) {
        if (opcion == null || opcion.length() != 1) {
            return false;
        }

        return opcion.matches("[0-9]");
    }

    /**
     * Genera un nombre de archivo único para capturas basado en timestamp.
     *
     * @param prefijo Prefijo del nombre de archivo
     * @return Nombre de archivo único con timestamp
     */
    public static String generarNombreArchivoCaptura(String prefijo) {
        if (prefijo == null || prefijo.trim().isEmpty()) {
            prefijo = "captura";
        }

        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));

        return prefijo.toLowerCase().replaceAll("[^a-z0-9]", "_") + "_" + timestamp;
    }

    /**
     * Calcula el timeout ajustado basado en un factor multiplicador.
     *
     * @param timeoutBase Timeout base en milisegundos
     * @param factor      Factor multiplicador (1.0 = sin cambio, 2.0 = doble tiempo)
     * @return Timeout ajustado
     */
    public static int calcularTimeoutAjustado(int timeoutBase, double factor) {
        if (factor <= 0) {
            factor = 1.0;
        }

        return (int) Math.round(timeoutBase * factor);
    }

    /**
     * Obtiene una lista inmutable de todos los códigos USSD disponibles.
     *
     * @return Lista de códigos USSD
     */
    public static List<String> obtenerTodosLosCodigosUSSD() {
        return Collections.unmodifiableList(Arrays.asList(
                CODIGO_USSD_611,
                CODIGO_USSD_SALDO,
                CODIGO_USSD_RECARGAS,
                CODIGO_USSD_CONSUMOS,
                CODIGO_USSD_PAQUETES_DATOS,
                CODIGO_USSD_SERVICIO_CLIENTE
        ));
    }

    /**
     * Obtiene una lista inmutable de todas las opciones numéricas válidas.
     *
     * @return Lista de opciones válidas (0-9)
     */
    public static List<String> obtenerOpcionesValidas() {
        return Collections.unmodifiableList(Arrays.asList(
                OPCION_0, OPCION_1, OPCION_2, OPCION_3, OPCION_4,
                OPCION_5, OPCION_6, OPCION_7, OPCION_8, OPCION_9
        ));
    }

    /**
     * Obtiene un mapa inmutable de ambientes y sus códigos USSD correspondientes.
     *
     * @return Mapa de ambiente -> código USSD
     */
    public static Map<String, String> obtenerMapaCodigosPorAmbiente() {
        return CODIGOS_POR_AMBIENTE;
    }

    /**
     * Obtiene un mapa inmutable de operaciones y sus timeouts correspondientes.
     *
     * @return Mapa de operación -> timeout en milisegundos
     */
    public static Map<String, Integer> obtenerMapaTimeoutsPorOperacion() {
        return TIMEOUTS_POR_OPERACION;
    }

    /**
     * Valida una configuración completa de parámetros USSD.
     *
     * @param numeroUSSD Número USSD a validar
     * @param ambiente   Ambiente a validar
     * @param timeout    Timeout a validar
     * @return true si todos los parámetros son válidos
     */
    public static boolean validarConfiguracionCompleta(String numeroUSSD, String ambiente, int timeout) {
        return esCodigoUSSDValido(numeroUSSD) &&
                CODIGOS_POR_AMBIENTE.containsKey(ambiente.toUpperCase()) &&
                timeout > 0 && timeout <= TIMEOUT_EXTENDED;
    }

    /**
     * Formatea un mensaje de error con timestamp y contexto.
     *
     * @param mensaje  Mensaje base del error
     * @param contexto Contexto adicional (opcional)
     * @return Mensaje formateado con timestamp
     */
    public static String formatearMensajeError(String mensaje, String contexto) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

        StringBuilder mensajeFormateado = new StringBuilder();
        mensajeFormateado.append("[").append(timestamp).append("] ");
        mensajeFormateado.append("❌ ").append(mensaje);

        if (contexto != null && !contexto.trim().isEmpty()) {
            mensajeFormateado.append(" | Contexto: ").append(contexto);
        }

        return mensajeFormateado.toString();
    }

    /**
     * Formatea un mensaje de éxito con timestamp y contexto.
     *
     * @param mensaje  Mensaje base del éxito
     * @param contexto Contexto adicional (opcional)
     * @return Mensaje formateado con timestamp
     */
    public static String formatearMensajeExito(String mensaje, String contexto) {
        String timestamp = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss"));

        StringBuilder mensajeFormateado = new StringBuilder();
        mensajeFormateado.append("[").append(timestamp).append("] ");
        mensajeFormateado.append("✅ ").append(mensaje);

        if (contexto != null && !contexto.trim().isEmpty()) {
            mensajeFormateado.append(" | Contexto: ").append(contexto);
        }

        return mensajeFormateado.toString();
    }

    /**
     * Obtiene información de versión y metadatos de la clase.
     *
     * @return String con información de la clase
     */
    public static String obtenerInfoVersion() {
        return String.format(
                "ConstantesUSSD v2.0 - %d constantes definidas - Última actualización: %s",
                contarConstantesDefinidas(),
                "2024-12-19"
        );
    }

    /**
     * Cuenta el número aproximado de constantes definidas en la clase.
     *
     * @return Número estimado de constantes
     */
    private static int contarConstantesDefinidas() {
        // Estimación basada en las secciones definidas
        return 150; // Aproximadamente 150 constantes entre todas las categorías
    }

    /**
     * Genera un reporte de uso de la configuración actual.
     *
     * @return Reporte con estadísticas de uso
     */
    public static String generarReporteUso() {
        StringBuilder reporte = new StringBuilder();
        reporte.append("📊 REPORTE DE CONFIGURACIÓN USSD\n");
        reporte.append("=====================================\n");
        reporte.append("🔧 Ambientes configurados: ").append(CODIGOS_POR_AMBIENTE.size()).append("\n");
        reporte.append("⏱️ Tipos de timeout: ").append(TIMEOUTS_POR_OPERACION.size()).append("\n");
        reporte.append("❌ Errores conocidos: ").append(TEXTOS_ERROR_CONOCIDOS.size()).append("\n");
        reporte.append("✅ Mensajes de éxito: ").append(TEXTOS_EXITO_CONOCIDOS.size()).append("\n");
        reporte.append("📱 Códigos USSD disponibles: ").append(obtenerTodosLosCodigosUSSD().size()).append("\n");
        reporte.append("🎯 Total de constantes: ~").append(contarConstantesDefinidas()).append("\n");
        reporte.append("=====================================");

        return reporte.toString();
    }
}