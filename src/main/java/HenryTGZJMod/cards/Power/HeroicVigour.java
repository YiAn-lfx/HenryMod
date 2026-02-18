package HenryTGZJMod.cards.Power;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.HeroicVigourPower;
import HenryTGZJMod.powers.TotentanzPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class HeroicVigour extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(HeroicVigour.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 2;
    private static final CardType TYPE = CardType.POWER; //类型
    private static final CardRarity RARITY = CardRarity.RARE; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型


    public HeroicVigour() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeBaseCost(1);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        this.magicNumber = AbstractDungeon.player.maxHealth/30;
        this.addToBot(new ApplyPowerAction(p, p, new HeroicVigourPower(p, magicNumber), magicNumber));
    }

}