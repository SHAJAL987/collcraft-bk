package com.middlewareExp.collcraft.payload;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class DownloadColl {
    private String coll;
    private String collName;

    public String getColl() {
        return coll;
    }

    public void setColl(String coll) {
        this.coll = coll;
    }

    public String getCollName() {
        return collName;
    }

    public void setCollName(String collName) {
        this.collName = collName;
    }
}
