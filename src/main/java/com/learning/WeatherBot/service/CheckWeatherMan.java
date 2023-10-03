package com.learning.WeatherBot.service;

import com.learning.WeatherBot.comands.BotCommands;
import com.learning.WeatherBot.config.BotConfig;
import com.learning.WeatherBot.weatherAPI.WeatherAPIService;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CheckWeatherMan extends TelegramLongPollingBot implements BotCommands {
    protected final BotConfig botConfig;
    private final WeatherAPIService weatherAPIService;

    public CheckWeatherMan(BotConfig botConfig, WeatherAPIService weatherAPIService) {
        this.botConfig = botConfig;
        this.weatherAPIService = weatherAPIService;

        try {
            this.execute(new SetMyCommands(LIST_OF_COMMANDS, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            String memberName = update.getMessage().getFrom().getFirstName();

            switch (messageText){
                case "/start":
                    startBot(chatId, memberName);
                    break;
                case "/help":
                    sendHelpText(chatId, HELP_TEXT);
                    break;
                default:
                    returnTemp(chatId, memberName, messageText);
            }
        }
        else if (update.getMessage().hasLocation()) {
            try {
                returnTempByLocation(update.getMessage().getChatId(), update.getMessage().getLocation().getLatitude(),
                        update.getMessage().getLocation().getLongitude());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ReplyKeyboardMarkup replyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setKeyboard(keyboardRows());

        return replyKeyboardMarkup;
    }

    public List<KeyboardRow> keyboardRows() {
        List<KeyboardRow> rows = new ArrayList<>();
        rows.add(new KeyboardRow(keyboardButtons()));
        return rows;
    }

    public List<KeyboardButton> keyboardButtons() {
        List<KeyboardButton> buttons = new ArrayList<>();
        KeyboardButton keyboardButton = new KeyboardButton("Send Location");
        keyboardButton.setRequestLocation(true);
        buttons.add(keyboardButton);
        return buttons;
    }
    private void returnTemp(long chatId, String userName, String city) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String temp = null;

        String correctCity = city;
        if (city.contains(" ")){
            city = city.replaceAll("\\s", "");
        }
        try {
            temp = String.valueOf(weatherAPIService
                    .getInfoByCity(city).getData().getValues().getTemperature());
        } catch (Exception e) {
            message.setText("Failed to find the city");
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error(e.getMessage());
                throw new RuntimeException(ex);
            }
        }
        message.setText(correctCity + " " +temp + "°C ");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            message.setText("Failed to find the city");
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                log.error(e.getMessage());
                throw new RuntimeException(ex);
            }
        }
    }

    private void returnTempByLocation(Long chatId, Double lat, Double lon) throws URISyntaxException, IOException, InterruptedException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        String temp = String.valueOf(weatherAPIService
                .getInfoByLocation(lat, lon).getData().getValues().getTemperature());
        message.setText("Temperature by your location:  "+temp + "°C ");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e) {
            message.setText("Failed to find the city by your location");
            try {
                execute(message);
            } catch (TelegramApiException ex) {
                throw new RuntimeException(ex);
            }
            log.error(e.getMessage());
        }
    }
    private void sendHelpText(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(textToSend);

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
    private void startBot(long chatId, String userName) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setReplyMarkup(replyKeyboardMarkup());
        message.setText("Welcome , " + userName + "! I'm ready to serve you. \n\n" +
                "If you want to get weather in some city, then you can send me name of the City.\n\n" +
                "If you want to get weather by your location, then you can send me your location and " +
                "i'll return current weather by your coordinates.");

        try {
            execute(message);
            log.info("Reply sent");
        } catch (TelegramApiException e){
            log.error(e.getMessage());
        }
    }
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}