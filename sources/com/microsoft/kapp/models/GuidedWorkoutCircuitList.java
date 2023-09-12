package com.microsoft.kapp.models;

import com.microsoft.kapp.services.healthandfitness.models.CircuitGroupType;
import com.microsoft.kapp.services.healthandfitness.models.WorkoutCircuit;
import java.util.ArrayList;
import java.util.HashMap;
/* loaded from: classes.dex */
public class GuidedWorkoutCircuitList extends ArrayList<WorkoutCircuit> {
    private static final long serialVersionUID = -3269567842327137684L;
    HashMap<CircuitGroupType, Integer> mCircuitGroupType;

    public GuidedWorkoutCircuitList(int length) {
        super(length);
        this.mCircuitGroupType = new HashMap<>();
    }

    @Override // java.util.ArrayList, java.util.AbstractList, java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(WorkoutCircuit circuit) {
        boolean success = super.add((GuidedWorkoutCircuitList) circuit);
        if (success) {
            int count = 1;
            if (this.mCircuitGroupType.containsKey(circuit.getGroupType())) {
                count = this.mCircuitGroupType.get(circuit.getGroupType()).intValue() + 1;
            }
            circuit.setGroupTypeIndex(count);
            this.mCircuitGroupType.put(circuit.getGroupType(), Integer.valueOf(count));
        }
        return success;
    }

    public int getGroupTypeCount(CircuitGroupType type) {
        if (this.mCircuitGroupType.containsKey(type)) {
            return this.mCircuitGroupType.get(type).intValue();
        }
        return 0;
    }
}
