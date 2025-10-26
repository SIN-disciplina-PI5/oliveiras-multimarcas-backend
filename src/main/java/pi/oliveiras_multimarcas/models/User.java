package pi.oliveiras_multimarcas.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pi.oliveiras_multimarcas.DTO.UserRequestDTO;
import pi.oliveiras_multimarcas.models.enums.UserPosition; 

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class User implements UserDetails { 
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    @Email
    @Column(unique = true) 
    private String email;
    
    @Column
    private String password;

   
    @Enumerated(EnumType.STRING) 
    private UserPosition position; 

    public User(UserRequestDTO dto){
        this.email = dto.getEmail();
        this.password = dto.getPassword();
        this.position = UserPosition.USER; 
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        
        if (this.position == UserPosition.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getUsername() {
        return this.email; 
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}