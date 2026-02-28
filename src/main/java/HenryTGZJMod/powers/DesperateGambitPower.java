package HenryTGZJMod.powers;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Objects;

public class DesperateGambitPower extends AbstractHenryPower implements OnReceivePowerPower {
    public static final String POWER_ID = ModHelper.makePath(DesperateGambitPower.class.getSimpleName()); // 能力的ID
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID); // 能力的本地化字段
    private static final String NAME = powerStrings.NAME; // 能力的名称
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS; // 能力的描述


    public DesperateGambitPower(AbstractCreature owner) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.type = PowerType.DEBUFF;
        // 如果需要不能叠加的能力，只需将上面的Amount参数删掉，并把下面的Amount改成-1就行
        this.amount = -1;

        this.initializeImages();
        // 首次添加能力更新描述
        this.updateDescription();
    }

    // 能力在更新时如何修改描述
    public void updateDescription() {
        this.description = DESCRIPTIONS[0];
    }



    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            this.addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        return !Objects.equals(abstractPower.ID, "HenryTGZJMod:StancePower");
    }
}

