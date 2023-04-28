package Classes;

public class User
{
    private String email;
    private String userType;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public User(String email, String userType){
        this.userType = userType;
        this.email = email;
    }
}
