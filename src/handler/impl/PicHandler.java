package handler.impl;

import org.apache.log4j.Logger;

import context.Context;
import handler.abs.AbstractHandler;

public class PicHandler extends AbstractHandler{

    private Logger logger = Logger.getLogger(PicHandler.class);

    @Override
    public void doGet(Context context) {
        logger.info("进入了handler--->PicHandler");
        context.getResponse().setHtmlFile("src/res/garfield.jpeg");
        context.getResponse().setContentType("image/webp");
    }
}