package boss;

/*
 *
 *
 * @author EMTI
 */

public class LunarNewYearEventManager extends BossManager {

    private static LunarNewYearEventManager instance;

    public static LunarNewYearEventManager gI() {
        if (instance == null) {
            instance = new LunarNewYearEventManager();
        }
        return instance;
    }

}
