package com.spring.crybot.bootstrap;

public abstract class DataLoader {

    public abstract void loadEnvironmentSpecificData();

    public void loadData() {
        loadCommonEnvironmentData();
        loadEnvironmentSpecificData();
    }

    public void loadCommonEnvironmentData() {
    }
}