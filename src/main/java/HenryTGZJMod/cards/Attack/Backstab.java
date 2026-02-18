package HenryTGZJMod.cards.Attack;

import HenryTGZJMod.actions.StealAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


public class Backstab extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Backstab.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK; //类型
    private static final CardRarity RARITY = CardRarity.COMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型


    public Backstab() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 12;
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
        this.isEthereal = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        if (p.hasPower("HenryTGZJMod:HiddenPower")) {
            this.addToBot(
                    new DamageAction(
                            m,
                            new DamageInfo(
                                    p,
                                    damage,
                                    DamageType.NORMAL
                            ),
                            AbstractGameAction.AttackEffect.SLASH_HORIZONTAL
                    )
            );
//            AbstractPower powerT = p.getPower("HenryTGZJMod:TwoForOnePower");
//            int loopCount = 1;
//            if (powerT != null) {
//                loopCount += powerT.amount; // 根据能力层数增加循环次数
//            }
//            for (int i=0; i<loopCount; i++){
                addToBot(StealAction.createSpecificBuff(m, p, StrengthPower.POWER_ID, 1, magicNumber));
//            }
        }
    }
}
