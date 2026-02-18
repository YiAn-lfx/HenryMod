package HenryTGZJMod.cards.Attack;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//public class Strike extends CustomCard {
//    public static final String ID = ModHelper.makePath(Strike.class.getSimpleName());
//    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
//    private static final String NAME = CARD_STRINGS.NAME;
//    //private static final String NAME = "打击";
//    private static final String IMG_PATH = "HenryTGZJModResources/img/cards/Strike.png";
//    private static final int COST = 1;
//    private  static  final String DESCRIPTION = CARD_STRINGS.DESCRIPTION;
//    //private static final String DESCRIPTION = "造成 !D! 点伤害。";
//    private static final CardType TYPE = CardType.ATTACK;
//    private static final CardColor COLOR = H_BROWN;
//    private static final CardRarity RARITY = CardRarity.BASIC;
//    private static final CardTarget TARGET = CardTarget.ENEMY;
//
//    public Strike() {
//        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
//        this.damage = this.baseDamage = 6;
//        this.tags.add(CardTags.STARTER_STRIKE);
//        this.tags.add(CardTags.STRIKE);
//    }

public class Strike extends AbstractHenryCard {
    public static  final  String ID = ModHelper.makePath(Strike.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    public Strike(){
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.tags.add(CardTags.STARTER_STRIKE);
        this.tags.add(CardTags.STRIKE);
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeDamage(3); //提升的伤害数值
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        this.addToBot(
                new DamageAction(
                        m,
                        new DamageInfo(
                                p,
                                damage,
                                DamageType.NORMAL
                        ),
                        AttackEffect.SLASH_HORIZONTAL
                )
        );
    }
}