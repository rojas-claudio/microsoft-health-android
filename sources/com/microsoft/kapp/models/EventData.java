package com.microsoft.kapp.models;

import com.microsoft.krestsdk.models.GoalDto;
import com.microsoft.krestsdk.models.UserEvent;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class EventData {
    private List<?> mEvents;
    private List<GoalDto> mPersonalGoals;
    private ArrayList<UserEvent> mPersonalBestevents = new ArrayList<>();
    private ArrayList<PersonalBest> mRunPersonalBestStatValues = new ArrayList<>();
    private ArrayList<PersonalBest> mExercisePersonalBestStatValues = new ArrayList<>();
    private ArrayList<PersonalBest> mBikePersonalBestStatValues = new ArrayList<>();

    public List<GoalDto> getPersonalGoals() {
        return this.mPersonalGoals;
    }

    public void setPersonalGoals(List<GoalDto> mPersonalGoals) {
        this.mPersonalGoals = mPersonalGoals;
    }

    public List<?> getEvents() {
        return this.mEvents;
    }

    public void setEvents(List<?> mEvents) {
        this.mEvents = mEvents;
    }

    public ArrayList<UserEvent> getPersonalBestEvents() {
        return this.mPersonalBestevents;
    }

    public void setPersonalBestevents(ArrayList<UserEvent> mPersonalBestevents) {
        this.mPersonalBestevents = mPersonalBestevents;
    }

    public ArrayList<PersonalBest> getRunPersonalBestStatValues() {
        return this.mRunPersonalBestStatValues;
    }

    public void setRunPersonalBestStatValues(ArrayList<PersonalBest> mRunPersonalBestStatValues) {
        this.mRunPersonalBestStatValues = mRunPersonalBestStatValues;
    }

    public ArrayList<PersonalBest> getExercisePersonalBestStatValues() {
        return this.mExercisePersonalBestStatValues;
    }

    public void setExercisePersonalBestStatValues(ArrayList<PersonalBest> mExercisePersonalBestStatValues) {
        this.mExercisePersonalBestStatValues = mExercisePersonalBestStatValues;
    }

    public ArrayList<PersonalBest> getBikePersonalBestStatValues() {
        return this.mBikePersonalBestStatValues;
    }

    public void setBikePersonalBestStatValues(ArrayList<PersonalBest> bikeBestStatValues) {
        this.mBikePersonalBestStatValues = bikeBestStatValues;
    }
}
