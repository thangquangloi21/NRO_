package boss.boss_manifest.Earth;

/*
 *
 *
 * @author EMTI
 */

import boss.Boss;
import boss.BossID;
import boss.BossStatus;
import boss.BossesData;
import item.Item;
import java.util.List;
import map.ItemMap;
import player.Player;
import services.ItemService;
import services.Service;
import utils.Util;

public class BOJACK extends Boss {

    private long st;

    public BOJACK() throws Exception {
        super(BossID.BOJACK, false, true, BossesData.BOJACK, BossesData.SUPER_BOJACK);
    }

    @Override
    public void reward(Player plKill) {
        // Xác suất rơi item 457
        if (Util.isTrue(30, 100)) {  // 50% rơi item 457 x1-5
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 457, Util.nextInt(1, 6), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        } else if (Util.isTrue(10, 100)) {  // 20% rơi item 457 x5-10
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 457, Util.nextInt(5, 11), this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        // Xác suất rơi item 16
        if (Util.isTrue(10, 100)) {  // 10% rơi item 16 x1
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 16, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        // Xác suất rơi item 674
        if (Util.isTrue(1, 100)) {  // 5% rơi item 674 x1
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 674, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }
        //        sự kiện
        int quantity = 1;
        ItemMap item1743 = new ItemMap(this.zone, 1743, quantity, this.location.x,
                this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, item1743);
    }

    @Override
    public void joinMap() {
        super.joinMap();
        st = System.currentTimeMillis();
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, 900000)) {
            this.leaveMapNew();
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();
        }
    }

    @Override
    public void doneChatS() {
        if (this.currentLevel == 1) {
            return;
        }
        this.changeStatus(BossStatus.AFK);
    }
}
