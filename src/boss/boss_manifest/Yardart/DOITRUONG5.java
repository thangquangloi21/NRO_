package boss.boss_manifest.Yardart;

/*
 *
 *
 * @author EMTI
 */

import boss.BossID;
import boss.BossesData;
import static boss.BossType.YARDART;

public class DOITRUONG5 extends Yardart {

    public DOITRUONG5() throws Exception {
        super(YARDART, BossID.DOI_TRUONG_5, BossesData.DOI_TRUONG_5);
    }

    @Override
    protected void init() {
        x = 1199;
        x2 = 1269;
        y = 456;
        y2 = 456;
        range = 1000;
        range2 = 150;
        timeHoiHP = 15000;
        rewardRatio = 2;
    }
}
