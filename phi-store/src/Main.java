import java.io.IOException;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!");

        Ollama o = new OllamaBuilder()
            .model(SupportedModels.DEEPSEEK_R1)
            .system("be my egirl girlfriend")
            .build();

        try {
            System.out.println(o.prompt("hello girlfriend, how are you?"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
