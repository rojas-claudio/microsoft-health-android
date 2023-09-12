package com.microsoft.kapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.facebook.AppEventsConstants;
import com.microsoft.kapp.KApplication;
import com.microsoft.kapp.R;
import com.microsoft.kapp.diagnostics.Validate;
import com.microsoft.kapp.logging.KLog;
import com.microsoft.kapp.models.goal.GoalProcessor;
import com.microsoft.kapp.models.goal.GoalProcessorManager;
import com.microsoft.kapp.tasks.GoalAddTask;
import com.microsoft.kapp.tasks.GoalDeleteTask;
import com.microsoft.kapp.tasks.GoalTemplatesGetTask;
import com.microsoft.kapp.tasks.GoalUpdateTask;
import com.microsoft.kapp.tasks.GoalsGetTask;
import com.microsoft.kapp.tasks.StateListenerTask;
import com.microsoft.kapp.utils.Constants;
import com.microsoft.kapp.utils.GoalsUtils;
import com.microsoft.kapp.utils.ToastUtils;
import com.microsoft.kapp.utils.ViewUtils;
import com.microsoft.kapp.widgets.ConfirmationBar;
import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.GoalOperationResultDto;
import com.microsoft.krestsdk.models.GoalTemplateDto;
import com.microsoft.krestsdk.models.GoalType;
import com.microsoft.krestsdk.models.GoalValueTemplateDto;
import com.microsoft.krestsdk.services.RestService;
import java.util.List;
import javax.inject.Inject;
/* loaded from: classes.dex */
public class EditGoalDialogFragment extends HeaderBarDialogFragment implements TextWatcher, CompoundButton.OnCheckedChangeListener, GoalsGetTask.OnGoalsRetrieveTaskListener, GoalTemplatesGetTask.OnGoalTemplatesRetrieveTaskListener, GoalAddTask.OnGoalAddTaskListener, GoalDeleteTask.OnGoalDeleteTaskListener, GoalUpdateTask.OnGoalUpdateTaskListener {
    private static final String TAG = EditGoalDialogFragment.class.getSimpleName();
    private ConfirmationBar mConfirmationBar;
    private LinearLayout mContentLinearLayout;
    private TextView mEstimateTextView;
    private GoalDto mGoal;
    private GoalAddTask mGoalAddTask;
    private GoalDeleteTask mGoalDeleteTask;
    private EditText mGoalEditText;
    private final GoalProcessor mGoalProcessor;
    @Inject
    GoalProcessorManager mGoalProcessorManager;
    private GoalTemplateDto mGoalTemplate;
    private GoalTemplatesGetTask mGoalTemplatesGetTask;
    private final GoalType mGoalType;
    private GoalUpdateTask mGoalUpdateTask;
    private int mGoalValue;
    private GoalsGetTask mGoalsGetTask;
    private boolean mIsInitialLoading;
    private Integer mOriginalGoalValue;
    private FrameLayout mProgressFrameLayout;
    @Inject
    RestService mRestService;
    private Switch mSwitch;
    private TextView mTitleTextView;

    public EditGoalDialogFragment(Context context, GoalType goalType) {
        Validate.notNull(context, Constants.FRE_INTENT_EXTRA_INFO);
        Validate.notNull(goalType, "goalType");
        ((KApplication) context.getApplicationContext()).inject(this);
        this.mGoalType = goalType;
        this.mGoalProcessor = this.mGoalProcessorManager.getGoalProcessor(this.mGoalType);
    }

