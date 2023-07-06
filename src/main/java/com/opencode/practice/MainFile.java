package com.opencode.practice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import com.opencode.practice.models.Role;
import com.opencode.practice.models.UserStatus;
import com.opencode.practice.models.User;
import com.opencode.practice.repos.ReposUser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableJpaAuditing
public class MainFile {
	public static void main(String[] args) {
		SpringApplication.run(MainFile.class, args);
	}

	@Bean
	public CommandLineRunner initializeUsers(ReposUser userService) {
		return args -> {
			// Проверяем, существует ли уже пользователь с email "admin@mail.ru"
			boolean adminExists = userService.existsByEmail("admin@mail.ru");

			// Если пользователь с email "admin@mail.ru" не существует
			if (!adminExists) {
				User user = new User();
				user.setEmail("admin@mail.ru");
				user.setPassword("password");

				// Создаем экземпляр BCryptPasswordEncoder с силой шифрования 8
				BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
				// Шифруем пароль пользователя
				String encodedPassword = passwordEncoder.encode(user.getPassword());

				user.setPassword(encodedPassword);
				user.setRole(Role.ADMIN);
				user.setUserStatus(UserStatus.ACTIVE);
				userService.save(user);
			}
		};
	}
}

