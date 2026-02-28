package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

public class EmbraceTheAbyssPower extends AbstractHenryPower implements OnPlayerDeathPower {
    public static final String POWER_ID = ModHelper.makePath(EmbraceTheAbyssPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    private int he;

    public EmbraceTheAbyssPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount = -1;


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
        he = owner.currentHealth-1;
        this.addToBot(new ApplyPowerAction(owner, owner , new StrengthPower(owner, (he/10)*5)));
        AbstractDungeon.player.heal(-he);
        AbstractDungeon.player.healthBarUpdatedEvent();
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new GainEnergyAction(2));
        this.flash();
    }

    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        this.addToBot(new MakeTempCardInDiscardAction(new Burn(), 1));
        this.flash();
        if (target.isDead) {
            AbstractDungeon.player.increaseMaxHp(10, true);
        }
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        this.flash();
        AbstractDungeon.player.decreaseMaxHealth(damageAmount);
        this.addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, damageAmount)));
        return 0;
    }

    @Override
    public void onVictory() {
        AbstractDungeon.player.heal(he);
        this.flash();
    }
}
