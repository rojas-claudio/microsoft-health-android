package com.google.android.gms.games;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.games.achievement.OnAchievementUpdatedListener;
import com.google.android.gms.games.achievement.OnAchievementsLoadedListener;
import com.google.android.gms.games.leaderboard.LeaderboardScoreBuffer;
import com.google.android.gms.games.leaderboard.OnLeaderboardMetadataLoadedListener;
import com.google.android.gms.games.leaderboard.OnLeaderboardScoresLoadedListener;
import com.google.android.gms.games.leaderboard.OnScoreSubmittedListener;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.google.android.gms.games.multiplayer.OnInvitationsLoadedListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeReliableMessageSentListener;
import com.google.android.gms.games.multiplayer.realtime.RealTimeSocket;
import com.google.android.gms.games.multiplayer.realtime.Room;
import com.google.android.gms.games.multiplayer.realtime.RoomConfig;
import com.google.android.gms.games.multiplayer.realtime.RoomUpdateListener;
import com.google.android.gms.internal.dm;
import com.google.android.gms.internal.em;
import java.util.List;
/* loaded from: classes.dex */
public final class GamesClient implements GooglePlayServicesClient {
    public static final String EXTRA_EXCLUSIVE_BIT_MASK = "exclusive_bit_mask";
    public static final String EXTRA_INVITATION = "invitation";
    public static final String EXTRA_MAX_AUTOMATCH_PLAYERS = "max_automatch_players";
    public static final String EXTRA_MIN_AUTOMATCH_PLAYERS = "min_automatch_players";
    public static final String EXTRA_PLAYERS = "players";
    public static final String EXTRA_ROOM = "room";
    public static final int MAX_RELIABLE_MESSAGE_LEN = 1400;
    public static final int MAX_UNRELIABLE_MESSAGE_LEN = 1168;
    public static final int NOTIFICATION_TYPES_ALL = -1;
    public static final int NOTIFICATION_TYPES_MULTIPLAYER = 1;
    public static final int NOTIFICATION_TYPE_INVITATION = 1;
    public static final int STATUS_ACHIEVEMENT_NOT_INCREMENTAL = 3002;
    public static final int STATUS_ACHIEVEMENT_UNKNOWN = 3001;
    public static final int STATUS_ACHIEVEMENT_UNLOCKED = 3003;
    public static final int STATUS_ACHIEVEMENT_UNLOCK_FAILURE = 3000;
    public static final int STATUS_APP_MISCONFIGURED = 8;
    public static final int STATUS_CLIENT_RECONNECT_REQUIRED = 2;
    public static final int STATUS_INTERNAL_ERROR = 1;
    public static final int STATUS_INVALID_REAL_TIME_ROOM_ID = 7002;
    public static final int STATUS_LICENSE_CHECK_FAILED = 7;
    public static final int STATUS_MULTIPLAYER_ERROR_CREATION_NOT_ALLOWED = 6000;
    public static final int STATUS_MULTIPLAYER_ERROR_NOT_TRUSTED_TESTER = 6001;
    public static final int STATUS_NETWORK_ERROR_NO_DATA = 4;
    public static final int STATUS_NETWORK_ERROR_OPERATION_DEFERRED = 5;
    public static final int STATUS_NETWORK_ERROR_OPERATION_FAILED = 6;
    public static final int STATUS_NETWORK_ERROR_STALE_DATA = 3;
    public static final int STATUS_OK = 0;
    public static final int STATUS_PARTICIPANT_NOT_CONNECTED = 7003;
    public static final int STATUS_REAL_TIME_CONNECTION_FAILED = 7000;
    public static final int STATUS_REAL_TIME_INACTIVE_ROOM = 7005;
    public static final int STATUS_REAL_TIME_MESSAGE_FAILED = -1;
    public static final int STATUS_REAL_TIME_MESSAGE_SEND_FAILED = 7001;
    public static final int STATUS_REAL_TIME_ROOM_NOT_JOINED = 7004;
    private final em mz;

    /* loaded from: classes.dex */
    public static final class Builder {
        private final GooglePlayServicesClient.ConnectionCallbacks iq;
        private final GooglePlayServicesClient.OnConnectionFailedListener ir;
        private String mA;
        private View mC;
        private final Context mContext;
        private String it = "<<default account>>";
        private String[] is = {Scopes.GAMES};
        private int mB = 49;

        public Builder(Context context, GooglePlayServicesClient.ConnectionCallbacks connectedListener, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener) {
            this.mContext = context;
            this.mA = context.getPackageName();
            this.iq = connectedListener;
            this.ir = connectionFailedListener;
        }

        public GamesClient create() {
            return new GamesClient(this.mContext, this.mA, this.it, this.iq, this.ir, this.is, this.mB, this.mC);
        }

        public Builder setAccountName(String accountName) {
            this.it = (String) dm.e(accountName);
            return this;
        }

        public Builder setGravityForPopups(int gravity) {
            this.mB = gravity;
            return this;
        }

