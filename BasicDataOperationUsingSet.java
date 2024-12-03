import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Клас BasicDataOperationUsingSet надає методи для виконання основних операцiй з даними типу String.
 * 
 * <p>Цей клас зчитує данi з файлу "list/LocalDateTime.data", сортує їх та виконує пошук значення в масивi та множинi.</p>
 * 
 * <p>Основнi методи:</p>
 * <ul>
 *   <li>{@link #main(String[])} - Точка входу в програму.</li>
 *   <li>{@link #doDataOperation()} - Виконує основнi операцiї з даними.</li>
 *   <li>{@link #sortSet()} - Сортує множину за допомогою потоків.</li>
 *   <li>{@link #searchInSet()} - Пошук конкретного значення в множині.</li>
 *   <li>{@link #findMinAndMaxInSet()} - Знаходить мiнiмальне і максимальне значення в множині.</li>
 *   <li>{@link #compareArrayAndSet()} - Порiвнює елементи масиву та множини.</li>
 * </ul>
 * 
 * <p>Конструктор:</p>
 * <ul>
 *   <li>{@link #BasicDataOperationUsingSet(String[])} - iнiцiалiзує об'єкт з значенням для пошуку.</li>
 * </ul>
 * 
 * <p>Константи:</p>
 * <ul>
 *   <li>{@link #PATH_TO_DATA_FILE} - Шлях до файлу з даними.</li>
 * </ul>
 * 
 * <p>Змiннi екземпляра:</p>
 * <ul>
 *   <li>{@link #StringValueToSearch} - Значення String для пошуку.</li>
 *   <li>{@link #StringValueArray} - Масив String.</li>
 *   <li>{@link #StringValueSet} - Множина String.</li>
 * </ul>
 * 
 * <p>Приклад використання:</p>
 * <pre>
 * {@code
 * java BasicDataOperationUsingSet "2024-03-16T00:12:38Z"
 * }
 * </pre>
 */
public class BasicDataOperationUsingSet {
    static final String PATH_TO_DATA_FILE = "list/String.data";

    String StringValueToSearch;
    String[] StringValueArray;
    Set<String> StringValueSet = new HashSet<>();

    public static void main(String[] args) {  
        BasicDataOperationUsingSet basicDataOperationUsingSet = new BasicDataOperationUsingSet(args);
        basicDataOperationUsingSet.doDataOperation();
    }

    /**
     * Конструктор, який iнiцiалiзує об'єкт з значенням для пошуку.
     * 
     * @param args Аргументи командного рядка, де перший аргумент - значення для пошуку.
     */
    BasicDataOperationUsingSet(String[] args) {
        if (args.length == 0) {
            throw new RuntimeException("Вiдсутнє значення для пошуку");
        }

        this.StringValueToSearch = args[0];

        StringValueArray = Utils.readArrayFromFile(PATH_TO_DATA_FILE);
        StringValueSet = new HashSet<>(Arrays.asList(StringValueArray));
    }

    /**
     * Виконує основнi операцiї з даними.
     * 
     * Метод зчитує масив та множину об'єктiв String з файлу, сортує їх та виконує пошук значення.
     */
    private void doDataOperation() {
        // операцiї з масивом даних
        searchInSet();
        findMinAndMaxInSet();

        sortSet();

        searchInSet();
        findMinAndMaxInSet();

        // порiвняння масиву та множини
        compareArrayAndSet();

        // записати вiдсортовану множину в окремий файл
        Utils.writeSetToFile(StringValueSet, PATH_TO_DATA_FILE + ".sorted");
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
            List<String> lines = Files.lines(Paths.get(pathToFile)) // Зчитуємо всі рядки з файлу за допомогою потоків
                .map(String::trim) // Обрізаємо зайві пробіли
                .collect(Collectors.toList()); // Перетворюємо потік у список

            return lines.toArray(new String[0]); // Перетворюємо список у масив
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0]; // Якщо сталася помилка, повертаємо порожній масив
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

