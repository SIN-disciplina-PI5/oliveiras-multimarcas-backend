package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.User;
import pi.oliveiras_multimarcas.repositories.UserRepositorie;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepositorie userRepositorie;

    @Transactional
    public User insert(UserRequestDTO dto){

        User user = new User(dto);

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user = userRepositorie.save(user);

        return user;
    }

    @Transactional(readOnly = true)
    public User findById(UUID id){
        Optional<User> user = userRepositorie.findById(id);
        if (user.isEmpty()) throw new NoSuchException("Usuario");

        return user.get();
    }

    public User findByEmail(String email){
        Optional<User> user = userRepositorie.findByEmail(email);
        if (user.isEmpty()) throw new NoSuchException("Usuario");

        return user.get();
    }

    @Transactional(readOnly = true)
    public List<User> findAll(){

        return userRepositorie.findAll();
    }

    @Transactional
    public void deleteById(UUID id){
        if(!userRepositorie.existsById(id)) throw new NoSuchException("Usuario");
        userRepositorie.deleteById(id);
    }
}
