package by.epam.task.lorry;

import by.epam.task.ferry.Ferry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static by.epam.task.run.Main.SIZEFIRSTQUEUE;
import static by.epam.task.run.Main.SIZESECONDQUEUE;

public class Lorry implements Runnable {

    private static final Logger LOG = LogManager.getLogger("name");

    private boolean bankA;
    private String nameBank;
    private int index;
    private double weight;
    private double square;

    public Lorry(double weight, double square, int ind, boolean bank) {
        this.weight = weight;
        this.square = square;
        this.index = ind;
        this.bankA = bank;
        if (bankA) {
            this.nameBank = "A";
        } else {
            this.nameBank = "B";
        }
    }

    private void transport() throws InterruptedException {
        Ferry.getInstance().setCurrentWeight(0);
        Ferry.getInstance().setCurrentSquare(0);
        Ferry.getInstance().setCurrentBank(!Ferry.getInstance().isCurrentBank());
        if (bankA) {
            LOG.info("Переправка транспорта из А в Б");
        } else {
            LOG.info("Переправка транспорта из Б в A");
        }
        Thread.sleep(1000);
    }

    @Override
    public void run() {
        LOG.info("Грузовик " + index + " " + nameBank + " прибыл к причалу");

        if (bankA) {
            while (index != Ferry.getInstance().getIndexOfQueueA() || bankA != Ferry.getInstance().isCurrentBank()) {
                Thread.yield();
            }
        } else {
            while (index != Ferry.getInstance().getIndexOfQueueB() || bankA != Ferry.getInstance().isCurrentBank()) {
                Thread.yield();
            }
        }

        while (true) {
            if (!Ferry.getInstance().getLocker().isLocked()) {
                try {
                    Ferry.getInstance().getLocker().lock();
                    if (!Ferry.getInstance().addCar(this.square, this.weight)) {
                        Ferry.getInstance().getLocker().unlock();
                        if (bankA) {
                            while (index != Ferry.getInstance().getIndexOfQueueA() || bankA != Ferry.getInstance().isCurrentBank()) {
                                Thread.yield();
                            }
                        } else {
                            while (index != Ferry.getInstance().getIndexOfQueueB() || bankA != Ferry.getInstance().isCurrentBank()) {
                                Thread.yield();
                            }
                        }
                        Ferry.getInstance().getLocker().lock();
                        Ferry.getInstance().addCar(this.square, this.weight);
                    }
                    Thread.sleep(1000);
                    LOG.info("Грузовик " + index + " " + nameBank + " погружен" + "("
                            + Ferry.getInstance().getCurrentSquare()
                            + " " + Ferry.getInstance().getCurrentWeight() + ")");
                    if (bankA) {
                        if (SIZEFIRSTQUEUE == Ferry.getInstance().getIndexOfQueueA() - 1) {
                            transport();
                        }
                    } else {
                        if (SIZESECONDQUEUE == Ferry.getInstance().getIndexOfQueueB() - 1) {
                            transport();
                        }
                    }
                    Ferry.getInstance().getLocker().unlock();
                    break;
                } catch (InterruptedException e) {
                    LOG.error(e.getStackTrace());
                    Thread.currentThread().interrupt();
                }
            } else {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    LOG.error(e.getStackTrace());
                    Thread.currentThread().interrupt();
                }
            }
        }

    }
}
