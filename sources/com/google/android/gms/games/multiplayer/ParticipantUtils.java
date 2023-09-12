package com.google.android.gms.games.multiplayer;

import com.google.android.gms.games.Player;
import com.google.android.gms.internal.dm;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class ParticipantUtils {
    private ParticipantUtils() {
    }

    public static boolean Q(String str) {
        dm.a(str, "Participant ID must not be null");
        return str.startsWith("p_");
    }

    public static String getParticipantId(ArrayList<Participant> participants, String playerId) {
        int size = participants.size();
        for (int i = 0; i < size; i++) {
            Participant participant = participants.get(i);
            Player player = participant.getPlayer();
            if (player != null && player.getPlayerId().equals(playerId)) {
                return participant.getParticipantId();
            }
        }
        return null;
    }
}
