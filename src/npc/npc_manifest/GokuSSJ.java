package npc.npc_manifest;

/**
 *
 * @author EMTI
 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.func.ChangeMapService;

public class GokuSSJ extends Npc {

    public GokuSSJ(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            switch (this.mapId) {
                case 80 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta mới hạ Fide, nhưng nó đã kịp đào 1 cái lỗ\nHành tinh này sắp nổ tung rồi\nMau lượn thôi",
                            "Chuẩn");
                case 131 ->
                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đây là đâu? Xong cmnr", "Bó tay", "Về chỗ cũ");
                default ->
                    super.openBaseMenu(player);
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (player.iDMark.getIndexMenu()) {
                case ConstNpc.BASE_MENU -> {
                    if (this.mapId == 131) {
                        if (select == 1) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                        }
                    } else if (this.mapId == 80) {
                        if (select == 0) {
                            ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 870);
                        }
                    }
                }
            }
        }
    }
}
