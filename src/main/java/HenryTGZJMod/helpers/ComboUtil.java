package HenryTGZJMod.helpers;

import HenryTGZJMod.actions.FreeHandCardAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

/** @deprecated */

public class ComboUtil {

    public enum FirstActionType {
        DAMAGE,      // 造成伤害
        BLOCK,       // 获得格挡
        NONE         // 无动作
    }

    /**
     * 通用姿态消耗方法
     * @param player 玩家对象
     * @param monster 目标怪物
     * @param amount 数值
     * @param stanceCost 需要消耗的姿态层数
     * @param firstActionType 第一个动作类型
     * @param actions 姿态消耗后的自定义动作列表
     */
    public static void Combo(
            AbstractPlayer player,
            AbstractMonster monster,
            int amount,
            int stanceCost,
            FirstActionType firstActionType,
            AbstractGameAction... actions) {

        // 1. 第一个固定伤害动作（可选）
        executeFirstAction(player, monster, amount, firstActionType);


        // 2. 检查姿态条件
        AbstractPower stancePower = player.getPower("HenryTGZJMod:StancePower");
        AbstractPower TirelessPower = player.getPower("HenryTGZJMod:TirelessPower");
        AbstractPower FollowThroughPower = player.getPower("HenryTGZJMod:FollowThroughPower");

        if (stancePower != null && stancePower.amount >= stanceCost) {

            //不知疲倦判定
            if (TirelessPower != null){
                AbstractDungeon.actionManager.addToBottom(
                        new GainEnergyAction(TirelessPower.amount)
                );
            }

            //顺势而为判定
            if (FollowThroughPower != null) {
                AbstractDungeon.actionManager.addToBottom(
                        new FreeHandCardAction(FollowThroughPower.amount)
                );
            }

            // 3. 根据是否有流畅姿态选择消耗方式
            if (player.hasPower("HenryTGZJMod:FlawlessFlowPower") ) {
                // 有流畅姿态时：减少姿态层数而不是清除
                AbstractDungeon.actionManager.addToBottom(
                        new ReducePowerAction(
                                player,
                                player,
                                "HenryTGZJMod:StancePower",
                                stanceCost
                        )
                );
            } else {
                // 没有流畅姿态时：清除整个姿态
                AbstractDungeon.actionManager.addToBottom(
                        new RemoveSpecificPowerAction(
                                player,
                                player,
                                "HenryTGZJMod:StancePower"
                        )
                );
            }

            // 4. 执行自定义的第二动作
            if (actions != null) {
                for (AbstractGameAction action : actions) {
                    if (action != null) {
                        AbstractDungeon.actionManager.addToBottom(action);
                    }
                }
            }
        }
    }
    private static void executeFirstAction(AbstractPlayer player, AbstractMonster monster,
                                           int amount,
                                           FirstActionType actionType) {

        switch (actionType) {
            case DAMAGE:
                // 造成伤害
                if (monster != null && !monster.isDeadOrEscaped()) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(
                                    monster,
                                    new DamageInfo(player, amount, DamageInfo.DamageType.NORMAL),
                                    AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                            )
                    );
                }
                break;

            case BLOCK:
                // 获得格挡
                AbstractDungeon.actionManager.addToBottom(
                        new GainBlockAction(player, player, amount)
                );
                break;

            case NONE:
                // 无动作
                break;

            default:
                // 默认为无动作
                break;
        }
    }
}