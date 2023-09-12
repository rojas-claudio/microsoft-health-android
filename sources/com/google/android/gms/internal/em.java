package com.google.android.gms.internal;

import android.content.Context;
import android.content.Intent;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.games.Game;
import com.google.android.gms.games.GameBuffer;
import com.google.android.gms.games.GameEntity;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.OnGamesLoadedListener;
import com.google.android.gms.games.OnPlayersLoadedListener;
import com.google.android.gms.games.OnSignOutCompleteListener;
import com.google.android.gms.games.Player;
import com.google.android.gms.games.PlayerBuffer;
import com.google.android.gms.games.PlayerEntity;
import com.google.android.gms.games.achievement.AchievementBuffer;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.google.android.gms.games.achievement.OnAchievementsLoadedListener;
import com.google.android.gms.games.leaderboard.LeaderboardBuffer;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.OnLeaderboardMetadataLoadedListener;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.leaderboard.SubmitScoreResult;
import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.InvitationBuffer;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.OnInvitationsLoadedListener;
import com.google.android.gms.games.multiplayer.ParticipantUtils;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessage;
import com.google.android.gms.games.multiplayer.realtime.RealTimeMessageReceivedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeReliableMessageSentListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomStatusUpdateListener;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.internal.de;
import com.google.android.gms.internal.er;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public final class em extends de<er> {
    private final String it;
    private final String mF;
    private final Map<String, et> mG;
    private PlayerEntity mH;
    private GameEntity mI;
    private final es mJ;
    private boolean mK;
    private final Binder mL;
    private final long mM;
    private final boolean mN;

    /* loaded from: classes.dex */
    abstract class a extends c {
        private final ArrayList<String> mO;

        a(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar);
            this.mO = new ArrayList<>();
            for (String str : strArr) {
                this.mO.add(str);
            }
        }

        @Override // com.google.android.gms.internal.em.c
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            a(roomStatusUpdateListener, room, this.mO);
        }

        protected abstract void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList);
    }

    /* loaded from: classes.dex */
    final class aa extends a {
        aa(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersDisconnected(room, arrayList);
        }
    }

    /* loaded from: classes.dex */
    final class ab extends a {
        ab(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerInvitedToRoom(room, arrayList);
        }
    }

    /* loaded from: classes.dex */
    final class ac extends a {
        ac(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerJoined(room, arrayList);
        }
    }

    /* loaded from: classes.dex */
    final class ad extends a {
        ad(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerLeft(room, arrayList);
        }
    }

    /* loaded from: classes.dex */
    final class ae extends el {
        private final OnPlayersLoadedListener ne;

        ae(OnPlayersLoadedListener onPlayersLoadedListener) {
            this.ne = (OnPlayersLoadedListener) dm.a(onPlayersLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void e(com.google.android.gms.common.data.d dVar) {
            em.this.a(new af(this.ne, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class af extends de<er>.c<OnPlayersLoadedListener> {
        af(OnPlayersLoadedListener onPlayersLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onPlayersLoadedListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(OnPlayersLoadedListener onPlayersLoadedListener, com.google.android.gms.common.data.d dVar) {
            onPlayersLoadedListener.onPlayersLoaded(dVar.getStatusCode(), new PlayerBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class ag extends de<er>.b<RealTimeReliableMessageSentListener> {
        private final int iC;
        private final String nf;
        private final int ng;

        ag(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener, int i, int i2, String str) {
            super(realTimeReliableMessageSentListener);
            this.iC = i;
            this.ng = i2;
            this.nf = str;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            if (realTimeReliableMessageSentListener != null) {
                realTimeReliableMessageSentListener.onRealTimeMessageSent(this.iC, this.ng, this.nf);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class ah extends el {
        final RealTimeReliableMessageSentListener nh;

        public ah(RealTimeReliableMessageSentListener realTimeReliableMessageSentListener) {
            this.nh = realTimeReliableMessageSentListener;
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void a(int i, int i2, String str) {
            em.this.a(new ag(this.nh, i, i2, str));
        }
    }

    /* loaded from: classes.dex */
    final class ai extends c {
        ai(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomStatusUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.c
        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomAutoMatching(room);
        }
    }

    /* loaded from: classes.dex */
    final class aj extends el {
        private final RoomUpdateListener ni;
        private final RoomStatusUpdateListener nj;
        private final RealTimeMessageReceivedListener nk;

        public aj(RoomUpdateListener roomUpdateListener) {
            this.ni = (RoomUpdateListener) dm.a(roomUpdateListener, "Callbacks must not be null");
            this.nj = null;
            this.nk = null;
        }

        public aj(RoomUpdateListener roomUpdateListener, RoomStatusUpdateListener roomStatusUpdateListener, RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            this.ni = (RoomUpdateListener) dm.a(roomUpdateListener, "Callbacks must not be null");
            this.nj = roomStatusUpdateListener;
            this.nk = realTimeMessageReceivedListener;
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void a(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new ab(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void b(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new ac(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void c(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new ad(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void d(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new z(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void e(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new y(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void f(com.google.android.gms.common.data.d dVar, String[] strArr) {
            em.this.a(new aa(this.nj, dVar, strArr));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void n(com.google.android.gms.common.data.d dVar) {
            em.this.a(new am(this.ni, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void o(com.google.android.gms.common.data.d dVar) {
            em.this.a(new p(this.ni, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onLeftRoom(int statusCode, String externalRoomId) {
            em.this.a(new u(this.ni, statusCode, externalRoomId));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onP2PConnected(String participantId) {
            em.this.a(new w(this.nj, participantId));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onP2PDisconnected(String participantId) {
            em.this.a(new x(this.nj, participantId));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onRealTimeMessageReceived(RealTimeMessage message) {
            ep.b("GamesClient", "RoomBinderCallbacks: onRealTimeMessageReceived");
            em.this.a(new v(this.nk, message));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void p(com.google.android.gms.common.data.d dVar) {
            em.this.a(new al(this.nj, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void q(com.google.android.gms.common.data.d dVar) {
            em.this.a(new ai(this.nj, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void r(com.google.android.gms.common.data.d dVar) {
            em.this.a(new ak(this.ni, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void s(com.google.android.gms.common.data.d dVar) {
            em.this.a(new h(this.nj, dVar));
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void t(com.google.android.gms.common.data.d dVar) {
            em.this.a(new i(this.nj, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class ak extends b {
        ak(RoomUpdateListener roomUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.b
        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomConnected(i, room);
        }
    }

    /* loaded from: classes.dex */
    final class al extends c {
        al(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomStatusUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.c
        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onRoomConnecting(room);
        }
    }

    /* loaded from: classes.dex */
    final class am extends b {
        public am(RoomUpdateListener roomUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.b
        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onRoomCreated(i, room);
        }
    }

    /* loaded from: classes.dex */
    final class an extends el {
        private final OnSignOutCompleteListener nl;

        public an(OnSignOutCompleteListener onSignOutCompleteListener) {
            this.nl = (OnSignOutCompleteListener) dm.a(onSignOutCompleteListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onSignOutComplete() {
            em.this.a(new ao(this.nl));
        }
    }

    /* loaded from: classes.dex */
    final class ao extends de<er>.b<OnSignOutCompleteListener> {
        public ao(OnSignOutCompleteListener onSignOutCompleteListener) {
            super(onSignOutCompleteListener);
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(OnSignOutCompleteListener onSignOutCompleteListener) {
            onSignOutCompleteListener.onSignOutComplete();
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class ap extends el {
        private final OnScoreSubmittedListener nm;

        public ap(OnScoreSubmittedListener onScoreSubmittedListener) {
            this.nm = (OnScoreSubmittedListener) dm.a(onScoreSubmittedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void d(com.google.android.gms.common.data.d dVar) {
            em.this.a(new aq(this.nm, new SubmitScoreResult(dVar)));
        }
    }

    /* loaded from: classes.dex */
    final class aq extends de<er>.b<OnScoreSubmittedListener> {
        private final SubmitScoreResult nn;

        public aq(OnScoreSubmittedListener onScoreSubmittedListener, SubmitScoreResult submitScoreResult) {
            super(onScoreSubmittedListener);
            this.nn = submitScoreResult;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(OnScoreSubmittedListener onScoreSubmittedListener) {
            onScoreSubmittedListener.onScoreSubmitted(this.nn.getStatusCode(), this.nn);
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    abstract class b extends de<er>.c<RoomUpdateListener> {
        b(RoomUpdateListener roomUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomUpdateListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(RoomUpdateListener roomUpdateListener, com.google.android.gms.common.data.d dVar) {
            a(roomUpdateListener, em.this.x(dVar), dVar.getStatusCode());
        }

        protected abstract void a(RoomUpdateListener roomUpdateListener, Room room, int i);
    }

    /* loaded from: classes.dex */
    abstract class c extends de<er>.c<RoomStatusUpdateListener> {
        c(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomStatusUpdateListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            a(roomStatusUpdateListener, em.this.x(dVar));
        }

        protected abstract void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room);
    }

    /* loaded from: classes.dex */
    final class d extends el {
        private final OnAchievementUpdatedListener mQ;

        d(OnAchievementUpdatedListener onAchievementUpdatedListener) {
            this.mQ = (OnAchievementUpdatedListener) dm.a(onAchievementUpdatedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void onAchievementUpdated(int statusCode, String achievementId) {
            em.this.a(new e(this.mQ, statusCode, achievementId));
        }
    }

    /* loaded from: classes.dex */
    final class e extends de<er>.b<OnAchievementUpdatedListener> {
        private final int iC;
        private final String mR;

        e(OnAchievementUpdatedListener onAchievementUpdatedListener, int i, String str) {
            super(onAchievementUpdatedListener);
            this.iC = i;
            this.mR = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(OnAchievementUpdatedListener onAchievementUpdatedListener) {
            onAchievementUpdatedListener.onAchievementUpdated(this.iC, this.mR);
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class f extends el {
        private final OnAchievementsLoadedListener mS;

        f(OnAchievementsLoadedListener onAchievementsLoadedListener) {
            this.mS = (OnAchievementsLoadedListener) dm.a(onAchievementsLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void b(com.google.android.gms.common.data.d dVar) {
            em.this.a(new g(this.mS, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class g extends de<er>.c<OnAchievementsLoadedListener> {
        g(OnAchievementsLoadedListener onAchievementsLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onAchievementsLoadedListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(OnAchievementsLoadedListener onAchievementsLoadedListener, com.google.android.gms.common.data.d dVar) {
            onAchievementsLoadedListener.onAchievementsLoaded(dVar.getStatusCode(), new AchievementBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class h extends c {
        h(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomStatusUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.c
        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onConnectedToRoom(room);
        }
    }

    /* loaded from: classes.dex */
    final class i extends c {
        i(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomStatusUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.c
        public void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room) {
            roomStatusUpdateListener.onDisconnectedFromRoom(room);
        }
    }

    /* loaded from: classes.dex */
    final class j extends el {
        private final OnGamesLoadedListener mT;

        j(OnGamesLoadedListener onGamesLoadedListener) {
            this.mT = (OnGamesLoadedListener) dm.a(onGamesLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void g(com.google.android.gms.common.data.d dVar) {
            em.this.a(new k(this.mT, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class k extends de<er>.c<OnGamesLoadedListener> {
        k(OnGamesLoadedListener onGamesLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onGamesLoadedListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(OnGamesLoadedListener onGamesLoadedListener, com.google.android.gms.common.data.d dVar) {
            onGamesLoadedListener.onGamesLoaded(dVar.getStatusCode(), new GameBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class l extends el {
        private final OnInvitationReceivedListener mU;

        l(OnInvitationReceivedListener onInvitationReceivedListener) {
            this.mU = onInvitationReceivedListener;
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void k(com.google.android.gms.common.data.d dVar) {
            InvitationBuffer invitationBuffer = new InvitationBuffer(dVar);
            try {
                Invitation freeze = invitationBuffer.getCount() > 0 ? invitationBuffer.get(0).freeze() : null;
                if (freeze != null) {
                    em.this.a(new m(this.mU, freeze));
                }
            } finally {
                invitationBuffer.close();
            }
        }
    }

    /* loaded from: classes.dex */
    final class m extends de<er>.b<OnInvitationReceivedListener> {
        private final Invitation mV;

        m(OnInvitationReceivedListener onInvitationReceivedListener, Invitation invitation) {
            super(onInvitationReceivedListener);
            this.mV = invitation;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(OnInvitationReceivedListener onInvitationReceivedListener) {
            onInvitationReceivedListener.onInvitationReceived(this.mV);
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class n extends el {
        private final OnInvitationsLoadedListener mW;

        n(OnInvitationsLoadedListener onInvitationsLoadedListener) {
            this.mW = onInvitationsLoadedListener;
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void j(com.google.android.gms.common.data.d dVar) {
            em.this.a(new o(this.mW, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class o extends de<er>.c<OnInvitationsLoadedListener> {
        o(OnInvitationsLoadedListener onInvitationsLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onInvitationsLoadedListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(OnInvitationsLoadedListener onInvitationsLoadedListener, com.google.android.gms.common.data.d dVar) {
            onInvitationsLoadedListener.onInvitationsLoaded(dVar.getStatusCode(), new InvitationBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class p extends b {
        public p(RoomUpdateListener roomUpdateListener, com.google.android.gms.common.data.d dVar) {
            super(roomUpdateListener, dVar);
        }

        @Override // com.google.android.gms.internal.em.b
        public void a(RoomUpdateListener roomUpdateListener, Room room, int i) {
            roomUpdateListener.onJoinedRoom(i, room);
        }
    }

    /* loaded from: classes.dex */
    final class q extends el {
        private final OnLeaderboardScoresLoadedListener mX;

        q(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            this.mX = (OnLeaderboardScoresLoadedListener) dm.a(onLeaderboardScoresLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void a(com.google.android.gms.common.data.d dVar, com.google.android.gms.common.data.d dVar2) {
            em.this.a(new r(this.mX, dVar, dVar2));
        }
    }

    /* loaded from: classes.dex */
    final class r extends de<er>.b<OnLeaderboardScoresLoadedListener> {
        private final com.google.android.gms.common.data.d mY;
        private final com.google.android.gms.common.data.d mZ;

        r(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener, com.google.android.gms.common.data.d dVar, com.google.android.gms.common.data.d dVar2) {
            super(onLeaderboardScoresLoadedListener);
            this.mY = dVar;
            this.mZ = dVar2;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.b
        public void a(OnLeaderboardScoresLoadedListener onLeaderboardScoresLoadedListener) {
            com.google.android.gms.common.data.d dVar;
            com.google.android.gms.common.data.d dVar2 = null;
            com.google.android.gms.common.data.d dVar3 = this.mY;
            com.google.android.gms.common.data.d dVar4 = this.mZ;
            if (onLeaderboardScoresLoadedListener != null) {
                try {
                    onLeaderboardScoresLoadedListener.onLeaderboardScoresLoaded(dVar4.getStatusCode(), new LeaderboardBuffer(dVar3), new LeaderboardScoreBuffer(dVar4));
                    dVar = null;
                } catch (Throwable th) {
                    if (dVar3 != null) {
                        dVar3.close();
                    }
                    if (dVar4 != null) {
                        dVar4.close();
                    }
                    throw th;
                }
            } else {
                dVar2 = dVar4;
                dVar = dVar3;
            }
            if (dVar != null) {
                dVar.close();
            }
            if (dVar2 != null) {
                dVar2.close();
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
            if (this.mY != null) {
                this.mY.close();
            }
            if (this.mZ != null) {
                this.mZ.close();
            }
        }
    }

    /* loaded from: classes.dex */
    final class s extends el {
        private final OnLeaderboardMetadataLoadedListener na;

        s(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener) {
            this.na = (OnLeaderboardMetadataLoadedListener) dm.a(onLeaderboardMetadataLoadedListener, "Listener must not be null");
        }

        @Override // com.google.android.gms.internal.el, com.google.android.gms.internal.eq
        public void c(com.google.android.gms.common.data.d dVar) {
            em.this.a(new t(this.na, dVar));
        }
    }

    /* loaded from: classes.dex */
    final class t extends de<er>.c<OnLeaderboardMetadataLoadedListener> {
        t(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, com.google.android.gms.common.data.d dVar) {
            super(onLeaderboardMetadataLoadedListener, dVar);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.android.gms.internal.de.c
        public void a(OnLeaderboardMetadataLoadedListener onLeaderboardMetadataLoadedListener, com.google.android.gms.common.data.d dVar) {
            onLeaderboardMetadataLoadedListener.onLeaderboardMetadataLoaded(dVar.getStatusCode(), new LeaderboardBuffer(dVar));
        }
    }

    /* loaded from: classes.dex */
    final class u extends de<er>.b<RoomUpdateListener> {
        private final int iC;
        private final String nb;

        u(RoomUpdateListener roomUpdateListener, int i, String str) {
            super(roomUpdateListener);
            this.iC = i;
            this.nb = str;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(RoomUpdateListener roomUpdateListener) {
            roomUpdateListener.onLeftRoom(this.iC, this.nb);
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class v extends de<er>.b<RealTimeMessageReceivedListener> {
        private final RealTimeMessage nc;

        v(RealTimeMessageReceivedListener realTimeMessageReceivedListener, RealTimeMessage realTimeMessage) {
            super(realTimeMessageReceivedListener);
            this.nc = realTimeMessage;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(RealTimeMessageReceivedListener realTimeMessageReceivedListener) {
            ep.b("GamesClient", "Deliver Message received callback");
            if (realTimeMessageReceivedListener != null) {
                realTimeMessageReceivedListener.onRealTimeMessageReceived(this.nc);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class w extends de<er>.b<RoomStatusUpdateListener> {
        private final String nd;

        w(RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            super(roomStatusUpdateListener);
            this.nd = str;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PConnected(this.nd);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class x extends de<er>.b<RoomStatusUpdateListener> {
        private final String nd;

        x(RoomStatusUpdateListener roomStatusUpdateListener, String str) {
            super(roomStatusUpdateListener);
            this.nd = str;
        }

        @Override // com.google.android.gms.internal.de.b
        public void a(RoomStatusUpdateListener roomStatusUpdateListener) {
            if (roomStatusUpdateListener != null) {
                roomStatusUpdateListener.onP2PDisconnected(this.nd);
            }
        }

        @Override // com.google.android.gms.internal.de.b
        protected void aF() {
        }
    }

    /* loaded from: classes.dex */
    final class y extends a {
        y(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeersConnected(room, arrayList);
        }
    }

    /* loaded from: classes.dex */
    final class z extends a {
        z(RoomStatusUpdateListener roomStatusUpdateListener, com.google.android.gms.common.data.d dVar, String[] strArr) {
            super(roomStatusUpdateListener, dVar, strArr);
        }

        @Override // com.google.android.gms.internal.em.a
        protected void a(RoomStatusUpdateListener roomStatusUpdateListener, Room room, ArrayList<String> arrayList) {
            roomStatusUpdateListener.onPeerDeclined(room, arrayList);
        }
    }

    public em(Context context, String str, String str2, GooglePlayServicesClient.ConnectionCallbacks connectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener onConnectionFailedListener, String[] strArr, int i2, View view, boolean z2) {
        super(context, connectionCallbacks, onConnectionFailedListener, strArr);
        this.mK = false;
        this.mF = str;
        this.it = (String) dm.e(str2);
        this.mL = new Binder();
        this.mG = new HashMap();
        this.mJ = es.a(this, i2);
        setViewForPopups(view);
        this.mM = hashCode();
        this.mN = z2;
    }

    private et K(String str) {
        et etVar;
        try {
            String M = bd().M(str);
            if (M == null) {
                etVar = null;
            } else {
                ep.e("GamesClient", "Creating a socket to bind to:" + M);
                LocalSocket localSocket = new LocalSocket();
                try {
                    localSocket.connect(new LocalSocketAddress(M));
                    etVar = new et(localSocket, str);
                    this.mG.put(str, etVar);
                } catch (IOException e2) {
                    ep.d("GamesClient", "connect() call failed on socket: " + e2.getMessage());
                    etVar = null;
                }
            }
            return etVar;
        } catch (RemoteException e3) {
            ep.d("GamesClient", "Unable to create socket. Service died.");
            return null;
        }
    }

    private void bR() {
        this.mH = null;
    }

    private void bS() {
        for (et etVar : this.mG.values()) {
            try {
                etVar.close();
            } catch (IOException e2) {
                ep.a("GamesClient", "IOException:", e2);
            }
        }
        this.mG.clear();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Room x(com.google.android.gms.common.data.d dVar) {
        com.google.android.gms.games.multiplayer.realtime.a aVar = new com.google.android.gms.games.multiplayer.realtime.a(dVar);
        try {
            return aVar.getCount() > 0 ? aVar.get(0).freeze() : null;
        } finally {
            aVar.close();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    /* renamed from: A */
    public er p(IBinder iBinder) {
        return er.a.C(iBinder);
    }

    public int a(byte[] bArr, String str, String[] strArr) {
        dm.a(strArr, "Participant IDs must not be null");
        try {
            return bd().b(bArr, str, strArr);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    public void a(int i2, IBinder iBinder, Bundle bundle) {
        if (i2 == 0 && bundle != null) {
            this.mK = bundle.getBoolean("show_welcome_popup");
        }
        super.a(i2, iBinder, bundle);
    }

    public void a(IBinder iBinder, Bundle bundle) {
        if (isConnected()) {
            try {
                bd().a(iBinder, bundle);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    public void a(ConnectionResult connectionResult) {
        super.a(connectionResult);
        this.mK = false;
    }

    public void a(OnPlayersLoadedListener onPlayersLoadedListener, int i2, boolean z2, boolean z3) {
        try {
            bd().a(new ae(onPlayersLoadedListener), i2, z2, z3);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        d dVar;
        if (onAchievementUpdatedListener == null) {
            dVar = null;
        } else {
            try {
                dVar = new d(onAchievementUpdatedListener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().a(dVar, str, this.mJ.bZ(), this.mJ.bY());
    }

    public void a(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i2) {
        d dVar;
        if (onAchievementUpdatedListener == null) {
            dVar = null;
        } else {
            try {
                dVar = new d(onAchievementUpdatedListener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().a(dVar, str, i2, this.mJ.bZ(), this.mJ.bY());
    }

    public void a(OnScoreSubmittedListener onScoreSubmittedListener, String str, long j2, String str2) {
        ap apVar;
        if (onScoreSubmittedListener == null) {
            apVar = null;
        } else {
            try {
                apVar = new ap(onScoreSubmittedListener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().a(apVar, str, j2, str2);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(dj djVar, de.d dVar) throws RemoteException {
        String locale = getContext().getResources().getConfiguration().locale.toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("com.google.android.gms.games.key.isHeadless", this.mN);
        djVar.a(dVar, GooglePlayServicesUtil.GOOGLE_PLAY_SERVICES_VERSION_CODE, getContext().getPackageName(), this.it, aY(), this.mF, this.mJ.bZ(), locale, bundle);
    }

    @Override // com.google.android.gms.internal.de
    protected void a(String... strArr) {
        boolean z2 = false;
        boolean z3 = false;
        for (String str : strArr) {
            if (str.equals(Scopes.GAMES)) {
                z3 = true;
            } else if (str.equals("https://www.googleapis.com/auth/games.firstparty")) {
                z2 = true;
            }
        }
        if (z2) {
            dm.a(!z3, String.format("Cannot have both %s and %s!", Scopes.GAMES, "https://www.googleapis.com/auth/games.firstparty"));
        } else {
            dm.a(z3, String.format("GamesClient requires %s to function.", Scopes.GAMES));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.gms.internal.de
    public void aZ() {
        super.aZ();
        if (this.mK) {
            this.mJ.bX();
            this.mK = false;
        }
    }

    @Override // com.google.android.gms.internal.de
    protected String ag() {
        return "com.google.android.gms.games.service.START";
    }

    @Override // com.google.android.gms.internal.de
    protected String ah() {
        return "com.google.android.gms.games.internal.IGamesService";
    }

    public void b(OnAchievementUpdatedListener onAchievementUpdatedListener, String str) {
        d dVar;
        if (onAchievementUpdatedListener == null) {
            dVar = null;
        } else {
            try {
                dVar = new d(onAchievementUpdatedListener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().b(dVar, str, this.mJ.bZ(), this.mJ.bY());
    }

    public void b(OnAchievementUpdatedListener onAchievementUpdatedListener, String str, int i2) {
        d dVar;
        if (onAchievementUpdatedListener == null) {
            dVar = null;
        } else {
            try {
                dVar = new d(onAchievementUpdatedListener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().b(dVar, str, i2, this.mJ.bZ(), this.mJ.bY());
    }

    public void bT() {
        if (isConnected()) {
            try {
                bd().bT();
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
            }
        }
    }

    @Override // com.google.android.gms.internal.de
    protected Bundle ba() {
        try {
            Bundle ba = bd().ba();
            if (ba != null) {
                ba.setClassLoader(em.class.getClassLoader());
                return ba;
            }
            return ba;
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return null;
        }
    }

    public void clearNotifications(int notificationTypes) {
        try {
            bd().clearNotifications(notificationTypes);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    @Override // com.google.android.gms.internal.de, com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        bR();
        super.connect();
    }

    public void createRoom(RoomConfig config) {
        try {
            bd().a(new aj(config.getRoomUpdateListener(), config.getRoomStatusUpdateListener(), config.getMessageReceivedListener()), this.mL, config.getVariant(), config.getInvitedPlayerIds(), config.getAutoMatchCriteria(), config.isSocketEnabled(), this.mM);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    @Override // com.google.android.gms.internal.de, com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.mK = false;
        if (isConnected()) {
            try {
                er bd = bd();
                bd.bT();
                bd.g(this.mM);
                bd.f(this.mM);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "Failed to notify client disconnect.");
            }
        }
        bS();
        super.disconnect();
    }

    public Intent getAchievementsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_ACHIEVEMENTS");
        intent.addFlags(67108864);
        return eo.c(intent);
    }

    public Intent getAllLeaderboardsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARDS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        intent.addFlags(67108864);
        return eo.c(intent);
    }

    public String getAppId() {
        try {
            return bd().getAppId();
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return null;
        }
    }

    public String getCurrentAccountName() {
        try {
            return bd().getCurrentAccountName();
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return null;
        }
    }

    public Game getCurrentGame() {
        bc();
        synchronized (this) {
            if (this.mI == null) {
                try {
                    GameBuffer gameBuffer = new GameBuffer(bd().bW());
                    try {
                        if (gameBuffer.getCount() > 0) {
                            this.mI = (GameEntity) gameBuffer.get(0).freeze();
                        }
                    } finally {
                        gameBuffer.close();
                    }
                } catch (RemoteException e2) {
                    ep.c("GamesClient", "service died");
                }
            }
        }
        return this.mI;
    }

    public Player getCurrentPlayer() {
        bc();
        synchronized (this) {
            if (this.mH == null) {
                try {
                    PlayerBuffer playerBuffer = new PlayerBuffer(bd().bU());
                    try {
                        if (playerBuffer.getCount() > 0) {
                            this.mH = (PlayerEntity) playerBuffer.get(0).freeze();
                        }
                    } finally {
                        playerBuffer.close();
                    }
                } catch (RemoteException e2) {
                    ep.c("GamesClient", "service died");
                }
            }
        }
        return this.mH;
    }

    public String getCurrentPlayerId() {
        try {
            return bd().getCurrentPlayerId();
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return null;
        }
    }

    public Intent getInvitationInboxIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_INVITATIONS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        return eo.c(intent);
    }

    public Intent getLeaderboardIntent(String leaderboardId) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.VIEW_LEADERBOARD_SCORES");
        intent.putExtra("com.google.android.gms.games.LEADERBOARD_ID", leaderboardId);
        intent.addFlags(67108864);
        return eo.c(intent);
    }

    public RealTimeSocket getRealTimeSocketForParticipant(String roomId, String participantId) {
        if (participantId == null || !ParticipantUtils.Q(participantId)) {
            throw new IllegalArgumentException("Bad participant ID");
        }
        et etVar = this.mG.get(participantId);
        return (etVar == null || etVar.isClosed()) ? K(participantId) : etVar;
    }

    public Intent getRealTimeWaitingRoomIntent(Room room, int minParticipantsToStart) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_REAL_TIME_WAITING_ROOM");
        dm.a(room, "Room parameter must not be null");
        intent.putExtra(GamesClient.EXTRA_ROOM, room.freeze());
        dm.a(minParticipantsToStart >= 0, "minParticipantsToStart must be >= 0");
        intent.putExtra("com.google.android.gms.games.MIN_PARTICIPANTS_TO_START", minParticipantsToStart);
        return eo.c(intent);
    }

    public Intent getSelectPlayersIntent(int minPlayers, int maxPlayers) {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SELECT_PLAYERS");
        intent.putExtra("com.google.android.gms.games.MIN_SELECTIONS", minPlayers);
        intent.putExtra("com.google.android.gms.games.MAX_SELECTIONS", maxPlayers);
        return eo.c(intent);
    }

    public Intent getSettingsIntent() {
        bc();
        Intent intent = new Intent("com.google.android.gms.games.SHOW_SETTINGS");
        intent.putExtra("com.google.android.gms.games.GAME_PACKAGE_NAME", this.mF);
        intent.addFlags(67108864);
        return eo.c(intent);
    }

    public void i(String str, int i2) {
        try {
            bd().i(str, i2);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void j(String str, int i2) {
        try {
            bd().j(str, i2);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void joinRoom(RoomConfig config) {
        try {
            bd().a(new aj(config.getRoomUpdateListener(), config.getRoomStatusUpdateListener(), config.getMessageReceivedListener()), this.mL, config.getInvitationId(), config.isSocketEnabled(), this.mM);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void leaveRoom(RoomUpdateListener listener, String roomId) {
        try {
            bd().e(new aj(listener), roomId);
            bS();
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadAchievements(OnAchievementsLoadedListener listener, boolean forceReload) {
        try {
            bd().b(new f(listener), forceReload);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadGame(OnGamesLoadedListener listener) {
        try {
            bd().d(new j(listener));
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadInvitations(OnInvitationsLoadedListener listener) {
        try {
            bd().e(new n(listener));
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, String leaderboardId, boolean forceReload) {
        try {
            bd().c(new s(listener), leaderboardId, forceReload);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, boolean forceReload) {
        try {
            bd().c(new s(listener), forceReload);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadMoreScores(OnLeaderboardScoresLoadedListener listener, LeaderboardScoreBuffer buffer, int maxResults, int pageDirection) {
        try {
            bd().a(new q(listener), buffer.cb().cc(), maxResults, pageDirection);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadPlayer(OnPlayersLoadedListener listener, String playerId) {
        try {
            bd().c(new ae(listener), playerId);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        try {
            bd().b(new q(listener), leaderboardId, span, leaderboardCollection, maxResults, forceReload);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        try {
            bd().a(new q(listener), leaderboardId, span, leaderboardCollection, maxResults, forceReload);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void registerInvitationListener(OnInvitationReceivedListener listener) {
        try {
            bd().a(new l(listener), this.mM);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public int sendReliableRealTimeMessage(RealTimeReliableMessageSentListener listener, byte[] messageData, String roomId, String recipientParticipantId) {
        try {
            return bd().a(new ah(listener), messageData, roomId, recipientParticipantId);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return -1;
        }
    }

    public int sendUnreliableRealTimeMessageToAll(byte[] messageData, String roomId) {
        try {
            return bd().b(messageData, roomId, (String[]) null);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
            return -1;
        }
    }

    public void setGravityForPopups(int gravity) {
        this.mJ.setGravity(gravity);
    }

    public void setUseNewPlayerNotificationsFirstParty(boolean newPlayerStyle) {
        try {
            bd().setUseNewPlayerNotificationsFirstParty(newPlayerStyle);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }

    public void setViewForPopups(View gamesContentView) {
        this.mJ.e(gamesContentView);
    }

    public void signOut(OnSignOutCompleteListener listener) {
        an anVar;
        if (listener == null) {
            anVar = null;
        } else {
            try {
                anVar = new an(listener);
            } catch (RemoteException e2) {
                ep.c("GamesClient", "service died");
                return;
            }
        }
        bd().a(anVar);
    }

    public void unregisterInvitationListener() {
        try {
            bd().g(this.mM);
        } catch (RemoteException e2) {
            ep.c("GamesClient", "service died");
        }
    }
}
