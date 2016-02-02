package service;

import model.local.SpaceL;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liweiqi on 2014/12/14.
 */
public class SpaceValiSrv {
    private static SpaceValiSrv ssCheck = new SpaceValiSrv();
    private final long taskPeriod = 24 * 60 * 60 * 1000;
    ScheduledExecutorService srv = Executors.newSingleThreadScheduledExecutor();
    Lock lock = new ReentrantLock();
    private boolean isStart = false;
    Runnable checkTask;

    public static SpaceValiSrv me() {
        return ssCheck;
    }

    private SpaceValiSrv() {
        checkTask = new Runnable() {
            @Override
            public void run() {
                //get all alive space
                List<SpaceL> spaces = SpaceSrv.me().allInUse();
                for (SpaceL spaceL : spaces) {
                    long endtime = spaceL.getLong(SpaceL.ENDTIME);
                    taskForDangerSpace(endtime, spaceL);
                    taskForWarningSpace(endtime, spaceL);
                }
            }
        };
    }

    private void taskForDangerSpace(long endTime, SpaceL spaceL) {
        if (endTime < System.currentTimeMillis()) {
            spaceL.set(SpaceL.STATE, SpaceL.ENDTIME).update();
        }
    }

    private void taskForWarningSpace(long endTime, SpaceL spaceL) {
        if (endTime > System.currentTimeMillis() && endTime < System.currentTimeMillis() - 7 * taskPeriod) {
            //todo send mail
        }
    }

    public void start() {
        lock.lock();
        try {
            Calendar firstTime = Calendar.getInstance();
            firstTime.set(Calendar.HOUR_OF_DAY, 2);
            firstTime.set(Calendar.MINUTE, 0);
            Date timeNow = new Date();
            if (firstTime.getTimeInMillis() < timeNow.getTime()) {
                firstTime.add(Calendar.DAY_OF_MONTH, 1);
            }
            long firstDelay = firstTime.getTimeInMillis() - timeNow.getTime();
            srv.scheduleAtFixedRate(checkTask, firstDelay, taskPeriod, TimeUnit.MILLISECONDS);
            isStart = true;
        } finally {
            lock.unlock();
        }
    }

    public void stop() {
        lock.lock();
        try {
            if (isStart) {
                srv.shutdownNow();
                isStart = false;
            }
        } finally {
            lock.unlock();
        }
    }
}
