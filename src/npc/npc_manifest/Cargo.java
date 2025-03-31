package npc.npc_manifest;

/**
 * @author EMTI
 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.NpcService;
import services.TaskService;
import services.func.ChangeMapService;

public class Cargo extends Npc {

    public Cargo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player pl) {
        if (canOpenNpc(pl)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                        "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                        "Đến\nTrái Đất", "Đến\nXayda", "Đến\nSiêu thị");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0 -> ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                    case 1 -> ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                    case 2 -> ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                }
            }
        }
    }
}
