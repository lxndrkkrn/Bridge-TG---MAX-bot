package com.example.bridgebot_tg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BridgeBotTgApplication {

	public static void main(String[] args) {
		SpringApplication.run(BridgeBotTgApplication.class, args);
	}

	@Bean
	public TelegramBotsApi telegramBotsApi(TelegramController telegramController) throws TelegramApiException {
		TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
		api.registerBot(telegramController);
		return api;
	}

}