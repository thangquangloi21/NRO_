package boss.boss_manifest.RedRibbonHQ;

/*
 *
 *
 * @author EMTI
 */

import consts.ConstPlayer;
import boss.*;
import static boss.BossType.PHOBANDT;
import consts.ConstRatio;
import map.ItemMap;
import map.Zone;
import player.Player;
import skill.Skill;
import services.EffectSkillService;
import services.PlayerService;
import services.Service;
import services.SkillService;
import services.func.ChangeMapService;
import utils.SkillUtil;
import utils.Util;

public class TrungUyThep extends Boss {

    private long lastTimeMove;

    public TrungUyThep(Zone zone, long dame, long hp) throws Exception {
        super(PHOBANDT, BossID.TRUNG_UY_THEP, new BossData(
                "Trung uý Thép", //name
                ConstPlayer.TRAI_DAT, //gender
                new short[]{129, 130, 131, -1, -1, -1}, //outfit {head, body, leg, bag, aura, eff}
                ((dame)), //dame
                new long[]{((hp))}, //hp
                new int[]{55}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5}, //                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                new String[]{}, //text chat 1
                new String[]{"|-1|Nếu bọn mi muốn lên tiếp tầng lầu trên",
                    "|-1|Phải bước qua xác chết của ta đã"}, //text chat 2
                new String[]{}, //text chat 3
                60
        ));

        this.zone = zone;
    }

    @Override
    public void reward(Player plKill) {
        if (Util.isTrue(100, 100)) {
            ItemMap it = new ItemMap(this.zone, Util.nextInt(16, 20), 1, this.location.x, this.zone.map.yPhysicInTop(this.location.x,
                    this.location.y - 24), plKill.id);
            Service.gI().dropItemMap(this.zone, it);
        }
//        sự kiện
        int quantity = 1;
        ItemMap item1743 = new ItemMap(this.zone, 1743, quantity, this.location.x,
                this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), plKill.id);
        Service.gI().dropItemMap(this.zone, item1743);
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public synchronized long injured(Player plAtt, long damage, boolean piercing, boolean isMobAttack) {
        if (!this.isDie()) {
            if (!piercing && Util.isTrue(20, 100)) {
                this.chat("Xí hụt");
                return 0;
            }
            damage = this.nPoint.subDameInjureWithDeff(damage / 2);
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = damage / 2;
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
    public void joinMap() {
        ChangeMapService.gI().changeMap(this, this.zone, 884, 312);
        this.changeStatus(BossStatus.CHAT_S);
    }

    @Override
    public void doneChatS() {
        Service.gI().setPos(this, 884, 312);
    }

    private void goToXY(int x, int y, boolean isTeleport) {
        if (!isTeleport) {
            byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
            byte move = (byte) Util.nextInt(50, 100);
            int x2 = this.location.x + (dir == 1 ? move : -move);
            x2 = x2 < 640 ? 640 : x2;
            x2 = x2 > 980 ? 980 : x2;
            x2 = x < 220 ? x : x2;
            PlayerService.gI().playerMove(this, x2, getY(x));
        } else {
            Service.gI().setPos(this, x, y);
        }
    }

    private void goToPlayer(Player pl, boolean isTeleport) {
        goToXY(pl.location.x, pl.location.y, isTeleport);
    }

    @Override
    public void attack() {
        try {
            Player playerAtt = getPlayerAttack();
            if (playerAtt == null || playerAtt.isDie() || playerAtt.location.x < 640 || playerAtt.location.x > 980) {
                if (Util.canDoWithTime(lastTimeMove, 1500)) {
                    lastTimeMove = System.currentTimeMillis();
                    goToXY(884, 312, false);
                }
                return;
            }
            if (playerAtt.location != null && playerAtt != null && playerAtt.zone != null && this.zone != null && this.zone.equals(playerAtt.zone)) {
                if (this.isDie()) {
                    return;
                }
                this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                if (Util.getDistance(this, playerAtt) <= this.getRangeCanAttackWithSkillSelect()) {
                    int x = playerAtt.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80));
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(x, getY(x), false);
                    }
                    if (playerAtt.location.y < 220) {
                        return;
                    }

                    SkillService.gI().useSkill(this, playerAtt, null, -1, null);
                    checkPlayerDie(playerAtt);
                } else {
                    goToPlayer(playerAtt, false);
                }
            }
        } catch (Exception ex) {
        }
    }

    private int getY(int x) {
        if (x < 638 || x > 966) {
            return 240;
        } else if (x < 707) {
            return 264;
        } else if (x > 949) {
            return 288;
        }
        return 312;
    }

    @Override
    public void die(Player plKill) {
        if (plKill != null) {
            reward(plKill);
        }
        this.changeStatus(BossStatus.DIE);
    }

    @Override
    public void leaveMap() {
        ChangeMapService.gI().exitMap(this);
        this.lastZone = null;
        this.lastTimeRest = System.currentTimeMillis();
        this.changeStatus(BossStatus.REST);
        RedRibbonHQManager.gI().removeBoss(this);
        this.dispose();
    }

}
