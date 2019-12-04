package cooperation;

import java.util.HashMap;
import java.util.Map;

public class ServerResponse {

    private Map<String, Object> data;
    private boolean error;
    private String errorMessage;

    public ServerResponse() {
        this.data = new HashMap<>();
        this.errorMessage = "";
    }

    public ServerResponse(Map<String, Object> data, boolean error, String errorMessage) {
        this.data = data;
        this.error = error;
        this.errorMessage = errorMessage;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}