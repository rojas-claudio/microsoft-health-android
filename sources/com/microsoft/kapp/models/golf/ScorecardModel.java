package com.microsoft.kapp.models.golf;

import com.microsoft.kapp.models.golf.ScorecardItem;
import com.microsoft.krestsdk.models.GolfEvent;
import com.microsoft.krestsdk.models.GolfEventHoleSequence;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ScorecardModel {
    private static final int IN_ITEMS_COUNT = 18;
    private static final int OUT_ITEMS_COUNT = 9;
    private List<ScorecardItem> mScorecardItems = new ArrayList();
    private String mScorecardTeesPlayed;

    public ScorecardModel(GolfEvent golfEvent) {
        populateScorecard(golfEvent);
    }

    private void populateScorecard(GolfEvent golfEvent) {
        GolfEventHoleSequence[] sequences = golfEvent.getSequences();
        for (GolfEventHoleSequence sequence : sequences) {
            int holeNumber = sequence.getHoleNumber();
            ScorecardItem item = getScorecardItem(sequence);
            this.mScorecardItems.add(item);
            if (holeNumber == 9) {
                this.mScorecardItems.add(getOutScorecardItem(sequences));
            } else if (holeNumber == 18) {
                this.mScorecardItems.add(getInScorecardItem(sequences));
            }
        }
        this.mScorecardItems.add(getTotalScorecardItem(sequences));
        setScorecardTeesPlayed(golfEvent.getTeeNameSelected());
    }

    private ScorecardItem getScorecardItem(GolfEventHoleSequence sequence) {
        ScorecardItem item = new ScorecardItem();
        ScorecardItemDetails itemDetails = new ScorecardItemDetails();
        itemDetails.setImageURI(sequence.getHoleShotOverlayImageUrl());
        itemDetails.setCals(sequence.getCaloriesBurned());
        itemDetails.setDistance(sequence.getDistanceWalkedInCm());
        itemDetails.setDuration(sequence.getDuration());
        itemDetails.setSteps(sequence.getStepCount());
        itemDetails.setMaxHR(sequence.getPeakHeartRate());
        itemDetails.setMinHR(sequence.getLowestHeartRate());
        itemDetails.setAvgHR(sequence.getAverageHeartRate());
        item.setDetails(itemDetails);
        item.setItemtype(ScorecardItem.ScorecardItemType.HOLE);
        item.setHole(sequence.getHoleNumber());
        item.setIndex(sequence.getHoleDifficultyIndex());
        item.setPar(sequence.getHolePar());
        item.setDistance(sequence.getDistanceToPinInCm());
        item.setScore(sequence.getUserScore());
        int scoreStateVal = item.getScore() - item.getPar();
        item.setScoreState(getScoreState(scoreStateVal));
        return item;
    }

    private ScorecardItem.ScorecardScoreState getScoreState(int scoreStateVal) {
        ScorecardItem.ScorecardScoreState scoreState = ScorecardItem.ScorecardScoreState.PAR;
        switch (scoreStateVal) {
            case -2:
                ScorecardItem.ScorecardScoreState scoreState2 = ScorecardItem.ScorecardScoreState.UNDER_2;
                return scoreState2;
            case -1:
                ScorecardItem.ScorecardScoreState scoreState3 = ScorecardItem.ScorecardScoreState.UNDER_1;
                return scoreState3;
            case 0:
                ScorecardItem.ScorecardScoreState scoreState4 = ScorecardItem.ScorecardScoreState.PAR;
                return scoreState4;
            case 1:
                ScorecardItem.ScorecardScoreState scoreState5 = ScorecardItem.ScorecardScoreState.OVER_1;
                return scoreState5;
            case 2:
                ScorecardItem.ScorecardScoreState scoreState6 = ScorecardItem.ScorecardScoreState.OVER_2;
                return scoreState6;
            default:
                if (scoreStateVal < -2) {
                    ScorecardItem.ScorecardScoreState scoreState7 = ScorecardItem.ScorecardScoreState.UNDER_3;
                    return scoreState7;
                } else if (scoreStateVal > 2) {
                    ScorecardItem.ScorecardScoreState scoreState8 = ScorecardItem.ScorecardScoreState.OVER_3;
                    return scoreState8;
                } else {
                    return scoreState;
                }
        }
    }

    private ScorecardItem getTotalScorecardItem(GolfEventHoleSequence[] sequences) {
        int totalDistance = 0;
        int totalPar = 0;
        int totalScore = 0;
        for (GolfEventHoleSequence sequence : sequences) {
            totalDistance += sequence.getDistanceToPinInCm();
            totalPar += sequence.getHolePar();
            totalScore += sequence.getUserScore();
        }
        ScorecardItem item = new ScorecardItem();
        item.setItemtype(ScorecardItem.ScorecardItemType.TOT);
        item.setDistance(totalDistance);
        item.setPar(totalPar);
        item.setScore(totalScore);
        return item;
    }

    private ScorecardItem getInScorecardItem(GolfEventHoleSequence[] sequences) {
        int totalDistance = 0;
        int totalPar = 0;
        int totalScore = 0;
        for (GolfEventHoleSequence sequence : sequences) {
            if (sequence.getHoleNumber() > 9 && sequence.getHoleNumber() <= 18) {
                totalDistance += sequence.getDistanceToPinInCm();
                totalPar += sequence.getHolePar();
                totalScore += sequence.getUserScore();
            }
        }
        ScorecardItem item = new ScorecardItem();
        item.setItemtype(ScorecardItem.ScorecardItemType.IN);
        item.setDistance(totalDistance);
        item.setPar(totalPar);
        item.setScore(totalScore);
        return item;
    }

    private ScorecardItem getOutScorecardItem(GolfEventHoleSequence[] sequences) {
        int totalDistance = 0;
        int totalPar = 0;
        int totalScore = 0;
        for (GolfEventHoleSequence sequence : sequences) {
            if (sequence.getHoleNumber() > 0 && sequence.getHoleNumber() <= 9) {
                totalDistance += sequence.getDistanceToPinInCm();
                totalPar += sequence.getHolePar();
                totalScore += sequence.getUserScore();
            }
        }
        ScorecardItem item = new ScorecardItem();
        item.setItemtype(ScorecardItem.ScorecardItemType.OUT);
        item.setDistance(totalDistance);
        item.setPar(totalPar);
        item.setScore(totalScore);
        return item;
    }

    public int getTotalRows() {
        return this.mScorecardItems.size();
    }

    public ScorecardItem getRow(int rowID) {
        return this.mScorecardItems.get(rowID);
    }

    public ScorecardItemDetails getDetailsforRow(int rowID) {
        return this.mScorecardItems.get(rowID).getDetails();
    }

    public String getScorecardTeesPlayed() {
        return this.mScorecardTeesPlayed;
    }

    public void setScorecardTeesPlayed(String mScorecardTeesPlayed) {
        this.mScorecardTeesPlayed = mScorecardTeesPlayed;
    }
}
