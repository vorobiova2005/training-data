import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class BasicDataOperationUsingSet {
    static final String PATH_TO_DATA_FILE = "list/String.data";

    String StringValueToSearch;
    String[] StringValueArray;
    Set<String> StringValueSet = new HashSet<>();

    public static void main(String[] args) {  
        BasicDataOperationUsingSet basicDataOperationUsingSet = new BasicDataOperationUsingSet(args);
        basicDataOperationUsingSet.doDataOperation();
    }

    BasicDataOperationUsingSet(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Вiдсутнє значення для пошуку");
        }

        this.StringValueToSearch = args[0];

        StringValueArray = Utils.readArrayFromFile(PATH_TO_DATA_FILE);
        StringValueSet = new HashSet<>(Arrays.asList(StringValueArray));
    }

    private void doDataOperation() {
        // операцiї з масивом даних
        searchArray();
        findMinAndMaxInArray();

        sortArray();

        searchArray();
        findMinAndMaxInArray();

        // порiвняння масиву та множини
        compareArrayAndSet();

        // записати вiдсортовану множину в окремий файл
        Utils.writeSetToFile(StringValueSet, PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Сортує масив String за допомогою потоків.
     * Вимiрює та виводить час, витрачений на сортування масиву в наносекундах.
     */
    private void sortArray() {
        long startTime = System.nanoTime();

        // Сортування масиву з використанням потоків
        StringValueArray = Arrays.stream(StringValueArray)
                                 .sorted()
                                 .toArray(String[]::new);

        Utils.printOperationDuration(startTime, "сортування масиву даних");
    }

    /**
     * Пошук конкретного значення в масиві за допомогою потоків.
     */
    private void searchArray() {
        long startTime = System.nanoTime();

        boolean isFound = Arrays.stream(StringValueArray)
                                .anyMatch(value -> value.equals(StringValueToSearch));

        Utils.printOperationDuration(startTime, "пошук в масиві даних");

        if (isFound) {
            System.out.println("Значення '" + StringValueToSearch + "' знайдено в масиві.");
        } else {
            System.out.println("Значення '" + StringValueToSearch + "' в масиві не знайдено.");
        }
    }

    /**
     * Пошук мiнiмального та максимального значення в масиві за допомогою потоків.
     */
    private void findMinAndMaxInArray() {
        if (StringValueArray == null || StringValueArray.length == 0) {
            System.out.println("Масив порожній або не ініціалізований.");
            return;
        }

        long startTime = System.nanoTime();

        Optional<String> min = Arrays.stream(StringValueArray).min(String::compareTo);
        Optional<String> max = Arrays.stream(StringValueArray).max(String::compareTo);

        Utils.printOperationDuration(startTime, "пошук мiнiмального i максимального значення в масиві");

        min.ifPresent(value -> System.out.println("Мiнiмальне значення в масиві: " + value));
        max.ifPresent(value -> System.out.println("Максимальне значення в масиві: " + value));
    }

    /**
     * Пошук конкретного значення в множині за допомогою потоків.
     */
    private void searchInSet() {
        long startTime = System.nanoTime();

        boolean isFound = StringValueSet.stream()
                                       .anyMatch(value -> value.equals(StringValueToSearch));

        Utils.printOperationDuration(startTime, "пошук в множині даних");

        if (isFound) {
            System.out.println("Значення '" + StringValueToSearch + "' знайдено в множині.");
        } else {
            System.out.println("Значення '" + StringValueToSearch + "' в множині не знайдено.");
        }
    }

    /**
     * Пошук мiнiмального та максимального значення в множині за допомогою потоків.
     */
    private void findMinAndMaxInSet() {
        if (StringValueSet == null || StringValueSet.isEmpty()) {
            System.out.println("Множина порожня або не iнiцiалiзована.");
            return;
        }

        long startTime = System.nanoTime();

        Optional<String> min = StringValueSet.stream().min(String::compareTo);
        Optional<String> max = StringValueSet.stream().max(String::compareTo);

        Utils.printOperationDuration(startTime, "пошук мiнiмального i максимального значення в множині");

        min.ifPresent(value -> System.out.println("Мiнiмальне значення в множині: " + value));
        max.ifPresent(value -> System.out.println("Максимальне значення в множині: " + value));
    }

    /**
     * Сортує множину String за допомогою потоків.
     * Вимiрює та виводить час, витрачений на сортування множини в наносекундах.
     */
    private void sortSet() {
        long startTime = System.nanoTime();

        StringValueSet = StringValueSet.stream()
                                       .sorted()
                                       .collect(Collectors.toCollection(LinkedHashSet::new));

        Utils.printOperationDuration(startTime, "сортування множини даних");
    }

    /**
     * Метод для порiвняння елементів масиву та множини.
     */
    private void compareArrayAndSet() {
        System.out.println("Кiлькiсть елементiв в масивi: " + StringValueArray.length);
        System.out.println("Кiлькiсть елементiв в множині: " + StringValueSet.size());

        boolean allElementsMatch = Arrays.stream(StringValueArray)
                                         .allMatch(StringValueSet::contains);

        if (allElementsMatch) {
            System.out.println("Усi елементи масиву присутнi в множині.");
        } else {
            System.out.println("Не всi елементи масиву присутнi в множині.");
        }
    }
}

/**
 * Клас з допомiжними методами для роботи з масивами та виведення результатiв.
 */
class Utils {

    /**
     * Зчитує масив об'єктiв String з файлу, використовуючи потоки та лямбда-вирази.
     *
     * @param pathToFile Шлях до файлу з даними.
     * @return Масив об'єктiв String.
     */
    static String[] readArrayFromFile(String pathToFile) {
        try {
            List<String> lines = Files.lines(Paths.get(pathToFile))
                .map(String::trim)
                .collect(Collectors.toList());

            return lines.toArray(new String[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    /**
     * Записує множину в файл.
     *
     * @param set Множина, яку треба записати.
     * @param path Шлях до файлу.
     */
    static void writeSetToFile(Set<String> set, String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            for (String str : set) {
                writer.write(str);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Виводить час, витрачений на виконання операції.
     *
     * @param startTime Час початку операції.
     * @param operationName Назва операції.
     */
    static void printOperationDuration(long startTime, String operationName) {
        long duration = System.nanoTime() - startTime;
        System.out.println(operationName + " виконано за " + duration + " наносекунд.");
    }
}