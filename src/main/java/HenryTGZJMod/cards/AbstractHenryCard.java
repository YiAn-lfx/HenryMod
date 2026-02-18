package HenryTGZJMod.cards;

import HenryTGZJMod.helpers.ModHelper;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static HenryTGZJMod.characters.Henry.PlayerColorEnum.H_BROWN;

public abstract class AbstractHenryCard extends CustomCard {

    public int stanceCost;          // 当前架势消耗
    public int baseStanceCost;      // 基础架势消耗
    public int secondDamage;          // 第二伤害数值
    public int baseSecondDamage;      // 基础第二伤害数值
    public int secondBlock;          // 第二格挡数值
    public int baseSecondBlock;      // 基础第二格挡数值


    // useTmpArt表示是否使用测试卡图，当你卡图不够用时可以使用
    public AbstractHenryCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE,
                             CardRarity RARITY, CardTarget TARGET) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                H_BROWN, RARITY, TARGET);
    }

    // 如果按这个方法实现，在cards文件夹下分别放test_attack.png、test_power.png、test_skill.png即可
    private static String getTmpImgPath(CardType t) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/test_%s.png"), type);
    }

    // 如果实现这个方法，只要将相应类型的卡牌丢进相应文件夹即可，如攻击牌卡图添加进img/cards/attack/下
    private static String getImgPath(CardType t, String name) {
        String type;
        switch (t) {
            case ATTACK:
                type = "attack";
                break;
            case POWER:
                type = "power";
                break;
            case STATUS:
                type = "status";
                break;
            case CURSE:
                type = "curse";
                break;
            case SKILL:
                type = "skill";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t);
        }
        return String.format(ModHelper.MakeAssetPath("img/cards/%s/%s.png"), type, name.replace(ModHelper.makePath(""), ""));
    }

    protected void updateStanceCost() {
        this.stanceCost = this.baseStanceCost;

        // 检查玩家是否有"笨拙"效果
        if (AbstractDungeon.player != null) {
            AbstractPower clumsyPower = AbstractDungeon.player.getPower("HenryTGZJMod:ClumsyPower");
            if (clumsyPower != null) {
                this.stanceCost += clumsyPower.amount;
            }

            // 可以在这里添加其他影响架势消耗的效果
            // AbstractPower otherPower = AbstractDungeon.player.getPower("OtherPower");
            // if (otherPower != null) {
            //     this.stanceCost += otherPower.amount;
            // }
        }

        // 确保架势消耗不会低于0
        if (this.stanceCost < 0) {
            this.stanceCost = 0;
        }
    }

    // 重写 applyPowers 方法
    @Override
    public void applyPowers() {
        super.applyPowers();
        updateStanceCost(); // 自动更新架势消耗
    }

    // Getter方法
    public int getStanceCost() {
        return stanceCost;
    }

    public int getBaseStanceCost() {
        return baseStanceCost;
    }

    // 升级方法（类似upgradeMagicNumber）
    public void upgradeStanceCost(int amount) {
        this.baseStanceCost += amount;
        this.stanceCost = this.baseStanceCost;
    }
    public void upgradeSecondDamage(int amount) {
        this.baseSecondDamage += amount;
        this.secondDamage = this.baseSecondDamage;
    }
    public void upgradeSecondBlock(int amount) {
        this.baseSecondBlock += amount;
        this.secondBlock = this.baseSecondBlock;
    }
}