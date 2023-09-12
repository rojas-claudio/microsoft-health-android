package com.microsoft.kapp.models;

import com.microsoft.band.device.StrappPageElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/* loaded from: classes.dex */
public class CustomStrappData {
    private boolean mAddLastUpdatedLayout;
    private int mLastLayoutIndex;
    private String mLastUpdatedText;
    private List<Integer> mLayoutArrays;
    private ArrayList<ArrayList<StrappPageElement>> mPageElements;
    private List<UUID> mPageUUIDs;
    private UUID mUUID;
    private boolean mVariableLength;

    public CustomStrappData(UUID strappUUID, List<UUID> pageUUIDs, ArrayList<ArrayList<StrappPageElement>> pageElements, List<Integer> layoutIndexArray, boolean hasVariableLength, String lastUpdatedText, int lastLayoutIndex, boolean addLastUpdateLayout) {
        this.mUUID = strappUUID;
        this.mPageUUIDs = pageUUIDs;
        this.mPageElements = pageElements;
        this.mLayoutArrays = layoutIndexArray;
        this.mVariableLength = hasVariableLength;
        this.mLastUpdatedText = lastUpdatedText;
        this.mLastLayoutIndex = lastLayoutIndex;
        this.mAddLastUpdatedLayout = addLastUpdateLayout;
    }

    public UUID getUUID() {
        return this.mUUID;
    }

    public int getLastLayoutIndex() {
        return this.mLastLayoutIndex;
    }

    public String getLastUpdatedText() {
        return this.mLastUpdatedText;
    }

    public List<UUID> getUUIDsForPages() {
        return this.mPageUUIDs;
    }

    public boolean isVariableLength() {
        return this.mVariableLength;
    }

    public ArrayList<ArrayList<StrappPageElement>> getPageElements() {
        return this.mPageElements;
    }

    public List<Integer> getLayoutArrays() {
        return this.mLayoutArrays;
    }

    public boolean isAddLastUpdatedLayout() {
        return this.mAddLastUpdatedLayout;
    }
}
