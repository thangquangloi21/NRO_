package boss.iboss;

/*
 *
 *
 * @author EMTI
 */

import player.Player;

public interface IBossDie {

    void doSomeThing(Player playerKill);

    void notifyDie(Player playerKill);

    void rewards(Player playerKill);

    void leaveMap();

}
