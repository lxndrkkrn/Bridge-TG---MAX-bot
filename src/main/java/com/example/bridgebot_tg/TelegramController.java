package com.example.bridgebot_tg;

import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;

import static java.awt.SystemColor.text;

@Component
public class TelegramController extends TelegramLongPollingBot {

    private final UserService userService;
    private final KeyBoards keyBoards;
    private final UserStateService userStateService;

    public TelegramController(UserService userService, KeyBoards keyBoards, UserStateService userStateService) {
        this.userService = userService;
        this.keyBoards = keyBoards;
        this.userStateService = userStateService;
    }

    @Override
    public String getBotUsername() {
        return "BridgeTG_MAX_bot";
    }

    @Override
    public String getBotToken() {
        return "8521753323:AAFVCUtKO-q-aTwT1O3qwTcHTFY3cPWR4HQ";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();
            BotState state = userStateService.getState(chatId);
            String tgUser = update.getMessage().getFrom().getUserName();

            SendMessage sm = new SendMessage();
            sm.setChatId(chatId);

            if (tgUser.equals("lxndrkkrn")) {
                if ("/admin".equals(text)) {
                    sm.setText("Панель активирована");
                    sm.setReplyMarkup(keyBoards.AdminReplyKeyboard());
                    userStateService.setState(chatId, BotState.ADMIN_PANEL);

                }  else if ("Создать токен".equals(text) && state == BotState.ADMIN_PANEL) {
                    sm.setText("Введи имя для мользователя:");
                    userStateService.setState(chatId, BotState.WAITING_FOR_USER_ADD);

                } else if ("Удалить токен".equals(text) && state == BotState.ADMIN_PANEL) {
                    sm.setText("Введи токен для удаления:");
                    userStateService.setState(chatId, BotState.WAITING_FOR_USER_REMOVE);

                } else if ("Инфо".equals(text) && state == BotState.ADMIN_PANEL) {
                    sm.setText(userService.findAllUsers().toString());

                } else if ("Инфо по токену".equals(text) && state == BotState.ADMIN_PANEL) {
                    sm.setText("Введи токен для получения информации:");
                    userStateService.setState(chatId, BotState.WAITING_FOR_USER_INFO);

                } else if ("Закрыть панель".equals(text) && state == BotState.ADMIN_PANEL) {
                    sm.setText("Панель выключена");
                    userStateService.setState(chatId, BotState.DEFAULT);

                } else if (state == BotState.WAITING_FOR_USER_ADD) {
                    sm.setText("200: OK, новый токен: " + userService.saveUser(text, LocalDateTime.now(), LocalDateTime.now().plusDays(30)));
                    userStateService.setState(chatId, BotState.ADMIN_PANEL);

                } else if (state == BotState.WAITING_FOR_USER_REMOVE) {
                    sm.setText("200: OK, токен удалён");
                    userService.removeUser(text);
                    userStateService.setState(chatId, BotState.ADMIN_PANEL);

                } else if (state == BotState.WAITING_FOR_USER_INFO) {
                    sm.setText("Информация о токене " + text + " : " + userService.findInfoUser(text));
                    userStateService.setState(chatId, BotState.ADMIN_PANEL);

                } else {sm.setText("Неизвестная команда");}
            }

            if (state == BotState.WAITING_FOR_TOKEN) {
                tokenInput(chatId, text);
                return;
            }

            try {
                execute(sm);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void tokenInput(Long chatId, String token) {
        userService.findInfoUser(token);

    }

}
