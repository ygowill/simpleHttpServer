package handler.impl;

import org.apache.log4j.Logger;

import context.Context;
import context.Response;
import handler.abs.AbstractHandler;

public class NotFoundHandler extends AbstractHandler {

    private Logger logger = Logger.getLogger(NotFoundHandler.class);
    private Response response;

    @Override
    public void doGet(Context context) {
        logger.info("进入了handler--->404Handler");
        response = context.getResponse();

        response.setStatuCode(404);
        response.setStatuCodeStr("Not Found");
        response.setHtmlFile("src/404.html");
    }
}