package transfers;


import java.io.Serializable;


public class Response implements Serializable {
    private ResponseStatus responseStatus;
    private String responseBody;

    public Response(ResponseStatus responseStatus, String responseBody) {
        this.responseStatus = responseStatus;
        this.responseBody = responseBody;
    }
    public Response(){

    }

    public ResponseStatus getStatus() {
        return responseStatus;
    }

    public String getResponseBody() {
        return responseBody;
    }

    @Override
    public String toString() {
        return "Response[" + responseStatus + ", " + responseBody + "]";
    }
}