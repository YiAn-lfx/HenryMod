package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class MasterStrikePower extends AbstractHenryPower {
    public static final String POWER_ID = ModHelper.makePath(MasterStrikePower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    private int shouldCounterDamage = 0;


    public MasterStrikePower(AbstractCreature owner) {
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
            this.description = String.format(DESCRIPTIONS[0], this.amount);
    }
    //效果


    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType damageType) {
        AbstractPower power = owner.getPower("HenryTGZJMod:StancePower");
        if (power != null && (int)damage % 10 == power.amount % 10) {
            this.flash();
//            this.shouldCounterDamage = (int)damage;
            damage = 0;
        }
        return damage;
    }

//    @Override
//    public int onAttacked(DamageInfo info, int damageAmount) {
//        AbstractPower power = owner.getPower("HenryTGZJMod:StancePower");
//        if (shouldCounterDamage > 0) {
//            this.addToBot(new DamageAction(info.owner, new DamageInfo(this.owner, shouldCounterDamage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
//            this.addToBot(new ApplyPowerAction(info.owner, this.owner, new TraumatizedPower(info.owner, power.amount)));
//            shouldCounterDamage = 0;
//        }
//        return damageAmount;
//    }

//    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
//        System.out.println("atDamageFinalReceive:" + damage);
//        return damage;
//    }
//
//    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
//        System.out.println("onAttackedToChangeDamage:" + damageAmount);
//        return damageAmount;
//    }

    @Override
    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
}
