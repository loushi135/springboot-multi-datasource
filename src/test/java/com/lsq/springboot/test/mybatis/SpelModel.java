

package com.lsq.springboot.test.mybatis;

public class SpelModel {
    private String dsGroup;
    private boolean slave;

    public String getDsGroup() {
        return dsGroup;
    }

    public void setDsGroup(String dsGroup) {
        this.dsGroup = dsGroup;
    }

    public boolean isSlave() {
        return slave;
    }

    public void setSlave(boolean slave) {
        this.slave = slave;
    }
}
