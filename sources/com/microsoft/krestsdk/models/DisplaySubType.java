package com.microsoft.krestsdk.models;
/* loaded from: classes.dex */
public enum DisplaySubType {
    UNKNOWN(0),
    TIME(1),
    TASK(2),
    RUNNING(3),
    BODYWEIGHT(4),
    STRENGTH(5),
    RUNNING_ONEOFF(6),
    RUNNING_MULTIWEEK(7),
    BODYWEIGHT_ONEOFF(8),
    BODYWEIGHT_MULTIWEEK(9),
    STRENGTH_ONEOFF(10),
    STRENGTH_MULTIWEEK(11),
    BIKING(12),
    BIKING_ONEOFF(13),
    BIKING_MULTIWEEK(14),
    HIKING(15),
    HIKING_ONEOFF(16),
    HIKING_MULTIWEEK(17),
    SWIMMING(18),
    SWIMMING_ONEOFF(19),
    SWIMMING_MULTIWEEK(20),
    GOLFING(21),
    GOLFING_ONEOFF(22),
    GOLFING_MULTIWEEK(23),
    SKIING(24),
    SKIING_ONEOFF(25),
    SKIING_MULTIWEEK(26),
    SLEEPING(27),
    SLEEPING_ONEOFF(28),
    SLEEPING_MULTIWEEK(29),
    YOGA(30),
    YOGA_ONEOFF(31),
    YOGA_MULTIWEEK(32),
    PILATES(33),
    PILATES_ONEOFF(34),
    PILATES_MULTIWEEK(35),
    TENNIS(36),
    TENNIS_ONEOFF(37),
    TENNIS_MULTIWEEK(38),
    TRIATHLON(39),
    TRIATHLON_ONEOFF(40),
    TRIATHLON_MULTIWEEK(41),
    SOCCER(42),
    SOCCER_ONEOFF(43),
    SOCCER_MULTIWEEK(44),
    FOOTBALL(45),
    FOOTBALL_ONEOFF(46),
    FOOTBALL_MULTIWEEK(47),
    BASEBALL(48),
    BASEBALL_ONEOFF(49),
    BASEBALL_MULTIWEEK(50),
    BASKETBALL(51),
    BASKETBALL_ONEOFF(52),
    BASKETBALL_MULTIWEEK(53);
    
    private static final int MAXIMUM_VALUE = 29;
    private static final int MINIMUM_VALUE = 0;
    private final int mValue;

    DisplaySubType(int value) {
        if (!isValid(value)) {
            this.mValue = 0;
        } else {
            this.mValue = value;
        }
    }

    public int value() {
        return this.mValue;
    }

    private static boolean isValid(int value) {
        return value >= 0 && value <= 29;
    }
}
