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
    @ManyToOne
    @JoinColumn(name="employee_id", nullable=true)
    private Employee employee;
    @Column(nullable = false)
    private LocalDateTime expires = LocalDateTime.now().plusDays(7);

    Token(String token, Employee employee){
        this.token = token;
        this.employee = employee;
    }
}
