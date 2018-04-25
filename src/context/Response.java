package context;

import java.nio.channels.SelectionKey;

public interface Response {

    public static final String SERVER_NAME = "";

    public String getContentType();

    public int getStatuCode();

    public String getStatuCodeStr();

    public String getHtmlFile();

    public void setHtmlFile(String htmlFile);

    public SelectionKey getKey();

    public void setContentType(String contentType);

    public void setStatuCode(int statuCode);

    public void setStatuCodeStr(String statuCodeStr);
}