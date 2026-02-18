package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class DamageTrackerPower extends AbstractHenryPower {
    public static final String POWER_ID = ModHelper.makePath(DamageTrackerPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述

    private static int globalCallCounter = 0;

    public DamageTrackerPower(AbstractCreature owner) {
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

    // ==================== 伤害计算钩子 ====================

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        print("atDamageGive", String.format("类型:%s 伤害:%.1f", type, damage));
        return damage;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
        print("atDamageFinalGive", String.format("类型:%s 最终伤害:%.1f", type, damage));
        return damage;
    }



    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        print("atDamageReceive", String.format("类型:%s 伤害:%.1f", type, damage));
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
        print("atDamageFinalReceive", String.format("类型:%s 最终伤害:%.1f", type, damage));
        return damage;
    }


    // ==================== 伤害事件钩子 ====================

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        print("onAttackedToChangeDamage", formatDamageInfo(info, damageAmount));
        return damageAmount;
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        print("onAttacked", formatDamageInfo(info, damageAmount) +
                String.format(" 当前生命:%d/%d", owner.currentHealth, owner.maxHealth));
        return damageAmount;
    }



    @Override
    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
        print("onAttack", String.format("目标:%s %s",
                target.name, formatDamageInfo(info, damageAmount)));
    }
    @Override
    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
        print("onAttackToChangeDamage", String.format("%s",
                formatDamageInfo(info, damageAmount)));

        return damageAmount;
    }


    @Override
    public void onInflictDamage(DamageInfo info, int damageAmount, AbstractCreature target) {
        print("onInflictDamage", String.format("目标:%s 实际伤害:%d",
                target.name, damageAmount));
    }

    @Override
    public void onDamageAllEnemies(int[] damage) {
        StringBuilder sb = new StringBuilder("伤害数组:[");
        for (int i = 0; i < damage.length; i++) {
            sb.append(damage[i]);
            if (i < damage.length - 1) sb.append(", ");
        }
        sb.append("]");
        print("onDamageAllEnemies", sb.toString());
    }

    @Override
    public int onLoseHp(int damageAmount) {
        print("onLoseHp", String.format("失去生命:%d", damageAmount));
        return damageAmount;
    }


    @Override
    public void wasHPLost(DamageInfo info, int damageAmount) {
        print("wasHPLost", formatDamageInfo(info, damageAmount));
    }

    // ==================== 卡牌伤害相关钩子 ====================

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        print("atDamageGiveWithCard", String.format("卡牌:%s 类型:%s 伤害:%.1f",
                card.name, type, damage));
        return damage;
    }

    @Override
    public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        print("atDamageFinalGiveWithCard", String.format("卡牌:%s 类型:%s 最终伤害:%.1f",
                card.name, type, damage));
        return damage;
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        print("atDamageReceiveWithCard", String.format("卡牌:%s 类型:%s 伤害:%.1f",
                card.name, type, damage));
        return damage;
    }

    @Override
    public float atDamageFinalReceive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        print("atDamageFinalReceiveWithCard", String.format("卡牌:%s 类型:%s 最终伤害:%.1f",
                card.name, type, damage));
        return damage;
    }



    // ==================== 辅助方法 ====================

    private void print(String hookName, String message) {
        System.out.printf("[%d] %s.%s: %s%n",
                ++globalCallCounter,
                owner.isPlayer ? "玩家" : owner.name,
                hookName,
                message);
    }

    private String formatDamageInfo(DamageInfo info, int damageAmount) {
        return String.format("基础:%d 实际:%d 类型:%s 来源:%s",
                info.base,
                damageAmount,
                info.type,
                info.owner != null ? info.owner.name : "未知");
    }

    // 重置计数器（每场战斗开始）
    @Override
    public void onInitialApplication() {
        globalCallCounter = 0;
        System.out.println("\n=== 伤害追踪器已激活 ===");
    }



}