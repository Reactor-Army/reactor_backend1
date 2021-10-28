package fiuba.tpp.reactorapp.entities.auth;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(	name = "\"Token\"",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "user_id")
        })
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String device;

    private String hashToken;

    private Date createdAt;

    public Token() {
    }

    public Token(User user, String hashToken, Date createdAt, String device) {
        this.user = user;
        this.hashToken = hashToken;
        this.createdAt = createdAt;
        this.device = device;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}
