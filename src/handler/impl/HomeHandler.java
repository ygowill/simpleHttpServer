package handler.impl;

import org.apache.log4j.Logger;

import context.Context;
import handler.abs.AbstractHandler;

public class HomeHandler extends AbstractHandler{

    private Logger logger = Logger.getLogger(HomeHandler.class);

    @Override
    public void doGet(Context context) {
        logger.info("进入了handler--->HomeHandler");
        context.getResponse().setHtmlFile("src/home.html");
    }
}