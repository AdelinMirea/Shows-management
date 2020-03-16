package networking;

import networking.dto.Requestable;

public class Request {

    private Type type;
    private Requestable requestable;

    public enum Type {
        LOGIN, BUY, SEARCH, FINDALL, LOGOUT
    }

    public Request build(Type type) {
        this.type = type;
        return this;
    }

    public Request setObject(Requestable requestable) {
        this.requestable = requestable;
        return this;
    }

    @Override
    public String toString() {
        if (requestable != null) {
            return type.toString() + "`" + requestable.toRequest() + "\n";
        }else{
            return type.toString() + "`\n";
        }
    }
}
