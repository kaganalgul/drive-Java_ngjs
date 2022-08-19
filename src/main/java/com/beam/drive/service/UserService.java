package com.beam.drive.service;

import com.beam.drive.dto.AuthenticationRequest;
import com.beam.drive.dto.AuthenticationResponse;
import com.beam.drive.model.User;
import com.beam.drive.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    public static final String SESSION_ACCOUNT = "account";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void register(User user) {
        if (userRepository.findByEmail(user.getEmail()).orElse(null) == null) {
            User newUser = new User()
                    .setName(user.getName())
                    .setSurname(user.getSurname())
                    .setEmail(user.getEmail())
                    .setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(newUser);
        }
    }

    public AuthenticationResponse login(AuthenticationRequest request) {

        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        AuthenticationResponse response = new AuthenticationResponse();

        if (optionalUser.isEmpty()) {
            response.setCode(1);
            return response;
        } else {
            if (passwordEncoder.matches(request.getPassword(), optionalUser.get().getPassword())) {
                User user = optionalUser.get();
                response.setCode(0)
                        .setUser(user);

                return response;
            } else {
                response.setCode(-1);
                return response;
            }
        }
    }
}
