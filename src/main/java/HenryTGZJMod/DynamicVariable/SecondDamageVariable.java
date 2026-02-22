package HenryTGZJMod.DynamicVariable;

import HenryTGZJMod.cards.AbstractHenryCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;


public class SecondDamageVariable extends DynamicVariable {
    @Override
    public String key() {
        return "D2";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).isSecondDamageModified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).secondDamage;
        }
        return 0;
    }



    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).baseSecondDamage;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).upgradedSecondDamage;
        }
        return false;
    }


}