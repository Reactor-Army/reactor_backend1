package fiuba.tpp.reactorapp.model.auth.request;

public class ResetPasswordRequest {

    private String code;
    private String password;

    public ResetPasswordRequest(String code, String password) {
        this.code = code;
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
