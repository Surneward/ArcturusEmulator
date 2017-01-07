package com.eu.habbo.messages.incoming.camera;

import com.eu.habbo.Emulator;
import com.eu.habbo.habbohotel.users.HabboInfo;
import com.eu.habbo.messages.incoming.MessageHandler;
import com.eu.habbo.messages.outgoing.camera.CameraPublishWaitMessageComposer;
import com.eu.habbo.messages.outgoing.camera.CameraURLComposer;
import com.eu.habbo.messages.outgoing.generic.alerts.GenericAlertComposer;
import com.eu.habbo.networking.camera.CameraClient;
import com.eu.habbo.networking.camera.messages.outgoing.CameraRenderImageComposer;
import com.eu.habbo.util.crypto.ZIP;

import java.io.ByteArrayOutputStream;
import java.util.zip.Inflater;

public class CameraRoomPictureEvent extends MessageHandler
{
    @Override
    public void handle() throws Exception
    {
        if (!this.client.getHabbo().hasPermission("acc_camera"))
        {
            this.client.sendResponse(new GenericAlertComposer(Emulator.getTexts().getValue("camera.permission")));
            return;
        }

        if (CameraClient.isLoggedIn)
        {
            int seconds = Emulator.getIntUnixTimestamp() - this.client.getHabbo().getHabboInfo().getPhotoTimestamp();
            if (seconds < (60 * 2))
            {
                this.client.sendResponse(new CameraPublishWaitMessageComposer(false, seconds - (60 * 2), ""));
                return;
            }

            this.packet.getBuffer().readFloat();

            byte[] data = this.packet.getBuffer().readBytes(this.packet.getBuffer().readableBytes()).array();

            String content = new String(ZIP.inflate(data));

            Emulator.getLogging().logDebugLine(content);

            CameraRenderImageComposer composer = new CameraRenderImageComposer(this.client.getHabbo().getHabboInfo().getId(), this.client.getHabbo().getHabboInfo().getCurrentRoom().getBackgroundTonerColor().getRGB(), 320, 320, content);

            this.client.getHabbo().getHabboInfo().setPhotoJSON(Emulator.getConfig().getValue("camera.extradata").replace("%timestamp%", composer.timestamp + ""));
            this.client.getHabbo().getHabboInfo().setPhotoTimestamp(composer.timestamp);

            Emulator.getCameraClient().sendMessage(composer);
        }
        else
        {
            this.client.sendResponse(new GenericAlertComposer(Emulator.getTexts().getValue("camera.disabled")));
        }

    }
}
