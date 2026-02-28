package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.actions.FocusAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import HenryTGZJMod.powers.DesperateGambitPower;
import HenryTGZJMod.powers.StancePower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;


public class DesperateGambit extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(DesperateGambit.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public DesperateGambit() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.exhaust = true;
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
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
        AbstractPower power = p.getPower("HenryTGZJMod:StancePower");
        this.addToBot(
                new FocusAction(
                    new ApplyPowerAction(p, p, new StancePower(p,power.amount)),
                    new ApplyPowerAction(p, p, new DesperateGambitPower(p))
                )
        );
    }
}
