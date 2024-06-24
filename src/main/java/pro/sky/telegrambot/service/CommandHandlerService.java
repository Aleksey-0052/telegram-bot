package pro.sky.telegrambot.service;

public interface CommandHandlerService {

    /**
     * Обработать входящую команду
     *
     * @param chatId идентификатор чата
     * @param command текст команды
     * @return ответ на команду
     */

    String handleCommand(Long chatId, String command);


}
