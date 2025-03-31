package boss.boss_manifest.Yardart;

/*
 *
 *
 * @author EMTI
 */

import boss.BossID;
import boss.BossesData;
import static boss.BossType.YARDART;

public class TAPSU2 extends Yardart {

    public TAPSU2() throws Exception {
        super(YARDART, BossID.TAP_SU_2, BossesData.TAP_SU_2);
    }

    @Override
    protected void init() {
        x = 582;
        x2 = 652;
        y = 432;
        y2 = 456;
        range = 1000;
        range2 = 150;
        timeHoiHP = 30000;
        rewardRatio = 5;
    }
}
