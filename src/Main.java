import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count=0;
        int countstr=0;
        int max=0;
        int min=1024;//максимальная длина строки
        while(true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file=new File(path);
            boolean fileExists=file.exists();
            boolean isDirectory=file.isDirectory();
            if(!fileExists) {
                System.out.println("Указан неверный путь или путь к несуществующему файлу");continue;}
            if(isDirectory){
                System.out.println("Указан путь к папке, а не к файлу");
            }
            else  {count++;
                System.out.println("Путь указан верно"+" "+"Это файл номер "+count);

            }
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;

                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if(max<length){max=length;}
                    if(max>=1024)throw new RuntimeException("строка длиннее 1024 символов");
                    if(min>length) {min=length;}
                    countstr++;//счетчик
                     }
                System.out.println("общее количество строк в файле: "+countstr);
                System.out.println("длина самой длинной строки в файле: "+max);
                System.out.println("длина самой короткой строки в файле: "+min);
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
    }
}