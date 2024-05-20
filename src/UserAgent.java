public class UserAgent {
    private String userAgentFullData;// Mozilla/5.0 (compatible; SemrushBot/7~bl; +http://www.semrush.com/bot.html)
    private String browser;// Mozilla)
    private String botName;//  SemrushBot
    private String operationSystem;// 7~bl


    public UserAgent(String userAgentFullData) {
        parseLogLineToLogEntryClass(userAgentFullData);
    }

    public String getUserAgentFullData() {
        return userAgentFullData;
    }


    public String getBrowser() {
        return browser;
    }

    private void setBrowser(String browser) {
        this.browser = browser;
    }


    public String getBotName() {
        return botName;
    }

    private void setBotName(String botName) {
        this.botName = botName;
    }

    public String getOperationSystem() {
        return operationSystem;
    }

    private void setOperationSystem(String operationSystem) {
        this.operationSystem = operationSystem;
    }


    public static boolean checkUserAgent(String fragment, String botName) {
        return fragment.equals(botName);
    }

    private void parseLogLineToLogEntryClass(String userAgentFullData) {
        String[] lineParts = userAgentFullData.split(" ");
        if (lineParts.length >= 2 && lineParts.length <= 5) {
            setBotName(extractBotNameFromUserAgent(userAgentFullData));

        } else if (lineParts.length >= 11) {
            setBrowser(lineParts[1].substring(1, lineParts[1].length() - 1));
            setBotName(extractBotNameFromUserAgent(userAgentFullData));
            setOperationSystem(extractOperationSystemFromUserAgent(userAgentFullData));

        }

    }

    public static String extractBotNameFromUserAgent(String fragment) {
        String botName;
        String[] parts = fragment.split(" ");
        for (int i = 0; i < parts.length; i++) {

            if (parts[i].startsWith("(compatible;")) {
                if (parts[i + 1] != null) {
                    String[] botTypeAndVersion = parts[i + 1].split("/");
                    botName = botTypeAndVersion[0].trim();
                    return botName;
                }
            }
        }
        return "-";
    }

    public static String extractOperationSystemFromUserAgent(String fragment) {
        String operationSystem;
        String[] parts = fragment.split(" ");
        for (int i = 0; i < parts.length; i++) {

            if (parts[i].startsWith("Gecko)")) {
                if (i + 1 <= parts.length - 1 && parts[i + 1] != null) {///////
                    String[] botTypeAndVersion = parts[i + 1].split("/");
                    operationSystem = botTypeAndVersion[0].trim();
                    return operationSystem;
                }
            }
        }
        return "-";
    }

    public static String extractBrowserNameFromUserAgent(String fragment) {
        String browser;
        String[] parts = fragment.split("\"");
        browser = parts[0];
        return browser;
    }

    @Override
    public String toString() {
        return "logs.UserAgent{" +
                "browser='" + browser +
                ", botName='" + botName + '\'' +
                ", operationSystem='" + operationSystem + '\'' +
                '}';
    }

}