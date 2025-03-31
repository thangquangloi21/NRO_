package boss;

/*
 *
 *
 * @author EMTI
 */
import EMTI.Functions;
import server.Maintenance;

public class SnakeWayManager extends BossManager {

    private static SnakeWayManager instance;

    public static SnakeWayManager gI() {
        if (instance == null) {
            instance = new SnakeWayManager();
        }
        return instance;
    }

    @Override
    public void run() {
        while (!Maintenance.isRunning) {
            try {
                long st = System.currentTimeMillis();
                for (int i = this.bosses.size() - 1; i >= 0; i--) {
                    if (i < this.bosses.size()) {
                        Boss boss = this.bosses.get(i);
                        try {
                            boss.update();
                        } catch (Exception e) {
                            e.printStackTrace();
                            try {
                                removeBoss(boss);
                            } catch (Exception ex) {
                            }
                        }
                    }
                }
//                if (500 - (System.currentTimeMillis() - st) > 0) {
//                    Thread.sleep(500 - (System.currentTimeMillis() - st));
//                }
                Functions.sleep(Math.max(150 - (System.currentTimeMillis() - st), 10));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
