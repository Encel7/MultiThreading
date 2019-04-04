package by.epam.task.run;

import by.epam.task.car.Car;
import by.epam.task.exception.FileException;
import by.epam.task.ferry.Ferry;
import by.epam.task.lorry.Lorry;
import by.epam.task.parser.FileParser;
import by.epam.task.reader.FileReader;
import by.epam.task.validator.LineValidator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
public class Main {

    public static int SIZEFIRSTQUEUE;
    public static int SIZESECONDQUEUE;

    public static void main(String[] args) throws InterruptedException, FileException {


        ReentrantLock lock = new ReentrantLock();

        FileReader fileReader = new FileReader();
        List<String> stringList = fileReader.read(new File("data/input.txt"));
        FileParser fileParser = new FileParser();
        List<List<String>> lists = fileParser.parseFile(stringList);
        LineValidator lineValidator = new LineValidator();
        if (!lineValidator.validLine(lists.get(1))) {
            System.exit(1);
        }
        stringList = lists.get(1);
        int numberOfCars = (int) Double.parseDouble(stringList.get(6));
        int numberOfLorries = (int) Double.parseDouble(stringList.get(7));
        int index = 1;

        LinkedList<Thread> carStackA = new LinkedList<>();
        LinkedList<Thread> carStackB = new LinkedList<>();

        for (int i = 0; i < numberOfCars/2; i++) {
            carStackA.add(new Thread(new Car(Double.parseDouble(stringList.get(5)),
                    Double.parseDouble(stringList.get(4)), index, true)));
            index++;
        }

        for (int i = 0; i < numberOfLorries/2; i++) {
            carStackA.add(new Thread(new Lorry(Double.parseDouble(stringList.get(3)),
                    Double.parseDouble(stringList.get(2)), index, true)));
            index++;
        }

        index = 1;

        for (int i = 0; i < numberOfCars/2; i++) {
            carStackB.add(new Thread(new Car(Double.parseDouble(stringList.get(5)),
                    Double.parseDouble(stringList.get(4)), index, false)));
            index++;
        }

        for (int i = 0; i < numberOfLorries/2; i++) {
            carStackB.add(new Thread(new Lorry(Double.parseDouble(stringList.get(3)),
                    Double.parseDouble(stringList.get(2)), index, false)));
            index++;
        }

        SIZEFIRSTQUEUE = carStackA.size();
        SIZESECONDQUEUE = carStackB.size();
        Ferry ferry = Ferry.getInstance();
        ferry.setMaxSquare(Double.parseDouble(stringList.get(0)));
        ferry.setMaxWeight(Double.parseDouble(stringList.get(1)));
        ferry.setLocker(lock);
        while (!carStackA.isEmpty() && !carStackB.isEmpty()) {
            Thread a = new Thread(carStackA.poll());
            a.start();
            Thread b = new Thread(carStackB.poll());
            b.start();
        }
    }
}
