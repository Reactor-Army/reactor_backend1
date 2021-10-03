package fiuba.tpp.reactorapp.entities.auth;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(	name = "\"Token\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "hashToken")
        })
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hashToken;

    private Date createdAt;

    public Token() {
    }

    public Token(String hashToken, Date createdAt) {
        this.hashToken = hashToken;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHashToken() {
        return hashToken;
    }

    public void setHashToken(String hashToken) {
        this.hashToken = hashToken;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
