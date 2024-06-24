package pro.sky.telegrambot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.configuration.TelegramBotConfiguration;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.exception.IncorrectCreateTaskCommandException;
import pro.sky.telegrambot.util.MessageUtil;

@Service
@RequiredArgsConstructor
public class CommandHandlerServiceImpl implements CommandHandlerService {

    private final NotificationTaskService notificationTaskService;
    private final TelegramBotConfiguration botConfig;

    private static final String Start_Command = "/start";
    private static final String Help_Command = "/help";

    /**
     * @param chatId идентификатор чата
     * @param command текст команды / сообщения от пользователя
     * @return ответное сообщение от бота:
     * - startMsg: "Hello! To create a task, send a message in the following format: 01.01.2022 20:00 Do homework".
     * - helpMsg: "Bot for planning tasks, helps not to forget your affairs, to create a task, send a message about the created task".
     * - результаты вызова метода handleCreateTaskCommand().
     */


    @Override
    public String handleCommand(Long chatId, String command) {
        switch (command) {
            case Start_Command:
                return botConfig.getStartMsg();
            case Help_Command:
                return botConfig.getHelpMsg();
            default:
                return handleCreateTaskCommand(chatId, command);
        }
    }

    /**
     * Метод handleCreateTaskCommand() вызывает статический метод parseCreateCommand() класса MessageUtil и вызывает у
     * объекта типа NotificationTaskService метод save() (а тот вызывает аналогичный метод у репозитория).
     * Метод parseCreateCommand() распарсивает строку и возвращает объект типа NotificationTask.
     * Метод save() сохраняет возвращенный объект в базу данных.
     * @param chatId идентификатор чата
     * @param command текст команды / сообщение от пользователя
     * @return ответное сообщение от бота - successMsg: "Task successfully scheduled!" или
     * errorMsg: "Incorrect message format, please try again".
     */

    private String handleCreateTaskCommand(Long chatId, String command) {
        try {
            NotificationTask notificationTask = MessageUtil.parseCreateCommand(chatId, command);
            notificationTaskService.save(notificationTask);
            return botConfig.getSuccessMsg();

        } catch (IncorrectCreateTaskCommandException e) {
            return botConfig.getErrorMsg();
        }
    }
}
