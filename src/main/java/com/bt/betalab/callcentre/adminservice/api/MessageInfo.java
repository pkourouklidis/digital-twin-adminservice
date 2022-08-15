/**
 * @author Joost Noppen (611749237), BetaLab, Applied Research
 * Date: 15/08/2022
 * Copyright (c) British Telecommunications plc 2022
 **/


package com.bt.betalab.callcentre.adminservice.api;

public class MessageInfo {
    private int messages_ready;
    private int messages_unacknowledged;

    public int getMessages_ready() {
        return messages_ready;
    }

    public void setMessages_ready(int messages_ready) {
        this.messages_ready = messages_ready;
    }

    public int getMessages_unacknowledged() {
        return messages_unacknowledged;
    }

    public void setMessages_unacknowledged(int messages_unacknowledged) {
        this.messages_unacknowledged = messages_unacknowledged;
    }
}
