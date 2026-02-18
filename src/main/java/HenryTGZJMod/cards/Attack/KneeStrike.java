package HenryTGZJMod.cards.Attack;

import HenryTGZJMod.Modifier.ComboDamageModifier;
import HenryTGZJMod.actions.ComboAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.StaggerPower;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class KneeStrike extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(KneeStrike.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型

    public KneeStrike() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.stanceCost = this.baseStanceCost = 3;
        this.tags.add(CardTags.STRIKE);
        this.exhaust = false;
        this.isEthereal = false;
        this.secondDamage = this.baseSecondDamage = 6;
        CardModifierManager.addModifier(this, new ComboDamageModifier(secondDamage));    }

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
//                p, m, damage, stanceCost, DAMAGE,
//                new ApplyPowerAction(m, p, new StaggerPower(m))
//        );
        this.addToBot(
                new ComboAction(
                        p, m, stanceCost,
                        new ApplyPowerAction(m, p, new StaggerPower(m))
                )
        );
    }
}
