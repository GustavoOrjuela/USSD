package hooks;

import net.thucydides.core.steps.StepEventBus;
import cucumber.api.java.Before;

public class RegisterOllamaListener {

    private static boolean registered = false;

    @Before(order = 0)
    public void register() {
        if (!registered) {
            StepEventBus.getEventBus().registerListener(new OllamaStepListener());
            System.out.println("🔗 OllamaStepListener ACTIVADO desde Hook");
            registered = true;
        }
    }
}