        public Builder setScopes(String... scopes) {
            this.is = scopes;
            return this;
        }

        public Builder setViewForPopups(View gamesContentView) {
            this.mC = (View) dm.e(gamesContentView);
            return this;
        }
    }

    private GamesClient(Context context, String gamePackageName, String accountName, GooglePlayServicesClient.ConnectionCallbacks connectedListener, GooglePlayServicesClient.OnConnectionFailedListener connectionFailedListener, String[] scopes, int gravity, View gamesContentView) {
        this.mz = new em(context, gamePackageName, accountName, connectedListener, connectionFailedListener, scopes, gravity, gamesContentView, false);
    }

    public void clearAllNotifications() {
        this.mz.clearNotifications(-1);
    }

    public void clearNotifications(int notificationTypes) {
        this.mz.clearNotifications(notificationTypes);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void connect() {
        this.mz.connect();
    }

    public void createRoom(RoomConfig config) {
        this.mz.createRoom(config);
    }

    public void declineRoomInvitation(String invitationId) {
        this.mz.j(invitationId, 0);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void disconnect() {
        this.mz.disconnect();
    }

    public void dismissRoomInvitation(String invitationId) {
        this.mz.i(invitationId, 0);
    }

    public Intent getAchievementsIntent() {
        return this.mz.getAchievementsIntent();
    }

    public Intent getAllLeaderboardsIntent() {
        return this.mz.getAllLeaderboardsIntent();
    }

    public String getAppId() {
        return this.mz.getAppId();
    }

    public String getCurrentAccountName() {
        return this.mz.getCurrentAccountName();
    }

    public Game getCurrentGame() {
        return this.mz.getCurrentGame();
    }

    public Player getCurrentPlayer() {
        return this.mz.getCurrentPlayer();
    }

    public String getCurrentPlayerId() {
        return this.mz.getCurrentPlayerId();
    }

    public Intent getInvitationInboxIntent() {
        return this.mz.getInvitationInboxIntent();
    }

    public Intent getLeaderboardIntent(String leaderboardId) {
        return this.mz.getLeaderboardIntent(leaderboardId);
    }

    public RealTimeSocket getRealTimeSocketForParticipant(String roomId, String participantId) {
        return this.mz.getRealTimeSocketForParticipant(roomId, participantId);
    }

    public Intent getRealTimeWaitingRoomIntent(Room room, int minParticipantsToStart) {
        return this.mz.getRealTimeWaitingRoomIntent(room, minParticipantsToStart);
    }

    public Intent getSelectPlayersIntent(int minPlayers, int maxPlayers) {
        return this.mz.getSelectPlayersIntent(minPlayers, maxPlayers);
    }

    public Intent getSettingsIntent() {
        return this.mz.getSettingsIntent();
    }

    public void incrementAchievement(String id, int numSteps) {
        this.mz.a((OnAchievementUpdatedListener) null, id, numSteps);
    }

    public void incrementAchievementImmediate(OnAchievementUpdatedListener listener, String id, int numSteps) {
        this.mz.a(listener, id, numSteps);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnected() {
        return this.mz.isConnected();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnecting() {
        return this.mz.isConnecting();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionCallbacksRegistered(GooglePlayServicesClient.ConnectionCallbacks listener) {
        return this.mz.isConnectionCallbacksRegistered(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public boolean isConnectionFailedListenerRegistered(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        return this.mz.isConnectionFailedListenerRegistered(listener);
    }

    public void joinRoom(RoomConfig config) {
        this.mz.joinRoom(config);
    }

    public void leaveRoom(RoomUpdateListener listener, String roomId) {
        this.mz.leaveRoom(listener, roomId);
    }

    public void loadAchievements(OnAchievementsLoadedListener listener, boolean forceReload) {
        this.mz.loadAchievements(listener, forceReload);
    }

    public void loadGame(OnGamesLoadedListener listener) {
        this.mz.loadGame(listener);
    }

    public void loadInvitablePlayers(OnPlayersLoadedListener listener, int pageSize, boolean forceReload) {
        this.mz.a(listener, pageSize, false, forceReload);
    }

    public void loadInvitations(OnInvitationsLoadedListener listener) {
        this.mz.loadInvitations(listener);
    }

    @Deprecated
    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener) {
        loadLeaderboardMetadata(listener, false);
    }

    @Deprecated
    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, String leaderboardId) {
        loadLeaderboardMetadata(listener, leaderboardId, false);
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, String leaderboardId, boolean forceReload) {
        this.mz.loadLeaderboardMetadata(listener, leaderboardId, forceReload);
    }

    public void loadLeaderboardMetadata(OnLeaderboardMetadataLoadedListener listener, boolean forceReload) {
        this.mz.loadLeaderboardMetadata(listener, forceReload);
    }

    public void loadMoreInvitablePlayers(OnPlayersLoadedListener listener, int pageSize) {
        this.mz.a(listener, pageSize, true, false);
    }

    public void loadMoreScores(OnLeaderboardScoresLoadedListener listener, LeaderboardScoreBuffer buffer, int maxResults, int pageDirection) {
        this.mz.loadMoreScores(listener, buffer, maxResults, pageDirection);
    }

    public void loadPlayer(OnPlayersLoadedListener listener, String playerId) {
        this.mz.loadPlayer(listener, playerId);
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults) {
        this.mz.loadPlayerCenteredScores(listener, leaderboardId, span, leaderboardCollection, maxResults, false);
    }

    public void loadPlayerCenteredScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        this.mz.loadPlayerCenteredScores(listener, leaderboardId, span, leaderboardCollection, maxResults, forceReload);
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults) {
        this.mz.loadTopScores(listener, leaderboardId, span, leaderboardCollection, maxResults, false);
    }

    public void loadTopScores(OnLeaderboardScoresLoadedListener listener, String leaderboardId, int span, int leaderboardCollection, int maxResults, boolean forceReload) {
        this.mz.loadTopScores(listener, leaderboardId, span, leaderboardCollection, maxResults, forceReload);
    }

    public void reconnect() {
        this.mz.disconnect();
        this.mz.connect();
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.mz.registerConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void registerConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.mz.registerConnectionFailedListener(listener);
    }

    public void registerInvitationListener(OnInvitationReceivedListener listener) {
        this.mz.registerInvitationListener(listener);
    }

    public void revealAchievement(String id) {
        this.mz.a((OnAchievementUpdatedListener) null, id);
    }

    public void revealAchievementImmediate(OnAchievementUpdatedListener listener, String id) {
        this.mz.a(listener, id);
    }

    public int sendReliableRealTimeMessage(RealTimeReliableMessageSentListener listener, byte[] messageData, String roomId, String recipientParticipantId) {
        return this.mz.sendReliableRealTimeMessage(listener, messageData, roomId, recipientParticipantId);
    }

    public int sendUnreliableRealTimeMessage(byte[] messageData, String roomId, String recipientParticipantId) {
        return this.mz.a(messageData, roomId, new String[]{recipientParticipantId});
    }

    public int sendUnreliableRealTimeMessage(byte[] messageData, String roomId, List<String> recipientParticipantIds) {
        return this.mz.a(messageData, roomId, (String[]) recipientParticipantIds.toArray(new String[recipientParticipantIds.size()]));
    }

    public int sendUnreliableRealTimeMessageToAll(byte[] messageData, String roomId) {
        return this.mz.sendUnreliableRealTimeMessageToAll(messageData, roomId);
    }

    public void setAchievementSteps(String id, int numSteps) {
        this.mz.b(null, id, numSteps);
    }

    public void setAchievementStepsImmediate(OnAchievementUpdatedListener listener, String id, int numSteps) {
        this.mz.b(listener, id, numSteps);
    }

    public void setGravityForPopups(int gravity) {
        this.mz.setGravityForPopups(gravity);
    }

    public void setUseNewPlayerNotificationsFirstParty(boolean newPlayerStyle) {
        this.mz.setUseNewPlayerNotificationsFirstParty(newPlayerStyle);
    }

    public void setViewForPopups(View gamesContentView) {
        dm.e(gamesContentView);
        this.mz.setViewForPopups(gamesContentView);
    }

    public void signOut() {
        this.mz.signOut(null);
    }

    public void signOut(OnSignOutCompleteListener listener) {
        this.mz.signOut(listener);
    }

    public void submitScore(String leaderboardId, long score) {
        this.mz.a((OnScoreSubmittedListener) null, leaderboardId, score, (String) null);
    }

    public void submitScore(String leaderboardId, long score, String scoreTag) {
        this.mz.a((OnScoreSubmittedListener) null, leaderboardId, score, scoreTag);
    }

    public void submitScoreImmediate(OnScoreSubmittedListener listener, String leaderboardId, long score) {
        this.mz.a(listener, leaderboardId, score, (String) null);
    }

    public void submitScoreImmediate(OnScoreSubmittedListener listener, String leaderboardId, long score, String scoreTag) {
        this.mz.a(listener, leaderboardId, score, scoreTag);
    }

    public void unlockAchievement(String id) {
        this.mz.b(null, id);
    }

    public void unlockAchievementImmediate(OnAchievementUpdatedListener listener, String id) {
        this.mz.b(listener, id);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionCallbacks(GooglePlayServicesClient.ConnectionCallbacks listener) {
        this.mz.unregisterConnectionCallbacks(listener);
    }

    @Override // com.google.android.gms.common.GooglePlayServicesClient
    public void unregisterConnectionFailedListener(GooglePlayServicesClient.OnConnectionFailedListener listener) {
        this.mz.unregisterConnectionFailedListener(listener);
    }

    public void unregisterInvitationListener() {
        this.mz.unregisterInvitationListener();
    }
}
