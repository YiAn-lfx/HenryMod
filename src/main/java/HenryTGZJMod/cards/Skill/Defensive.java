package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.Modifier.ComboBlockModifier;
import HenryTGZJMod.Modifier.ComboDamageModifier;
import HenryTGZJMod.actions.ComboAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;


public class Defensive extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Defensive.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.COMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public Defensive() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = 5;
        this.stanceCost = baseStanceCost = 2;
        this.exhaust = true;
        this.secondBlock = this.baseSecondBlock = 5;
        CardModifierManager.addModifier(this, new ComboBlockModifier(secondBlock));
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeStanceCost(-1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果

//        ComboUtil.Combo(
//                p, m, block, stanceCost, ComboUtil.FirstActionType.BLOCK,
//                new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber))
//        );
        this.addToBot(
                new ComboAction(
                        p, m, stanceCost,
                        new ApplyPowerAction(p, p, new PlatedArmorPower(p, magicNumber))
                )
        );
    }
}