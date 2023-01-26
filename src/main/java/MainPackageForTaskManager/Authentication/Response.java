package MainPackageForTaskManager.Authentication;


// class that represents an authentication response
public class Response {
    private String jwt;

    public Response(String jwt) {
        this.jwt=jwt;
    }

    public Response() {
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}

  /*  store the JWT that is returned to the client upon successful authentication,
   and is used to authenticate the user in subsequent requests.

  this class could be used as the response payload in an API endpoint for authenticating users.
  The API endpoint would receive an HTTP POST request with the user's credentials,
  authenticate the user, and return an HTTP response with the JWT in the body.
  The client can then use the JWT to authenticate itself in subsequent requests by including the JWT in the
   "Authorization" header of the request.*/