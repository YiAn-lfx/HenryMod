package HenryTGZJMod.potions;

import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.SaviourSchnappsPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static HenryTGZJMod.modcore.HenryTGZJMod.MY_COLOR;


public class SaviourSchnapps extends AbstractHenryPotion {
    public static final String ID = ModHelper.makePath(SaviourSchnapps.class.getSimpleName());
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(ID);

    public SaviourSchnapps() {
        super(POTION_STRINGS.NAME, ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.ENERGY);
        this.labOutlineColor = MY_COLOR;
        this.isThrown = false;
        this.targetRequired = false;

    }
    @Override
    public void initializeData() {
        this.potency = this.getPotency();
        this.description = POTION_STRINGS.DESCRIPTIONS[0] + this.potency + POTION_STRINGS.DESCRIPTIONS[1];
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
    }

    @Override
    public void use(AbstractCreature target) {
        AbstractCreature p = AbstractDungeon.player;
        this.addToBot(new ApplyPowerAction(p, p, new SaviourSchnappsPower(p, p.currentHealth)));
        this.addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, potency)));
        this.addToBot(new ApplyPowerAction(p, p, new DexterityPower(p, potency)));
    }

    @Override
    public int getPotency(int ascensionLevel) {
        return 3;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SaviourSchnapps();
    }

}
