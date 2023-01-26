package MainPackageForTaskManager.Authentication;


// it represents an authentication request in a Spring Boot application with security enabled.

public class Request {
    private String username;
    private String password;

    public Request() {
    }

    public Request(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }
}
/* storing the credentials of a user who is attempting to authenticate, and is passed to the authentication
mechanism for verification.
this class could be used as the request payload in an API endpoint for authenticating users.
The API endpoint would receive an HTTP POST
request with the username and password fields in the request body, and use
them to authenticate the user. If the authentication is successful,
the API might return a JWT or
other type of token that can be used to authenticate the user in subsequent requests.*/