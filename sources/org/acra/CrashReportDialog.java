package org.acra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;
import org.acra.collector.CrashReportData;
import org.acra.util.ToastSender;
/* loaded from: classes.dex */
public class CrashReportDialog extends Activity implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener {
    private static final String STATE_COMMENT = "comment";
    private static final String STATE_EMAIL = "email";
    AlertDialog mDialog;
    String mReportFileName;
    private SharedPreferences prefs;
    private EditText userComment;
    private EditText userEmail;

    @Override // android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean forceCancel = getIntent().getBooleanExtra("FORCE_CANCEL", false);
        if (forceCancel) {
            ACRA.log.d(ACRA.LOG_TAG, "Forced reports deletion.");
            cancelReports();
            finish();
            return;
        }
        this.mReportFileName = getIntent().getStringExtra("REPORT_FILE_NAME");
        Log.d(ACRA.LOG_TAG, "Opening CrashReportDialog for " + this.mReportFileName);
        if (this.mReportFileName == null) {
            finish();
        }
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        int resourceId = ACRA.getConfig().resDialogTitle();
        if (resourceId != 0) {
            dialogBuilder.setTitle(resourceId);
        }
        int resourceId2 = ACRA.getConfig().resDialogIcon();
        if (resourceId2 != 0) {
            dialogBuilder.setIcon(resourceId2);
        }
        dialogBuilder.setView(buildCustomView(savedInstanceState));
        dialogBuilder.setPositiveButton(17039370, this);
        dialogBuilder.setNegativeButton(17039360, this);
        cancelNotification();
        this.mDialog = dialogBuilder.create();
        this.mDialog.setCanceledOnTouchOutside(false);
        this.mDialog.setOnDismissListener(this);
        this.mDialog.show();
    }

    private View buildCustomView(Bundle savedInstanceState) {
        String savedValue;
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(10, 10, 10, 10);
        root.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
        root.setFocusable(true);
        root.setFocusableInTouchMode(true);
        ScrollView scroll = new ScrollView(this);
        root.addView(scroll, new LinearLayout.LayoutParams(-1, -1, 1.0f));
        LinearLayout scrollable = new LinearLayout(this);
        scrollable.setOrientation(1);
        scroll.addView(scrollable);
        TextView text = new TextView(this);
        int dialogTextId = ACRA.getConfig().resDialogText();
        if (dialogTextId != 0) {
            text.setText(getText(dialogTextId));
        }
        scrollable.addView(text);
        int commentPromptId = ACRA.getConfig().resDialogCommentPrompt();
        if (commentPromptId != 0) {
            TextView label = new TextView(this);
            label.setText(getText(commentPromptId));
            label.setPadding(label.getPaddingLeft(), 10, label.getPaddingRight(), label.getPaddingBottom());
            scrollable.addView(label, new LinearLayout.LayoutParams(-1, -2));
            this.userComment = new EditText(this);
            this.userComment.setLines(2);
            if (savedInstanceState != null && (savedValue = savedInstanceState.getString(STATE_COMMENT)) != null) {
                this.userComment.setText(savedValue);
            }
            scrollable.addView(this.userComment);
        }
        int emailPromptId = ACRA.getConfig().resDialogEmailPrompt();
        if (emailPromptId != 0) {
            TextView label2 = new TextView(this);
            label2.setText(getText(emailPromptId));
            label2.setPadding(label2.getPaddingLeft(), 10, label2.getPaddingRight(), label2.getPaddingBottom());
            scrollable.addView(label2);
            this.userEmail = new EditText(this);
            this.userEmail.setSingleLine();
            this.userEmail.setInputType(33);
            this.prefs = getSharedPreferences(ACRA.getConfig().sharedPreferencesName(), ACRA.getConfig().sharedPreferencesMode());
            String savedValue2 = null;
            if (savedInstanceState != null) {
                savedValue2 = savedInstanceState.getString("email");
            }
            if (savedValue2 != null) {
                this.userEmail.setText(savedValue2);
            } else {
                this.userEmail.setText(this.prefs.getString(ACRA.PREF_USER_EMAIL_ADDRESS, ""));
            }
            scrollable.addView(this.userEmail);
        }
        return root;
    }

    protected void cancelNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        notificationManager.cancel(666);
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialog, int which) {
        if (which == -1) {
            sendCrash();
        } else {
            cancelReports();
        }
        finish();
    }

    private void cancelReports() {
        ACRA.getErrorReporter().deletePendingNonApprovedReports(false);
    }

    private void sendCrash() {
        String usrEmail;
        String comment = this.userComment != null ? this.userComment.getText().toString() : "";
        if (this.prefs != null && this.userEmail != null) {
            usrEmail = this.userEmail.getText().toString();
            SharedPreferences.Editor prefEditor = this.prefs.edit();
            prefEditor.putString(ACRA.PREF_USER_EMAIL_ADDRESS, usrEmail);
            prefEditor.commit();
        } else {
            usrEmail = "";
        }
        CrashReportPersister persister = new CrashReportPersister(getApplicationContext());
        try {
            Log.d(ACRA.LOG_TAG, "Add user comment to " + this.mReportFileName);
            CrashReportData crashData = persister.load(this.mReportFileName);
            crashData.put((CrashReportData) ReportField.USER_COMMENT, (ReportField) comment);
            crashData.put((CrashReportData) ReportField.USER_EMAIL, (ReportField) usrEmail);
            persister.store(crashData, this.mReportFileName);
        } catch (IOException e) {
            Log.w(ACRA.LOG_TAG, "User comment not added: ", e);
        }
        Log.v(ACRA.LOG_TAG, "About to start SenderWorker from CrashReportDialog");
        ACRA.getErrorReporter().startSendingReports(false, true);
        int toastId = ACRA.getConfig().resDialogOkToast();
        if (toastId != 0) {
            ToastSender.sendToast(getApplicationContext(), toastId, 1);
        }
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (this.userComment != null && this.userComment.getText() != null) {
            outState.putString(STATE_COMMENT, this.userComment.getText().toString());
        }
        if (this.userEmail != null && this.userEmail.getText() != null) {
            outState.putString("email", this.userEmail.getText().toString());
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialog) {
        finish();
    }
}
