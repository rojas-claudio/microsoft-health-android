package com.google.android.gms.games.multiplayer.realtime;

import android.os.Bundle;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.internal.dm;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public final class RoomConfig {
    private final String nM;
    private final int nR;
    private final RoomUpdateListener od;
    private final RoomStatusUpdateListener oe;
    private final RealTimeMessageReceivedListener of;
    private final String[] og;
    private final Bundle oh;
    private final boolean oi;

    /* loaded from: classes.dex */
    public static final class Builder {
        int nR;
        final RoomUpdateListener od;
        RoomStatusUpdateListener oe;
        RealTimeMessageReceivedListener of;
        Bundle oh;
        boolean oi;
        String oj;
        ArrayList<String> ok;

        private Builder(RoomUpdateListener updateListener) {
            this.oj = null;
            this.nR = -1;
            this.ok = new ArrayList<>();
            this.oi = false;
            this.od = (RoomUpdateListener) dm.a(updateListener, "Must provide a RoomUpdateListener");
        }

        public Builder addPlayersToInvite(ArrayList<String> playerIds) {
            dm.e(playerIds);
            this.ok.addAll(playerIds);
            return this;
        }

        public Builder addPlayersToInvite(String... playerIds) {
            dm.e(playerIds);
            this.ok.addAll(Arrays.asList(playerIds));
            return this;
        }

        public RoomConfig build() {
            return new RoomConfig(this);
        }

        public Builder setAutoMatchCriteria(Bundle autoMatchCriteria) {
            this.oh = autoMatchCriteria;
            return this;
        }

        public Builder setInvitationIdToAccept(String invitationId) {
            dm.e(invitationId);
            this.oj = invitationId;
            return this;
        }

        public Builder setMessageReceivedListener(RealTimeMessageReceivedListener listener) {
            this.of = listener;
            return this;
        }

        public Builder setRoomStatusUpdateListener(RoomStatusUpdateListener listener) {
            this.oe = listener;
            return this;
        }

        public Builder setSocketCommunicationEnabled(boolean enableSockets) {
            this.oi = enableSockets;
            return this;
        }

        public Builder setVariant(int variant) {
            this.nR = variant;
            return this;
        }
    }

    private RoomConfig(Builder builder) {
        this.od = builder.od;
        this.oe = builder.oe;
        this.of = builder.of;
        this.nM = builder.oj;
        this.nR = builder.nR;
        this.oh = builder.oh;
        this.oi = builder.oi;
        this.og = (String[]) builder.ok.toArray(new String[builder.ok.size()]);
        if (this.of == null) {
            dm.a(this.oi, "Must either enable sockets OR specify a message listener");
        }
    }

    public static Builder builder(RoomUpdateListener listener) {
        return new Builder(listener);
    }

    public static Bundle createAutoMatchCriteria(int minAutoMatchPlayers, int maxAutoMatchPlayers, long exclusiveBitMask) {
        Bundle bundle = new Bundle();
        bundle.putInt(GamesClient.EXTRA_MIN_AUTOMATCH_PLAYERS, minAutoMatchPlayers);
        bundle.putInt(GamesClient.EXTRA_MAX_AUTOMATCH_PLAYERS, maxAutoMatchPlayers);
        bundle.putLong(GamesClient.EXTRA_EXCLUSIVE_BIT_MASK, exclusiveBitMask);
        return bundle;
    }

    public Bundle getAutoMatchCriteria() {
        return this.oh;
    }

    public String getInvitationId() {
        return this.nM;
    }

    public String[] getInvitedPlayerIds() {
        return this.og;
    }

    public RealTimeMessageReceivedListener getMessageReceivedListener() {
        return this.of;
    }

    public RoomStatusUpdateListener getRoomStatusUpdateListener() {
        return this.oe;
    }

    public RoomUpdateListener getRoomUpdateListener() {
        return this.od;
    }

    public int getVariant() {
        return this.nR;
    }

    public boolean isSocketEnabled() {
        return this.oi;
    }
}
