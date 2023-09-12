package com.microsoft.kapp.services.bedrock;

import android.os.AsyncTask;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.microsoft.kapp.Callback;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.services.bedrock.models.BedrockCluster;
import com.microsoft.kapp.services.bedrock.models.BedrockEntity;
import com.microsoft.kapp.services.bedrock.models.BedrockEntityList;
import com.microsoft.kapp.services.bedrock.models.BedrockImage;
import com.microsoft.kapp.services.bedrock.models.BedrockImageCollection;
import com.microsoft.kapp.services.bedrock.models.BedrockImageId;
import com.microsoft.kapp.services.bedrock.models.BedrockImageType;
import com.microsoft.kapp.services.bedrock.models.BedrockResponseEnvelope;
import com.microsoft.krestsdk.services.NetworkProvider;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class BedrockRestServiceImpl implements BedrockRestService {
    private NetworkProvider mNetworkProvider;
    private static final String TAG = BedrockRestServiceImpl.class.getSimpleName();
    private static final Gson DefaultGson = new Gson();

    public BedrockRestServiceImpl(NetworkProvider provider) {
        this.mNetworkProvider = provider;
    }

    @Override // com.microsoft.kapp.services.bedrock.BedrockRestService
    public void getBedrockImageUrl(BedrockImageId bedrockImageId, final Callback<String> callback) {
        String restUrl;
        switch (bedrockImageId) {
            case RUN:
                restUrl = BedrockCloudConstants.HERO_RUN_IMAGE_PROVIDER;
                break;
            case WORKOUT:
                restUrl = BedrockCloudConstants.HERO_WORKOUT_IMAGE_PROVIDER;
                break;
            case INACTIVE:
                restUrl = BedrockCloudConstants.HERO_INACTIVE_IMAGE_PROVIDER;
                break;
            case WELCOME:
                restUrl = BedrockCloudConstants.HERO_WELCOME_IMAGE_PROVIDER;
                break;
            case SLEEP:
                restUrl = BedrockCloudConstants.HERO_SLEEP_IMAGE_PROVIDER;
                break;
            default:
                throw new IllegalArgumentException(String.format("bedrockImageId [%s] is not supported", bedrockImageId.toString()));
        }
        RestQuery<BedrockResponseEnvelope> query = new RestQuery<>(restUrl, new Callback<BedrockResponseEnvelope>() { // from class: com.microsoft.kapp.services.bedrock.BedrockRestServiceImpl.1
            @Override // com.microsoft.kapp.Callback
            public void callback(BedrockResponseEnvelope bedrockResponseEnvelope) {
                try {
                    String imageURL = BedrockRestServiceImpl.extractFirstLowResImageURL(bedrockResponseEnvelope);
                    callback.callback(imageURL);
                } catch (Exception ex) {
                    callback.onError(ex);
                }
            }

            @Override // com.microsoft.kapp.Callback
            public void onError(Exception ex) {
                callback.onError(ex);
            }
        }, DefaultGson, new TypeToken<BedrockResponseEnvelope>() { // from class: com.microsoft.kapp.services.bedrock.BedrockRestServiceImpl.2
        });
        query.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String extractFirstLowResImageURL(BedrockResponseEnvelope bedrockResponseEnvelope) {
        BedrockCluster[] bedrockClusters = null;
        BedrockCluster bedrockCluster = null;
        BedrockEntityList bedrockEntityList = null;
        BedrockEntity[] bedrockEntities = null;
        BedrockEntity bedrockEntity = null;
        BedrockImageCollection bedrockImageCollection = null;
        BedrockImage[] images = null;
        if (bedrockResponseEnvelope != null) {
            bedrockClusters = bedrockResponseEnvelope.getBedrockClusters();
        }
        if (bedrockClusters != null && bedrockClusters.length > 0) {
            bedrockCluster = bedrockClusters[0];
        }
        if (bedrockCluster != null) {
            bedrockEntityList = bedrockCluster.getBedrockEntityList();
        }
        if (bedrockEntityList != null) {
            bedrockEntities = bedrockEntityList.getEntities();
        }
        if (bedrockEntities != null && bedrockEntities.length > 0) {
            bedrockEntity = bedrockEntities[0];
        }
        if (bedrockEntity != null) {
            bedrockImageCollection = bedrockEntity.getBedrockImageCollection();
        }
        if (bedrockImageCollection != null) {
            images = bedrockImageCollection.getImages();
        }
        if (images != null && images.length > 0) {
            BedrockImage[] arr$ = images;
            for (BedrockImage image : arr$) {
                if (image.getType() == BedrockImageType.LOW_RES) {
                    return image.getURL();
                }
            }
        }
        throw new IllegalArgumentException("bedrockResponseEnvelope is invalid.");
    }

    /* loaded from: classes.dex */
    private class RestQuery<T> extends AsyncTask<String, Void, T> {
        private Callback<T> mCallBack;
        private Exception mException;
        private Gson mGson;
        private String mRestUrl;
        private Type mType;

        public RestQuery(String restUrl, Callback<T> callback, Gson gson, TypeToken<T> token) {
            this.mRestUrl = restUrl;
            this.mCallBack = callback;
            this.mGson = gson;
            this.mType = token.getType();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public T doInBackground(String... strings) {
            String response = null;
            T parsedResponse = null;
            try {
                Map<String, String> headers = new HashMap<>();
                response = BedrockRestServiceImpl.this.mNetworkProvider.executeHttpGet(null, headers);
                parsedResponse = (T) this.mGson.fromJson(response, this.mType);
            } catch (JsonSyntaxException exception) {
                KLog.e("[GET REQUEST]", "JSON parsing exception", exception);
                this.mException = exception;
            } catch (Exception e) {
                KLog.w("[GET REQUEST]", "Network exception", e);
                this.mException = e;
            }
            KLog.v(BedrockRestServiceImpl.TAG, "[Request URL]:  " + ((String) null) + " [Response]: " + response);
            return parsedResponse;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(T result) {
            if (this.mException == null) {
                this.mCallBack.callback(result);
            } else {
                this.mCallBack.onError(this.mException);
            }
        }
    }
}
