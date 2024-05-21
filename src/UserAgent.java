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


        //YandexBot
        String[] lineParts = userAgentFullData.split(" ");
        for (int i = 1; i < lineParts.length; i++) {
            if (lineParts[1].startsWith("(compatible;")) {
                String[] botParts = lineParts[2].split("/");
                botName = botParts[0].trim();
                setBotName(botName);
            }

            if (lineParts.length >= 5) {

                if (!lineParts[1].startsWith("(compatible;")) {
                    //  operationSystem = lineParts[1].trim();
                    //   setOperationSystem(operationSystem);

                    if (lineParts[1].equals("(Windows") || lineParts[1].equals("(Linux") || lineParts[1].equals("(Android")) {
                        String os = lineParts[1].substring(1);
                        operationSystem = os.trim();
                        setOperationSystem(operationSystem);

                    } else if (lineParts[1].equals("(iPad;U;CPU")) {
                        String os = lineParts[1].substring(1, lineParts[1].length() - 6);

                        operationSystem = os.trim();
                        setOperationSystem(operationSystem);

                    } else if (lineParts[1].equals("(Linux;u;Android")) {
                        String os = lineParts[1].substring(1, lineParts[1].length() - 10);

                        operationSystem = os.trim();
                        setOperationSystem(operationSystem);

                    } else {
                        String os = lineParts[1].substring(1, lineParts[1].length() - 1);

                        operationSystem = os.trim();
                        setOperationSystem(operationSystem);

                    }
                }

            }
            for (int j = 0; j < lineParts.length - 1; j++) {
                if (lineParts[j].startsWith("Gecko)")) {
                    String nextPart = lineParts[j + 1];
                    if (nextPart.startsWith("Safari") || nextPart.startsWith("SamsungBrowser")
                            || nextPart.startsWith("HeadlessChrome")
                            || nextPart.startsWith("Chrome")
                    ) {
                        String[] browserAndVersion = nextPart.split("/");
                        browser = browserAndVersion[0].trim();
                        setBrowser(browser);
                        break;
                    } else if (nextPart.startsWith("JavaFX") && lineParts.length <= 9
                            || (nextPart.startsWith("Version") && (lineParts.length >= 14 && lineParts.length <= 15)
                            && lineParts[j].equals(lineParts[lineParts.length - 2])
                            || (nextPart.startsWith("Ubuntu") && lineParts.length <= 10)
                    )

                    ) {
                        String partWithBrowser = lineParts[j + 2];
                        String[] browserAndVersion = partWithBrowser.split("/");
                        browser = browserAndVersion[0].trim();
                        setBrowser(browser);
                        break;
                    } else if (
                            nextPart.startsWith("Ubuntu")
                                    || nextPart.startsWith("JavaFX/8.0")
                                    || nextPart.startsWith("JavaFX") || nextPart.trim().startsWith(" ")
                                    || nextPart.startsWith("OPiOS") || nextPart.startsWith("GSA")
                                    || (nextPart.startsWith("FxiOS") && lineParts.length <= 17)
                                    || (nextPart.startsWith("Version") && lineParts.length == 16
                                    && lineParts[j].equals(lineParts[lineParts.length - 3])
                                    || nextPart.startsWith("CriOS"))

                    ) {
                        String partWithBrowser = lineParts[j + 3];
                        String[] browserAndVersion = partWithBrowser.split("/");
                        browser = browserAndVersion[0].trim();
                        setBrowser(browser);
                        break;
                    } else if ((nextPart.startsWith("FxiOS") && lineParts.length > 17)) {
                        String partWithBrowser = lineParts[j + 4];
                        String[] browserAndVersion = partWithBrowser.split("/");
                        browser = browserAndVersion[0].trim();
                        setBrowser(browser);
                        break;
                    }
                }
            }

        }

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