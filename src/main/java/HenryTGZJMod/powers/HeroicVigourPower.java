package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class HeroicVigourPower extends AbstractHenryPower {
    public static final String POWER_ID = ModHelper.makePath(HeroicVigourPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    public HeroicVigourPower(AbstractCreature owner, int Amount) {
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
    public void atStartOfTurn() {
        this.addToBot(new GainEnergyAction(this.amount));
        this.flash();
    }
}
