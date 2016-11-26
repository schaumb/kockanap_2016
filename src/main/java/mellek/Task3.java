package mellek;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Scanner;

/**
 * Created by qqcs on 26/11/16.
 */
public class Task3 {
    public static class Time {
        public String arr, dep;

        public Time(String arr, String dep) {
            this.arr = arr;
            this.dep = dep;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String[] arrival = scanner.nextLine().split(";");
        String[] departure = scanner.nextLine().split(";");

        ArrayList<Time> times = new ArrayList<>();
        for(int i = 0; i < arrival.length; ++i) {
            times.add(new Time(arrival[i], departure[i]));
        }
        Deque<Time> currentTimes = new ArrayDeque<>();

        int requiredStations = 0;

        for(int i = 0; i < times.size(); ++i) {
            currentTimes.add(times.get(i));

            String a = currentTimes.getFirst().arr,
                    b = currentTimes.getFirst().dep,
                    c = currentTimes.getLast().arr;

            while(!atSameTime(currentTimes.getFirst(), currentTimes.getLast())) {
                currentTimes.removeFirst();
            }

            requiredStations = Math.max(requiredStations, currentTimes.size());
        }

        System.out.println(requiredStations);
    }

    private static boolean atSameTime(Time first, Time last) {
        if(first.arr.equals(last.arr) ||
                first.dep.equals(last.arr))
            return true;

        if(first.arr.equals(first.dep))
            return false;

        int cmpAB = first.arr.compareTo(first.dep);
        int cmpAC = first.arr.compareTo(last.arr);
        int cmpBC = first.dep.compareTo(last.arr);

        if(cmpAB < 0 && cmpBC < 0)
            return false;
        if(cmpAC > 0 && cmpAB < 0)
            return false;
        if(cmpBC < 0 && cmpAC > 0)
            return false;

        return true;
    }
}
