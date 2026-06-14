import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Лабораторна робота №4
 * Читання m останніх слів з n останніх рядків текстового файлу
 */
public class Lab4 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        System.out.println("=== Лабораторна робота №4: Файловий ввід/вивід ===");
        System.out.println();

        // Введення шляху до вхідного файлу
        System.out.print("Введіть шлях до вхідного файлу: ");
        String inputPath = scanner.nextLine().trim();

        // Введення шляху до вихідного файлу
        System.out.print("Введіть шлях до вихідного файлу: ");
        String outputPath = scanner.nextLine().trim();

        // Введення n (кількість останніх рядків)
        int n = 0;
        while (n <= 0) {
            System.out.print("Введіть n (кількість останніх рядків): ");
            try {
                n = Integer.parseInt(scanner.nextLine().trim());
                if (n <= 0) System.out.println("  [!] Значення n має бути більше 0.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Невірний формат. Введіть ціле число.");
            }
        }

        // Введення m (кількість останніх слів у кожному рядку)
        int m = 0;
        while (m <= 0) {
            System.out.print("Введіть m (кількість останніх слів у кожному рядку): ");
            try {
                m = Integer.parseInt(scanner.nextLine().trim());
                if (m <= 0) System.out.println("  [!] Значення m має бути більше 0.");
            } catch (NumberFormatException e) {
                System.out.println("  [!] Невірний формат. Введіть ціле число.");
            }
        }

        System.out.println();
        System.out.println("--- Початок обробки ---");

        // Читання файлу
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            System.out.println("[ПОМИЛКА] Файл не знайдено: " + inputPath);
            return;
        }
        if (!inputFile.canRead()) {
            System.out.println("[ПОМИЛКА] Немає прав для читання файлу: " + inputPath);
            return;
        }

        List<String> allLines = new ArrayList<>();
        System.out.println("[INFO] Відкриваємо файл: " + inputFile.getAbsolutePath());

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8))) {

            String line;
            int lineCount = 0;
            while ((line = reader.readLine()) != null) {
                allLines.add(line);
                lineCount++;
            }
            System.out.println("[INFO] Прочитано рядків: " + lineCount);

        } catch (IOException e) {
            System.out.println("[ПОМИЛКА] Не вдалося прочитати файл: " + e.getMessage());
            return;
        }

        if (allLines.isEmpty()) {
            System.out.println("[ПОПЕРЕДЖЕННЯ] Файл порожній. Нічого обробляти.");
            return;
        }

        // Визначення n останніх рядків
        int actualN = Math.min(n, allLines.size());
        if (actualN < n) {
            System.out.println("[ПОПЕРЕДЖЕННЯ] Файл містить лише " + allLines.size()
                    + " рядків. Беремо всі " + actualN + " рядків.");
        }
        int startIndex = allLines.size() - actualN;
        List<String> lastNLines = allLines.subList(startIndex, allLines.size());
        System.out.println("[INFO] Обробляємо останніх " + actualN + " рядків (рядки "
                + (startIndex + 1) + "–" + allLines.size() + ").");

        // Обробка: витягуємо m останніх слів з кожного рядка
        List<String> resultLines = new ArrayList<>();
        System.out.println();
        System.out.println("--- Результати обробки ---");

        for (int i = 0; i < lastNLines.size(); i++) {
            String currentLine = lastNLines.get(i);
            int globalLineNum = startIndex + i + 1;

            // Розбиваємо рядок на слова (один або більше пробілів як роздільник)
            String[] words = currentLine.trim().isEmpty()
                    ? new String[0]
                    : currentLine.trim().split("\\s+");

            String resultLine;
            if (words.length == 0) {
                System.out.println("  Рядок " + globalLineNum + " [порожній]: (пропущено)");
                resultLine = "";
            } else {
                int actualM = Math.min(m, words.length);
                String[] lastMWords = Arrays.copyOfRange(words, words.length - actualM, words.length);
                resultLine = String.join(" ", lastMWords);

                System.out.println("  Рядок " + globalLineNum + " [" + words.length + " слів]"
                        + " -> останніх " + actualM + ": \"" + resultLine + "\"");
            }
            resultLines.add(resultLine);
        }

        // Запис результатів у вихідний файл
        System.out.println();
        System.out.println("[INFO] Записуємо результати у файл: " + outputPath);

        File outputFile = new File(outputPath);
        // Створення батьківських директорій, якщо потрібно
        if (outputFile.getParentFile() != null && !outputFile.getParentFile().exists()) {
            outputFile.getParentFile().mkdirs();
            System.out.println("[INFO] Створено директорію: " + outputFile.getParentFile().getAbsolutePath());
        }

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {

            writer.write("Результат: " + m + " останніх слів з " + actualN + " останніх рядків");
            writer.newLine();
            writer.write("Вхідний файл: " + inputFile.getAbsolutePath());
            writer.newLine();
            writer.write("=".repeat(50));
            writer.newLine();

            for (int i = 0; i < resultLines.size(); i++) {
                int globalLineNum = startIndex + i + 1;
                writer.write("Рядок " + globalLineNum + ": " + resultLines.get(i));
                writer.newLine();
            }

            System.out.println("[INFO] Результати успішно записано. Рядків у вихідному файлі: "
                    + resultLines.size());

        } catch (IOException e) {
            System.out.println("[ПОМИЛКА] Не вдалося записати у файл: " + e.getMessage());
            return;
        }

        System.out.println();
        System.out.println("=== Обробку завершено успішно ===");
    }
}
