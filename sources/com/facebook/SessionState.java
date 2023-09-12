package com.facebook;
/* loaded from: classes.dex */
public enum SessionState {
    CREATED(Category.CREATED_CATEGORY),
    CREATED_TOKEN_LOADED(Category.CREATED_CATEGORY),
    OPENING(Category.CREATED_CATEGORY),
    OPENED(Category.OPENED_CATEGORY),
    OPENED_TOKEN_UPDATED(Category.OPENED_CATEGORY),
    CLOSED_LOGIN_FAILED(Category.CLOSED_CATEGORY),
    CLOSED(Category.CLOSED_CATEGORY);
    
    private final Category category;

    /* loaded from: classes.dex */
    private enum Category {
        CREATED_CATEGORY,
        OPENED_CATEGORY,
        CLOSED_CATEGORY;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Category[] valuesCustom() {
            Category[] valuesCustom = values();
            int length = valuesCustom.length;
            Category[] categoryArr = new Category[length];
            System.arraycopy(valuesCustom, 0, categoryArr, 0, length);
            return categoryArr;
        }
    }

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static SessionState[] valuesCustom() {
        SessionState[] valuesCustom = values();
        int length = valuesCustom.length;
        SessionState[] sessionStateArr = new SessionState[length];
        System.arraycopy(valuesCustom, 0, sessionStateArr, 0, length);
        return sessionStateArr;
    }

    SessionState(Category category) {
        this.category = category;
    }

    public boolean isOpened() {
        return this.category == Category.OPENED_CATEGORY;
    }

    public boolean isClosed() {
        return this.category == Category.CLOSED_CATEGORY;
    }
}
