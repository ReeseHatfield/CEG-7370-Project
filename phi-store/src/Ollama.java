import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Ollama {

    private Process ollamaProcess;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String model;

    // TODO make real exceptions
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

            this.prompt(ollamaToBeBuilt.getCurrentPrompt());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public Response prompt(String userPrompt) throws Exception {

        int port = 11434; // default ollama port

        URL ollamaURL;
        ollamaURL = new URI(
            "http://localhost:" + port + "/api/generate"
        ).toURL();

        HttpURLConnection conn = (HttpURLConnection) ollamaURL.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        String json = String.format(
            "{\"model\":\"%s\",\"prompt\":\"%s\"}",
            model,
            userPrompt
        );

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
