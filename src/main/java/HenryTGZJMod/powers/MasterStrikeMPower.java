package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class MasterStrikeMPower extends AbstractHenryPower implements InvisiblePower {
    public static final String POWER_ID = ModHelper.makePath(MasterStrikeMPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    private int saveDamage = 0;
    private boolean saved = false;


    public MasterStrikeMPower(AbstractCreature owner) {
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
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        System.out.println("atDamageFinalGive" + damage);
        AbstractPower power = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
        if (saveDamage == 0 && type == DamageInfo.DamageType.NORMAL && power != null && (int)damage % 10 == power.amount % 10) {
            System.out.println("atDamageFinalGive2:" + damage);
            if (!saved) {
                saveDamage = (int) damage;
                saved = true;
//            this.addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, (int)damage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
//            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new TraumatizedPower(this.owner, power.amount)));
//            damage = 0;
            }
        }
        return damage;
    }

    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
            System.out.println("onAttackToChangeDamage" + damageAmount);
            AbstractPower power = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
            if (saved && saveDamage >0 && info.type == DamageInfo.DamageType.NORMAL  /*power != null && damageAmount % 10 == power.amount % 10*/) {
                System.out.println("onAttackToChangeDamage2:" + damageAmount);
                this.addToBot(new DamageAction(this.owner, new DamageInfo(AbstractDungeon.player, saveDamage, DamageInfo.DamageType.HP_LOSS), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new TraumatizedPower(this.owner, power.amount)));
                saved = false;
                //damageAmount = 0;
                saveDamage = 0;

            }

        return damageAmount;
    }



//    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
//        System.out.println("atDamageFinalGiveM:" + damage);
//        return damage;
//    }
//
//    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
//        System.out.println("onAttackToChangeDamageM:" + damageAmount);
//        return damageAmount;
//    }

    @Override
    public void atEndOfRound() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }
}
