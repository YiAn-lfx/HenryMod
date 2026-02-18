package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.FeintPower;
import HenryTGZJMod.powers.FocusPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class Feint extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Feint.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public Feint() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = 2;
        //this.cardsToPreview = new KneeStrike();
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        AbstractPower powerS = p.getPower("HenryTGZJMod:FocusPower");
        if (powerS != null) {
//            this.addToBot(
//                    new MakeTempCardInHandAction(new KneeStrike(), 1)
//            );
            this.addToBot(
                    new ApplyPowerAction(
                            p, p, new FeintPower(p, 1)
                    )
            );
            this.addToBot(
                    //new GainEnergyAction(2)
                    new DrawCardAction(magicNumber)
            );
        } else {
            this.addToBot(new ApplyPowerAction(p, p, new FocusPower(p)));
        }
    }
}