    @Override // android.support.v4.app.DialogFragment, android.support.v4.app.Fragment
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(0, R.style.AppTheme);
        this.mIsInitialLoading = true;
    }

    @Override // android.support.v4.app.Fragment
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().getWindow().setSoftInputMode(16);
        View view = inflater.inflate(R.layout.fragment_edit_goal, container, false);
        this.mProgressFrameLayout = (FrameLayout) ViewUtils.getValidView(view, R.id.progress_frame_layout, FrameLayout.class);
        this.mContentLinearLayout = (LinearLayout) ViewUtils.getValidView(view, R.id.content_linear_layout, LinearLayout.class);
        this.mSwitch = (Switch) ViewUtils.getValidView(view, R.id.enabled_switch, Switch.class);
        this.mTitleTextView = (TextView) ViewUtils.getValidView(view, R.id.goal_title_text_view, TextView.class);
        this.mGoalEditText = (EditText) ViewUtils.getValidView(view, R.id.goal_value_edit_text, EditText.class);
        this.mEstimateTextView = (TextView) ViewUtils.getValidView(view, R.id.estimate_text_view, TextView.class);
        this.mConfirmationBar = (ConfirmationBar) ViewUtils.getValidView(view, R.id.confirmation_bar, ConfirmationBar.class);
        if (this.mGoalProcessor != null) {
            this.mTitleTextView.setText(this.mGoalProcessor.getEditGoalTitleText());
        }
        InputFilter filter = new InputFilter() { // from class: com.microsoft.kapp.fragments.EditGoalDialogFragment.1
            @Override // android.text.InputFilter
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!Character.isDigit(source.charAt(i))) {
                        return "";
                    }
                }
                int length = dest.length();
                if (length >= 5) {
                    return "";
                }
                return null;
            }
        };
        this.mGoalEditText.setFilters(new InputFilter[]{filter});
        this.mGoalEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() { // from class: com.microsoft.kapp.fragments.EditGoalDialogFragment.2
            @Override // android.widget.TextView.OnEditorActionListener
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId & 6) != 0) {
                    EditGoalDialogFragment.this.saveOrDismiss();
                    return true;
                }
                return false;
            }
        });
        this.mConfirmationBar.setOnConfirmButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.EditGoalDialogFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                EditGoalDialogFragment.this.saveOrDismiss();
            }
        });
        this.mConfirmationBar.setOnCancelButtonClickListener(new View.OnClickListener() { // from class: com.microsoft.kapp.fragments.EditGoalDialogFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                EditGoalDialogFragment.this.dismiss();
            }
        });
        this.mGoalEditText.addTextChangedListener(this);
        this.mSwitch.setOnCheckedChangeListener(this);
        load();
        return view;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable s) {
        try {
            if (s.length() == 0) {
                this.mGoalValue = 0;
                s.append(AppEventsConstants.EVENT_PARAM_VALUE_NO);
            } else {
                if (s.charAt(0) == '0' && s.length() > 1) {
                    s.delete(0, 1);
                }
                this.mGoalValue = Integer.parseInt(s.toString());
            }
            updateEstimate();
            if (!this.mIsInitialLoading) {
                this.mConfirmationBar.setVisibility(0);
            }
        } catch (NumberFormatException e) {
            KLog.w(TAG, "Non-digit character detected.");
        }
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        this.mGoalEditText.setEnabled(isChecked);
        updateEstimate();
        if (isChecked) {
            setFocusOnGoalEditView();
        }
        if (!this.mIsInitialLoading) {
            this.mConfirmationBar.setVisibility(0);
        }
    }

    @Override // com.microsoft.kapp.tasks.GoalsGetTask.OnGoalsRetrieveTaskListener
    public void onGoalsRetrieved(List<GoalDto> goalsList) {
        GoalDto foundGoal = null;
        if (goalsList != null) {
            for (GoalDto goal : goalsList) {
                if (goal.getType() == this.mGoalType) {
                    foundGoal = goal;
                }
            }
        }
        this.mGoal = foundGoal;
        if (this.mGoal == null) {
            this.mGoalTemplatesGetTask = new GoalTemplatesGetTask.Builder().forParentFragment(this).usingRestService(this.mRestService).withListener(this).build();
            this.mGoalTemplatesGetTask.execute();
            return;
        }
        int value = GoalsUtils.getGoalValue(this.mGoal);
        this.mGoalValue = value;
        this.mOriginalGoalValue = Integer.valueOf(this.mGoalValue);
        displayContentView(true, true);
    }

    @Override // com.microsoft.kapp.tasks.GoalTemplatesGetTask.OnGoalTemplatesRetrieveTaskListener
    public void onGoalTemplatesRetrieved(List<GoalTemplateDto> templates) {
        if (templates == null) {
            KLog.w(TAG, "The server did not return any goal template.");
            displayGoalRetrieveError();
            return;
        }
        for (GoalTemplateDto template : templates) {
            if (template.getType() == this.mGoalType) {
                this.mGoalTemplate = template;
            }
        }
        if (this.mGoalTemplate == null) {
            String message = String.format("The server did not return any goal template for goal type %s.", this.mGoalType.toString());
            KLog.w(TAG, message);
            displayGoalRetrieveError();
            return;
        }
        GoalValueTemplateDto template2 = this.mGoalTemplate.getGoalValueTemplates().get(0);
        Object value = template2.getRecommended();
        if (value == null) {
            String message2 = String.format("The server did not return any recommended value for goal type %s.", this.mGoalType.toString());
            KLog.w(TAG, message2);
            value = template2.getThreshold();
        }
        if (value == null) {
            String message3 = String.format("The server did not return any threshold value for goal type %s.", this.mGoalType.toString());
            KLog.w(TAG, message3);
            displayGoalRetrieveError();
        } else {
            this.mGoalValue = ((Double) value).intValue();
        }
        displayContentView(false, true);
    }

    @Override // com.microsoft.kapp.tasks.GoalAddTask.OnGoalAddTaskListener
    public void onGoalAdded(GoalOperationResultDto result) {
        if (result.isSucceeded()) {
            dismiss();
        } else {
            displayError(R.string.edit_goal_save_goal_failed);
        }
    }

    @Override // com.microsoft.kapp.tasks.GoalUpdateTask.OnGoalUpdateTaskListener
    public void onGoalUpdated(GoalOperationResultDto result) {
        if (result.isSucceeded()) {
            dismiss();
        } else {
            displayError(R.string.edit_goal_save_goal_failed);
        }
    }

    @Override // com.microsoft.kapp.tasks.GoalDeleteTask.OnGoalDeleteTaskListener
    public void onGoalDeleted() {
        this.mOriginalGoalValue = null;
        dismiss();
    }

    @Override // com.microsoft.kapp.tasks.OnTaskStateChangedListener
    public void onTaskFailed(StateListenerTask task, Exception ex) {
        if (task == this.mGoalsGetTask || task == this.mGoalTemplatesGetTask) {
            displayGoalRetrieveError();
        } else {
            displayError(R.string.edit_goal_save_goal_failed);
        }
    }

    private void displayGoalRetrieveError() {
        displayError(R.string.edit_goal_retrieve_goal_failed);
    }

    private void displayProgressView() {
        this.mProgressFrameLayout.setVisibility(0);
        this.mContentLinearLayout.setVisibility(8);
    }

    private void displayContentView(boolean enableGoal, boolean updateGoalValue) {
        this.mSwitch.setChecked(enableGoal);
        this.mGoalEditText.setEnabled(enableGoal);
        if (updateGoalValue) {
            this.mGoalEditText.setText(String.valueOf(this.mGoalValue));
        }
        this.mProgressFrameLayout.setVisibility(8);
        this.mContentLinearLayout.setVisibility(0);
        if (enableGoal) {
            setFocusOnGoalEditView();
        }
        this.mIsInitialLoading = false;
    }

    private void load() {
        displayProgressView();
        this.mGoalsGetTask = new GoalsGetTask.Builder().forParentFragment(this).usingRestService(this.mRestService).withListener(this).build();
        this.mGoalsGetTask.execute();
    }

    private void addGoal() {
        displayProgressView();
        this.mGoalAddTask = new GoalAddTask.Builder().forParentFragment(this).usingRestService(this.mRestService).withListener(this).usingGoalTemplate(this.mGoalTemplate).withGoalValue(this.mGoalValue).build();
        this.mGoalAddTask.execute();
    }

    private void updateGoal() {
        displayProgressView();
        this.mGoalUpdateTask = new GoalUpdateTask.Builder().forParentFragment(this).usingRestService(this.mRestService).withListener(this).forGoal(this.mGoal).withGoalValue(this.mGoalValue).build();
        this.mGoalUpdateTask.execute();
    }

    private void deleteGoal() {
        displayProgressView();
        this.mGoalDeleteTask = new GoalDeleteTask.Builder().forParentFragment(this).usingRestService(this.mRestService).withListener(this).targettingGoalId(this.mGoal.getId()).build();
        this.mGoalDeleteTask.execute();
    }

    private void updateEstimate() {
        if (this.mSwitch.isChecked()) {
            if (this.mGoalProcessor != null) {
                this.mEstimateTextView.setText(this.mGoalProcessor.getEditGoalEstimateText(this.mGoalValue));
                return;
            } else {
                this.mEstimateTextView.setText(R.string.edit_goal_dialog_fragment_empty_user_profile_error_text);
                return;
            }
        }
        this.mEstimateTextView.setText(R.string.edit_goal_dialog_fragment_estimate_disabled);
    }

    private void setFocusOnGoalEditView() {
        this.mGoalEditText.setSelection(this.mGoalEditText.length());
        this.mGoalEditText.requestFocus();
        ViewUtils.closeSoftKeyboard(getActivity(), this.mGoalEditText);
    }

    private void displayError(int messageResourceId) {
        disableGoal();
        displayContentView(false, false);
        Activity activity = getActivity();
        if (activity != null) {
            ToastUtils.showLongToast(activity, messageResourceId);
        }
    }

    private void disableGoal() {
        this.mSwitch.setEnabled(false);
        this.mGoalEditText.setEnabled(false);
        this.mGoalValue = 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void saveOrDismiss() {
        this.mConfirmationBar.setVisibility(8);
        boolean isGoalEnabled = this.mSwitch.isChecked();
        if (this.mOriginalGoalValue == null && isGoalEnabled) {
            addGoal();
        } else if (this.mOriginalGoalValue != null && !isGoalEnabled) {
            deleteGoal();
        } else if (this.mOriginalGoalValue != null && this.mOriginalGoalValue.intValue() != this.mGoalValue) {
            updateGoal();
        } else {
            dismiss();
        }
    }
}
