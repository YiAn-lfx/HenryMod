package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

public class HiddenPower extends AbstractHenryPower implements HealthBarRenderPower {
    public static final String POWER_ID = ModHelper.makePath(HiddenPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public HiddenPower(AbstractCreature owner) {
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
        this.description = DESCRIPTIONS[0];
    }
    //效果


    public void onInitialApplication(){
        AbstractPower power = owner.getPower("HenryTGZJMod:CreepingPhantomPower");
        this.addToTop(
                new RemoveSpecificPowerAction(this.owner, this.owner, "HenryTGZJMod:FocusPower")
        );
        if (power != null) {
            this.addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, power.amount)));
        }
    }


    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount >0 ) {
            this.addToBot(
                    new RemoveSpecificPowerAction(
                            this.owner, this.owner, this.ID
                    )
            );
            AbstractPower power = owner.getPower("HenryTGZJMod:CreepingPhantomPower");
            if (power != null) {
                this.addToBot(new ReducePowerAction(owner, owner, "Dexterity", power.amount));
            }
            this.addToBot(
                    new ApplyPowerAction(
                            this.owner, this.owner, new ClumsyPower(
                                    this.owner, 1, false
                            )
                    )
            );
        }

        return damageAmount/2;
    }

    public void onUseCard(AbstractCard card, UseCardAction action){
        if (card.type == AbstractCard.CardType.ATTACK ){
            this.addToBot(
                    new RemoveSpecificPowerAction(
                            this.owner, this.owner, this.ID
                    )
            );
            AbstractPower power = owner.getPower("HenryTGZJMod:CreepingPhantomPower");
            if (power != null) {
                this.addToBot(new ReducePowerAction(owner, owner, "Dexterity", power.amount));
            }
        }
    }

    @Override
    public int getHealthBarAmount() {
        return AbstractDungeon.player.maxHealth;
    }

    @Override
    public Color getColor() {
        return Color.GRAY;
    }


}
