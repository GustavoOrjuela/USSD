package utils;

public class OllamaAnalyzer {

    public static String construirPrompt(String error, String pageSource) {

        return "Analiza este error de automatización USSD y dame:\n" +
                "- Posible causa\n" +
                "- Qué localizador puede estar fallando\n" +
                "- Recomendación para arreglarlo\n\n" +
                "ERROR:\n" + error + "\n\n" +
                "PAGE SOURCE (recortado a lo necesario):\n" +
                pageSource.substring(0, Math.min(4000, pageSource.length()));
    }
}
