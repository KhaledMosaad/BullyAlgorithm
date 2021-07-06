package com.company;

public class Election {
    private Process[] processes=Main.processes;
    private boolean isCoordinatorExist;
    public void setCoordinatorExist(boolean isCoordinatorExist)
    {
        this.isCoordinatorExist=isCoordinatorExist;
    }
    public boolean getIsCoordinatorExist()
    {
        return isCoordinatorExist;
    }
    public void Begin(Process process) {

    }
}
