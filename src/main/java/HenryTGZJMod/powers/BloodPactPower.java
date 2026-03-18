package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.unique.ExpertiseAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class BloodPactPower extends AbstractHenryPower implements OnPlayerDeathPower {
    public static final String POWER_ID = ModHelper.makePath(BloodPactPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    public BloodPactPower(AbstractCreature owner, int Amount) {
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
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        return false;
    }

    @Override
    public void onInitialApplication() {
        int he = owner.currentHealth - 1;
        //this.addToBot(new ApplyPowerAction(owner, owner , new StrengthPower(owner, (he /10)*5)));
        AbstractDungeon.player.heal(-he);
        AbstractDungeon.player.healthBarUpdatedEvent();
        this.addToBot(new GainEnergyAction(2));
        this.addToBot(new ExpertiseAction(owner, 10));
        this.flash();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new GainEnergyAction(2));
        this.addToBot(new ExpertiseAction(owner, 10));
        this.flash();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flash();
        return 0;
    }


    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        this.flash();
        AbstractDungeon.player.decreaseMaxHealth(1);
    }

    @Override
    public void onVictory() {
        AbstractDungeon.player.heal(owner.maxHealth / 2);
        this.flash();
    }

    @Override
    public void atEndOfRound() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, this, 1));
            if (this.amount == 1) {
                this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
            }
        }
    }

    @Override
    public void onRemove() {
//        AbstractDungeon.player.heal(-99999);
//        AbstractDungeon.player.healthBarUpdatedEvent();
        for (int i = 1; i < 99; i++) {
            this.addToTop(new DamageAction(owner, new DamageInfo(owner, 99999, DamageInfo.DamageType.HP_LOSS)));
        }
    }
}
