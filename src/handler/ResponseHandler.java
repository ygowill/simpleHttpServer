package handler;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;

import org.apache.log4j.Logger;

import context.Context;
import context.Request;
import context.Response;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;

public class ResponseHandler {

    private Request request;
    private Response response;
    private String protocol;
    private int statuCode;
    private String statuCodeStr;
    private ByteBuffer buffer;
    private String serverName;
    private String contentType;
    private SocketChannel channel;
    private Selector selector;
    private SelectionKey key;
    private Logger logger = Logger.getLogger(ResponseHandler.class);
    private BufferedReader reader;
    private String htmlFile;

    static BASE64Encoder encoder = new sun.misc.BASE64Encoder();

    public void write(Context context) {
        //从context中得到相应的参数
        request = context.getRequest();
        response = context.getResponse();
        protocol = request.getProtocol();
        statuCode = response.getStatuCode();
        statuCodeStr = response.getStatuCodeStr();
        serverName = Response.SERVER_NAME;
        contentType = response.getContentType();
        key = response.getKey();
        selector = key.selector();
        channel = (SocketChannel)key.channel();
        htmlFile = response.getHtmlFile();

        //得到响应正文内容
        byte[] html = setHtml(context);


        StringBuilder sb = new StringBuilder();
        //状态行
        sb.append(protocol + " " + statuCode + " " + statuCodeStr + "\r\n");
        //响应头
        sb.append("Server: " + serverName + "\r\n");
        sb.append("Content-Type: " + contentType + "\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        if(reader != null) {
            sb.append("Content-Length: " + html.length + "\r\n");
        }

        //响应内容
        sb.append("\r\n");

        buffer = ByteBuffer.allocate(sb.length()*10);
        buffer.put(sb.toString().getBytes());
        //从写模式，切换到读模式
        buffer.flip();
        try {
            logger.info("生成响应\r\n"+sb.toString());
            channel.register(selector, SelectionKey.OP_WRITE);
            channel.write(buffer);
            buffer.flip();
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            buffer = ByteBuffer.allocate(html.length);
            buffer.put(html);
            buffer.flip();
            channel.register(selector, SelectionKey.OP_WRITE);
            channel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private byte[] setHtml(Context context) {
        byte[] data = null;
        StringBuilder html = null;
        if(htmlFile != null && htmlFile.length() > 0) {
            html = new StringBuilder();
            try {
                File f = new File(htmlFile);
                if(contentType.equals("image/webp")){
                    FileImageInputStream input = null;
                    try {
                        input = new FileImageInputStream(f);
                        ByteArrayOutputStream output = new ByteArrayOutputStream();
                        byte[] buf = new byte[1024];
                        int numBytesRead = 0;
                        while ((numBytesRead = input.read(buf)) != -1) {
                            output.write(buf, 0, numBytesRead);
                        }
                        String tmp="\r\n";
                        output.write(tmp.getBytes(),0,tmp.getBytes().length);
                        data = output.toByteArray();
                        output.close();
                        input.close();
                    }
                    catch (FileNotFoundException ex1) {
                        ex1.printStackTrace();
                    }
                }
                else{
                    reader = new BufferedReader(new FileReader(f));
                    String htmlStr;
                    htmlStr = reader.readLine();
                    while(htmlStr != null) {
                        html.append(htmlStr + "\r\n");
                        htmlStr = reader.readLine();
                    }
                    data = html.toString().getBytes();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}
