package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.DeleteMyCommands;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Конфигурация бота
 */
@Configuration
@Data
public class TelegramBotConfiguration {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.msg.start}")
    private String startMsg;

    @Value("${telegram.bot.msg.error}")
    private String errorMsg;

    @Value("${telegram.bot.msg.success}")
    private String successMsg;

    @Value("${telegram.bot.msg.help}")
    private String helpMsg;



    @Bean
    public TelegramBot telegramBot() {
        TelegramBot bot = new TelegramBot(token);

        //bot.execute(new DeleteMyCommands());
        return bot;
    }

}
