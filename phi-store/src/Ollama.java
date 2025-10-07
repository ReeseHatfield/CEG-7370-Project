import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Ollama {

    private Process ollamaProcess;
    private BufferedWriter writer;
    private BufferedReader reader;
    // psuedo system prompt
    private String systemPrompt;
    private String model;

    public Ollama(OllamaBuilder ollamaToBeBuilt) throws Exception {
        this.model = ollamaToBeBuilt.getCurrentModel().toString();
        try {
            ollamaProcess = new ProcessBuilder(
                Arrays.asList("ollama", "serve")
            ).start();

            writer = new BufferedWriter(
                new OutputStreamWriter(ollamaProcess.getOutputStream())
            );

            reader = new BufferedReader(
                new InputStreamReader(ollamaProcess.getInputStream())
            );

            // this.prompt(ollamaToBeBuilt.getCurrentPrompt());
            this.systemPrompt = ollamaToBeBuilt.getCurrentPrompt();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Response prompt(String userPrompt) throws Exception {




        StringBuilder promptBuilder = new StringBuilder();

        // this is not technically how system prompts *really* work
        // but I think implementing a real context system is beyond the scope of this project
        promptBuilder.append(this.systemPrompt);
        promptBuilder.append("\n");
        promptBuilder.append(userPrompt);

        String prompt = promptBuilder.toString();


        int port = 11434; // default ollama port

        URL ollamaURL;
        ollamaURL = new URI(
            "http://localhost:" + port + "/api/generate"
        ).toURL();

        HttpURLConnection conn = (HttpURLConnection) ollamaURL.openConnection();

        conn.setRequestMethod("POST"); // this should be a post, surprisingly worked before
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String safePrompt = prompt
            .replace("\\", "\\\\")  // escape backslashes first
            .replace("\"", "\\\"")  // escape quotes
            .replace("\n", "\\n");  // escape newlines

        String json = String.format(
            "{\"model\":\"%s\",\"prompt\":\"%s\",\"stream\":false}",
            model,
            safePrompt
        );

        System.out.println("Sending to Ollama:\n" + json);


        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
            os.flush();
        }

        StringBuilder resp = new StringBuilder();
        BufferedReader in = new BufferedReader(
            new InputStreamReader(conn.getInputStream())
        );

        try {
            String line;

            while ((line = in.readLine()) != null) {
                resp.append(line);
            }
        } catch (Exception e) {}

        return Response.fromRaw(resp.toString());
    }
}
