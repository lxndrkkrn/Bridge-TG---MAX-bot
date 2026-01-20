package com.example.bridgebot_tg;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public String saveUser(String name, LocalDateTime localStartDate, LocalDateTime localEndDate) {

        User user = new User();

        user.setUserName(name);
        user.setStartDate(localStartDate);
        user.setEndDate(localEndDate);
        user.setUserRegisterToken(createToken());

        userRepository.save(user);

        return user.getUserRegisterToken();
    }

    @Transactional
    public void removeUser(String token) {
            userRepository.deleteById(userRepository.findByUserRegisterToken(token).get().getId());
    }

    public String findInfoUser(String token) {
        return userRepository.findByUserRegisterToken(token)
                .map(user -> "Информация о токене: " + user.toString()) // Если нашли
                .orElse("Ошибка: Пользователь с таким токеном не найден.");
    }

    @Transactional
    public String createToken() {

        StringBuilder token = new StringBuilder(29);
        String characters = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        Random random = new Random();

        for (int i = 0; i < 25; i++) {
            int randomIndex = random.nextInt(characters.length());
            token.append(characters.charAt(randomIndex));
        }

        return token.toString();
    }

}
