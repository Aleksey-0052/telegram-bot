package pro.sky.telegrambot.exception;

public class IncorrectCreateTaskCommandException extends RuntimeException {

    public IncorrectCreateTaskCommandException(String message) {
        super(message);
    }
}
