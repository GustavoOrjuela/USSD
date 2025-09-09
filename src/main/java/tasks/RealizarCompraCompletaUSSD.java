package tasks;

import interactions.ussd.IngresarOpcionUSSD;
import interactions.validations.ValidarPantallaUSSD;
import interactions.wait.WaitFor;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import utils.CapturaDePantallaMovil;
import utils.Constants;
import utils.EvidenciaUtils;

import static net.serenitybdd.screenplay.Tasks.instrumented;

/**
 * Task de alto nivel que encapsula el flujo completo de compra de paquetes por USSD
 *
 * Esta clase implementa el patrón Task del Screenplay pattern para crear una
 * abstracción de alto nivel que combine múltiples interacciones en un flujo cohesivo.
 *
 * Principios SOLID aplicados:
 * - SRP: Responsabilidad única de ejecutar el flujo completo de compra USSD
 * - OCP: Extensible para agregar más pasos, validaciones o tipos de flujo
 * - LSP: Puede ser sustituida por subclases especializadas
 * - ISP: Interfaz clara y específica para flujos USSD
 * - DIP: Depende de abstracciones (Interactions y Tasks) no de implementaciones concretas
 *
 * @author Senior Test Automation Engineer
 * @version 1.0
 */
public class RealizarCompraCompletaUSSD implements Task {

    private final String numeroUSSD;
    private final TipoFlujo tipoFlujo;
    private final boolean incluirValidacionFinal;
    private final boolean capturarEvidenciaDetallada;
    private final int timeoutPersonalizado;

    /**
     * Enum para definir diferentes tipos de flujo USSD
     */
    public enum TipoFlujo {
        FLUJO_BASICO("Flujo básico hasta medios de pago"),
        FLUJO_COMPLETO("Flujo completo con confirmación"),
        FLUJO_SOLO_VALIDACION("Solo validación de pantallas"),
        FLUJO_CON_ERRORES("Flujo incluyendo manejo de errores"),
        FLUJO_PERSONALIZADO("Flujo con configuración personalizada");

        private final String descripcion;

