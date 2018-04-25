package context;

import java.util.Map;
import java.util.Set;

public interface Request {

    public static final String POST = "POST";

    public static final String GET = "GET";

    public Map<String, Object> getAttribute();

    public String getMethod();

    public String getUri();

    public String getProtocol();

    public Map<String, Object> getHeaders();

    public Set<String> getHeaderNames();

    public Object getHeader(String key);
}