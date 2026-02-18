package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.ui.campfire.SharpenOption;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public class WeaponStatus extends AbstractHenryRelic {
    public static final String ID = ModHelper.makePath(WeaponStatus.class.getSimpleName()); // 遗物ID
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public int maxCounter = 120;

    public WeaponStatus() {
        super(ID, RELIC_TIER, LANDING_SOUND);
        this.counter = 100;
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new WeaponStatus();
    }



    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        int reduction = (100 - counter) / 10; // 整数除法
        damage -= reduction;
        return damage;
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        if (info.type == DamageInfo.DamageType.NORMAL && info.owner == AbstractDungeon.player) {
            this.counter -= (damageAmount +10)/10;
        }
    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new SharpenOption(this.counter < maxCounter));
    }



}






