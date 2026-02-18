package HenryTGZJMod.cards.Attack;

import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.MasterStrikeMPower;
import HenryTGZJMod.powers.MasterStrikePower;
import com.evacipated.cardcrawl.mod.stslib.actions.common.AllEnemyApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.PressEndTurnButtonAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class MasterStrike extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(MasterStrike.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 0;
    private static final CardType TYPE = CardType.ATTACK; //类型
    private static final CardRarity RARITY = CardRarity.RARE; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public MasterStrike() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;
        this.isEthereal = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.exhaust = false;
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {//卡牌使用效果
        this.addToBot(new PressEndTurnButtonAction());
        this.addToBot(new ApplyPowerAction(p, p, new MasterStrikePower(p)));
        this.addToBot(new AllEnemyApplyPowerAction(p, 1, MasterStrikeMPower::new));
    }
}
