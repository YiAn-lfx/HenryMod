package HenryTGZJMod.Modifier;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.BanePoisonPower;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.extraeffects.ExtraEffectModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class BaneCardModifier extends ExtraEffectModifier {
    public static final String ID = ModHelper.makePath(BaneCardModifier.class.getSimpleName());
    public final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    public BaneCardModifier(int value) {
        super(VariableType.MAGIC, value);
        this.baseValue = value;
    }


    @Override
    protected boolean stackEffects(AbstractCard card, AbstractCardModifier other) {
        if (!(other instanceof BaneCardModifier)) return false;

        BaneCardModifier otherMod = (BaneCardModifier) other;

        if (this.baseValue < otherMod.baseValue) {
            otherMod.baseValue = this.baseValue;  // 替换为较小的值
        }

        return true;
    }

    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer player, AbstractCreature creature, UseCardAction action) {
        this.addToBot(new ApplyPowerAction(creature, player, new BanePoisonPower(creature, baseValue(card))));
    }


    @Override
    public String getExtraText(AbstractCard abstractCard) {
        return CARD_STRINGS.DESCRIPTION;
    }

    @Override
    public String getEffectId(AbstractCard abstractCard) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new BaneCardModifier(baseValue);
    }
}
