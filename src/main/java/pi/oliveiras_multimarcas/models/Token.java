package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private UUID userId;
    @Column(nullable = false)
    private LocalDateTime expires;

    Token(String token, UUID userId, LocalDateTime expites){
        this.token = token;
        this.userId = userId;
        this.expires = expites;
    }
}
