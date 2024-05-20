import java.time.Duration;
import java.time.LocalDateTime;

public class Statistics {

    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

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
    }

    public void getTrafficRate() {
        long diffInHours = Duration.between(minTime, maxTime).toHours();

        if (diffInHours == 0) {
            System.out.println("Нет промежутка времени и данные не получить");
        } else {
            double averageTrafficRate = totalTraffic / diffInHours;
            System.out.println("Средний объем трафика за час: " + averageTrafficRate + " байт.");
        }
    }
}