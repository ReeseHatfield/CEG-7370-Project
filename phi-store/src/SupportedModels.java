public enum SupportedModels {
    DEEPSEEK_R1("deepseek-r1"),
    GPT2("mapler/gpt2"),
    LLAMA3DOT2("hf.co/prithivMLmods/Llama-3.2-1B-GGUF");

    private final String modelName;

    SupportedModels(String modelName) {
        this.modelName = modelName;
    }

    @Override
    public String toString() {
        return modelName;
    }
}
