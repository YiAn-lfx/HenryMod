package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.ui.campfire.RepairOption;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.ui.campfire.LiftOption;

import java.util.ArrayList;

public class PlateArmorStatus extends AbstractHenryRelic {
    public static final String ID = ModHelper.makePath(PlateArmorStatus.class.getSimpleName()); // 遗物ID
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public PlateArmorStatus() {
        super(ID, RELIC_TIER, LANDING_SOUND);
        this.counter = 100;
    }
    @Override
    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
        if (this.counter >120) {
            this.counter = 120;
        }
        if (this.counter <0) {
            this.counter = 0;
        }
        super.renderCounter(sb, inTopPanel);

    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new PlateArmorStatus();
    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        int baseDamage = damageAmount;
        damageAmount += (100 - counter) / 10;
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner != AbstractDungeon.player && baseDamage > 0) {
            this.counter -= (baseDamage + 10) / 10;
        }
        return damageAmount;
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new RepairOption(this.counter < 120));
    }



}











