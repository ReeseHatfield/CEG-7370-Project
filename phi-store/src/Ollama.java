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

    public String prompt(String userPrompt) throws Exception {
        // String lineOfOutput = "";
        // String outputLines = "";

        // System.out.println("userprompt:" + userPrompt);

        // writer.write(userPrompt + "\n");
        // writer.flush();

        // System.out.println("got here");

        // System.out.println(this.ollamaProcess);

        // // while ((lineOfOutput = reader.readLine()) != null) {
        // //     System.out.println(lineOfOutput);

        // //     outputLines += lineOfOutput;

        // //     if (lineOfOutput.contains(">>>")) break;
        // // }
        // //

        // System.out.println("leaving prompt()");
        // System.out.println(outputLines);
        //

        // assuming ollama has been served + port is set to default

        int port = 11434;

        URL ollamaURL = new URI(
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

        return resp.toString();
    }
}
