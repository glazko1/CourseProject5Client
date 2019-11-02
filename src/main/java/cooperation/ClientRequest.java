package cooperation;

import java.util.Map;

public class ClientRequest {

    private String commandName;
    private Map<String, Object> data;

    public ClientRequest() {}

    public ClientRequest(String commandName, Map<String, Object> data) {
        this.commandName = commandName;
        this.data = data;
    }

    public String getCommandName() {
        return commandName;
    }

    public Map<String, Object> getData() {
        return data;
    }
}
