package service.splitwise.response;

import common.response.Response;
import service.User.User;

public class SplitBillResponse {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public SplitBillResponse splitBillResponseWithSuccess(String message) {
        Response response = new Response();
        response.setMessage(message);
        response.setStatus(true);
        setResponse(response);
        return this;
    }

    public SplitBillResponse splitBillResponseWithFailure(String message) {
        Response response = new Response();
        response.setMessage(message);
        response.setStatus(false);
        setResponse(response);
        return this;
    }
}



