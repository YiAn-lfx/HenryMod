package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class DuelPower extends AbstractHenryPower implements OnReceivePowerPower {
    public static final String POWER_ID = ModHelper.makePath(DuelPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public DuelPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = Amount;


        this.initializeImages();

        // 首次添加能力更新描述
        this.updateDescription();
    }


    // 能力在更新时如何修改描述
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], this.amount);
        } else {
            this.description = String.format(DESCRIPTIONS[1], this.amount);
        }
    }
    //效果


    @Override
    public void atEndOfRound() {
        this.amount -= 1;
        if (this.amount <= 0) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }


    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return abstractPower.type != PowerType.BUFF ||
                abstractPower.ID.equals("HenryTGZJMod:FocusPower") ||
                abstractPower.ID.equals("HenryTGZJMod:StancePower");
    }

}
