package HenryTGZJMod.potions;

import HenryTGZJMod.Modifier.BaneCardModifier;
import HenryTGZJMod.helpers.ModHelper;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.actions.common.SelectCardsInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import static HenryTGZJMod.modcore.HenryTGZJMod.MY_COLOR;


public class BanePoison extends AbstractHenryPotion {
    public static final String ID = ModHelper.makePath(BanePoison.class.getSimpleName());
    private static final PotionStrings POTION_STRINGS = CardCrawlGame.languagePack.getPotionString(ID);

    public BanePoison() {
        super(POTION_STRINGS.NAME, ID, PotionRarity.RARE, PotionSize.SPHERE, PotionColor.SMOKE);
        this.labOutlineColor = MY_COLOR;
        this.isThrown = true;
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
//        this.addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new BanePoisonPower(target, potency)));

        addToBot(new SelectCardsInHandAction(POTION_STRINGS.DESCRIPTIONS[2],
                (card) -> card.type == AbstractCard.CardType.ATTACK,
                (cards) -> {
                    AbstractCard selected = cards.get(0);
                    CardModifierManager.addModifier(selected, new BaneCardModifier(potency));
                }
        ));


    }

    @Override
    public int getPotency() {
        int basePotency = this.getPotency(AbstractDungeon.ascensionLevel);

        if (AbstractDungeon.player != null &&
                AbstractDungeon.player.hasRelic("SacredBark")) {
            // 效果减半
            return Math.max(1, basePotency / 2);
        }
        return basePotency;
    }

    @Override
    public int getPotency(int i) {
        return 5;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new BanePoison();
    }

}
