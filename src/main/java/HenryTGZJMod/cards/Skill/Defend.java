package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//public class Defend extends CustomCard {
//    public static final String ID = ModHelper.makePath(Defend.class.getSimpleName());
//    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
//    private static final String NAME = CARD_STRINGS.NAME; //名称
//    private static final String IMG_PATH = "HenryTGZJModResources/img/cards/Strike.png";
//    private static final int COST = 1;
//    private  static  final String DESCRIPTION = CARD_STRINGS.DESCRIPTION; //描述
//    private static final CardType TYPE = CardType.SKILL; //类型
//    private static final CardColor COLOR = H_BROWN;
//    private static final CardRarity RARITY = CardRarity.BASIC; //稀有度
//    private static final CardTarget TARGET = CardTarget.SELF; //指向类型
//
//    public Defend() {
//        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
//        this.block = this.baseBlock = 5;
//        this.tags.add(CardTags.STARTER_DEFEND);
//    }
public class Defend extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Defend.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.BASIC; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public Defend() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.block = this.baseBlock = 5;
        this.tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeBlock(3); //提升的伤害数值
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        this.addToBot(
                new GainBlockAction(
                        p, p, this.block)
        );
    }
}