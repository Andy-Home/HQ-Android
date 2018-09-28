package com.andy.dao;

public class DaoManager {
    private DaoManager() {
    }

    static class SingleHolder {
        final static DaoManager mInstance = new DaoManager();
    }

    public static DaoManager getInstance() {
        return SingleHolder.mInstance;
    }

    public CatalogService mCatalogService = null;

    public void initCatalogService() {
        if (mCatalogService == null) {
            mCatalogService = CatalogService.getInstance();
        }
    }

    public RecordService mRecordService;

    public void initRecordService() {
        if (mRecordService == null) {
            mRecordService = RecordService.getInstance();
        }
    }

    public UserService mUserService;

    public void initUserService() {
        if (mUserService == null) {
            mUserService = UserService.getInstance();
        }
    }
}
