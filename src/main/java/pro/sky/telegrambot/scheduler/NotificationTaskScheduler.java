package pro.sky.telegrambot.scheduler;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import liquibase.pro.packaged.N;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pro.sky.telegrambot.entity.NotificationTask;
import pro.sky.telegrambot.service.NotificationTaskService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * Шедулер для отправки напоминаний
 */

@Component
@EnableScheduling
@RequiredArgsConstructor
public class NotificationTaskScheduler {

    private final Logger logger = LoggerFactory.getLogger(NotificationTaskScheduler.class);

    private final NotificationTaskService notificationTaskService;
    private final TelegramBot telegramBot;


    /**
     * Метод sendTaskNotification() будет вызываться один раз в минуту и выводить из базы данных все напоминания,
     * которые будут совпадать с текущим моментом времени с точностью до одной минуты.
     * Так как scheduler может запускаться с лагом в несколько миллисекунд, то для поиска необходимо не просто брать
     * текущее время методом LocalDateTime.now(), но “обрезать” его до минут, чтобы получилось время с 00 секунд.
     * Для этого необходимо вызвать метод truncatedTo().
     * Метод sendTaskNotification() вызывает из сервиса метод findAllByNotificationDateTime() (а тот вызывает
     * соответствующий метод из репозитория), который находит в базе данных все уведомления (напоминания) на текущий
     * момент времени.
     * Для каждого объекта типа NotificationTask (напоминание) вызывается метод sendNotification().
     * Метод sendNotification() создает объект типа SendMessage с полями "идентификатор чата", из которого поступило
     * сообщение / команда, и "напоминание". Эти данные хранятся в таблице в базе данных.
     * Объект типа SendMessage поступает в параметры метода execute(), в результате чего напоминание отправляется
     * по идентификатору в соответствующий чат мессенджера Telegram.
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.MINUTES)
    public void sendTaskNotification() {
        LocalDateTime dateTimeToFindTasks = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        logger.info("Task scheduler started, {}", dateTimeToFindTasks);
        notificationTaskService.findAllByNotificationDateTime(dateTimeToFindTasks).forEach(this::sendNotification);
    }

    private void sendNotification(NotificationTask notificationTask) {
        SendResponse response = telegramBot.execute(new SendMessage(notificationTask.getChatId(), notificationTask.getMessage()));
        if (response.isOk()) {
            notificationTaskService.delete(notificationTask);
        }
    }

}
