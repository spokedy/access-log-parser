import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count=0;
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
        }
    }
}