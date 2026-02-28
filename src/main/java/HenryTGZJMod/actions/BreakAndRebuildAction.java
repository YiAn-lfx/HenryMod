package HenryTGZJMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;

public class BreakAndRebuildAction extends AbstractGameAction{

    public BreakAndRebuildAction() {
        this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        AbstractPlayer p = AbstractDungeon.player;

        // 统计消耗的卡牌数量
        int strikeCount = 0;
        int defendCount = 0;

        // 用于暂存要消耗的卡牌
        ArrayList<AbstractCard> cardsToExhaust = new ArrayList<>();

        // 1. 遍历手牌
        for (AbstractCard c : p.hand.group) {
            checkAndAddCard(c, cardsToExhaust);
        }

        // 2. 遍历抽牌堆
        for (AbstractCard c : p.drawPile.group) {
            checkAndAddCard(c, cardsToExhaust);
        }

        // 3. 遍历弃牌堆
        for (AbstractCard c : p.discardPile.group) {
            checkAndAddCard(c, cardsToExhaust);
        }

        // 4. 先统计数量（因为消耗后卡牌信息会丢失）
        for (AbstractCard c : cardsToExhaust) {
            if (c.hasTag(AbstractCard.CardTags.STARTER_STRIKE)) {
                strikeCount++;
            } else if (c.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
                defendCount++;
            }
        }

        // 5. 消耗所有符合条件的卡牌
        for (AbstractCard c : cardsToExhaust) {
            // 从所有可能的卡组中尝试消耗（ExhaustSpecificCardAction会自动处理）
            this.addToTop(new ExhaustSpecificCardAction(c, p.hand));
            this.addToTop(new ExhaustSpecificCardAction(c, p.drawPile));
            this.addToTop(new ExhaustSpecificCardAction(c, p.discardPile));
        }

        // 6. 根据消耗数量施加效果
        if (strikeCount > 0) {
            this.addToBot(new ApplyPowerAction(
                    p, p,
                    new WeakPower(p, strikeCount, false),
                    strikeCount
            ));
        }

        if (defendCount > 0) {
            this.addToBot(new ApplyPowerAction(
                    p, p,
                    new FrailPower(p, defendCount, false),
                    defendCount
            ));
        }

        this.isDone = true;
    }

    // 检查卡牌是否符合条件并添加到待消耗列表
    private void checkAndAddCard(AbstractCard card, ArrayList<AbstractCard> list) {
        if (card.hasTag(AbstractCard.CardTags.STARTER_STRIKE) ||
                card.hasTag(AbstractCard.CardTags.STARTER_DEFEND)) {
            list.add(card);
        }
    }
}