package mellek;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by qqcs on 26/11/16.
 */
public class Task4 {
    public static class Attacking {
        ArrayList<Integer> enemies = new ArrayList<>();
        ArrayList<Integer> weapons = new ArrayList<>();

        public boolean canMakeIt() {
            enemies.sort((i1, i2) -> Integer.compare(i2, i1));
            weapons.sort(Integer::compareTo);
            while(!enemies.isEmpty()) {
                int i = 0;
                while(i < weapons.size()) {
                    if(weapons.get(i) >= enemies.get(0)) {
                        weapons.set(i, weapons.get(i) - enemies.get(0));
                        break;
                    }
                    ++i;
                }
                if(i == weapons.size()) {
                    return false;
                }

                enemies.remove(0);
                weapons.sort(Integer::compareTo);

            }
            return true;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int N = scanner.nextInt();
        int M = scanner.nextInt();

        Attacking land = new Attacking();
        Attacking air = new Attacking();
        Attacking water = new Attacking();

        for(int i = 0; i < N; ++i) {
            String[] enemy = scanner.next().split(":");

            switch (enemy[0]) {
                case "vizi":
                    water.enemies.add(Integer.parseInt(enemy[1]));
                    break;
                case "légi":
                    air.enemies.add(Integer.parseInt(enemy[1]));
                    break;
                case "szárazföldi":
                    land.enemies.add(Integer.parseInt(enemy[1]));
                    break;
            }
        }

        for(int i = 0; i < M; ++i) {
            String[] weapon = scanner.next().split(":");

            switch (weapon[0]) {
                case "szigony":
                    water.weapons.add(Integer.parseInt(weapon[1]));
                    break;
                case "íj":
                    air.weapons.add(Integer.parseInt(weapon[1]));
                    break;
                case "kard":
                    land.weapons.add(Integer.parseInt(weapon[1]));
                    break;
            }
        }

        if(water.canMakeIt() && air.canMakeIt() && land.canMakeIt()) {
            System.out.println("SIKER");
        } else {
            System.out.println("KUDARC");
        }
    }
}
