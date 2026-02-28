package HenryTGZJMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

public class FreeHandCardAction extends AbstractGameAction {
    private final int amount;

    public FreeHandCardAction() {
        this(1);
    }

    public FreeHandCardAction(int amount) {
        this.amount = amount;
        this.duration = 0.0F;
        this.actionType = ActionType.CARD_MANIPULATION;
    }

    @Override
    public void update() {
        // 完全复刻木乃伊之手的筛选逻辑
        ArrayList<AbstractCard> validCards = new ArrayList<>();

        // 1. 筛选手牌（和木乃伊之手完全一样的条件）
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cost > 0 && c.costForTurn > 0 && !c.freeToPlayOnce) {
                validCards.add(c);
            }
        }

        // 2. 排除已在队列中的牌（和木乃伊之手完全一样）
        for (CardQueueItem i : AbstractDungeon.actionManager.cardQueue) {
            if (i.card != null) {
                validCards.remove(i.card);
            }
        }

        // 3. 随机选择并设置为0费
        for (int i = 0; i < Math.min(amount, validCards.size()); i++) {
            AbstractCard card = validCards.get(
                    AbstractDungeon.cardRandomRng.random(0, validCards.size() - 1)
            );
            card.setCostForTurn(0);
            card.flash();
            validCards.remove(card);
        }

        this.isDone = true;
    }
}