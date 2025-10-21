package pi.oliveiras_multimarcas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import pi.oliveiras_multimarcas.exceptions.NoSuchException;
import pi.oliveiras_multimarcas.models.Token;
import pi.oliveiras_multimarcas.repositories.TokenRepositorie;

import java.util.Optional;

public class TokenService {

    @Autowired
    TokenRepositorie tokenRepositorie;

    @Transactional(readOnly = true)
    public boolean isTokenActive(String token){
        Optional<Token> tokenFinded = tokenRepositorie.findByToken(token);
        return tokenFinded.isPresent();
    }

    @Transactional
    public Token insert(String token){
        Token newToken = new Token();
        newToken.setToken(token);

        newToken = tokenRepositorie.save(newToken);

        return newToken;
    }

    @Transactional
    public void deleteByToken(String token){
        Optional<Token> tokenFinded = tokenRepositorie.findByToken(token);
        if (tokenFinded.isEmpty()) throw new NoSuchException("Token");

        tokenRepositorie.deleteByToken(token);
    }
}
