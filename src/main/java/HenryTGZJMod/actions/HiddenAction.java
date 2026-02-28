package HenryTGZJMod.actions;

import HenryTGZJMod.powers.HiddenPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class HiddenAction extends AbstractGameAction{

    private final AbstractGameAction[] actions;

    public HiddenAction(AbstractGameAction... actions) {
        this.actions = actions;
        //this.actionType = ActionType.POWER;
    }


    @Override
    public void update() {
        if (player.hasPower("HenryTGZJMod:HiddenPower")) {
            if (actions != null) {
                for (AbstractGameAction action : actions) {
                    if (action != null) {
                        AbstractDungeon.actionManager.addToBottom(action);
                    }
                }
            }
        } else {
            this.addToBot(new ApplyPowerAction(player, player, new HiddenPower(player)));
        }
        this.isDone = true;
    }

}

