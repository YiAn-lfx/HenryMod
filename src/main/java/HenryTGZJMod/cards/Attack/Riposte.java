package HenryTGZJMod.cards.Attack;

import HenryTGZJMod.actions.ComboAction;
import HenryTGZJMod.cards.AbstractHenryCard;
import HenryTGZJMod.helpers.ComboUtil;
import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static HenryTGZJMod.helpers.ComboUtil.FirstActionType.DAMAGE;


public class Riposte extends AbstractHenryCard {
    public static final String ID = ModHelper.makePath(Riposte.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID); // 从游戏系统读取本地化资源
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK; //类型
    private static final CardRarity RARITY = CardRarity.UNCOMMON; //稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY; //指向类型

    public Riposte() {
        super(ID, true, CARD_STRINGS, COST, TYPE, RARITY, TARGET);
        this.damage = this.baseDamage = 6;
        this.stanceCost = baseStanceCost = 3;
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
//
//        ComboUtil.Combo(
//                p, m, damage, stanceCost, DAMAGE,
//                new AbstractGameAction() {
//                    @Override
//                    public void update() {
//                        // 计算本回合技能牌数量
//                        // 1. 计算本回合打出的纪念牌数量
//                        int skillCount = 0;
//                        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
//                            if (c.type == CardType.SKILL) {
//                                skillCount++;
//                            }
//                        }
//
//                        // 2. 根据技能牌数量造成伤害
//                        if (skillCount > 0) {
//                            for (int i = 1; i <= skillCount; i++) {
//                                this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
//                            }
//
//                        }
//                        this.isDone = true;
//                    }
//                }
//
//        );
        this.addToBot(
                new ComboAction(
                        p, m, stanceCost,
                        new AbstractGameAction() {
                            @Override
                            public void update() {
                                // 计算本回合技能牌数量
                                // 1. 计算本回合打出的技能牌数量
                                int skillCount = 0;
                                for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn) {
                                    if (c.type == CardType.SKILL) {
                                        skillCount++;
                                    }
                                }

                                // 2. 根据技能牌数量造成伤害
                                if (skillCount > 0) {
                                    for (int i = 1; i <= skillCount; i++) {
                                        this.addToBot(new DamageAction(m, new DamageInfo(p, damage, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                                    }

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
            if (c.type == CardType.SKILL) {
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