package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.ReceiveDamagePower;
import HenryTGZJMod.ui.campfire.RepairOption;
import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;

import java.util.ArrayList;

public class PlateArmorStatus extends AbstractHenryRelic implements CustomSavable<PlateArmorStatus.PlateArmorSaveData> {
    public static final String ID = ModHelper.makePath(PlateArmorStatus.class.getSimpleName()); // 遗物ID
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public int maxCounter = 120;
    public int damageCounter = 0;
    public int reduceDamage = 0;
    public int totalDamage = 0;
    public int unblockedTotalDamage = 0;



    public PlateArmorStatus() {
        super(ID, RELIC_TIER, LANDING_SOUND);
        this.counter = 100;

    }


    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        getReduceDamage();
        if (reduceDamage >= 0) {
            return this.DESCRIPTIONS[0] + reduceDamage + this.DESCRIPTIONS[2] + damageCounter + this.DESCRIPTIONS[3] + totalDamage + this.DESCRIPTIONS[4] + unblockedTotalDamage;
        } else {
            return this.DESCRIPTIONS[1] + -reduceDamage + this.DESCRIPTIONS[2] + damageCounter + this.DESCRIPTIONS[3] + totalDamage + this.DESCRIPTIONS[4] + unblockedTotalDamage;
        }
    }
    public void updateDescription() {
        this.description = this.getUpdatedDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }
    public AbstractRelic makeCopy() {
        return new PlateArmorStatus();
    }


    @Override
    public void atBattleStart() {
        AbstractCreature p = AbstractDungeon.player;
        if (reduceDamage != 0) {
            this.addToBot(new ApplyPowerAction(p, p, new ReceiveDamagePower(p, reduceDamage)));
        }
        updateDescription();
    }
    @Override
    public void onEnterRoom(AbstractRoom room) {
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        AbstractCreature p = AbstractDungeon.player;
        AbstractPower power = p.getPower("HenryTGZJMod:ReceiveDamagePower");
        if (info.type == DamageInfo.DamageType.NORMAL) {
            damageCounter += info.output;
            totalDamage += info.output;
            unblockedTotalDamage += damageAmount;
            if (damageCounter >= 10) {
                this.counter -= damageCounter / 10;
                if (this.counter < 0) {
                    this.counter = 0;
                }
                damageCounter = damageCounter % 10;
            }
        }
        getReduceDamage();
        if (power != null && power.amount < reduceDamage) {
            this.addToTop(new ApplyPowerAction(p, p, new ReceiveDamagePower(p, reduceDamage - power.amount)));
        } else if (power != null && power.amount > reduceDamage) {
            this.addToTop(new ApplyPowerAction(p, p, new ReceiveDamagePower(p, reduceDamage - power.amount)));
        } else if (power == null && reduceDamage != 0){
            this.addToTop(new ApplyPowerAction(p, p, new ReceiveDamagePower(p, reduceDamage)));
        }
        this.updateDescription();
        return damageAmount;
    }
    public void getReduceDamage() {
        if (this.counter <= 100) {
            reduceDamage =  (100 - this.counter) / 10;
        } else {
            reduceDamage =  (100 - this.counter - 9) / 10;
        }

    }

    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        options.add(new RepairOption(this.counter < 120));
    }

    @Override
    public PlateArmorSaveData onSave() {
        return new PlateArmorSaveData(
                this.damageCounter,
                this.totalDamage,
                this.unblockedTotalDamage
        );
    }

    @Override
    public void onLoad(PlateArmorSaveData save) {
        if (save != null) {
            this.damageCounter = save.damageCounter;
            this.totalDamage = save.totalDamage;
            this.unblockedTotalDamage = save.unblockedTotalDamage;

            // 重新计算并更新
            this.getReduceDamage();
            this.updateDescription();
        }
    }

    public static class PlateArmorSaveData {
        public int damageCounter;
        public int totalDamage;
        public int unblockedTotalDamage;

        public PlateArmorSaveData() {
            this(0, 0, 0);
        }

        public PlateArmorSaveData(int damageCounter, int totalDamage, int unblockedTotalDamage) {
            this.damageCounter = damageCounter;
            this.totalDamage = totalDamage;
            this.unblockedTotalDamage = unblockedTotalDamage;
        }
    }

}











