package fiuba.tpp.reactorapp.model.auth.response;

public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private UserResponse user;


    public LoginResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
