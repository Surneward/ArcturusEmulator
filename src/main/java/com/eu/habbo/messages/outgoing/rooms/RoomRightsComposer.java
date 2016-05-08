package com.eu.habbo.messages.outgoing.rooms;

import com.eu.habbo.habbohotel.rooms.RoomRightLevels;
import com.eu.habbo.messages.ServerMessage;
import com.eu.habbo.messages.outgoing.MessageComposer;
import com.eu.habbo.messages.outgoing.Outgoing;

public class RoomRightsComposer extends MessageComposer{

    private final RoomRightLevels type;

    public RoomRightsComposer(RoomRightLevels type)
    {
        this.type = type;
    }

    @Override
    public ServerMessage compose()
    {
        this.response.init(Outgoing.RoomRightsComposer);
        this.response.appendInt32(this.type.level);
        return this.response;
    }
}
