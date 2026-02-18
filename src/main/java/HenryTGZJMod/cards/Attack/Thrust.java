package HenryTGZJMod.cards.Attack;

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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;


public class Thrust extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Thrust.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK; //类型
    private static final CardRarity RARITY = CardRarity.BASIC; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型


    public Thrust() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 8;
        this.magicNumber = this.baseMagicNumber = 1;
        this.stanceCost = this.baseStanceCost = 1;
        this.secondDamage = this.baseSecondDamage = 6;
        CardModifierManager.addModifier(this, new ComboDamageModifier(secondDamage));
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

//        ComboUtil.Combo(
//                p, m, damage, stanceCost, ComboUtil.FirstActionType.DAMAGE,
//                new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)),
//                new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false))
//        );
        this.addToBot(
                new ComboAction(
                        p, m, stanceCost,
                        new ApplyPowerAction(m, p, new VulnerablePower(m, magicNumber, false)),
                        new ApplyPowerAction(m, p, new WeakPower(m, magicNumber, false))
                )
        );
    }
}
