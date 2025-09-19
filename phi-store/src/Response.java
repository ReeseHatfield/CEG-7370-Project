import java.util.ArrayList;
import java.util.List;

class Response {
    private final String[] chunks;

    private Response(String[] chunks) {
        this.chunks = chunks;
    }

    
    public static Response fromRaw(String raw) {


        List<String> parts = new ArrayList<>();
        int idx = 0;
        while (true) {
            // horrible breaking of chunks
            // This could be done a lot easier, but I don't wanna pull in 
            // GSON or any other dependency right now
            int start = raw.indexOf("\"response\":\"", idx);

            if (start == -1){
                break;
            }


            start += "\"response\":\"".length();
            int end = raw.indexOf("\"", start);


            String value = raw.substring(start, end);
            parts.add(unescape(value));

            idx = end + 1;
        }


        return new Response(parts.toArray(new String[0]));
    }

    public String getFullResponse() {
        StringBuilder sb = new StringBuilder();

        for (String c : chunks){
            sb.append(c);
        }

        return sb.toString();

    }

    // kill all escapes
    private static String unescape(String s) {
        return s.replace("\\n", "\n")
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}