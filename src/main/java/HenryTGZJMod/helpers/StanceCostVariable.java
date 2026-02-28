package HenryTGZJMod.helpers;

import HenryTGZJMod.cards.AbstractHenryCard;
import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class StanceCostVariable extends DynamicVariable {

    @Override
    public String key() {
        return "STANCE_COST";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).isStanceCostModified
                    || ((AbstractHenryCard) card).showStanceCostAsModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).stanceCost;
        }
        return 0;
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).baseStanceCost;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).upgradedStanceCost;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractHenryCard) {
            // 这个字段专门用于升级预览
            ((AbstractHenryCard) card).showStanceCostAsModified = v;
        }
    }

    @Override
    public Color getUpgradedColor() {
        return Settings.GREEN_TEXT_COLOR;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Settings.RED_TEXT_COLOR;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Settings.GREEN_TEXT_COLOR;
    }
}