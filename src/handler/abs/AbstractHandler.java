package handler.abs;

import context.Context;
import context.Request;
import handler.Handler;
import handler.ResponseHandler;

public class AbstractHandler implements Handler {

    protected Context context;

    @Override
    public void init(Context context) {
        this.context = context;
        this.service(context);
    }

    @Override
    public void service(Context context) {
        //通过请求方式选择doGET或者doPost
        String method = context.getRequest().getMethod();
        if(method.equals(Request.GET)) {
            this.doGet(context);
        } else if (method.equals(Request.POST)) {
            this.doPost(context);
        }
        sendResponse(context);
    }

    @Override
    public void doGet(Context context) {

    }

    @Override
    public void doPost(Context context) {

    }

    @Override
    public void destory(Context context) {
        context = null;
    }

    private void sendResponse(Context context) {
        new ResponseHandler().write(context);
    }

}