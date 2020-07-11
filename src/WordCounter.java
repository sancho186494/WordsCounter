import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordCounter {
    public static void main(String[] args) {
        BufferedReader systemInReader = new BufferedReader(new InputStreamReader(System.in));
        Map<String, Integer> mapp = new TreeMap<>();

        String fileAdress = null;
        System.out.println("Введите адрес файла:");
        try {
            fileAdress = systemInReader.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Некорректный ввод");
        }

        //Создание списка потенциальных адресов файла в случае ввода относительного пути вида "file.txt" или "/file.txt"
        //и соответствующий перебор значений. В случае неудовлетворительного результата, требуется запустить программу
        // заново.
        //Также предусмотрена работа в Windows (в адресе используется обратная косая черта "\")
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
        int max = 0;                            //наибольшее количество повторений
        try {
            BufferedReader readFile = new BufferedReader(new InputStreamReader(fileIn));
            while (readFile.ready()) {
                String input = readFile.readLine();
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(input);
                while(matcher.find()) {
                    String word = (input.substring(matcher.start(), matcher.end())).toLowerCase();
                    if (mapp.containsKey(word)) {
                        int value = mapp.get(word);
                        value++;
                        mapp.put(word, value);
                        if (value > max)
                            max = value;
                    } else {
                        mapp.put(word, 1);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("-----------Статистика по всем словам-----------");
        mapp.forEach((key, value) -> System.out.println(key + ": " + value));

        System.out.println("-----------Слова с наибольшим количеством повторений-----------");
        int finalMax = max;
        mapp.entrySet().stream().filter(pair -> pair.getValue() == finalMax).forEach(pair ->
                System.out.println(pair.getKey() + ": " + pair.getValue()));
    }
}
