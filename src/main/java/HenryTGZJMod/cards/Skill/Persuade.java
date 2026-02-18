package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AngerPower;


public class Persuade extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Persuade.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型

    public Persuade() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;
        this.isEthereal = false;
        this.dontTriggerOnUseCard = false;
        this.isInnate = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeBaseCost(0);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        float roll = AbstractDungeon.cardRng.random();
        if (roll < 0.5f) {
            this.addToBot(new StunMonsterAction(m, p));
        } else {
            this.addToBot(new ApplyPowerAction(m, p, new AngerPower(m, 1)));
        }
    }
}