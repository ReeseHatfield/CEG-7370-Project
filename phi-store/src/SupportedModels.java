public enum SupportedModels {
    DEEPSEEK_R1("deepseek-r1"),
    GPT2("mapler/gpt2");

    private final String modelName;

    SupportedModels(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return modelName;
    }
}
