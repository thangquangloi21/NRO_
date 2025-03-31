package npc.npc_manifest;

/**
 * @author EMTI
 */
import consts.ConstNpc;
import consts.ConstTask;
import consts.ConstTaskBadges;
import item.Item;

import java.util.ArrayList;
import java.util.List;

import jdbc.daos.PlayerDAO;
import npc.Npc;
import player.Player;
import services.InventoryService;
import services.ItemService;
import services.NpcService;
import services.PetService;
import services.Service;
import services.TaskService;
import services.func.Input;
import shop.ShopService;
import task.Badges.BadgesTaskService;
import utils.Util;

public class OngGohan extends Npc {

    public OngGohan(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    int costNapVang = 1;

    int[][] napVang = {{20000, 40}, {50000, 110}, {100000, 250}, {500000, 2500}, {1000000, 5000}, {2000000, 10000}, {5000000, 30000}};

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                        "\n\b|7|Con đang có :" + player.getSession().cash + " VND\n|0|"
                        + "\nDùng COIN, VND hãy qua NPC BARDOCK hoặc QUY LÃO, Poc ở đảo Kame nhé!!!"
                        + "\n Con có thể nhận quà nạp mốc, đua top sự kiện tại Quy Lão Kame\n"
                        + "Code tân thủ:\n|4|"
                        + "• giftcode : \n"
                        + "       - duatoptest\n"
                        + "       -\n"
                        + "       -\n"
                       
                        + "Mở thành viên tại Bardock làng , nhận đệ tử miễn phí\n|1|"
                        + "Lưu ý: Chỉ giao dịch nạp tiền qua duy nhất qua admin key vàng"
                        + "\nmọi rủi ro tự chịu nếu không chấp hành.",
                        "GiftCode",
                        "Nạp tiền",
                        "Xóa đệ",
                        "Nhận ngọc\nMiễn phí",
                        
                        "Hỗ trợ\nNhiệm vụ",
                        "Hộp thư",
                        "Nhận đệ tử",
                        "Từ chối");
            }
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            if (player.iDMark.isBaseMenu()) {
                switch (select) {
                    case 0: // mã quà tặng
                        Input.gI().createFormGiftCode(player);
                        break;
                    case 1: // nạp tiền
                        String npcSay = "Số dư của con là: " + Util.mumberToLouis(player.getSession().cash) + " VND dùng để nạp qua đơn vị khác\n"
                                + "Ta đang giữ giúp con " + Util.mumberToLouis(player.getSession().goldBar) + " thỏi vàng";
                        createOtherMenu(player, ConstNpc.NAP_TIEN, npcSay,
                                "Nạp VNĐ",
                                "Nạp vàng",
                                "Nhận\nThỏi vàng",
                                "Đóng");
                        break;
                    case 2:// xóa đệ
                        this.createOtherMenu(player, 12456,
                                "|0|Bạn muốn xóa đệ với giá 10K VND ?",
                                "Đồng ý", "Không");
                        break;

                    case 3: // nhận ngọc
                        if (player.inventory.gem >= 1000000) {
                            Service.gI().sendThongBao(player, "Tiêu hết đi đã r nhận cu");
                            return;
                        }
                        int soLuongNgoc = 100_000;
                        player.inventory.gem += soLuongNgoc;
                        Service.gI().sendMoney(player);
                        Service.gI().sendThongBao(player, "Bạn vừa nhận " + Util.mumberToLouis(soLuongNgoc) + " ngọc xanh");
                        break;
                    case 6: // nhận đệ
//                        if (!player.getSession().actived) {
//                            Service.gI().sendThongBao(player, "Vui lòng mở thành viên trước");
//                            return;
//                        }
                        if (player.pet == null) {
                            PetService.gI().createNormalPet(player);
                            Service.gI().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                        } else {
                            this.npcChat(player, "Bú ít thôi con, giấu số đá còn lại ở đâu r ");
                        }

                        break;
                    case 4:
                        if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_9_0 && TaskService.gI().getIdTask(player) < ConstTask.TASK_11_0) {
                            player.playerTask.taskMain.id = 10;
                            player.playerTask.taskMain.index = 0;
                            TaskService.gI().sendNextTaskMain(player);
                            Service.gI().sendThongBao(player, "Bạn đã được hỗ trợ nhiệm vụ thành công");
                        } else if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_18_0 && TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                            player.playerTask.taskMain.id = 19;
                            player.playerTask.taskMain.index = 0;
                            TaskService.gI().sendNextTaskMain(player);
                            Service.gI().sendThongBao(player, "Bạn đã được hỗ trợ nhiệm vụ thành công");
                        } else {
                            Service.gI().sendThongBao(player, "Chỉ hỗ trợ nhiệm vụ tàu pảy pảy và nhiệm vụ DHVT, Trung úy trắng");
                        }
                        break;
                    case 5:
                        this.createOtherMenu(player, ConstNpc.MAIL_BOX,
                                "|0|Tình yêu như một dây đàn\n"
                                + "Tình vừa được thì đàn đứt dây\n"
                                + "Đứt dây này anh thay dây khác\n"
                                + "Mất em rồi anh biết thay ai?",
                                "Hòm Thư\n(" + (player.inventory.itemsMailBox.size()
                                - InventoryService.gI().getCountEmptyListItem(player.inventory.itemsMailBox))
                                + " món)",
                                "Xóa Hết\nHòm Thư", "Đóng");
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.MAIL_BOX) {
                switch (select) {
                    case 0:
//                                player.inventory.itemsMailBox.clear();
//                                player.inventory.itemsMailBox.addAll(GodGK.getMailBox(player));
                        ShopService.gI().opendShop(player, "ITEMS_MAIL_BOX", true);
                        break;
                    case 1:
                        NpcService.gI().createMenuConMeo(player,
                                ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX, this.avartar,
                                "|3|Bạn chắc muốn xóa hết vật phẩm trong hòm thư?\n"
                                + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                "Đồng ý", "Hủy bỏ");
                        break;
                    case 2:
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_TIEN) {
                switch (select) {
                    case 0:
                        NpcService.gI().createBigMessage(player, avartar, "Nhớ đăng nhập xong sau đó bấm NẠP!!!", (byte) 1, "NẠP", "http://nrolegion.online");
                        break;
                    case 1:
                        List<String> menu = new ArrayList<>();
                        for (int i = 0; i < napVang.length; i++) {
                            menu.add(i, Util.mumberToLouis(napVang[i][0]) + "\n" + Util.mumberToLouis(napVang[i][1] * costNapVang) + " Thỏi\nvàng");
                        }
                        String[] menus = menu.toArray(new String[0]);
                        createOtherMenu(player, ConstNpc.NAP_VANG, "Ta sẽ giữ giúp con\n"
                                + "Nếu con cần dùng tới hãy quay lại đây gặp ta!", menus);
                        break;
                    case 2:
                        if (player.getSession().goldBar > 0) {
                            List<Item> listItem = new ArrayList<>();
                            Item thoiVang = ItemService.gI().createNewItem((short) 457, player.getSession().goldBar);
                            listItem.add(thoiVang);
                            if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                                Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                            }
                            for (Item it : listItem) {
                                InventoryService.gI().addItemBag(player, it);
                                InventoryService.gI().sendItemBag(player);
                            }
                            Service.gI().sendThongBao(player, "Bạn đã nhận được " + player.getSession().goldBar + " thỏi vàng");
                            PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                            PlayerDAO.subGoldBar(player, player.getSession().goldBar);
                        }
                        break;
                }
            } else if (player.iDMark.getIndexMenu() == ConstNpc.NAP_VANG) {
                if (player.getSession().cash >= napVang[select][0]) {
                    List<Item> listItem = new ArrayList<>();
                    if (InventoryService.gI().getCountEmptyBag(player) < listItem.size()) {
                        Service.gI().sendThongBao(player, "Cần ít nhất " + listItem.size() + " ô trống trong hành trang");
                    }
                    for (Item it : listItem) {
                        InventoryService.gI().addItemBag(player, it);
                        InventoryService.gI().sendItemBag(player);
                    }
                    PlayerDAO.subcash(player, napVang[select][0]);
                    BadgesTaskService.updateCountBagesTask(player, ConstTaskBadges.DAI_GIA_MOI_NHU, napVang[select][0]);
                    PlayerDAO.subGoldBar(player, -(napVang[select][1] * costNapVang));
                    Service.gI().sendThongBao(player, "Bạn có thêm " + Util.mumberToLouis(napVang[select][1] * costNapVang) + " thỏi vàng");
                } else {
                    Service.gI().sendThongBao(player, "Không đủ số dư");
                }
            } else if (player.iDMark.getIndexMenu() == 12456) {
                switch (select) {
                    case 0:
                        if (player.getSession().cash < 10000) {
                             Service.gI().sendThongBao(player, "Xóa đệ cần 10K VND");
                            return;
                        }
                        if (player.pet != null) {
                            PetService.gI().deletePet(player);
                        }
                        break;
                    case 1:
                        break;

                }
            }
        }
    }
}
