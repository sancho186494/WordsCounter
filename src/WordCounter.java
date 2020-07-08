import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static void main(String[] args) {
        BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> mapp = new TreeMap<>();

        String fileAdress = null;
        System.out.println("Введите адрес файла:");
        try {
            fileAdress = systemInReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Некорректный ввод");
        }

        //Создание списка потенциальных адресов файла в случае ввода относительного пути вида "file.txt" или "/file.txt"
        //предусмотрена работа в Windows (в адресе используется обратная косая черта "\") и соответствующий перебор
        //значение. В случае неудовлетворительного результата, требуется запустить программу заново
        List<String> adresses = new ArrayList<>();
        adresses.add(fileAdress);
        adresses.add(System.getProperty("user.dir") + "/" + fileAdress);
        adresses.add(System.getProperty("user.dir") + "\\" + fileAdress);
        adresses.add(System.getProperty("user.dir") + fileAdress);
        InputStream fileIn;
        int count = 0;
        while (true) {
            try {
                fileIn = new FileInputStream(adresses.get(count));
                break;
            } catch (FileNotFoundException e) {
                count++;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Адрес файла не распознан / файл не найден, требуется повторный запуск");
                return;
            }
        }

        //построчный анализ файла, поиск по шаблону и добавление найденных слов в TreeMap, вывод списка в консоль
        String regex = "[а-яА-Яa-zA-Z]+";       //шаблон поиска
        try {
            BufferedReader readFile = new BufferedReader(new InputStreamReader(fileIn));
            while (readFile.ready()) {
                String input = readFile.readLine();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input);
                while(matcher.find()) {
                    String word = (input.substring(matcher.start(), matcher.end()));
                    if (mapp.containsKey(word)) {
                        int value = mapp.get(word);
                        value++;
                        mapp.put(word, value);
                    } else {
                        mapp.put(word, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Map.Entry<String, Integer> pair : mapp.entrySet()) {
            System.out.println(pair.getKey() + ": " + pair.getValue());
        }
    }
}
