package context;

import java.nio.channels.SelectionKey;

public abstract class Context {

    protected Request request;
    protected Response response;

    public abstract void setContext(String requestHeader, SelectionKey key);

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}