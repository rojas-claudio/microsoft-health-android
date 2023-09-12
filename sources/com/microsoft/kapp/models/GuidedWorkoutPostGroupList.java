package com.microsoft.kapp.models;

import com.microsoft.kapp.models.guidedworkout.Group;
import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public class GuidedWorkoutPostGroupList extends ArrayList<Group> {
    private static final long serialVersionUID = -3269567842327137684L;
    HashMap<CircuitGroupType, Integer> mCircuitGroupType;

    public GuidedWorkoutPostGroupList() {
        this.mCircuitGroupType = new HashMap<>();
    }

    public GuidedWorkoutPostGroupList(int length) {
        super(length);
        this.mCircuitGroupType = new HashMap<>();
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Group group) {
        boolean success = super.add((GuidedWorkoutPostGroupList) group);
        if (success && group != null) {
            int count = 1;
            CircuitGroupType key = group.getCircuitGroupType();
            if (!group.getIsRestCircuit() && key != CircuitGroupType.Rest) {
                if (this.mCircuitGroupType.containsKey(key)) {
                    count = this.mCircuitGroupType.get(key).intValue() + 1;
                }
                group.setCircuitGroupTypeIndex(count);
                this.mCircuitGroupType.put(key, Integer.valueOf(count));
            }
        }
        return success;
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.List
    public Group set(int position, Group group) {
        Group previousCircuit = (Group) super.set(position, (int) group);
        if (group != null && previousCircuit == null) {
            int count = 1;
            CircuitGroupType key = group.getCircuitGroupType();
            if (!group.getIsRestCircuit() && key != CircuitGroupType.Rest) {
                if (this.mCircuitGroupType.containsKey(key)) {
                    count = this.mCircuitGroupType.get(key).intValue() + 1;
                }
                group.setCircuitGroupTypeIndex(count);
                this.mCircuitGroupType.put(key, Integer.valueOf(count));
            }
        }
        return previousCircuit;
    }

    public int getGroupTypeCount(CircuitGroupType groupType) {
        if (this.mCircuitGroupType.containsKey(groupType)) {
            return this.mCircuitGroupType.get(groupType).intValue();
        }
        return 0;
    }
}
