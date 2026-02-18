package HenryTGZJMod.patch.event;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.events.shrines.Nloth;


/** @deprecated */


import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.core.CardCrawlGame;

@SpirePatch(clz = Nloth.class, method = "buttonEffect")
public class NlothPatch {

    private static final String[] PROTECTED_RELIC_IDS = {
            "HenryTGZJMod:PlateArmorStatus",
            "HenryTGZJMod:WeaponStatus",

    };

    @SpirePrefixPatch
    public static SpireReturn<Void> preventRemove(Nloth __instance, int buttonPressed) {
        // 只检查前两个按钮（交换遗物）
        if (buttonPressed != 0 && buttonPressed != 1) {
            return SpireReturn.Continue();
        }

        try {
            // 获取要交换的遗物ID
            String relicIdToLose = null;

            if (buttonPressed == 0) {
                // 第一个选项
                relicIdToLose = (String) com.megacrit.cardcrawl.dungeons.AbstractDungeon.class
                        .getDeclaredMethod("getRelicIdToLose")
                        .invoke(AbstractDungeon.player);
            } else {
                // 第二个选项
                relicIdToLose = (String) com.megacrit.cardcrawl.dungeons.AbstractDungeon.class
                        .getDeclaredMethod("getRelicIdToLose2")
                        .invoke(AbstractDungeon.player);
            }

            // 检查是否是受保护的遗物
            if (relicIdToLose != null && isProtectedRelic(relicIdToLose)) {
                // 阻止交换并提示
                CardCrawlGame.sound.play("BLOCK_ATTACK");
                __instance.imageEventText.updateBodyText("这个遗物被神秘力量保护，无法交换！");
                __instance.imageEventText.updateDialogOption(0, "离开");
                __instance.imageEventText.clearRemainingOptions();
                return SpireReturn.Return();
            }

        } catch (Exception e) {
            // 忽略异常，继续执行原逻辑
            e.printStackTrace();
        }

        return SpireReturn.Continue();
    }

    private static boolean isProtectedRelic(String relicId) {
        for (String id : PROTECTED_RELIC_IDS) {
            if (id.equals(relicId)) {
                return true;
            }
        }
        return false;
    }
}