package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class ClumsyPower extends AbstractHenryPower {
    public static final String POWER_ID = ModHelper.makePath(ClumsyPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    private boolean justApplied = false;

    public ClumsyPower(AbstractCreature owner, int Amount, boolean isSourceMonster) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        this.amount =Amount;


        this.initializeImages();
        // 首次添加能力更新描述
        this.updateDescription();

        if (AbstractDungeon.actionManager.turnHasEnded /*&& isSourceMonster*/) {
            this.justApplied = true;
        }
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

    public void atEndOfRound() {
        if (this.justApplied) {
            this.justApplied = false;
        } else {
            if (this.amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
            } else {
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }

        }
    }

//    public void onUseCard(AbstractCard card, UseCardAction action) {
//        if (card.type == AbstractCard.CardType.ATTACK) {
//            this.addToBot(
//                    new RemoveSpecificPowerAction(
//                            this.owner, this.owner, this.ID
//                    )
//            );
//        }
//    }
}