package com.task1.Task.scheduler;

import com.task1.Task.entity.User;
import com.task1.Task.repository.UserRepository;
import com.task1.Task.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class InactiveUsersScheduler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

//    @Scheduled(cron = "0 0 0 * * ?") // Runs every midnight
//@Scheduled(fixedRate = 60000)
@Scheduled(cron = "${cron.expression.test}") //alternate for 60sec
public void checkInactiveUsers() {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
//    LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusMinutes(1);

        List<User> inactiveUsers = userRepository.findInactiveUsers(thirtyDaysAgo);

        for (User user : inactiveUsers) {
            emailService.sendEmail(user.getEmail(),
                    "We miss you!",
                    "You haven't posted in a while. Come back and share something new!");
        }
    }
}
