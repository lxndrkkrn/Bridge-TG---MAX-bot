package com.example.bridgebot_tg;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service

public class UserStateService {

    private final Map<Long, BotState> userStates = new ConcurrentHashMap<>();

    public void setState(Long chatId, BotState state) {
        userStates.put(chatId, state);
    }

    public BotState getState(Long chatId) {
        return userStates.getOrDefault(chatId, BotState.DEFAULT);
    }

}
