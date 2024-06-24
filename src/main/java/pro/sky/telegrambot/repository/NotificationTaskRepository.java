package pro.sky.telegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.sky.telegrambot.entity.NotificationTask;

import java.time.LocalDateTime;
import java.util.List;


/**
 * Репозиторий по работе с уведомлениями по задачам
 */

@Repository
public interface NotificationTaskRepository extends JpaRepository<NotificationTask, Long> {

    /**
     * Получить список задач на текущее время
     * @param localDateTime время для поиска задач
     * @return список задач
     */
    List<NotificationTask> findAllByNotificationDateTime(LocalDateTime localDateTime);


}
