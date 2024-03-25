- Copilot
```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Uso: java HtmlAnalyzer <URL>");
            System.exit(1);
        }

        String url = args[0]; // A URL a ser analisada

        try {
            // Baixe o conteúdo HTML da URL
            URLConnection connection = new URL(url).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder htmlContent = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line);
            }
            reader.close();

            // Analise o HTML para encontrar o trecho de texto desejado
            String deepestText = findDeepestText(htmlContent.toString());
            System.out.println(deepestText);
        } catch (IOException e) {
            System.err.println("Erro ao acessar a URL: " + e.getMessage());
        }
    }

    private static String findDeepestText(String html) {
        // Remova espaços iniciais e linhas em branco
        html = html.trim();

        // Verifique se o HTML está bem-formado
        if (!html.startsWith("<html>") || !html.endsWith("</html>")) {
            return "malformed HTML";
        }

        // Encontre o primeiro trecho de texto dentro das tags <title> ou <body>
        int titleStart = html.indexOf("<title>");
        int titleEnd = html.indexOf("</title>");
        int bodyStart = html.indexOf("<body>");
        int bodyEnd = html.indexOf("</body>");

        if (titleStart != -1 && titleEnd != -1) {
            return html.substring(titleStart + 7, titleEnd).trim();
        } else if (bodyStart != -1 && bodyEnd != -1) {
            return html.substring(bodyStart + 6, bodyEnd).trim();
        } else {
            return "malformed HTML";
        }
    }
}

```

- GPT
```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlAnalyzer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java HtmlAnalyzer <URL>");
            return;
        }

        String urlStr = args[0];

        try {
            String htmlContent = fetchHtml(urlStr);
            if (htmlContent == null) {
                System.out.println("URL connection error");
                return;
            }

            String deepestText = findDeepestText(htmlContent);
            System.out.println(deepestText);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String fetchHtml(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.connect();

        StringBuilder htmlContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                htmlContent.append(line).append("\n");
            }
        }
        return htmlContent.toString();
    }

    private static String findDeepestText(String htmlContent) {
        String[] lines = htmlContent.split("\n");
        String deepestText = null;
        int maxDepth = 0;
        int currentDepth = 0;

        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;

            if (line.startsWith("</")) {
                currentDepth--;
            } else if (!line.startsWith("<")) {
                if (currentDepth == maxDepth && (deepestText == null || deepestText.isEmpty())) {
                    deepestText = line;
                }
            } else {
                currentDepth++;
                if (currentDepth > maxDepth) {
                    maxDepth = currentDepth;
                    deepestText = null;
                }
            }
        }

        if (maxDepth == 0) {
            return "malformed HTML";
        } else {
            return deepestText.trim();
        }
    }
}
```

https://github.com/mozca33/Axur/blob/main/AxurChallenge.java
