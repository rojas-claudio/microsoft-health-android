package net.hockeyapp.android.objects;

import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import net.hockeyapp.android.Constants;
/* loaded from: classes.dex */
public class FeedbackAttachment implements Serializable {
    private static final long serialVersionUID = 5059651319640956830L;
    private String createdAt;
    private String filename;
    private int id;
    private int messageId;
    private String updatedAt;
    private String url;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCacheId() {
        return "" + this.messageId + this.id;
    }

    public boolean isAvailableInCache() {
        File folder = Constants.getHockeyAppStorageDir();
        if (folder.exists() && folder.isDirectory()) {
            File[] match = folder.listFiles(new FilenameFilter() { // from class: net.hockeyapp.android.objects.FeedbackAttachment.1
                @Override // java.io.FilenameFilter
                public boolean accept(File dir, String filename) {
                    return filename.equals(FeedbackAttachment.this.getCacheId());
                }
            });
            return match != null && match.length == 1;
        }
        return false;
    }

    public String toString() {
        return "\n" + FeedbackAttachment.class.getSimpleName() + "\nid         " + this.id + "\nmessage id " + this.messageId + "\nfilename   " + this.filename + "\nurl        " + this.url + "\ncreatedAt  " + this.createdAt + "\nupdatedAt  " + this.updatedAt;
    }
}
