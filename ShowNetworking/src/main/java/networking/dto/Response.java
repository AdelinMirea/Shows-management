package networking.dto;

import networking.Request;

public class Response {

    private Request.Type type;
    private Object rsvp;

    public Response build(String type){
        if(type.equals(Request.Type.LOGIN.toString())){
            this.type = Request.Type.LOGIN;
        }else if(type.equals(Request.Type.BUY.toString())){
            this.type = Request.Type.BUY;
        }else if(type.equals(Request.Type.SEARCH.toString())){
            this.type = Request.Type.SEARCH;
        }else if(type.equals(Request.Type.FINDALL.toString())){
            this.type = Request.Type.FINDALL;
        }
        return this;
    }

    public Response setResponse(Object rsvp){
        this.rsvp = rsvp;
        return this;
    }

    public Request.Type getType() {
        return type;
    }

    public Object getRsvp() {
        return rsvp;
    }


}
