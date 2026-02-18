package HenryTGZJMod.tags;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
/** @deprecated */
public class CustomTags {
    // 是否为连击卡牌（有连击词条）
    @SpireEnum
    public static AbstractCard.CardTags COMBO_CARD;

    // 是否触发了连击（当前回合）
    @SpireEnum
    public static AbstractCard.CardTags COMBO_TRIGGERED;

    // 其他自定义标签...
//    @SpireEnum
//    public static AbstractCard.CardTags STANCE_CARD;
}