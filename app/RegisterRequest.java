public class RegisterRequest
{
    private String userName;
    private String loginId;
    private String password;
    private String provider;

    public RegisterRequest(String userName, String loginId, String password) {
        this.userName = userName;
        this.loginId = loginId;
        this.password = password;
        this.provider = "LOCAL";
    }
}
