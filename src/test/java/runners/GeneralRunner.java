package runners;

import cucumber.api.CucumberOptions;
import cucumber.api.SnippetType;
import hooks.OllamaStepListener;
import net.thucydides.core.steps.StepEventBus;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import utils.BeforeSuite;

import java.io.IOException;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepDefinitions", "utils", "hooks"},
        plugin = {"pretty"},
        snippets = SnippetType.CAMELCASE,
        tags = "@USSD_001"
)

@RunWith(CustomRunner.class)
public class GeneralRunner {
    @BeforeSuite
    public static void setUp() throws InvalidFormatException, IOException {
        System.out.println("🚀 Iniciando configuración para pruebas USSD...");
        System.out.println("✅ Configuración USSD completada");
    }
    @BeforeClass
    public static void registerListeners() {
        StepEventBus.getEventBus().registerListener(new OllamaStepListener());
        System.out.println("🔗 OllamaStepListener registrado correctamente");
    }
}