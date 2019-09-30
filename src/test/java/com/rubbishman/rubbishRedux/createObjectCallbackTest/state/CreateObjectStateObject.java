package com.rubbishman.rubbishRedux.createObjectCallbackTest.state;

public class CreateObjectStateObject {
    public int id;
    public String message;

    public String toString() {
        return "{" +
                "   id: " + id +
                "   message: " + message +
                "}";
    }

    public CreateObjectStateObject clone() {
        CreateObjectStateObject clone = new CreateObjectStateObject();
        clone.id = id;
        clone.message = message;

        return clone;
    }
}
