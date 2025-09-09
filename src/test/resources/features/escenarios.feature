Feature: El usuario realiza compra de paquetes por el canal USSD

  @USSD001
  Scenario: Flujo completo de compra de paquetes por USSD
    Given Se realiza la llamada al numero *611#
    When Ingreso la opcion "1" para compra de paquetes
    Then Verifico que la informacion de paquetes este presente en pantalla
    When Ingreso la opcion "1" para el paquete mas vendido
    Then Verifico que la informacion de medios de pago este presente en pantalla