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
            String url = args[0].toString();
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