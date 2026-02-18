package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;

public class FocusPower extends AbstractHenryPower implements HealthBarRenderPower {
    public static final String POWER_ID = ModHelper.makePath(FocusPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public FocusPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
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
    public  void onInitialApplication(){
        this.addToTop(
                new RemoveSpecificPowerAction(this.owner, this.owner, "HenryTGZJMod:HiddenPower")
        );
        AbstractPower power = owner.getPower("HenryTGZJMod:CreepingPhantomPower");
        if (power != null && owner.hasPower("HenryTGZJMod:HiddenPower")) {
            this.addToBot(new ReducePowerAction(owner, owner, "Dexterity", power.amount));
        }
    }
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target){
        if (info.type != DamageType.THORNS && info.type != DamageType.HP_LOSS && info.owner != null) {
            this.flash();
            this.addToTop(
                new ApplyPowerAction(owner, owner, new StancePower(owner,1)));
        }
    }

    @Override
    public int getHealthBarAmount() {
        return AbstractDungeon.player.maxHealth;
    }

    @Override
    public Color getColor() {
        return Color.WHITE;
    }
}

