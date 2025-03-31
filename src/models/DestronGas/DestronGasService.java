package models.DestronGas;

/*
 *
 *
 * @author EMTI
 */

import clan.Clan;
import clan.ClanMember;
import java.util.ArrayList;
import java.util.List;
import map.Zone;
import player.Player;
import services.Service;
import utils.Util;

public class DestronGasService {

    private static DestronGasService instance;

    public static DestronGasService gI() {
        if (instance == null) {
            instance = new DestronGasService();
        }
        return instance;
    }

    public List<DestronGas> khiGasHuyDiets;

    private DestronGasService() {
        this.khiGasHuyDiets = new ArrayList<>();
        for (int i = 0; i < DestronGas.AVAILABLE; i++) {
            this.khiGasHuyDiets.add(new DestronGas(i));
        }
    }

    public void addMapKhiGasHuyDiet(int id, Zone zone) {
        this.khiGasHuyDiets.get(id).getZones().add(zone);
    }

    public void openKhiGasHuyDiet(Player player, byte level) {
        Clan clan = player.clan;
        if (clan != null) {
            ClanMember cm = clan.getClanMember((int) player.id);
            if (cm != null) {
//                if (player.clanMember.getNumDateFromJoinTimeToToday() < 1) {
//                    return;
//                }
                if (clan.members.size() < DestronGas.N_PLAYER_CLAN) {
                    return;
                }
                if (player.clan.KhiGasHuyDiet == null) {
                    if (level >= 1 && level <= 110) {
                        if (clan.isLeader(player)) {
                            DestronGas khiGasHuyDiet = null;
                            for (DestronGas kghd : this.khiGasHuyDiets) {
                                if (!kghd.isOpened) {
                                    khiGasHuyDiet = kghd;
                                    break;
                                }
                            }
                            if (khiGasHuyDiet != null) {
                                if (Util.isAfterMidnight(player.clan.lastTimeOpenKhiGasHuyDiet)) {
                                    player.clan.timesPerDayKGHD = 1;
                                } else {
                                    player.clan.timesPerDayKGHD++;
                                    if (player.clan.timesPerDayKGHD > 3) {
                                        Service.gI().sendThongBao(player, "Hãy chờ đến ngày mai");
                                        return;
                                    }
                                }
                                khiGasHuyDiet.openKhiGasHuyDiet(player, player.clan, level);
                            } else {
                                Service.gI().sendThongBao(player, "Destron Gas đã đầy, hãy quay lại sau 30 phút");
                                return;
                            }
                        }
                    } else {
                        Service.gI().sendThongBao(player, "Không thể thực hiện");
                        return;
                    }
                }
                if (player.clan.KhiGasHuyDiet != null && !player.iDMark.isGoToKGHD()) {
                    player.iDMark.setLastTimeGoToKGHD(System.currentTimeMillis());
                    player.iDMark.setGoToKGHD(true);
                    player.type = 4;
                    player.maxTime = 5;
                    Service.gI().Transport(player, 1);
                }
            }
        }
    }
}
