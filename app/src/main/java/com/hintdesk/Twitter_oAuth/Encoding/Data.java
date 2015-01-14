package com.hintdesk.Twitter_oAuth.Encoding;

import twitter4j.Status;

import java.util.ArrayList;

/**
 * Created by Christopher Stokes on 06/07/14.
 */
public class Data {
    private static ArrayList<Status> previousStatuses = new ArrayList<Status>();

    public static boolean checkIfNewStatus(Status status){
        if(previousStatuses.contains(status)){
            return false;
        } else {
            return true;
        }
    }

    public static void addStatus(Status status){
        previousStatuses.add(status);
    }

    public static Status getStatus(int index){
        return previousStatuses.get(index);
    }

    public static Status getLastStatusAdded(){
        return previousStatuses.get((previousStatuses.size() - 1));
    }

}
