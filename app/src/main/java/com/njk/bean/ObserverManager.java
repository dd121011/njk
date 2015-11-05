package com.njk.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by a on 2015/9/23.
 */
public class ObserverManager {
    private static ObserverManager instance;

    private ObserverManager() {}

    public static ObserverManager getInstance(){
        if(instance==null){
            instance = new ObserverManager();
        }
        return instance;
    }
    private static List<ReviewListener> reviewList = new ArrayList<>();
    private static List<AreasUpdateListener> areasUpdateListenerList = new ArrayList<>();

    public void addReviewListener(ReviewListener listener){
        if(listener!=null){
            reviewList.add(listener);
        }
    }

    public void removeReviewListener(ReviewListener listener){
        if(listener!=null && reviewList.contains(listener)){
            reviewList.remove(listener);
        }
    }

    public void notifyReviewOchange(){
        for (ReviewListener item: reviewList){
            item.notifyUpdateReview();
        }
    }

    public interface ReviewListener{
        void notifyUpdateReview();
    }


    public void addAreasUpdateListener(AreasUpdateListener listener){
        if(listener!=null){
            areasUpdateListenerList.add(listener);
        }
    }

    public void removeAreasUpdateListener(AreasUpdateListener listener){
        if(listener!=null && areasUpdateListenerList.contains(listener)){
            areasUpdateListenerList.remove(listener);
        }
    }

    public void notifyAreasOchange(){
        for (AreasUpdateListener item: areasUpdateListenerList){
            item.notifyUpdate();
        }
    }

    public interface AreasUpdateListener{
        void notifyUpdate();
    }
}
