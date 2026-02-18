package HenryTGZJMod.DynamicVariable;

import HenryTGZJMod.cards.AbstractHenryCard;
import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class StanceCostVariable extends DynamicVariable {

    // 存储原始基础值，用于判断增减
    private int lastBaseValue = 0;

    @Override
    public String key() {
        return "STANCE_COST";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card != null) {
            AbstractHenryCard stanceCard = (AbstractHenryCard) card;
            // 获取当前值和基础值
            int currentValue = stanceCard.getStanceCost();
            int baseValue = stanceCard.getBaseStanceCost();

            // 记录基础值变化
            if (baseValue != lastBaseValue) {
                lastBaseValue = baseValue;
            }

            // 返回true表示数值被修改（增减都会返回true）
            return currentValue != baseValue;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            AbstractHenryCard stanceCard = (AbstractHenryCard) card;
            return stanceCard.getStanceCost();
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            AbstractHenryCard stanceCard = (AbstractHenryCard) card;
            return stanceCard.getBaseStanceCost();
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        // 卡牌升级不影响这个变量
        return true;
    }

    @Override
    public Color getNormalColor() {
        // 未修改时的颜色 - 使用游戏设置的白色
        return Settings.CREAM_COLOR;
    }

    @Override
    public Color getIncreasedValueColor() {
        // 数值增加时的颜色 - 使用游戏内置的红色
        return Settings.RED_TEXT_COLOR;
    }

    @Override
    public Color getDecreasedValueColor() {
        // 数值减少时的颜色 - 使用游戏内置的绿色
        return Settings.GREEN_TEXT_COLOR;
    }

    @Override
    public Color getUpgradedColor() {
        // 卡牌升级时的颜色 - 使用游戏内置的绿色
        return Settings.GREEN_TEXT_COLOR;
    }
}