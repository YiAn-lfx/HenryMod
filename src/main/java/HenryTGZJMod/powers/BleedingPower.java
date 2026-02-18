package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.WeakPower;

public class BleedingPower extends AbstractHenryPower implements NonStackablePower  {
    public static final String POWER_ID = ModHelper.makePath(BleedingPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public BleedingPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount = Amount;
        this.amount2 = Amount;


        this.initializeImages();
        // 首次添加能力更新描述
        this.updateDescription();

    }


    // 能力在更新时如何修改描述
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], this.amount2, this.amount);
        } else {
            this.description = String.format(DESCRIPTIONS[1], this.amount2, this.amount);
        }
    }
    //效果

    @Override
    public void atStartOfTurn() {
        this.flash();
        this.addToBot(new DamageAction(this.owner, new DamageInfo(this.owner, amount2, DamageInfo.DamageType.HP_LOSS)));
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new WeakPower(this.owner, 1, false)));
        amount--;
        this.updateDescription();
        if (amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }

    public boolean isStackable(AbstractPower power) {
        return false;
    }
}