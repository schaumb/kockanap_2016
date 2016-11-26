import communication.UDPClient;
import map.IMap;
import strategy.*;

/**
 * Created by qqcs on 25/11/16.
 */
public class MyMain {

    public static void main(String[] args) {
        while (true) {
            IStrategy strategy = new StrategyCollector(new MapRefresherStrategy(), new PackageCollectorStrategy());
            try (UDPClient client = new UDPClient(strategy)) {
                IMap.MapConfig.getMap().initialize();

                client.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
