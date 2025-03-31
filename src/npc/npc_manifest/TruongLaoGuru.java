package npc.npc_manifest;

/**
 *
 * @author EMTI
 */
import clan.Clan;
import consts.ConstNpc;
import consts.ConstPlayer;
import consts.ConstTranhNgocNamek;
import item.Item;
import java.util.ArrayList;
import models.DragonNamecWar.TranhNgoc;
import models.DragonNamecWar.TranhNgocService;
import npc.Npc;
import player.Player;
import server.Manager;
import services.InventoryService;
import services.NpcService;
import services.RewardService;
import services.Service;
import services.TaskService;
import services.func.ChangeMapService;
import services.func.Input;
import shop.ShopService;
import utils.Util;

public class TruongLaoGuru extends Npc {

    public TruongLaoGuru(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                if (player.gender != ConstPlayer.NAMEC) {
                    NpcService.gI().createTutorial(player, tempId, avartar, "Con hãy về hành tinh của mình mà thể hiện");
                    return;
                }
                ArrayList<String> menu = new ArrayList<>();
                    if (!player.canReward) {
                        menu.add("Nhiệm vụ");
                        menu.add("Học\nKỹ năng");
                        Clan clan = player.clan;
                        if (clan != null) {
                            menu.add("Về khu\nvực bang");
                            if (clan.isLeader(player)) {
                                menu.add("Giải tán\nBang hội");
                            }
                        }
                    } else {
                        menu.add("Giao\nLân con");
                    }
                    String[] menus = menu.toArray(String[]::new);
                    createOtherMenu(player, ConstNpc.BASE_MENU,
                            "Chào con, ta rất vui khi gặp được con\nCon muốn làm gì nào ?", menus);
                
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.canReward) {
                RewardService.gI().rewardLancon(player);
                return;
            }
            if (player.iDMark.isBaseMenu()) {
               
                switch (select) {
                    case 0 ->
                        NpcService.gI().createTutorial(player, tempId, avartar, player.playerTask.taskMain.subTasks.get(player.playerTask.taskMain.index).name);
                    case 1 ->
                        Service.gI().sendThongBao(player, "Bạn đã học hết các kỹ năng");
                    case 2 -> {
                        Clan clan = player.clan;
                        if (clan != null) {
                            ChangeMapService.gI().changeMapNonSpaceship(player, 153, Util.nextInt(100, 200), 432);
                        }
                    }
                    case 3 -> {
                        Clan clan = player.clan;
                        if (clan != null) {
                            if (clan.isLeader(player)) {
                                createOtherMenu(player, 3, "Con có chắc muốn giải tán bang hội không?", "Đồng ý", "Từ chối");
                            }
                        }
                    }
                }
            } else if (player.iDMark.getIndexMenu() == 3) {
                Clan clan = player.clan;
                if (clan != null) {
                    if (clan.isLeader(player)) {
                        if (select == 0) {
                            Input.gI().createFormGiaiTanBangHoi(player);
                        }
                    }
                }
            }
        }
    }
}
