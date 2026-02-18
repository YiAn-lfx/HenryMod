package HenryTGZJMod.ui.campfire;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.vfx.campfire.CampfireRepairEffect;
import HenryTGZJMod.vfx.campfire.CampfireSharpenEffect;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

public class SharpenOption extends AbstractCampfireOption {
    public static final String ID = ModHelper.makePath(SharpenOption.class.getSimpleName());
    private static final UIStrings UI_STRINGS = CardCrawlGame.languagePack.getUIString(ID);



    public SharpenOption(boolean active) {
        this.label = UI_STRINGS.TEXT[0];               // 选项标签
        this.usable = active;               // 是否可用
        this.description = active ? UI_STRINGS.TEXT[1] : UI_STRINGS.TEXT[2];  // 根据可用性设置描述
        this.img = ImageMaster.CAMPFIRE_SMITH_BUTTON;  // 按钮图标
    }

    public void useOption() {
        if (this.usable) {
            AbstractDungeon.effectList.add(new CampfireSharpenEffect());
        }
    }

}
