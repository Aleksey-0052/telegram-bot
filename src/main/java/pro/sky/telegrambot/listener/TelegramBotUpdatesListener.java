package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.service.CommandHandlerService;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Класс, принимающий от пользователя Telegram все сообщения и отправляющий пользователю ответные сообщения
 */

@Service
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final CommandHandlerService commandHandlerService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }


    @Override
    public int process(List<Update> updates) {
        try {
            updates.stream()
                    .filter(update -> update != null)
                    .forEach(this::processUpdate);
        } catch (Exception e) {
            logger.error("Error during processing telegram update", e);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    /**
     * Получение сообщения от пользователя и отправление пользователю ответного сообщения.
     * @param update любое текстовое сообщение (команда) со стороны пользователя, на которое может реагировать наш бот.
     * Из объекта типа Update получаем идентификатор чата и текстовое сообщение в формате
     * "01.07.2024 20:00 Сделать домашнюю работу".
     * Вызов метода handleCommand() приводит к распарсиванию строки (команды) от пользователя, возвращению объекта
     * типа NotificationTask, сохранению этого объекта в базу данных и возвращению ответного сообщения пользователю.
     * В результате вызова метода sendMessage() ответное сообщение пользователю отправляется.
     */
    private void processUpdate(Update update) {
        logger.info("Processing update: {}", update);

        Long chatId = update.message().chat().id();
        String text = update.message().text();

        String rs = commandHandlerService.handleCommand(chatId, text);
        sendMessage(chatId, rs);
    }

    /**
     * Отправление сообщения пользователю
     * @param chatId идентификатор чата
     * @param message сообщение пользователю
     * Для того, чтобы отправить сообщение обратно пользователю, создаем объект типа SendMessage.
     * В конструктор этого объекта передаем переменную - идентификатор чата и переменную - сообщение, которое
     * будет отправлено пользователю.
     * Перечень ответных сообщений определен в файле - application.properties.
     * Для отправки сообщения вызываем у переменной telegramBot метод execute() и в параметры метода передаем объект
     * типа SendMessage.
     */
    private void sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId, message);
        SendResponse response = telegramBot.execute(sendMessage);
        if (!response.isOk()) {
            logger.error("Error during sending message: {}", response.description());
        }
    }

}
