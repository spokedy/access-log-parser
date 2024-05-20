import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count = 0;
        int countstr = 0;
        int max = 0;
        int min = 1024;//максимальная длина строки
        String YANDEX_BOT_NAME = "YandexBot";
        String GOOGLE_BOT_NAME = "Googlebot";
        Statistics statistics = new Statistics();
        double yndx = 0, gogle = 0;
        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Указан неверный путь или путь к несуществующему файлу");
                continue;
            }
            if (isDirectory) {
                System.out.println("Указан путь к папке, а не к файлу");
            } else {
                count++;
                System.out.println("Путь указан верно" + " " + "Это файл номер " + count);

            }
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;

                while ((line = reader.readLine()) != null) {
                    countstr++;
                    int length = line.length();
                    //максимум_минимум символов в строке
                    if (max < length) {
                        max = length;
                    }
                    if (max >= 1024) throw new RuntimeException("строка длиннее 1024 символов");
                    if (min > length) {
                        min = length;
                    }
                    //максимум_минимум символов в строке
                    List<LogEntry> logDataList = new ArrayList<>();

                    LogEntry logData = new LogEntry(line);
                    if (logData != null) {
                        logDataList.add(logData);
                        statistics.addEntry(logData);
                    }
                    if (logData.getUserAgentFullData() != null || logData.getUserAgentFullData().equals("-")) {
                        UserAgent userAgent = new UserAgent(logData.getUserAgentFullData());
                        String botNameFromUserAgent = userAgent.getBotName();
                        if (botNameFromUserAgent != null) {
                            if (LogEntry.checkUserAgent(botNameFromUserAgent, YANDEX_BOT_NAME)) {
                                yndx++;
                            }

                            if (LogEntry.checkUserAgent(botNameFromUserAgent, GOOGLE_BOT_NAME)) {
                                gogle++;
                            }
                        }
                    }

                }
                statistics.getTrafficRate();
                reader.close();
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException("Файл не найден");
            } catch (IOException e) {
                throw new NumberFormatException("Ошибка ввода или вывода");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            double totalBotCount = yndx + gogle;

            System.out.println("Количество строк в файле: " + countstr);
            System.out.println("Сумма YandexBot и Googlebot составляет: " + totalBotCount);
            System.out.println("Количество запросов от " + YANDEX_BOT_NAME + " составляет : " + yndx);
            System.out.println("Количество запросов от " + GOOGLE_BOT_NAME + " составляет : " + gogle);
            System.out.println("Доля запросов от " + YANDEX_BOT_NAME + " составляет : " + String.format("%.2f", (yndx * 1.0 / totalBotCount)));
            System.out.println("Доля запросов от " + GOOGLE_BOT_NAME + " составляет : " + String.format("%.2f", (gogle * 1.0 / totalBotCount)));
            break;
        }

    }
}
//                    String[] parts = line.split(";");
//                    if (parts.length >= 2) {
//                        String fragment = parts[1];
//                        fragment=fragment.replaceAll("\\s+","");//убираю пробелы
//                        String[] parts2=fragment.split("/");
//                        if (parts2.length >= 2) {
//                            String fragment_res = parts2[0];//все до слэша
//                            if(Objects.equals(fragment_res, "YandexBot")){yndx++;}
//                            if(Objects.equals(fragment_res, "Googlebot")){gogle++;}
//                                         }
//