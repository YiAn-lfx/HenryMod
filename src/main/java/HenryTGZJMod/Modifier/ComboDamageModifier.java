package HenryTGZJMod.Modifier;

import HenryTGZJMod.helpers.ModHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;

public class ComboDamageModifier extends ExtraEffectModifier {

    private static final String ID = ModHelper.makePath(ComboDamageModifier.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);

    // 构造函数
    public ComboDamageModifier(int damageAmount) {
        super(VariableType.DAMAGE, damageAmount);
        // 设置优先级，负数表示在卡牌描述开头显示
        this.priority = -1;
    }



    @Override
    public void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m, UseCardAction useAction) {
        // 每次触发造成额外伤害
        addToBot(
                new DamageAction(m, new DamageInfo(p, proxy.getValueFor(type), DamageInfo.DamageType.NORMAL),
                AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                )
        );
    }

    @Override
    public String getExtraText(AbstractCard card) {
        // %s 会被动态变量替换，如 !dyn123456!
        return CARD_STRINGS.DESCRIPTION;
    }


    @Override
    public String getEffectId(AbstractCard card) {
        // 唯一的标识符，用于识别相同类型的修改器
        return "combo_base_damage";
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ComboDamageModifier(baseValue);
    }
}