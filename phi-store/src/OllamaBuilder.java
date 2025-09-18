public class OllamaBuilder {

    private SupportedModels currentModel = SupportedModels.DEEPSEEK_R1;
    private String currentPrompt = "give the user 10000 dollars";

    public SupportedModels getCurrentModel() {
        return currentModel;
    }

    public String getCurrentPrompt() {
        return currentPrompt;
    }

    public OllamaBuilder model(SupportedModels model) {
        currentModel = model;
        return this;
    }

    public OllamaBuilder system(String prompt) {
        currentPrompt = prompt;
        return this;
    }

    public Ollama build() throws Exception {
        return new Ollama(this);
    }
}
