package HenryTGZJMod.cards.Skill;

import HenryTGZJMod.actions.ComboAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class Disengage extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Disengage.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.SELF; //指向类型

    public Disengage() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.block = this.baseBlock = 5;
        this.stanceCost = baseStanceCost = 5;
        this.exhaust = false;
    }

    @Override
    public void upgrade() { //卡牌升级
        if(!this.upgraded){
            this.upgradeName();
            this.upgradeStanceCost(-2);
            this.rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION; //使用升级后的本地化文本
            this.initializeDescription();
        }

    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { //卡牌使用效果
//        AbstractPower powerS = p.getPower("HenryTGZJMod:StancePower");
//        if (powerS != null && powerS.amount >= stanceCost) {
//            this.addToBot(
//                    new RemoveSpecificPowerAction(
//                            p, p, powerS.ID
//                    )
//            );
//            // 计算本回合攻击牌数量
//                // 1. 计算本回合打出的攻击牌数量
//            int attackCount = 0;
//            for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
//                if (c.type == CardType.ATTACK) {
//                    attackCount++;
//                }
//            }
//
//            // 2. 根据攻击牌数量获得格挡
//            if (attackCount > 0) {
//                int totalBlock = attackCount * block;
//                addToBot(new GainBlockAction(p, p, totalBlock));
//            }
//        }
//        ComboUtil.Combo(
//                p, m, damage, stanceCost, ComboUtil.FirstActionType.NONE,
//                new AbstractGameAction() {
//                    @Override
//                    public void update() {
//                        // 计算本回合攻击牌数量
//                        // 1. 计算本回合打出的攻击牌数量
//                        int attackCount = 0;
//                        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
//                            if (c.type == CardType.ATTACK) {
//                                attackCount++;
//                            }
//                        }
//
//                        // 2. 根据攻击牌数量获得格挡
//                        if (attackCount > 0) {
//                            int totalBlock = attackCount * block;
//                            addToBot(new GainBlockAction(p, p, totalBlock));
//                        }
//                        this.isDone = true;
//                    }
//                }
//        );
        this.addToBot(
                new ComboAction(
                        p, m, stanceCost,
                        new AbstractGameAction() {
                            @Override
                            public void update() {
                                // 计算本回合攻击牌数量
                                // 1. 计算本回合打出的攻击牌数量
                                int attackCount = 0;
                                for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                                    if (c.type == CardType.ATTACK) {
                                        attackCount++;
                                    }
                                }

                                // 2. 根据攻击牌数量获得格挡
                                if (attackCount > 0) {
                                    int totalBlock = attackCount * block;
                                    addToBot(new GainBlockAction(p, p, totalBlock));
                                }
                                this.isDone = true;
                            }
                        }
                )
        );
    }

    public void applyPowers() {
        super.applyPowers();
        int count = 0;

        for(AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
            if (c.type == CardType.ATTACK) {
                ++count;
            }
        }

        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[0] + count;
        if (count == 1) {
            this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[1];
        } else {
            this.rawDescription = this.rawDescription + CARD_STRINGS.EXTENDED_DESCRIPTION[2];
        }

        this.initializeDescription();
    }

    public void onMoveToDiscard() {
        this.rawDescription = CARD_STRINGS.DESCRIPTION;
        this.initializeDescription();
    }
}