package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.actions.StealAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class PettyTheft extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(PettyTheft.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型

    public PettyTheft() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.magicNumber = baseMagicNumber = 20;
        this.exhaust = false;
        this.isEthereal = false;
        this.dontTriggerOnUseCard = false;
        this.isInnate = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(10);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        if (p.hasPower("HenryTGZJMod:HiddenPower")) {
            this.addToBot(StealAction.createGold(m, p, 1, magicNumber));
        }
    }
}