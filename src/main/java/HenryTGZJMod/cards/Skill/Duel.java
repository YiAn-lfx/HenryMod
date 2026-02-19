package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.DuelPower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class Duel extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Duel.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ALL; //指向类型

    public Duel() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.magicNumber = this.baseMagicNumber = 3;
        this.exhaust = true;
        this.isEthereal = false;
        this.dontTriggerOnUseCard = false;
        this.isInnate = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeMagicNumber(-2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        if (p.hasPower("HenryTGZJMod:FocusPower")) {
            this.addToBot(new ApplyPowerAction(p, p, new DuelPower(p, magicNumber)));
            this.addToBot(new AllEnemyApplyPowerAction(p, magicNumber, (mo) -> new DuelPower(mo, magicNumber)));
        }
    }
}