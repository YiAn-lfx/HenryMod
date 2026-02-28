package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class StancePower extends AbstractHenryPower {
    public static final String POWER_ID = ModHelper.makePath(StancePower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public StancePower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = Amount;
        if (this.amount >= 10) {
            this.amount = 10;
        }

        this.initializeImages();

        // 首次添加能力更新描述
        this.updateDescription();
    }
    public void stackPower(int stackAmount){
        AbstractPower powerD = owner.getPower("HenryTGZJMod:DictateTheTempoPower");
        if (powerD != null) {
            this.flash();
            this.addToBot(new DrawCardAction((powerD.amount * stackAmount)));
        }
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        if (this.amount == 0) {
            this.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, "HenryTGZJMod:StancePower"));
        }
        if (this.amount >= 10) {
            this.amount = 10;
        }
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
            this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
    //效果


    public void onInitialApplication() {
        AbstractPower powerD = owner.getPower("HenryTGZJMod:DictateTheTempoPower");
        if (powerD != null){
            this.flash();
            this.addToBot(new DrawCardAction(powerD.amount * this.amount));
        }
    }

    public void atStartOfTurn(){
        this.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, (this.amount+1)/2));
    }


}
