package by.epam.task.ferry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.locks.ReentrantLock;

public class Ferry {

    private static final Logger LOG = LogManager.getLogger("name");

    private static Ferry instance = new Ferry();

    private int indexOfQueueA = 1;
    private int indexOfQueueB = 1;
    private boolean currentBank = true;
    private ReentrantLock locker;
    private double maxSquare;
    private double maxWeight;

    private double currentSquare = 0;

    private double currentWeight = 0;

    public static Ferry getInstance() {
        return instance;
    }

    public ReentrantLock getLocker() {
        return locker;
    }

    public boolean isCurrentBank() {
        return currentBank;
    }

    public double getCurrentSquare() {
        return currentSquare;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public int getIndexOfQueueA() {
        return indexOfQueueA;
    }

    public int getIndexOfQueueB() {
        return indexOfQueueB;
    }

    public void setCurrentBank(final boolean newCurrentBank) {
        this.currentBank = newCurrentBank;
    }

    public void setLocker(final ReentrantLock locker) {
        this.locker = locker;
    }

    public void setMaxSquare(final double newMaxSquare) {
        this.maxSquare = newMaxSquare;
    }

    public void setMaxWeight(final double newMaxWeight) {
        this.maxWeight = newMaxWeight;
    }

    public void setCurrentSquare(final double newCurrentSquare) {
        this.currentSquare = newCurrentSquare;
    }

    public void setCurrentWeight(final double newCurrentWeight) {
        this.currentWeight = newCurrentWeight;
    }

    public boolean addCar(final double square, final double weight) throws InterruptedException {
        if (currentBank) {
            if (currentWeight + weight >= maxWeight || currentSquare + square >= maxSquare) {
                LOG.info("Переправка транспорта из А в Б");
                currentBank = false;
                this.currentSquare = 0;
                this.currentWeight = 0;
                Thread.sleep(1000);
                return false;
            }
            this.currentWeight += weight;
            this.currentSquare += square;
            indexOfQueueA++;
            return true;
        } else {
            if (currentWeight + weight >= maxWeight || currentSquare + square >= maxSquare) {
                LOG.info("Переправка транспорта из Б в А");
                currentBank = true;
                this.currentSquare = 0;
                this.currentWeight = 0;
                Thread.sleep(1000);
                return false;
            }
            this.currentWeight += weight;
            this.currentSquare += square;
            indexOfQueueB++;
            return true;
        }
    }
}
