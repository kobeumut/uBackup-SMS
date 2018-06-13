package com.grisoft.umut.uBackup;

/**
 * Created by Umut on 24.01.2016.
 */

public class APIpojo {

    /**
     * json :
     */

    public String backup;

    public String getBackup() {
        return backup;
    }

    public void setBackup(String backup) {
        this.backup = backup;
    }

    private String json;
    public APIpojo() {}
    public APIpojo(String json) {
        this.json = json;
    }
    public void setJson(String json) {
        this.json = json;
    }

    public String getJson() {
        return json;
    }
}
