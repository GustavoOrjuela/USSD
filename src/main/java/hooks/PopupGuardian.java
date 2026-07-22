package hooks;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import utils.MyDriver;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Guardian que detecta y cierra popups disruptivos en cualquier momento:
 * - Durante la ejecución del escenario (@Before/@After no alcanzan)
 * - Hasta 30 segundos después del @After (delay natural del popup SIM Claro)
 *
 * Solución al problema del timeout: usa implicitlyWait(0) exclusivamente
 * en el contexto del guardian, restaurándolo antes de devolver control al driver.
 *
 * Sincronización: ReentrantLock evita colisión con el thread principal de Appium.
 */
public class PopupGuardian {

    // ── Lock compartido: el guardian y los steps comparten el mismo driver ──
    public static final ReentrantLock DRIVER_LOCK = new ReentrantLock();

    private static ScheduledExecutorService scheduler;
    private static ScheduledFuture<?> tareaActiva;

    /** Intervalo de polling del guardian (segundos) */
    private static final int INTERVALO_SEGUNDOS = 1;

    /** Cuántos segundos mantener el guardian activo después del @After */
    private static final int SEGUNDOS_POST_AFTER = 35;

    // ─────────────────────────────────────────────────────────────────
    // Ciclo de vida: @Before inicia el guardian, @After lo mantiene
    // activo 35s más para cubrir el delay tardío del popup
    // ─────────────────────────────────────────────────────────────────

    @Before(order = 5)
    public void iniciarGuardian() {
        if (scheduler == null || scheduler.isShutdown()) {
            scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
                Thread t = new Thread(r, "PopupGuardian");
                t.setDaemon(true);
                return t;
            });
        }

        cancelarTareaActiva();

        tareaActiva = scheduler.scheduleAtFixedRate(
                PopupGuardian::detectarYCerrarPopups,
                1,
                INTERVALO_SEGUNDOS,
                TimeUnit.SECONDS
        );

        System.out.println("🛡️ [PopupGuardian] Iniciado — polling cada "
                + INTERVALO_SEGUNDOS + "s");
    }

    @After(order = 100)
    public void extenderGuardianPostEscenario() {
        // Cancelar tarea anterior y reprogramar para cubrir los 35s post-ejecución
        cancelarTareaActiva();

        tareaActiva = scheduler.scheduleAtFixedRate(
                PopupGuardian::detectarYCerrarPopups,
                0,
                INTERVALO_SEGUNDOS,
                TimeUnit.SECONDS
        );

        // Programar la parada automática tras SEGUNDOS_POST_AFTER
        scheduler.schedule(() -> {
            cancelarTareaActiva();
            System.out.println("🛡️ [PopupGuardian] Detenido tras "
                    + SEGUNDOS_POST_AFTER + "s post-escenario");
        }, SEGUNDOS_POST_AFTER, TimeUnit.SECONDS);

        System.out.println("🛡️ [PopupGuardian] Extendido " + SEGUNDOS_POST_AFTER
                + "s post-escenario para capturar popup tardío");
    }

    // ─────────────────────────────────────────────────────────────────
    // Lógica de detección — se ejecuta en el thread del guardian
    // ─────────────────────────────────────────────────────────────────

    private static void detectarYCerrarPopups() {
        // Intentar adquirir el lock sin bloquear:
        // si el thread principal está usando el driver, se omite este ciclo
        if (!DRIVER_LOCK.tryLock()) {
            return;
        }

        AndroidDriver driver = MyDriver.get();
        if (driver == null) {
            DRIVER_LOCK.unlock();
            return;
        }

        try {
            // Sin implicit wait → findElements retorna INMEDIATAMENTE si no hay popup
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);

            cerrarSimClaro(driver);
            cerrarIniciarExplorador(driver);
            cerrarErrorConexion(driver);

        } catch (Exception e) {
            // Silencioso: el guardian nunca interrumpe la ejecución
        } finally {
            // Siempre restaurar el implicit wait original antes de soltar el lock
            try {
                driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
            } catch (Exception ignored) {}

            DRIVER_LOCK.unlock();
        }
    }

    // ── Detectores individuales ────────────────────────────────────────

    private static void cerrarSimClaro(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Continua la compra de tus productos Claro')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("🛡️ [PopupGuardian] Popup 'SIM Claro' cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

    private static void cerrarIniciarExplorador(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Iniciar el explorador')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Cancelar']")).click();
                System.out.println("🛡️ [PopupGuardian] Popup 'Iniciar explorador' cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

    private static void cerrarErrorConexion(AndroidDriver driver) {
        try {
            if (!driver.findElements(By.xpath(
                    "//*[contains(@text,'Problema de conexión o código')]")).isEmpty()) {
                driver.findElement(By.xpath("//*[@text='Aceptar']")).click();
                System.out.println("🛡️ [PopupGuardian] Error conexión/MMI cerrado");
            }
        } catch (Exception e) { /* Silencioso */ }
    }

    // ─────────────────────────────────────────────────────────────────
    // Utilidades internas
    // ─────────────────────────────────────────────────────────────────

    private static void cancelarTareaActiva() {
        if (tareaActiva != null && !tareaActiva.isCancelled()) {
            tareaActiva.cancel(false); // false = no interrumpir si está corriendo
        }
    }
}