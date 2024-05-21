import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
public class Statistics {

    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    HashSet<String> addressList = new HashSet<>();
    HashMap<String, Integer> operationSystemStatistics = new HashMap<>();
    HashMap<String, Integer> browserStatistics = new HashMap<>();
    HashSet<String> nonExistingPages = new HashSet<>();

    private long nonBotVisits;
    private long errorCodeVisits;
    private long uniqueRealUsers;

    public HashSet<String> getNonExistingPages() {
        return nonExistingPages;
    }

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.MAX;
        this.maxTime = LocalDateTime.MIN;
    }

    public void addEntry(LogEntry logEntry) {
        totalTraffic += logEntry.getDataSize();

        if (logEntry.getDateTime().isBefore(minTime)) {
            minTime = logEntry.getDateTime();
        }

        if (logEntry.getDateTime().isAfter(maxTime)) {
            maxTime = logEntry.getDateTime();
        }

        if (logEntry.getResponseCode() == 200) {
            addressList.add(logEntry.getRequestPath());
        }
        UserAgent userAgent = new UserAgent(logEntry.getUserAgentFullData());
        String operationSystem = userAgent.getOperationSystem();
        operationSystemStatistics.put(operationSystem, operationSystemStatistics.getOrDefault(operationSystem,
                0) + 1);


        if (logEntry.getResponseCode() == 404) {
            nonExistingPages.add(logEntry.getRequestPath());
        }


        String browser = userAgent.getBrowser();
        browserStatistics.put(browser, browserStatistics.getOrDefault(browser, 0) + 1);
        if (logEntry.getResponseCode() == 200) {
            addressList.add(logEntry.getRequestPath());
        }

        if (userAgent.getBotName() != null && !userAgent.getBotName().contains("bot")) {
            nonBotVisits++;
            uniqueRealUsers++;
        }

        if (logEntry.getResponseCode() >= 400 && logEntry.getResponseCode() <= 500) {
            errorCodeVisits++;
        }
    }


    public HashMap<String, Double> calculateOperationSystem() {
        HashMap<String, Double> operationSystemShare = new HashMap<>();
        // Возможные ОС: "Windows", "iPad", "SMART-TV", "Macintosh", "Linux","Android", "iPod", "iPhone","Unknown", "X11"
        HashSet<String> allowedSystems = new HashSet<>(Arrays.asList("Windows", "Macintosh", "Linux"));

        if (operationSystemStatistics.size() == 0) {
            System.out.println("Отсутствуют данные по операционным системам.");
            return operationSystemShare;
        }

        int totalAllowedSystems = 0;
        for (String os : allowedSystems) {
            if (operationSystemStatistics.containsKey(os)) {
                totalAllowedSystems += operationSystemStatistics.get(os);
            }
        }

        for (Map.Entry<String, Integer> entry : operationSystemStatistics.entrySet()) {
            if (allowedSystems.contains(entry.getKey())) {
                double share = (double) entry.getValue() / totalAllowedSystems;
                operationSystemShare.put(entry.getKey(), share);
            }
        }

        return operationSystemShare;
    }

    public HashMap<String, Double> calculateBrowser() {
        HashMap<String, Double> browserShare = new HashMap<>();
        HashSet<String> allowedBrowser = new HashSet<>(Arrays.asList("Safari", "SamsungBrowser", "Chrome", "HeadlessChrome"));
        if (browserStatistics.size() == 0) {
            System.out.println("Нет данных по браузерам.");
            return browserShare;
        }

        int totalBrowsers = 0;
        for (Map.Entry<String, Integer> entry : browserStatistics.entrySet()) {
            if (allowedBrowser.contains(entry.getKey())) {
                totalBrowsers += entry.getValue();
            }
        }

        for (Map.Entry<String, Integer> entry : browserStatistics.entrySet()) {
            if (allowedBrowser.contains(entry.getKey())) {
                double share = (double) entry.getValue() / totalBrowsers;
                browserShare.put(entry.getKey(), share);
            }
        }

        return browserShare;
    }

    public void getTrafficRate() {
        long diffInHours = Duration.between(maxTime, minTime).toHours();

        if (diffInHours == 0) {
            System.out.println("Нет промежутка времени и данные не получить");
        } else {
            double averageTrafficRate = totalTraffic / diffInHours;
            System.out.println("Средний объем трафика за час: " + averageTrafficRate + " байт.");
        }

    }

    public double calculateAverageNonBotVisitsHour() {
        long diffInHours = Duration.between(minTime, maxTime).toHours();
        return (double) nonBotVisits / diffInHours;
    }

    public double calculateAverageErrorCodeVisitsHour() {
        long diffInHours = Duration.between(minTime, maxTime).toHours();
        return (double) errorCodeVisits / diffInHours;
    }

    public double calculateAverageVisitsPerRealUser() {
        return (double) nonBotVisits / uniqueRealUsers;
    }

    public int calculatePeakNonBotVisitsPerSecond() {
        Map<Integer, Long> visitsPerSecond = addressList.stream()
                .filter(path -> path.startsWith("/"))
                .collect(Collectors.groupingBy(path -> path.hashCode() % 1000, Collectors.counting()));

        return visitsPerSecond.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .orElse(0);
    }


    public int calculateMaxVisitsPerRealUser() {
        Map<String, Long> visitsPerUser = addressList.stream()
                .filter(path -> path.startsWith("/"))
                .collect(Collectors.groupingBy(path -> String.valueOf(path.hashCode() % 1000), Collectors.counting())); // Группируем по пользователям

        return visitsPerUser.values().stream()
                .mapToInt(Long::intValue)
                .max()
                .orElse(0);
    }
}