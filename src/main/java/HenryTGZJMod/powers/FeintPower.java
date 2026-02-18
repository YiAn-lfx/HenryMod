package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.DamageModApplyingPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.Collections;
import java.util.List;

public class FeintPower extends AbstractHenryPower implements DamageModApplyingPower {
    public static final String POWER_ID = ModHelper.makePath(FeintPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述



    // 创建一个内部类作为伤害修改器
    private static class IgnoreBlockModifier extends AbstractDamageModifier {
        @Override
        public boolean ignoresBlock(AbstractCreature target) {
            return true; // 关键：无视格挡
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new IgnoreBlockModifier();
        }
    }

    public FeintPower(AbstractCreature owner, int Amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.BUFF;
        this.amount =Amount;



        this.initializeImages();

        // 首次添加能力更新描述
        this.updateDescription();
    }



    public void updateDescription() {
        if (this.amount == 1) {
            this.description = String.format(DESCRIPTIONS[0], this.amount);
        } else {
            this.description = String.format(DESCRIPTIONS[1], this.amount);
        }
    }

    // DamageModApplyingPower接口方法1：是否应该应用修改器
    @Override
    public boolean shouldPushMods(DamageInfo info, Object instigator, List<AbstractDamageModifier> activeModifiers) {
        // 只在instigator是攻击牌，且我们有层数时应用
        return instigator instanceof AbstractCard
                && ((AbstractCard) instigator).type == AbstractCard.CardType.ATTACK
                && this.amount > 0;
    }

    // DamageModApplyingPower接口方法2：要应用哪些修改器
    @Override
    public List<AbstractDamageModifier> modsToPush(DamageInfo info, Object instigator, List<AbstractDamageModifier> activeModifiers) {

        // 返回一个无视格挡的修改器
        return Collections.singletonList(new IgnoreBlockModifier());
    }

    @Override
    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            this.addToBot(
                    new ReducePowerAction(this.owner, this.owner, this.ID, 1)
            );
        }
    }

    @Override
    public void atEndOfRound() {
        this.addToBot(
                new RemoveSpecificPowerAction(this.owner, this.owner, this.ID)
        );

    }


}