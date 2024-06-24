package pro.sky.telegrambot.service;

import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис по работе с уведомлениями по задачам
 */

public interface NotificationTaskService {

    /**
     * Сохранить задачу
     * @param task задача
     */
    void save(NotificationTask task);

    /**
     * Удалить задачу
     * @param task задача
     */
    void delete(NotificationTask task);

    /**
     * Получить список задач на текущее время
     * @param localDateTime время для поиска задач
     * @return список задач
     */
    List<NotificationTask> findAllByNotificationDateTime(LocalDateTime localDateTime);
}
