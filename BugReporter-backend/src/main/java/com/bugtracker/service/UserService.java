package com.bugtracker.service;

import com.bugtracker.entity.User;
import com.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username-ul exista deja");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email-ul exista deja");
        }
        if(userRepository.findByPhone(user.getPhone()).isPresent()){
            throw new RuntimeException("Numarul de telefon exista deja");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username-ul nu exista"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Nume de utilizator sau parola incorecta");
        }

        if(user.isBanned()){
            throw new RuntimeException("Contul tau a fost suspendat");
        }

        return user;
    }

    public Optional<List<User>> searchUsersByUsername(String username){
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }

    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id).orElseThrow();
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        return userRepository.save(user);
    }

    @Transactional
    public User banUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setBanned(true);
        User savedUser = userRepository.save(user);

        String subiect = "Contul tău a fost suspendat";
        String mesaj = "Salut, " + savedUser.getUsername() +
                ". Îți aducem la cunoștință că acest cont a fost suspendat pe o perioadă " +
                "nedeterminată de pe aplicația TrackMyBug.";

        if (savedUser.getEmail() != null && !savedUser.getEmail().isEmpty()) {
            emailService.sendEmail(savedUser.getEmail(), subiect, mesaj);
        }

        if (savedUser.getPhone() != null && !savedUser.getPhone().isEmpty()) {
            try {
                RestTemplate restTemplate = new RestTemplate();
                String url = "http://localhost:3000/send-message";

                Map<String, String> requestBody = Map.of(
                        "phone", savedUser.getPhone(),
                        "message", mesaj
                );

                restTemplate.postForObject(url, requestBody, Map.class);

            } catch (Exception e) {
                System.err.println("Eroare la trimiterea mesajului pe WhatsApp: " + e.getMessage());
            }
        }

        return savedUser;
    }

    public User unbanUser(Long id){
        User user=userRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("User not found"));

        user.setBanned(false);
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}