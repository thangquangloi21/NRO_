package npc.npc_manifest;

/**
 *
 * @author EMTI
 */

import consts.ConstNpc;
import npc.Npc;
import player.Player;
import services.Service;

public class GiuMaDauBo extends Npc {

    public GiuMaDauBo(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        super(mapId, status, cx, cy, tempId, avartar);
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi đang muốn tìm mảnh vỡ và mảnh hồn bông tai Porata trong truyền thuyết, ta sẽ đưa ngươi đến đó ?",
                    "OK", "Đóng");
        }
    }

    @Override
    public void confirmMenu(Player player, int select) {
        if (canOpenNpc(player)) {
            switch (select) {
                case 1 -> {
                    // Code xử lý khi chọn option 1 (nếu có)
                }
                case 0 -> {
                    // Kiểm tra nếu sức mạnh của người chơi đạt 5 tỷ trở lên
                    if (player.nPoint.power >= 5_000_000_000L) {
                        player.type = 5;
                        player.maxTime = 5;
                        // Giữ nguyên map cũ (không thay đổi)
                        Service.gI().Transport(player);
                    } else {
                        // Nếu sức mạnh không đủ, hiển thị thông báo yêu cầu sức mạnh
                        Service.gI().sendThongBao(player, "Yêu cầu sức mạnh đạt 5 tỉ");
                    }
                }
                default -> {
                    // Code xử lý khi không có lựa chọn hợp lệ
                }
            }
        }
    }
}
