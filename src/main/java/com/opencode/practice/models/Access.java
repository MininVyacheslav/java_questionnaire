package com.opencode.practice.models;

public enum Access {
    DEVELOPERS_READ("developers:read"),
    DEVELOPERS_WRITE("developers:write");

    private final String access;

    Access(String access) {
        this.access = access;
    }

    public String getAccess() {
        return access;
    }
}
