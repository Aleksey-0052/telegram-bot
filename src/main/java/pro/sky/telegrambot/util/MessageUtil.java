package pro.sky.telegrambot.util;

import org.springframework.util.StringUtils;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.exception.IncorrectCreateTaskCommandException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessageUtil {

    /**
     * Распарсиваем строку, поступившую от пользователя в формате "01.07.2024 20:00 Сделать домашнюю работу".
     * Первая часть паттерна описывает дату и время: ([0-9\.\:\s]{16}).
     * Вторая часть паттерна описывает пробел: (\s).
     * Третья часть паттерна описывает текстовое сообщение от пользователя: ([\W+]+).
     * Создаем объект типа Matcher для выполнения операций поиска по шаблону.
     * Проверяем, подходит ли строка под паттерн: matcher.find().
     * Если строка подходит, то извлекаем из нее первую часть (дату и время), передаем ее в качестве первого параметра
     * в статический метод parse() класса LocalDateTime, в качестве второго параметра передаем объект типа
     * DateTimeFormatter и в результате получаем дату и время в виде объекта LocalDateTime.
     * Извлекаем из строки третью часть (текстовое сообщение) и сохраняем ссылку на нее в переменную taskTest типа String.
     * Создаем объект типа NotificationTask с полями "идентификатор чата", "напоминание" и "время", который будет
     * сохранен в базу данных.
     */

    private static final Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public static NotificationTask parseCreateCommand(Long chatId, String command) throws IncorrectCreateTaskCommandException {
        if (StringUtils.hasText(command)) {
            Matcher matcher = pattern.matcher(command);

            if (matcher.find()) {

                LocalDateTime dateTime = LocalDateTime.parse(matcher.group(1), dateTimeFormatter);
                String taskTest = matcher.group(3);

                checkDate(dateTime);
                checkTaskText(taskTest);

                return new NotificationTask(chatId, taskTest, dateTime);
            }
        }

        throw new IncorrectCreateTaskCommandException("Incorrect command: " + command);
    }

    private static void checkDate(LocalDateTime dateTime) throws IncorrectCreateTaskCommandException {
        if (dateTime == null) {
            throw new IncorrectCreateTaskCommandException("Incorrect task dateTime: " + dateTime);
        } else if (dateTime.isBefore(LocalDateTime.now())) {
            throw new IncorrectCreateTaskCommandException("Incorrect task dateTime: " + dateTime);
        }
    }

    private static void checkTaskText(String text) throws IncorrectCreateTaskCommandException {
        if (!StringUtils.hasText(text)) {
            throw new IncorrectCreateTaskCommandException("Incorrect task text: " + text);
        }
    }
}
