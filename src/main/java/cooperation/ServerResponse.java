package cooperation;

import java.util.HashMap;
import java.util.Map;

public class ServerResponse {

    private Map<String, Object> data;
    private boolean error;

    public ServerResponse() {
        this.data = new HashMap<>();
    }

    public ServerResponse(Map<String, Object> data, boolean error) {
        this.data = data;
        this.error = error;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putData(String name, Object obj) {
        data.put(name, obj);
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