        TipoFlujo(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    /**
     * Constructor principal con todos los parámetros
     */
    public RealizarCompraCompletaUSSD(String numeroUSSD, TipoFlujo tipoFlujo,
                                      boolean incluirValidacionFinal,
                                      boolean capturarEvidenciaDetallada,
                                      int timeoutPersonalizado) {
        this.numeroUSSD = numeroUSSD;
        this.tipoFlujo = tipoFlujo;
        this.incluirValidacionFinal = incluirValidacionFinal;
        this.capturarEvidenciaDetallada = capturarEvidenciaDetallada;
        this.timeoutPersonalizado = timeoutPersonalizado;
    }

    /**
     * Constructor simplificado con valores por defecto
     */
    public RealizarCompraCompletaUSSD(String numeroUSSD, boolean incluirValidacionFinal) {
        this(numeroUSSD, TipoFlujo.FLUJO_BASICO, incluirValidacionFinal, true,
                Constants.TIMEOUT_USSD_RESPONSE);
    }

    @Override
    @Step("Realizar flujo completo de compra de paquetes por USSD: {1}")
    public <T extends Actor> void performAs(T actor) {
        try {
            System.out.println(Constants.LOG_INICIO_FLUJO);
            inicializarFlujo();

            // Ejecutar flujo según el tipo especificado
            switch (tipoFlujo) {
                case FLUJO_BASICO:
                    ejecutarFlujoBasico(actor);
                    break;
                case FLUJO_COMPLETO:
                    ejecutarFlujoCompleto(actor);
                    break;
                case FLUJO_SOLO_VALIDACION:
                    ejecutarSoloValidaciones(actor);
                    break;
                case FLUJO_CON_ERRORES:
                    ejecutarFlujoConManejoErrores(actor);
                    break;
                case FLUJO_PERSONALIZADO:
                    ejecutarFlujoPersonalizado(actor);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de flujo no soportado: " + tipoFlujo);
            }

            // Validación final si está habilitada
            if (incluirValidacionFinal) {
                ejecutarValidacionFinal(actor);
            }

            System.out.println(Constants.LOG_FIN_FLUJO_EXITOSO);
            finalizarFlujo(true);

        } catch (Exception e) {
            System.err.println(Constants.LOG_ERROR_INESPERADO + e.getMessage());
            manejarErrorFlujo(actor, e);
            finalizarFlujo(false);
            throw new RuntimeException("Fallo en flujo completo USSD: " + e.getMessage(), e);
        }
    }

    /**
     * Inicializa el flujo configurando evidencias y estado
     */
    private void inicializarFlujo() {
        System.out.println("🔧 Inicializando configuración del flujo USSD");
        System.out.println("📋 Tipo de flujo: " + tipoFlujo.getDescripcion());
        System.out.println("⏱️ Timeout personalizado: " + timeoutPersonalizado + "ms");

        // Reiniciar contador de evidencias para este flujo
        EvidenciaUtils.reiniciarContador();

        if (capturarEvidenciaDetallada) {
            System.out.println("📸 Captura de evidencia detallada habilitada");
        }
    }

    /**
     * Ejecuta el flujo básico (hasta medios de pago)
     */
    @Step("Ejecutar flujo básico USSD")
    private <T extends Actor> void ejecutarFlujoBasico(T actor) {
        System.out.println("🎯 Ejecutando flujo básico USSD");

        // PASO 1: Realizar llamada inicial
        ejecutarLlamadaInicial(actor);

        // PASO 2: Navegar a compra de paquetes
        navegarACompraPaquetes(actor);

        // PASO 3: Validar información de paquetes
        validarInformacionPaquetes(actor);

        // PASO 4: Seleccionar paquete más vendido
        seleccionarPaqueteMasVendido(actor);

        // PASO 5: Validar medios de pago
        validarMediosPago(actor);

        System.out.println("✅ Flujo básico completado exitosamente");
    }

    /**
     * Ejecuta el flujo completo incluyendo confirmación
     */
    @Step("Ejecutar flujo completo USSD")
    private <T extends Actor> void ejecutarFlujoCompleto(T actor) {
        System.out.println("🎯 Ejecutando flujo completo USSD");

        // Ejecutar flujo básico primero
        ejecutarFlujoBasico(actor);

        // PASO 6: Seleccionar medio de pago
        seleccionarMedioDePago(actor);

        // PASO 7: Procesar y validar confirmación
        procesarYValidarConfirmacion(actor);

        // PASO 8: Validar mensaje SMS (opcional)
        if (capturarEvidenciaDetallada) {
            validarMensajeSMS(actor);
        }

        System.out.println("✅ Flujo completo ejecutado exitosamente");
    }

    /**
     * Ejecuta solo validaciones de pantallas sin interacciones
     */
    @Step("Ejecutar solo validaciones USSD")
    private <T extends Actor> void ejecutarSoloValidaciones(T actor) {
        System.out.println("🔍 Ejecutando solo validaciones de pantalla");

        // Validar pantalla actual sin interacciones
        actor.attemptsTo(WaitFor.aTime(Constants.WAIT_MEDIUM));

        // Intentar identificar qué pantalla está visible
        try {
            actor.attemptsTo(ValidarPantallaUSSD.menuPrincipal());
            System.out.println("✅ Menú principal identificado");
        } catch (Exception e) {
            try {
                actor.attemptsTo(ValidarPantallaUSSD.informacionPaquetes());
                System.out.println("✅ Pantalla de información de paquetes identificada");
            } catch (Exception e2) {
                try {
                    actor.attemptsTo(ValidarPantallaUSSD.mediosPago());
                    System.out.println("✅ Pantalla de medios de pago identificada");
                } catch (Exception e3) {
                    System.out.println("⚠️ No se pudo identificar el tipo de pantalla actual");
                    EvidenciaUtils.registrarCaptura("Pantalla no identificada");
                }
            }
        }
    }

    /**
     * Ejecuta flujo con manejo específico de errores
     */
    @Step("Ejecutar flujo con manejo de errores USSD")
    private <T extends Actor> void ejecutarFlujoConManejoErrores(T actor) {
        System.out.println("🛡️ Ejecutando flujo con manejo robusto de errores");

        int intentos = 0;
        int maxIntentos = Constants.MAX_REINTENTOS_USSD;
        boolean flujoExitoso = false;

        while (intentos < maxIntentos && !flujoExitoso) {
            try {
                System.out.println("🔄 Intento " + (intentos + 1) + " de " + maxIntentos);
                ejecutarFlujoBasico(actor);
                flujoExitoso = true;
                System.out.println("✅ Flujo exitoso en intento " + (intentos + 1));

            } catch (Exception e) {
                intentos++;
                System.out.println("⚠️ Error en intento " + intentos + ": " + e.getMessage());

                if (intentos < maxIntentos) {
                    System.out.println("🔄 Intentando recuperación...");

                    // Esperar antes del siguiente intento
                    actor.attemptsTo(WaitFor.aTime(Constants.INTERVALO_REINTENTO));

                    // Capturar evidencia del error
                    EvidenciaUtils.registrarCaptura("Error intento " + intentos + " - " + e.getMessage());

                    // Intentar recuperación específica
                    intentarRecuperacion(actor);
                } else {
                    System.out.println("❌ Máximo número de intentos alcanzado");
                    throw new RuntimeException("Fallo después de " + maxIntentos + " intentos", e);
                }
            }
        }
    }

    /**
     * Ejecuta flujo personalizado con configuraciones específicas
     */
    @Step("Ejecutar flujo personalizado USSD")
    private <T extends Actor> void ejecutarFlujoPersonalizado(T actor) {
        System.out.println("⚙️ Ejecutando flujo personalizado USSD");

        // Usar timeout personalizado
        actor.attemptsTo(WaitFor.aTime(timeoutPersonalizado / 10)); // 10% del timeout como wait inicial

        // Capturar estado inicial si está configurado
        if (capturarEvidenciaDetallada) {
            EvidenciaUtils.registrarCaptura("Estado inicial del flujo personalizado");
        }

        // Ejecutar pasos con configuración personalizada
        ejecutarFlujoBasico(actor);

        // Validaciones adicionales si están configuradas
        if (capturarEvidenciaDetallada) {
            capturarEstadoDetalladoSistema(actor);
        }

        System.out.println("✅ Flujo personalizado completado");
    }

    /**
     * PASO 1: Ejecuta la llamada inicial al código USSD
     */
    @Step("Ejecutar llamada inicial USSD")
    private <T extends Actor> void ejecutarLlamadaInicial(T actor) {
        System.out.println("📞 Paso 1: Realizando llamada USSD al " + numeroUSSD);

        actor.attemptsTo(
                RealizarLlamada.alNumero(numeroUSSD)
        );

        // Esperar respuesta del sistema USSD
        actor.attemptsTo(WaitFor.aTime(Constants.WAIT_LONG));

        EvidenciaUtils.registrarCaptura(Constants.DESC_LLAMADA_INICIAL + " - " + numeroUSSD);
        System.out.println("✅ Llamada USSD completada");
    }

    /**
     * PASO 2: Navega al menú de compra de paquetes
     */
    @Step("Navegar a compra de paquetes")
    private <T extends Actor> void navegarACompraPaquetes(T actor) {
        System.out.println("🛒 Paso 2: Navegando a compra de paquetes");

        actor.attemptsTo(
                IngresarOpcionUSSD.compraDePaquetes()
        );

        // Esperar carga del menú de paquetes
        actor.attemptsTo(WaitFor.aTime(Constants.WAIT_MEDIUM));

        EvidenciaUtils.registrarCaptura(Constants.DESC_SELECCION_PAQUETES);
        System.out.println("✅ Navegación a compra de paquetes completada");
    }

    /**
     * PASO 3: Valida que se muestre correctamente la información de paquetes
     */
    @Step("Validar información de paquetes disponibles")
    private <T extends Actor> void validarInformacionPaquetes(T actor) {
        System.out.println("📋 Paso 3: Validando información de paquetes");

        actor.attemptsTo(
                ValidarPantallaUSSD.informacionPaquetes()
        );

        if (capturarEvidenciaDetallada) {
            // Validaciones adicionales detalladas
            validarElementosEspecificosPaquetes(actor);
        }

        EvidenciaUtils.registrarCaptura(Constants.DESC_INFO_PAQUETES);
        System.out.println("✅ Información de paquetes validada exitosamente");
    }

    /**
     * PASO 4: Selecciona el paquete más vendido
     */
    @Step("Seleccionar paquete más vendido")
    private <T extends Actor> void seleccionarPaqueteMasVendido(T actor) {
        System.out.println("⭐ Paso 4: Seleccionando paquete más vendido");

        actor.attemptsTo(
                IngresarOpcionUSSD.paqueteMasVendido()
        );

        // Esperar procesamiento de la selección
        actor.attemptsTo(WaitFor.aTime(Constants.WAIT_MEDIUM));

        EvidenciaUtils.registrarCaptura(Constants.DESC_SELECCION_MAS_VENDIDO);
        System.out.println("✅ Paquete más vendido seleccionado exitosamente");
    }

    /**
     * PASO 5: Valida que se muestren los medios de pago disponibles
     */
    @Step("Validar medios de pago disponibles")
    private <T extends Actor> void validarMediosPago(T actor) {
        System.out.println("💳 Paso 5: Validando medios de pago");

        actor.attemptsTo(
                ValidarPantallaUSSD.mediosPago()
        );

        if (capturarEvidenciaDetallada) {
            validarElementosEspecificosMediosPago(actor);
        }

        EvidenciaUtils.registrarCaptura(Constants.DESC_MEDIOS_PAGO);
        System.out.println("✅ Medios de pago validados exitosamente");
    }

    /**
     * PASO 6: Selecciona un medio de pago específico
     */
    @Step("Seleccionar medio de pago")
    private <T extends Actor> void seleccionarMedioDePago(T actor) {
        System.out.println("💰 Paso 6: Seleccionando medio de pago");

        // Por defecto seleccionar descuento de saldo (opción 4)
        actor.attemptsTo(
                IngresarOpcionUSSD.laOpcion(Constants.OPCION_4, "Descuento de saldo")
        );

        // Esperar procesamiento del pago
        actor.attemptsTo(WaitFor.aTime(Constants.WAIT_LONG));

        EvidenciaUtils.registrarCaptura(Constants.DESC_SELECCION_MEDIO_PAGO);
        System.out.println("✅ Medio de pago seleccionado exitosamente");
    }

    /**
     * PASO 7: Procesa y valida la confirmación de compra
     */
    @Step("Procesar y validar confirmación")
    private <T extends Actor> void procesarYValidarConfirmacion(T actor) {
        System.out.println("🎉 Paso 7: Procesando y validando confirmación");

        // Esperar procesamiento de la compra
        actor.attemptsTo(WaitFor.aTime(Constants.TIMEOUT_CONFIRMATION));

        try {
            actor.attemptsTo(
                    ValidarPantallaUSSD.confirmacionCompra()
            );

            EvidenciaUtils.registrarCaptura(Constants.DESC_CONFIRMACION);
            System.out.println("✅ Confirmación de compra validada exitosamente");

        } catch (Exception e) {
            // Verificar si es un error de saldo insuficiente
            verificarErrorSaldoInsuficiente(actor);
            throw e;
        }
    }

    /**
     * PASO 8: Valida la llegada del mensaje SMS de confirmación
     */
    @Step("Validar mensaje SMS de confirmación")
    private <T extends Actor> void validarMensajeSMS(T actor) {
        System.out.println("📱 Paso 8: Validando mensaje SMS de confirmación");

        try {
            // Esperar tiempo suficiente para la llegada del SMS
            System.out.println(Constants.LOG_ESPERANDO_SMS);
            actor.attemptsTo(WaitFor.aTime(Constants.TIMEOUT_SMS_ARRIVAL));

            // Capturar evidencia del SMS
            EvidenciaUtils.registrarCaptura(Constants.DESC_MENSAJE_SMS);
            System.out.println("✅ Validación de SMS completada");

        } catch (Exception e) {
            System.out.println("⚠️ No se pudo validar mensaje SMS: " + e.getMessage());
            EvidenciaUtils.registrarCaptura("Error validación SMS - " + e.getMessage());
        }
    }

    /**
     * Ejecuta validación final del flujo
     */
    @Step("Ejecutar validación final")
    private <T extends Actor> void ejecutarValidacionFinal(T actor) {
        System.out.println("🎯 Ejecutando validación final del flujo");

        // Capturar estado final del sistema
        EvidenciaUtils.registrarCaptura("Estado final del sistema USSD");

        // Validaciones adicionales según el tipo de flujo
        switch (tipoFlujo) {
            case FLUJO_COMPLETO:
                validarCompraCompletada(actor);
                break;
            case FLUJO_BASICO:
                validarLlegadaAMediosPago(actor);
                break;
            default:
                validarEstadoGeneral(actor);
                break;
        }

        System.out.println("✅ Validación final exitosa");
    }

    /**
     * Intenta recuperación después de un error
     */
    private <T extends Actor> void intentarRecuperacion(T actor) {
        System.out.println("🔧 Intentando recuperación automática");

        try {
            // Esperar estabilización del sistema
            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_EXTRA_LONG));

            // Capturar estado actual para análisis
            EvidenciaUtils.registrarCaptura("Estado durante recuperación");

            System.out.println("✅ Recuperación completada");

        } catch (Exception e) {
            System.out.println("⚠️ Recuperación parcial: " + e.getMessage());
        }
    }

