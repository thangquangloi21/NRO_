package boss.boss_manifest.Cumber;

/*
 *
 *
 * @author EMTI
 */
import boss.*;
import consts.ConstPlayer;
import consts.ConstTask;
import consts.ConstTaskBadges;
import item.Item;
import map.ItemMap;
import player.Player;
import services.*;
import task.Badges.BadgesTaskService;
import utils.Util;

public class Cumber extends Boss {

    private long st;
    private int timeLeaveMap;

    public Cumber() throws Exception {
        super(BossID.CUMBER, false, true, BossesData.CUMBER, BossesData.SUPER_CUMBER);
    }

    @Override
    public void reward(Player plKill) {
        // Cập nhật số lần giết boss trong nhiệm vụ huy hiệu
        BadgesTaskService.updateCountBagesTask(plKill, ConstTaskBadges.TRUM_SAN_BOSS, 1);

        // Xác suất rơi item 674
        if (Util.isTrue(30, 100)) {  // 30% rơi item 674 x2
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 674, 2, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        } else if (Util.isTrue(50, 100)) {  // 50% rơi item 674 x1
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 674, 1, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        } else if (Util.isTrue(10, 100)) {  // 10% rơi item 674 x3
            Service.gI().dropItemMap(this.zone, new ItemMap(this.zone, 674, 3, this.location.x,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id));
        }

        // Nếu boss có head ID là 1311, rơi item 1274 với các thuộc tính đặc biệt
        if (super.head == 1311) {
            ItemMap it = new ItemMap(this.zone, 1274, 1,
                    this.location.x + Util.nextInt(-15, 15),
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    plKill.id);

            // Thêm các option cho vật phẩm
            it.options.add(new Item.ItemOption(50, 20));
            it.options.add(new Item.ItemOption(77, 20));
            it.options.add(new Item.ItemOption(103, 20));
            it.options.add(new Item.ItemOption(117, 15));
            it.options.add(new Item.ItemOption(93, Util.nextInt(1, 5)));

            // Thả vật phẩm xuống bản đồ
            Service.gI().dropItemMap(this.zone, it);
        }
        int quantity = Util.nextInt(4,6);
        ItemMap item1743 = new ItemMap(this.zone, 1743, quantity, this.location.x,
                this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, item1743);
    }

    @Override
    public synchronized long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 1000)) {
                this.chat("Xí hụt");
                return 0;
            }
//            giảm 50% st phải nhận khi nhận sát thương (cumber2)
//            if (this.currentLevel != 0) {
//                damage /= 2;
//            }
            damage = this.nPoint.subDameInjureWithDeff(damage - Util.nextInt(100000));
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                this.setDie(plAtt);
                die(plAtt);
            }
            return damage;
        } else {
            return 0;
        }
    }

    @Override
    public void autoLeaveMap() {
        if (Util.canDoWithTime(st, timeLeaveMap)) {
            if (Util.isTrue(1, 2)) {
                this.leaveMap();
            } else {
                this.leaveMapNew();
            }
        }
        if (this.zone != null && this.zone.getNumOfPlayers() > 0) {
            st = System.currentTimeMillis();
            timeLeaveMap = Util.nextInt(300000, 900000);
        }
    }

    @Override
    public void joinMap() {
        this.name = this.data[this.currentLevel].getName() + " " + Util.nextInt(1, 100);
        super.joinMap();
        st = System.currentTimeMillis();
        timeLeaveMap = Util.nextInt(600000, 900000);
    }

    @Override
    public void attack() {
        if (Util.canDoWithTime(this.lastTimeAttack, 100) && this.typePk == ConstPlayer.PK_ALL) {
            this.lastTimeAttack = System.currentTimeMillis();
            try {
                Player pl = getPlayerAttack();
                if (pl == null || pl.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                int dis = Util.getDistance(this, pl);
                if (dis > 450) {
                    move(pl.location.x - 24, pl.location.y);
                } else if (dis > 100) {
                    int dir = (this.location.x - pl.location.x < 0 ? 1 : -1);
                    int move = Util.nextInt(50, 100);
                    move(this.location.x + (dir == 1 ? move : -move), pl.location.y);
                } else {
                    if (Util.isTrue(30, 100)) {
                        int move = Util.nextInt(50);
                        move(pl.location.x + (Util.nextInt(0, 1) == 1 ? move : -move), this.location.y);
                    }
                    SkillService.gI().useSkill(this, pl, null, -1, null);
                    checkPlayerDie(pl);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
