package pi.oliveiras_multimarcas.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.exceptions.InvalidArguments;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.repositories.UserRepositorie;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepositorie userRepositorie;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<User> findAll() {
        logger.info("Finding all users");
        return userRepositorie.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        logger.info("Finding user by id: {}", id);
        return userRepositorie.findById(id)
                .orElseThrow(() -> new NoSuchException("User not found with id: " + id));
    }

    @Transactional
    public User create(UserRequestDTO userRequestDTO) {
        logger.info("Creating new user");
        if (userRequestDTO == null) {
            throw new InvalidArguments("User object cannot be null");
        }
        User user = new User();
        user.setName(userRequestDTO.getName());
        user.setEmail(userRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        return userRepositorie.save(user);
    }

    @Transactional
    public User update(UUID id, UserRequestDTO userRequestDTO) {
        logger.info("Updating user with id: {}", id);
        if (userRequestDTO == null) {
            throw new InvalidArguments("User object cannot be null");
        }
        User existingUser = findById(id);
        existingUser.setName(userRequestDTO.getName());
        existingUser.setEmail(userRequestDTO.getEmail());
        if (userRequestDTO.getPassword() != null && !userRequestDTO.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        }
        return userRepositorie.save(existingUser);
    }

    @Transactional
    public void delete(UUID id) {
        logger.info("Deleting user with id: {}", id);
        if (!userRepositorie.existsById(id)) {
            throw new NoSuchException("User not found with id: " + id);
        }
        userRepositorie.deleteById(id);
    }

    public User findByEmail(String email) {
        return userRepositorie.findByEmail(email).orElseThrow(() -> new NoSuchException("User not found with email: " + email));
    }
}
