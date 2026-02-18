package HenryTGZJMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

public class ComboAction extends AbstractGameAction{

    private final int stanceCost;
    private final AbstractGameAction[] actions;

    public ComboAction(AbstractCreature source, AbstractCreature target, int stanceCost, AbstractGameAction... actions) {
        this.source = source;
        this.target = target;
        this.stanceCost = stanceCost;
        this.actions = actions;
        //this.actionType = ActionType.POWER; // 设置为POWER类型，因为涉及姿态消耗
    }


    @Override
    public void update() {
        AbstractPower stancePower = player.getPower("HenryTGZJMod:StancePower");
        AbstractPower tirelessPower = player.getPower("HenryTGZJMod:tirelessPower");
        AbstractPower followThroughPower = player.getPower("HenryTGZJMod:followThroughPower");

        if (stancePower != null && stancePower.amount >= stanceCost) {

            //不知疲倦判定
            if (tirelessPower != null) {
                AbstractDungeon.actionManager.addToBottom(
                        new GainEnergyAction(tirelessPower.amount)
                );
                tirelessPower.flash();
            }

            //顺势而为判定
            if (followThroughPower != null) {
                AbstractDungeon.actionManager.addToBottom(
                        new FreeHandCardAction(followThroughPower.amount)
                );
                followThroughPower.flash();
            }

            //根据是否有行云流水选择消耗方式
            if (player.hasPower("HenryTGZJMod:FlawlessFlowPower")) {
                // 有行云流水时：减少姿态层数而不是清除
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(
                                player,
                                player,
                                "HenryTGZJMod:StancePower",
                                stanceCost
                        )
                );
                stancePower.flash();
            } else {
                // 没有行云流水时：清除整个姿态
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(
                                player,
                                player,
                                "HenryTGZJMod:StancePower"
                        )
                );
                stancePower.flash();
            }

            //执行自定义动作列表
            if (actions != null) {
                for (AbstractGameAction action : actions) {
                    if (action != null) {
                        AbstractDungeon.actionManager.addToBottom(action);
                    }
                }
            }
        }
        this.isDone = true;
    }
}

