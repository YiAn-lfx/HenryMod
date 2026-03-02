package HenryTGZJMod.actions;

import HenryTGZJMod.powers.StancePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class DoubleStanceAction extends AbstractGameAction {

    @Override
    public void update() {
        AbstractPower power = player.getPower("HenryTGZJMod:StancePower");
        if (power != null){
            this.addToTop(new ApplyPowerAction(player, player, new StancePower(player,power.amount)));
        }
        this.isDone = true;
    }
}
