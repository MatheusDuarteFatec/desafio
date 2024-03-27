```
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HtmlAnalyzer {
    public static void main(String[] args) {
        try {
            // Define the URL to fetch HTML content from
            String url = "http://hiring.axreng.com/internship/example5.html";
                        //args[0].toString()

            // Fetch HTML content from the URL
            String html = fetchHtml(url);

            // Extract and print the deepest content within HTML tags
            String content = getContent(html);
            System.out.println(content);
        } catch (IOException e) {
            // Handle URL connection errors
            System.err.println("URL connection error");
        }
    }

    // Method to fetch HTML content from a given URL
    private static String fetchHtml(String url) throws IOException {
        // Open a connection to the URL
        URLConnection connection = new URL(url).openConnection();

        // Get input stream from the connection
        InputStream inputStream = connection.getInputStream();

        // Create a reader to read from the input stream
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        // Read HTML content line by line and append to StringBuilder
        StringBuilder htmlContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            htmlContent.append(line).append("\n");
        }

        // Close the reader
        reader.close();

        // Return the HTML content as a string
        return htmlContent.toString();
    }

    // Method to extract content within the deepest HTML tag
    private static String getContent(String html) {
        // Find the deepest HTML tag within the content
        String deepestTag = getDeepestTag(html);

        // Find the beginning and ending indices of the content within the deepest tag
        int begin = html.indexOf("<" + deepestTag + ">") + deepestTag.length() + 2;
        int end = html.indexOf("</" + deepestTag + ">");

        // Extract and return the content within the deepest tag
        return html.substring(begin, end).trim();
    }

    // Method to find the deepest HTML tag within the content
    private static String getDeepestTag(String html) {
        int maxDeep = 0;  // Track the maximum depth of nested tags
        String deepestTag = "";  // Track the name of the deepest tag
        int depth = 0;  // Track the current depth of nested tags

        // Iterate through each character in the HTML content
        for (int i = 0; i < html.length(); i++) {
            // Check if the character is the beginning of an HTML tag
            if (html.charAt(i) == '<') {
                int j = i + 1;
                boolean isTagClosed = false;

                // Check if the tag is a closing tag
                if (html.charAt(j) == '/') {
                    j++;
                    isTagClosed = true;
                }

                // Extract the tag name
                StringBuilder stringBuilder = new StringBuilder();
                while (j < html.length() && html.charAt(j) != '>') {
                    stringBuilder.append(html.charAt(j));
                    j++;
                }

                String tagName = stringBuilder.toString();

                // Update the depth based on whether the tag is opening or closing
                if (!isTagClosed) {
                    depth++;
                    if (depth > maxDeep) {
                        maxDeep = depth;
                        deepestTag = tagName;
                    }
                } else {
                    depth--;
                }
                i = j;
            }
        }

        // Return the name of the deepest HTML tag
        return deepestTag;
    }
}
```

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

```
import java.net.URL; // Open connections to internet resources using URLs to read or record data
import java.net.URLConnection;  // Open the good connection doors(HTTP) using URLs
import java.io.InputStream; // Convert byte streams to character streams
import java.util.Scanner; // Read input information
import java.net.MalformedURLException; // Error : Invalid URLs
import java.io.IOException;  // Error: Input/output error while reading or writing

public class HtmlAnalyzer {
	public static void main(String[] args) {
		// Check if the correct number of arguments have been provided in the command line
		if (args.length == 1) {
			String urlString = args[0];
			try {
				URL url = new URL(urlString);
				URLConnection connection = url.openConnection();
				connection.connect();
	
				InputStream inputStream = connection.getInputStream();
				Scanner scanner = new Scanner(inputStream);

				int max_deep = 0;
				int current_deep = 0;
				String deep_text = "";
				StringBuilder currentText = new StringBuilder();

				// Read line by line from the web page 
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine().trim();

					if (line.isEmpty()) {
						continue;
					}
					// Check the prefix of tag </>
					if (line.startsWith("</")) {
						current_deep--;
						if (current_deep == max_deep) {
							if (deep_text.isEmpty()) {
								deep_text = currentText.toString().trim();
							}
							currentText = new StringBuilder();
						}
					} else if (line.startsWith("<")) {
						current_deep++;
					} else {
						currentText.append(line).append(" ");
						if (current_deep > max_deep) {
  							max_deep = current_deep;
							deep_text = currentText.toString().trim();
							currentText = new StringBuilder();
						}
					}
				}
				scanner.close();

				if (deep_text.isEmpty())
					System.exit(1);

				System.out.println(deep_text);
			} catch (MalformedURLException e) {
				System.out.print("malformed HTML");
				System.exit(1);
			} catch (IOException e) {
				System.out.print("URL connection error");
				System.exit(1);
			}
		} else {
			System.exit(1);
		}
	}
}
```
