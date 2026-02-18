package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnSmithRelic;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.actions.unique.ArmamentsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Traits extends AbstractHenryRelic implements BetterOnSmithRelic {
    public static final String ID = ModHelper.makePath(Traits.class.getSimpleName()); // 遗物ID
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    private final int maxCounter = 8;

    public Traits() {
        super(ID, RELIC_TIER, LANDING_SOUND);
        this.counter = 0;
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.setDescription();
    }

    private String setDescription() {

        StringBuilder sb = new StringBuilder("#y" + this.DESCRIPTIONS[0]);


        for (int i = 1; i <= this.counter; i++) {
            sb.append(" NL #g").append(this.DESCRIPTIONS[i]);
        }
        if (this.counter != maxCounter) {
            sb.append(" NL [#555555]【待解锁】：[]");
        }
        for (int i = this.counter + 1; i <= maxCounter; i++) {
            sb.append(" NL [#555555]").append(this.DESCRIPTIONS[i]).append("[]");
        }


        return sb.toString();
    }

    public void updateDescription() {
        this.description = this.setDescription();
        this.tips.clear();
        this.tips.add(new PowerTip(this.name, this.description));
        this.initializeTips();
    }

    @Override
    public void setCounter(int counter) {
        super.setCounter(counter);  // 先设置值
        updateDescription();        // 然后更新描述
    }



    public AbstractRelic makeCopy() {
        return new Traits();
    }


    @Override
    public void onMonsterDeath(AbstractMonster m) {
        //磨刀石
        if (this.counter >= 1) {
            WeaponStatus relicW = (WeaponStatus) AbstractDungeon.player.getRelic("HenryTGZJMod:WeaponStatus");
            if (relicW != null && this.counter < relicW.maxCounter) {
                relicW.counter += 10;
                if (relicW.counter > relicW.maxCounter) {
                    relicW.counter = relicW.maxCounter;
                }
                relicW.flash();
                this.flash();
            }
        }
        //拉德季的传承
        if (this.counter >= 4) {
            this.addToBot(new ArmamentsAction(true));
            this.flash();
        }
        //无畏骑士
        if (this.counter >= 8) {
            this.addToBot(new GainBlockAction(AbstractDungeon.player, 20));
            this.flash();
        }
        //遗物升级
//        if (m.type == AbstractMonster.EnemyType.ELITE || m.type == AbstractMonster.EnemyType.BOSS) {
//            if (this.counter < maxCounter) {
//                this.counter += 1;
//                System.out.println("升级");
//                this.flash();
//                updateDescription();
//            }
//        }
    }

    @Override
    public void onVictory() {
        //就地取材
        if (this.counter >= 3) {
            PlateArmorStatus relicP = (PlateArmorStatus) AbstractDungeon.player.getRelic("HenryTGZJMod:PlateArmorStatus");
            if (relicP != null && this.counter < relicP.maxCounter) {
                relicP.counter += 5;
                if (relicP.counter > relicP.maxCounter) {
                    relicP.counter = relicP.maxCounter;
                }
                relicP.flash();
                this.flash();
            }
        }
        //遗物升级
        if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite || AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss) {
            if (this.counter < maxCounter) {
                this.counter += 1;
                this.flash();
                updateDescription();
            }
        }

    }

    @Override
    public void atTurnStart() {
        //身形矫健
        if (this.counter >= 5) {
            this.addToBot(new GainEnergyAction(1));
            this.flash();
        }
        //好战分子
        if (this.counter >= 6) {
            this.addToBot(new DrawCardAction(1));
            this.flash();
        }
    }

    @Override
    public void atBattleStart() {
        //炼金术士
        if (this.counter >= 7) {
            this.addToBot(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
            this.flash();
        }
    }
    @Override
    public void betterOnSmith(AbstractCard abstractCard) {
        //马丁的传承
        if (this.counter >= 2) {
            ArrayList<AbstractCard> upgradableCards = new ArrayList<>();

            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c.canUpgrade()) {
                    upgradableCards.add(c);
                }
            }

            Collections.shuffle(upgradableCards, new Random(AbstractDungeon.miscRng.randomLong()));
            if (!upgradableCards.isEmpty()) {
                if (upgradableCards.size() == 1) {
                    upgradableCards.get(0).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy()));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                } else {
                    upgradableCards.get(0).upgrade();
                    upgradableCards.get(1).upgrade();
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(0));
                    AbstractDungeon.player.bottledCardUpgradeCheck(upgradableCards.get(1));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(0).makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F - AbstractCard.IMG_WIDTH / 2.0F - 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(upgradableCards.get(1).makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F + AbstractCard.IMG_WIDTH / 2.0F + 20.0F * Settings.scale, (float) Settings.HEIGHT / 2.0F));
                    AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect((float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
                }
            }
            this.flash();
        }
    }

//    @Override
//    public void renderCounter(SpriteBatch sb, boolean inTopPanel) {
//        if (this.counter > maxCounter) {
//            this.counter = maxCounter;
//        }
//        if (this.counter < 0) {
//            this.counter = 0;
//        }
//        super.renderCounter(sb, inTopPanel);
//    }
}






