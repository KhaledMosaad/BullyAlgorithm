package com.company;


public class BullyNode {
    private boolean isCoordinator;

    public void setCoordinator(boolean coordinator) {
        isCoordinator = coordinator;
    }
    public boolean getCoordinator(){
        return isCoordinator;
    }
    public static void main(String[] args)
    {
       System.out.println("Process "+args[0]);
    }
}
