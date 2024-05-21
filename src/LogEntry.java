import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
public class LogEntry {

    private String ipAddress; //IP-адрес клиента, который сделал запрос к серверу
    private String propertyOne; //Пропущенное свойство 1
    private String propertySecond; //Пропущенное свойство 2
    private LocalDateTime dateTime; //Дата и время запроса в квадратных скобках.
    private HttpMethods requestMethod; //Метод запроса (в примере выше — GET) и путь, по которому сделан запрос.
    private String requestPath; //Путь, по которому сделан запрос.
    private int responseCode; //Код HTTP-ответ
    private int dataSize; //Размер отданных данных в байтах
    private String referer; //Путь к странице, с которой перешли на текущую страницу
    private String userAgentFullData; //Информация о браузере или другом клиенте, который выполнил запрос.

    public LogEntry(String logLine) {
        parseLogLineToLogEntryClass(logLine);
    }


    public String getIpAddress() {
        return ipAddress;
    }

    private void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPropertyOne() {
        return propertyOne;
    }

    private void setPropertyOne(String propertyOne) {
        this.propertyOne = propertyOne;
    }

    public String getPropertySecond() {
        return propertySecond;
    }

    private void setPropertySecond(String propertySecond) {
        this.propertySecond = propertySecond;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    private void setDateTime(String dateTimeFull) {
        String dateTime = dateTimeFull.substring(1, dateTimeFull.length() - 1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
        this.dateTime = LocalDateTime.parse(dateTime, formatter);
    }

    public HttpMethods getRequestMethod() {
        return requestMethod;
    }

    private void setRequestMethod(HttpMethods requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getRequestPath() {
        return requestPath;
    }

    private void setRequestPath(String requestPath) {
        this.requestPath = requestPath;
    }

    public int getResponseCode() {
        return responseCode;
    }

    private void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public int getDataSize() {
        return dataSize;
    }

    private void setDataSize(int dataSize) {
        this.dataSize = dataSize;
    }

    public String getReferer() {
        return referer;
    }

    private void setReferer(String referer) {
        this.referer = referer;
    }

    public String getUserAgentFullData() {
        return userAgentFullData;
    }

    private void setUserAgentFullData(String userAgentFullData) {
        this.userAgentFullData = userAgentFullData;
    }

    private void parseLogLineToLogEntryClass(String logLine) {
        String[] lineParts = logLine.split(" ");
        if (lineParts.length >= 10) {
            setIpAddress(lineParts[0]);
            setPropertyOne(lineParts[1]);
            setPropertySecond(lineParts[2]);
            setDateTime(lineParts[3] + " " + lineParts[4]);
            setRequestMethod(extractHttpMethod(lineParts[5]));
            setRequestPath(lineParts[6]);
            setResponseCode(Integer.parseInt(lineParts[8]));
            setDataSize(Integer.parseInt(lineParts[9]));
            setReferer(lineParts[10].equals("-") ? "" : lineParts[10]);
            setUserAgentFullData(extractUserAgent(logLine));

        }

    }

    private static HttpMethods extractHttpMethod(String logLine) {
        String httpMethodsString = logLine.replace("\"", "");
        HttpMethods httpMethods = HttpMethods.valueOf(httpMethodsString);
        return httpMethods;

    }

    private static String extractUserAgent(String logLine) {
        String[] parts = logLine.split("\"");
        for (String part : parts) {
            if (part.startsWith("Mozilla/5.0")) {
                return part;
            }
        }
        return "-";
    }

    public static boolean checkUserAgent(String fragment, String botName) {
        return fragment.equals(botName);
    }

    @Override
    public String toString() {
        return "logs.LogEntry{" +
                "ipAddress='" + ipAddress + 'p' +
                ", propertyOne='" + propertyOne + '\'' +
                ", propertySecond='" + propertySecond + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", requestPath='" + requestPath + '\'' +
                ", responseCode=" + responseCode +
                ", dataSize=" + dataSize +
                ", referer='" + referer + '\'' +
                ", userAgent='" + userAgentFullData + '\'' +
                '}';
    }

}
enum HttpMethods {GET, POST, PUT, DELETE, PATCH, HEAD, OPTIONS, TRACE}