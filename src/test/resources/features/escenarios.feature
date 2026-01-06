Feature: El usuario realiza compra de paquetes por el canal USSD

  @USSD_001 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - El mas vendido
    Given Se realiza la llamada al numero *611#
    When  Valida Menu Inicio
    When Ingreso la opcion "1" para compra de paquetes
    Then Valida Menu Compra De Paquetes
    When Ingreso la opcion "1" para el paquete mas vendido
    Then Valida Menu Medios De Pago De Paquetes El Mas vendido



  @USSD_002 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes  de datos
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "2" para paquete de datos
    And Valida Menu Op De Paquetes De Datos
    And Ingreso la opcion "1" para medios de pago
    And Valida Menu Medios De Pago De Paquetes De Datos


  @USSD_003 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes todo incluido 1
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "3" para paquetes todo incluido
    And Valida Menu Paquetes Todo Incluido
    And Ingreso la opcion "1" para medios de pago
    And Valida Menu Medios De Pago De Paquetes Todo Incluido

  @USSD_004 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes todo incluido 2
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "3" para paquetes todo incluido
    And Valida Menu Paquetes Todo Incluido
    And Ingreso la opcion "9" para ver mas paquetes todo incluido
    And Valida Menu Mas Paquetes Todo Incluido
    And Ingreso la opcion "1" para ver medios de pago
    And Valida Menu Medios De Pago De Paquetes Todo Incluido Con PSE

  @USSD_005 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes todo incluido 3
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "3" para paquetes todo incluido
    And Valida Menu Paquetes Todo Incluido
    And Ingreso la opcion "9" para ver mas paquetes todo incluido
    And Valida Menu Mas Paquetes Todo Incluido
    And Ingreso la opcion "9" para ver mas mas paquetes todo incluido
    And Valida Menu Mas Mas Paquetes Todo Incluido
    And Ingreso la opcion "1" para ver medios de pago
    And Valida Menu Medios De Pago De Paquetes Todo Incluido Con PSE

  @USSD_006 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes todo incluido 4
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "9" para paquetes todo incluido
    And Valida Menu Paquetes Especiales Comunidad
    And Ingreso la opcion "1" para paquetes de relevo
    And Valida Menu Paquetes Relevo Comunidad
    And Ingreso la opcion "1" para medios de pago de paquetes de relevo
    And Valida Menu Medios De Pago De Paquetes Todo Incluido Con PSE

  @USSD_007 @Compra_De_Paquetes @USSDPRE
  Scenario: Compra de paquetes - paquetes todo incluido 5
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "1" para compra de paquetes
    And Valida Menu Compra De Paquetes
    And Ingreso la opcion "9" para paquetes todo incluido
    And Valida Menu Paquetes Especiales Comunidad
    And Ingreso la opcion "2" para paquetes de Voz
    And Valida Menu Paquetes Voz
    And Ingreso la opcion "1" para medios de pago
    And Valida Menu Medios De Pago De Paquetes De Datos
    Then Cancelar

  @USSD_08 @Recargas @USSDPRE
  Scenario: Compra de recargas - paquetes todo incluido
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "2" para compra de recargas
    And Valida Menu Recargas
    And Ingreso la opcion "1" para paquetes todo incluido
    And Valida Menu Medios De Pago Recarga
    Then Cancelar

  @USSD_09 @Recargas @USSDPRE
  Scenario: Compra de recargas - mas opciones de recargas 1
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "2" para compra de recargas
    And Valida Menu Recargas
    And Ingreso la opcion "9" para mas opciones de recargas
    And Valida Menu Recargas Mas
    And Ingreso la opcion "1" para medios de pago
    And Valida Menu Medios De Pago Recarga Con Tarjeta
    Then Cancelar

  @USSD_010 @Recargas @USSDPRE
  Scenario: Compra de recargas - mas opciones de recargas 2
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "2" para compra de recargas
    And Valida Menu Recargas
    And Ingreso la opcion "9" para mas opciones de recargas
    And Valida Menu Recargas Mas
    And Ingreso la opcion "9" para mas opciones de recargas
    And Valida Menu Recarga Valores Altos
    And Ingreso la opcion "1" para medios de pago
    And Valida Menu Medios De Pago Recarga Con PSE
    Then Cancelar


  @USSD_011 @ConsultaDeSaldosYConsumos @USSDPRE
  Scenario: Consulta de saldos y consumos ver saldo y comprar paquete y recargas
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "3" para consulta de saldo y consumos
    And Valida Menu Beneficios 3x1
    And Ingreso la opcion "1" para ver saldo y comprar paquete y recargas
    And Valida Menu Beneficios 3x1 Consulta
    And Ingreso la opcion "1" para comprar tu paquete
    And Valida Menu Principal Paquetes
    Then Cancelar

  @USSD_012 @ConsultaDeSaldosYConsumos @USSDPRE
  Scenario: Consulta de saldos y consumos mas opciones de recarga
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "3" para consulta de saldo y consumos
    And Valida Menu Beneficios 3x1
    And Ingreso la opcion "1" para ver saldo y comprar paquete y recargas
    And Valida Menu Beneficios 3x1 Consulta
    And Ingreso la opcion "2" para opciones de recarga
    And Valida Menu Recargas
    And Ingreso la opcion "9" para mas opciones de recarga
    And Valida Menu Recargas Mas
    And Ingreso la opcion "9" para opciones de recarga altos
    And Valida Menu Recarga Valores Altos
    Then Cancelar

  @USSD_013 @ConsultaDeSaldosYConsumos @USSDPRE
  Scenario: Consulta de saldos y consumos - Ver opciones de paquetes despues de conusltar saldo
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "3" para consulta de saldo y consumos
    And Valida Menu Beneficios 3x1
    And Ingreso la opcion "1" para ver saldo y comprar paquete y recargas
    And Valida Menu Beneficios 3x1 Consulta
    And Ingreso la opcion "2" para opciones de recarga
    And Valida Menu Recargas
    And Ingreso la opcion "9" para mas opciones de recarga
    And Valida Menu Recargas Mas
    And Ingreso la opcion "9" para opciones de recarga altos
    And Valida Menu Recarga Valores Altos
    Then Cancelar

  @USSD_014 @ConsultaDeSaldosYConsumos @USSDPRE
  Scenario: Consulta de saldos y consumos - Consultar consumo
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "3" para consulta de saldo y consumos
    And Valida Menu Beneficios 3x1
    And Ingreso la opcion "2" para consultar si hay un paquete en consumo
    And Valida Menu Detalle Si no Hay Un Paquete
    Then Cancelar
    #And Ingreso la opcion "1" para consultar consumo de paquete y vigencia
    #And Valida Menu Detalle De Consumo

  @USSD_015 @ActiavionDeLinea @USSDPRE
  Scenario: Activacion de Linea
    Given Se realiza la llamada al numero *611#
    When Valida Menu Inicio
    And Ingreso la opcion "4" para activacion de linea
    And Valida Menu Autorizacion Datos
    And Ingreso la opcion "1" para aceptar y ver tipos de documento
    And Valida Menu Tipo Documento
    Then Cancelar