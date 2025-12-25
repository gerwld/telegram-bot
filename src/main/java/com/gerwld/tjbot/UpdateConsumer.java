package com.gerwld.tjbot;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;


@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;
    public UpdateConsumer() {
        this.telegramClient = new OkHttpTelegramClient(
                "8174170697:AAFjZHIjEWjgVfAlJde2OZlfWOIW-RwMjSY"
        );
    }

    @SneakyThrows
    @Override
    public void consume(Update update) {
        if(update.hasMessage()) {
            String messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();

            if(messageText.equals("/start")) {
              sendMainMenu(chatId);
            }

            else {
                SendMessage message = SendMessage.builder()
                        .chatId(chatId)
                        .text("Вверен неверный запрос. Введите /start чтобы продолжить").build();
                telegramClient.execute(message);
            }
        }
    }
    @SneakyThrows
    private void sendMainMenu(Long chatId) {
        SendMessage message = SendMessage.builder()
                .text("Добро пожаловать! Выберите действие:")
                .chatId(chatId)
                .build();

        var button1 = InlineKeyboardButton.builder()
                .text("Как меня зовут?")
                .callbackData("my_name")
                .build();

        var button2 = InlineKeyboardButton.builder()
                .text("Cлучайное число")
                .callbackData("random")
                .build();

        var button3 = InlineKeyboardButton.builder()
                .text("Долгий процесс")
                .callbackData("long_process")
                .build();

        List<InlineKeyboardRow> keyboardRows = List.of(
                new InlineKeyboardRow(button1),
                new InlineKeyboardRow(button2),
                new InlineKeyboardRow(button3)
        );

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup(keyboardRows);

        message.setReplyMarkup(markup);

        telegramClient.execute(message);
    }

}
