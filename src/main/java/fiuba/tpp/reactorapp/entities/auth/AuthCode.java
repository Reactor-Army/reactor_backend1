package fiuba.tpp.reactorapp.entities.auth;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name ="AUTH_CODE")
public class AuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String code;

    private Date refreshDate;

    public AuthCode(User user, String code, Date refreshDate) {
        this.user = user;
        this.code = code;
        this.refreshDate = refreshDate;
    }

    public AuthCode() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getRefreshDate() {
        return refreshDate;
    }

    public void setRefreshDate(Date refreshDate) {
        this.refreshDate = refreshDate;
    }
}