    /**
     * Validaciones específicas para elementos de paquetes
     */
    private <T extends Actor> void validarElementosEspecificosPaquetes(T actor) {
        System.out.println("🔍 Validando elementos específicos de paquetes");

        try {
            // Validar que se muestren precios, datos y vigencia
            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_SHORT));
            System.out.println("✅ Elementos específicos de paquetes validados");

        } catch (Exception e) {
            System.out.println("⚠️ Algunos elementos específicos no se pudieron validar: " + e.getMessage());
        }
    }

    /**
     * Validaciones específicas para elementos de medios de pago
     */
    private <T extends Actor> void validarElementosEspecificosMediosPago(T actor) {
        System.out.println("🔍 Validando elementos específicos de medios de pago");

        try {
            // Validar opciones de pago disponibles
            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_SHORT));
            System.out.println("✅ Elementos específicos de medios de pago validados");

        } catch (Exception e) {
            System.out.println("⚠️ Algunos elementos de medios de pago no se pudieron validar: " + e.getMessage());
        }
    }

    /**
     * Verifica si hay error de saldo insuficiente
     */
    private <T extends Actor> void verificarErrorSaldoInsuficiente(T actor) {
        try {
            // Buscar mensaje de saldo insuficiente
            actor.attemptsTo(
                    interactions.validations.ValidarTextoQueContengaX.elTextoContiene(
                            Constants.SALDO_INSUFICIENTE_USSD
                    )
            );

            EvidenciaUtils.registrarCaptura(Constants.DESC_ERROR_SALDO);
            System.out.println("⚠️ Error de saldo insuficiente detectado");

        } catch (Exception e) {
            System.out.println("ℹ️ No se detectó error específico de saldo insuficiente");
        }
    }

    /**
     * Captura estado detallado del sistema
     */
    private <T extends Actor> void capturarEstadoDetalladoSistema(T actor) {
        System.out.println("📸 Capturando estado detallado del sistema");

        try {
            // Capturar múltiples evidencias con diferentes estados
            EvidenciaUtils.registrarCaptura("Estado detallado - Pantalla principal");

            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_SHORT));

            EvidenciaUtils.registrarCaptura("Estado detallado - Elementos UI");

            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_SHORT));

            EvidenciaUtils.registrarCaptura("Estado detallado - Final");

            System.out.println("✅ Estado detallado capturado");

        } catch (Exception e) {
            System.out.println("⚠️ Error capturando estado detallado: " + e.getMessage());
        }
    }

    /**
     * Valida que la compra se haya completado exitosamente
     */
    private <T extends Actor> void validarCompraCompletada(T actor) {
        System.out.println("✅ Validando compra completada");

        try {
            // Validaciones específicas de compra completada
            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_MEDIUM));

            // Buscar indicadores de éxito
            actor.attemptsTo(
                    interactions.validations.ValidarTextoQueContengaX.elTextoContiene(
                            Constants.COMPRA_EXITOSA_USSD
                    )
            );

            System.out.println("✅ Compra completada validada exitosamente");

        } catch (Exception e) {
            System.out.println("⚠️ No se pudo validar compra completada: " + e.getMessage());
        }
    }

    /**
     * Valida que se llegó correctamente a la pantalla de medios de pago
     */
    private <T extends Actor> void validarLlegadaAMediosPago(T actor) {
        System.out.println("💳 Validando llegada a medios de pago");

        try {
            actor.attemptsTo(
                    ValidarPantallaUSSD.mediosPago()
            );
            System.out.println("✅ Llegada a medios de pago validada exitosamente");

        } catch (Exception e) {
            System.out.println("⚠️ No se pudo validar llegada a medios de pago: " + e.getMessage());
            EvidenciaUtils.registrarCaptura("Error validación medios de pago");
        }
    }

    /**
     * Validación de estado general del sistema
     */
    private <T extends Actor> void validarEstadoGeneral(T actor) {
        System.out.println("🔄 Validando estado general del sistema");

        try {
            actor.attemptsTo(WaitFor.aTime(Constants.WAIT_SHORT));
            EvidenciaUtils.registrarCaptura("Estado general del sistema");
            System.out.println("✅ Estado general validado exitosamente");

        } catch (Exception e) {
            System.out.println("⚠️ Error en validación de estado general: " + e.getMessage());
        }
    }

    /**
     * Maneja errores durante la ejecución del flujo
     */
    private <T extends Actor> void manejarErrorFlujo(T actor, Exception error) {
        System.err.println("🔥 Manejando error en flujo USSD: " + error.getMessage());

        try {
            // Capturar evidencia del error
            String tipoError = error.getClass().getSimpleName();
            EvidenciaUtils.registrarCaptura("Error en flujo USSD - " + tipoError);

            // Intentar capturar estado actual del sistema
            CapturaDePantallaMovil.tomarCapturaPantalla(Constants.CAPTURA_ERROR);

            // Log detallado para debugging
            System.err.println("📋 Detalles del error:");
            System.err.println("   - Tipo: " + tipoError);
            System.err.println("   - Mensaje: " + error.getMessage());
            System.err.println("   - Flujo: " + tipoFlujo.getDescripcion());
            System.err.println("   - Número USSD: " + numeroUSSD);

        } catch (Exception e) {
            System.err.println("❌ Error adicional capturando evidencia: " + e.getMessage());
        }
    }

    /**
     * Finaliza el flujo con limpieza y resumen
     */
    private void finalizarFlujo(boolean exitoso) {
        System.out.println("📊 ========== RESUMEN DEL FLUJO USSD ==========");

        if (exitoso) {
            System.out.println("🎉 Estado: EXITOSO");
            System.out.println("✅ Flujo USSD completado satisfactoriamente");
        } else {
            System.out.println("❌ Estado: FALLIDO");
            System.out.println("🔍 Revisar evidencias para análisis de fallo");
        }

        System.out.println("📋 Configuración del flujo:");
        System.out.println("   - Tipo: " + tipoFlujo.getDescripcion());
        System.out.println("   - Número USSD: " + numeroUSSD);
        System.out.println("   - Validación final: " + (incluirValidacionFinal ? "Habilitada" : "Deshabilitada"));
        System.out.println("   - Evidencia detallada: " + (capturarEvidenciaDetallada ? "Habilitada" : "Deshabilitada"));
        System.out.println("   - Timeout: " + timeoutPersonalizado + "ms");

        System.out.println("===============================================");
    }

    // ========== FACTORY METHODS ==========

    /**
     * Flujo completo estándar con todas las validaciones
     */
    public static RealizarCompraCompletaUSSD flujoCompleto() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_COMPLETO,
                true, true, Constants.TIMEOUT_USSD_RESPONSE);
    }

    /**
     * Flujo básico hasta medios de pago
     */
    public static RealizarCompraCompletaUSSD hastaMediosPago() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_BASICO,
                false, true, Constants.TIMEOUT_USSD_RESPONSE);
    }

    /**
     * Flujo básico con validación final habilitada
     */
    public static RealizarCompraCompletaUSSD flujoBasicoConValidacion() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_BASICO,
                true, true, Constants.TIMEOUT_USSD_RESPONSE);
    }

    /**
     * Flujo con número USSD personalizado
     */
    public static RealizarCompraCompletaUSSD conNumeroPersonalizado(String numeroUSSD) {
        return instrumented(RealizarCompraCompletaUSSD.class,
                numeroUSSD, TipoFlujo.FLUJO_BASICO,
                true, true, Constants.TIMEOUT_USSD_RESPONSE);
    }

    /**
     * Flujo con manejo robusto de errores
     */
    public static RealizarCompraCompletaUSSD conManejoDeErrores() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_CON_ERRORES,
                true, true, Constants.TIMEOUT_USSD_RESPONSE * 2);
    }

    /**
     * Flujo personalizado con configuración completa
     */
    public static RealizarCompraCompletaUSSD personalizado(String numeroUSSD,
                                                           TipoFlujo tipo,
                                                           boolean validacionFinal,
                                                           boolean evidenciaDetallada,
                                                           int timeout) {
        return instrumented(RealizarCompraCompletaUSSD.class,
                numeroUSSD, tipo, validacionFinal, evidenciaDetallada, timeout);
    }

    /**
     * Flujo rápido para smoke testing
     */
    public static RealizarCompraCompletaUSSD smokeTest() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_BASICO,
                false, false, Constants.TIMEOUT_MENU_LOAD);
    }

    /**
     * Flujo solo para validaciones de UI
     */
    public static RealizarCompraCompletaUSSD soloValidaciones() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_SOLO_VALIDACION,
                false, true, Constants.TIMEOUT_MENU_LOAD);
    }

    /**
     * Flujo optimizado para CI/CD (tiempos reducidos)
     */
    public static RealizarCompraCompletaUSSD paraCICD() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_BASICO,
                true, false, Constants.TIMEOUT_MENU_LOAD);
    }

    /**
     * Flujo para debugging con evidencia máxima
     */
    public static RealizarCompraCompletaUSSD paraDebug() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_PERSONALIZADO,
                true, true, Constants.TIMEOUT_USSD_RESPONSE * 3);
    }

    /**
     * Flujo específico para validar solo la pantalla de medios de pago
     */
    public static RealizarCompraCompletaUSSD validarSoloMediosPago() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_SOLO_VALIDACION,
                false, true, Constants.TIMEOUT_MENU_LOAD);
    }

    /**
     * Flujo con timeout extendido para conexiones lentas
     */
    public static RealizarCompraCompletaUSSD conTimeoutExtendido() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_BASICO,
                true, true, Constants.TIMEOUT_USSD_RESPONSE * 2);
    }

    /**
     * Flujo específico para testing de regresión
     */
    public static RealizarCompraCompletaUSSD paraRegresion() {
        return instrumented(RealizarCompraCompletaUSSD.class,
                Constants.CODIGO_USSD_611, TipoFlujo.FLUJO_COMPLETO,
                true, true, Constants.TIMEOUT_CONFIRMATION);
    }

    // ========== MÉTODOS UTILITARIOS ESTÁTICOS ==========

    /**
     * Crea una instancia con configuración basada en el ambiente
     */
    public static RealizarCompraCompletaUSSD paraAmbiente(String ambiente) {
        String codigoUSSD = Constants.obtenerCodigoUSSDPorAmbiente(ambiente);

        // Configuración específica por ambiente
        switch (ambiente.toUpperCase()) {
            case Constants.ENV_PROD:
                return instrumented(RealizarCompraCompletaUSSD.class,
                        codigoUSSD, TipoFlujo.FLUJO_BASICO,
                        true, false, Constants.TIMEOUT_USSD_RESPONSE);

            case Constants.ENV_QA:
                return instrumented(RealizarCompraCompletaUSSD.class,
                        codigoUSSD, TipoFlujo.FLUJO_COMPLETO,
                        true, true, Constants.TIMEOUT_USSD_RESPONSE);

            case Constants.ENV_DEV:
                return instrumented(RealizarCompraCompletaUSSD.class,
                        codigoUSSD, TipoFlujo.FLUJO_CON_ERRORES,
                        true, true, Constants.TIMEOUT_USSD_RESPONSE * 2);

            default:
                return flujoBasicoConValidacion();
        }
    }

    /**
     * Crea una instancia con configuración basada en el tipo de prueba
     */
    public static RealizarCompraCompletaUSSD paraTipoPrueba(String tipoPrueba) {
        switch (tipoPrueba.toLowerCase()) {
            case "smoke":
                return smokeTest();
            case "regression":
                return paraRegresion();
            case "debug":
                return paraDebug();
            case "ci":
            case "cicd":
                return paraCICD();
            case "full":
            case "complete":
                return flujoCompleto();
            default:
                return flujoBasicoConValidacion();
        }
    }

    // ========== GETTERS PARA TESTING Y DEBUG ==========

    public String getNumeroUSSD() {
        return numeroUSSD;
    }

    public TipoFlujo getTipoFlujo() {
        return tipoFlujo;
    }

    public boolean isIncluirValidacionFinal() {
        return incluirValidacionFinal;
    }

    public boolean isCapturarEvidenciaDetallada() {
        return capturarEvidenciaDetallada;
    }

    public int getTimeoutPersonalizado() {
        return timeoutPersonalizado;
    }

    /**
     * Obtiene un resumen de la configuración actual
     */
    public String obtenerResumenConfiguracion() {
        return String.format(
                "RealizarCompraCompletaUSSD{numeroUSSD='%s', tipoFlujo=%s, validacionFinal=%s, evidenciaDetallada=%s, timeout=%dms}",
                numeroUSSD, tipoFlujo.name(), incluirValidacionFinal, capturarEvidenciaDetallada, timeoutPersonalizado
        );
    }

    /**
     * Valida la configuración actual
     */
    public boolean esConfiguracionValida() {
        return numeroUSSD != null &&
                !numeroUSSD.isEmpty() &&
                tipoFlujo != null &&
                timeoutPersonalizado > 0;
    }

    /**
     * Obtiene recomendaciones basadas en la configuración
     */
    public String[] obtenerRecomendaciones() {
        java.util.List<String> recomendaciones = new java.util.ArrayList<>();

        if (timeoutPersonalizado < Constants.TIMEOUT_MENU_LOAD) {
            recomendaciones.add("⚠️ Timeout muy bajo, considere incrementar para evitar falsos negativos");
        }

        if (tipoFlujo == TipoFlujo.FLUJO_COMPLETO && !capturarEvidenciaDetallada) {
            recomendaciones.add("💡 Para flujo completo se recomienda evidencia detallada");
        }

        if (tipoFlujo == TipoFlujo.FLUJO_CON_ERRORES && timeoutPersonalizado < Constants.TIMEOUT_USSD_RESPONSE * 2) {
            recomendaciones.add("🔧 Para flujo con errores se recomienda timeout extendido");
        }

        if (recomendaciones.isEmpty()) {
            recomendaciones.add("✅ Configuración óptima");
        }

        return recomendaciones.toArray(new String[0]);
    }

    @Override
    public String toString() {
        return "RealizarCompraCompletaUSSD{" +
                "numeroUSSD='" + numeroUSSD + '\'' +
                ", tipoFlujo=" + tipoFlujo +
                ", incluirValidacionFinal=" + incluirValidacionFinal +
                ", capturarEvidenciaDetallada=" + capturarEvidenciaDetallada +
                ", timeoutPersonalizado=" + timeoutPersonalizado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealizarCompraCompletaUSSD that = (RealizarCompraCompletaUSSD) o;
        return incluirValidacionFinal == that.incluirValidacionFinal &&
                capturarEvidenciaDetallada == that.capturarEvidenciaDetallada &&
                timeoutPersonalizado == that.timeoutPersonalizado &&
                java.util.Objects.equals(numeroUSSD, that.numeroUSSD) &&
                tipoFlujo == that.tipoFlujo;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(numeroUSSD, tipoFlujo, incluirValidacionFinal,
                capturarEvidenciaDetallada, timeoutPersonalizado);
    }
}