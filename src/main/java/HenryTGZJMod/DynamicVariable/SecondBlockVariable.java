package HenryTGZJMod.DynamicVariable;

import HenryTGZJMod.cards.AbstractHenryCard;
import basemod.abstracts.DynamicVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;


public class SecondBlockVariable extends DynamicVariable {
    @Override
    public String key() {
        return "B2";
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).isSecondBlockModified;
        }
        return false;
    }

    @Override
    public void setIsModified(AbstractCard card, boolean v) {
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).secondBlock;
        }
        return 0;
    }



    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).baseSecondBlock;
        }
        return 0;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractHenryCard) {
            return ((AbstractHenryCard) card).upgradedSecondBlock;
        }
        return false;
    }


}