package HenryTGZJMod.actions;

import HenryTGZJMod.powers.FocusPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class FocusAction extends AbstractGameAction{

    private final AbstractGameAction[] actions;

    public FocusAction(AbstractGameAction... actions) {
        this.actions = actions;
        //this.actionType = ActionType.POWER; // 设置为POWER类型，因为涉及姿态消耗
    }


    @Override
    public void update() {
        if (player.hasPower("HenryTGZJMod:FocusPower")) {
            if (actions != null) {
                for (AbstractGameAction action : actions) {
                    if (action != null) {
                        AbstractDungeon.actionManager.addToBottom(action);
                    }
                }
            }
        } else {
            this.addToBot(new ApplyPowerAction(player, player, new FocusPower(player)));
        }
        this.isDone = true;
    }

}

