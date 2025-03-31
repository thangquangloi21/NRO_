package models.MajinBuu;

/*
 *
 *
 * @author EMTI
 */

import java.util.ArrayList;
import java.util.List;
import map.Zone;
import map.MaBuHold;
import player.Player;
import services.MapService;
import services.func.ChangeMapService;

public class MajinBuu14HService {

    private static MajinBuu14HService instance;

    public static MajinBuu14HService gI() {
        if (instance == null) {
            instance = new MajinBuu14HService();
        }
        return instance;
    }

    public List<MajinBuu14H> maBu2Hs;

    private MajinBuu14HService() {
        this.maBu2Hs = new ArrayList<>();
        for (int i = 0; i < MajinBuu14H.AVAILABLE; i++) {
            this.maBu2Hs.add(new MajinBuu14H(i));
        }
    }

    public void addMapMaBu2H(int id, Zone zone) {
        if (zone.map.mapId == 128) {
            for (int slot = 0; slot < 4; slot++) {
                zone.maBuHolds.add(new MaBuHold(slot, null));
            }
        }
        this.maBu2Hs.get(id).getZones().add(zone);
    }

    public void joinMaBu2H(Player player) {
        for (MajinBuu14H M2H : this.maBu2Hs) {
            for (Zone zone : M2H.zones) {
                if (zone.getNumOfPlayers() < 5 && zone.map.mapId == 127) {
                    ChangeMapService.gI().changeMap(player, zone, -1, 312);
                    return;
                }
            }
        }
        ChangeMapService.gI().changeMap(player, MapService.gI().getMapWithRandZone(127), -1, 312);
    }

}
