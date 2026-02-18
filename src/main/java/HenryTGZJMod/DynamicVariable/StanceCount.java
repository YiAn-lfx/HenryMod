package HenryTGZJMod.DynamicVariable;

import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
/** @deprecated */

public class StanceCount extends DynamicVariable {

    @Override
    public String key() {
        return "SC";  // 在描述中用 !STANCE! 引用
    }

    @Override
    public boolean isModified(AbstractCard card) {
        // 检查玩家当前架势层数是否大于0
        if (AbstractDungeon.player != null) {
            AbstractPower stance = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
            return stance != null && stance.amount > 0;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        // 通常不需要实现，除非需要特殊处理
    }

    @Override
    public int value(AbstractCard card) {
        // 返回玩家当前的架势层数
        if (AbstractDungeon.player != null) {
            AbstractPower stance = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
            return stance != null ? stance.amount : 0;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        // 基础值为0（没有架势时）
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        // 卡牌升级不影响这个变量
        return false;
    }
}