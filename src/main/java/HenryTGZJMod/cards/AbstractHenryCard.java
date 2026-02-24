package HenryTGZJMod.cards;

import HenryTGZJMod.helpers.ModHelper;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static HenryTGZJMod.characters.Henry.PlayerColorEnum.H_BROWN;

public abstract class AbstractHenryCard extends CustomCard {


    public int secondDamage;          // 第二伤害数值
    public int baseSecondDamage;      // 基础第二伤害数值
    public int secondBlock;          // 第二格挡数值
    public int baseSecondBlock;      // 基础第二格挡数值
    public boolean isSecondDamageModified;
    public boolean upgradedSecondDamage;
    public boolean isMultiSecondDamage;
    public int[] multiSecondDamage;
    public boolean isSecondBlockModified;
    public boolean upgradedSecondBlock;
    public int baseStanceCost;           // 基础值
    public int stanceCost;                // 当前值
    public boolean upgradedStanceCost;    // 是否升级过
    public boolean isStanceCostModified;  // 是否被外部效果修改
    public boolean showStanceCostAsModified;  // 用于升级预览的显示标志


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



    /**
     * 升级姿态费用
     */
    public void upgradeStanceCost(int by) {
        this.baseStanceCost += by;
        this.stanceCost += by;
        this.upgradedStanceCost = true;
    }

    /**
     * 计算姿态费用 - 应用外部影响
     */
    public void calculateStanceCost() {
        this.isStanceCostModified = false;

        if (AbstractDungeon.player == null) return;

        float tmp = this.baseStanceCost;

        // 应用笨拙效果
        AbstractPower clumsy = AbstractDungeon.player.getPower("HenryTGZJMod:ClumsyPower");
        if (clumsy != null) {
            tmp += clumsy.amount;
            this.isStanceCostModified = true;
        }


        // 确保非负
        if (tmp < 0) tmp = 0;

        this.stanceCost = Math.round(tmp);
    }






    public void applyPowersToSecondDamage() {
        // 单目标模式
        if (!this.isMultiSecondDamage) {
            float tmp = (float)this.baseSecondDamage;

            // 遗物加成
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                tmp = r.atDamageModify(tmp, this);
                if (this.baseSecondDamage != (int)tmp) {
                    this.isSecondDamageModified = true;
                }
            }

            // 能力加成
            for (AbstractPower p : AbstractDungeon.player.powers) {
                tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
                if (this.baseSecondDamage != (int)tmp) {
                    this.isSecondDamageModified = true;
                }
            }

            // 姿态加成
            tmp = AbstractDungeon.player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            if (this.baseSecondDamage != (int)tmp) {
                this.isSecondDamageModified = true;
            }

            // 最终加成
            for (AbstractPower p : AbstractDungeon.player.powers) {
                tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
                if (this.baseSecondDamage != (int)tmp) {
                    this.isSecondDamageModified = true;
                }
            }

            // 确保非负
            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            this.secondDamage = MathUtils.floor(tmp);

        } else {
            // 多目标模式
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[monsters.size()];

            // 初始化
            for(int i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseSecondDamage;
            }

            // 遗物加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    tmp[i] = r.atDamageModify(tmp[i], this);
                    if (this.baseSecondDamage != (int)tmp[i]) {
                        this.isSecondDamageModified = true;
                    }
                }
            }

            // 能力加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this);
                }
                tmp[i] = AbstractDungeon.player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
            }

            // 最终加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this);
                }
            }

            // 确保非负并取整
            this.multiSecondDamage = new int[tmp.length];
            for(int i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
                this.multiSecondDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.secondDamage = this.multiSecondDamage[0];
        }
    }

    // 针对特定怪物的伤害计算
    public void calculateSecondDamage(AbstractMonster mo) {
        if (!this.isMultiSecondDamage && mo != null) {
            float tmp = (float)this.baseSecondDamage;

            // 遗物加成
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                tmp = r.atDamageModify(tmp, this);
            }

            // 玩家能力加成
            for (AbstractPower p : AbstractDungeon.player.powers) {
                tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this);
            }

            // 姿态加成
            tmp = AbstractDungeon.player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);

            // 怪物受到的加成（易伤等）
            for (AbstractPower p : mo.powers) {
                tmp = p.atDamageReceive(tmp, this.damageTypeForTurn, this);
            }

            // 最终加成
            for (AbstractPower p : AbstractDungeon.player.powers) {
                tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this);
            }

            // 怪物最终受到的加成
            for (AbstractPower p : mo.powers) {
                tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this);
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            this.secondDamage = MathUtils.floor(tmp);

        } else {
            // 多目标逻辑
            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            float[] tmp = new float[monsters.size()];

            // 初始化
            for(int i = 0; i < tmp.length; ++i) {
                tmp[i] = (float)this.baseSecondDamage;
            }

            // 遗物和玩家能力加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    tmp[i] = r.atDamageModify(tmp[i], this);
                }

                for (AbstractPower p : AbstractDungeon.player.powers) {
                    tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn, this);
                }

                tmp[i] = AbstractDungeon.player.stance.atDamageGive(tmp[i], this.damageTypeForTurn, this);
            }

            // 各个怪物受到的加成
            for(int i = 0; i < tmp.length; ++i) {
                AbstractMonster monster = monsters.get(i);
                if (!monster.isDying && !monster.isEscaping) {
                    for (AbstractPower p : monster.powers) {
                        tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn, this);
                    }
                }
            }

            // 最终加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractPower p : AbstractDungeon.player.powers) {
                    tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn, this);
                }
            }

            // 怪物最终加成
            for(int i = 0; i < tmp.length; ++i) {
                AbstractMonster monster = monsters.get(i);
                if (!monster.isDying && !monster.isEscaping) {
                    for (AbstractPower p : monster.powers) {
                        tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn, this);
                    }
                }
            }

            // 取整
            this.multiSecondDamage = new int[tmp.length];
            for(int i = 0; i < tmp.length; ++i) {
                if (tmp[i] < 0.0F) {
                    tmp[i] = 0.0F;
                }
                this.multiSecondDamage[i] = MathUtils.floor(tmp[i]);
            }

            this.secondDamage = this.multiSecondDamage[0];
        }
    }

    // 升级第二个伤害
    protected void upgradeSecondDamage(int amount) {
        this.baseSecondDamage += amount;
        this.upgradedSecondDamage = true;
    }

    /**
     * 复刻原版的格挡计算逻辑
     * 原版格挡计算链：
     * 1. 基础格挡值
     * 2. 各种力量（powers）的modifyBlock方法
     * 3. 各种力量的modifyBlockLast方法
     */
    public void applyPowersToSecondBlock() {
        // 复刻 AbstractCard.applyPowersToBlock() 的完整逻辑
        this.isSecondBlockModified = false;

        // 从基础值开始
        float tmp = (float)this.baseSecondBlock;

        // 第一阶段：modifyBlock
        // 所有力量先进行初步修改
        for (AbstractPower p : AbstractDungeon.player.powers) {
            tmp = p.modifyBlock(tmp, this);
            if (this.baseSecondBlock != MathUtils.floor(tmp)) {
                this.isSecondBlockModified = true;
            }
        }

        // 第二阶段：modifyBlockLast
        // 所有力量进行最后修改
        for (AbstractPower p : AbstractDungeon.player.powers) {
            tmp = p.modifyBlockLast(tmp);
            if (this.baseSecondBlock != MathUtils.floor(tmp)) {
                this.isSecondBlockModified = true;
            }
        }

        // 确保格挡值非负
        if (tmp < 0.0F) {
            tmp = 0.0F;
        }

        // 取整
        if (this.baseSecondBlock != MathUtils.floor(tmp)) {
            this.isSecondBlockModified = true;
        }
        this.secondBlock = MathUtils.floor(tmp);
    }

    /**
     * 升级第二个格挡
     */
    protected void upgradeSecondBlock(int amount) {
        this.baseSecondBlock += amount;
        this.upgradedSecondBlock = true;
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 计算第一个伤害
        super.calculateCardDamage(mo);
        // 计算第二个伤害
        this.calculateSecondDamage(mo);

    }

    // 重写 applyPowers 方法
    @Override
    public void applyPowers() {
        super.applyPowers();
        this.applyPowersToSecondDamage();
        this.applyPowersToSecondBlock();
        this.calculateStanceCost();
    }


    // 重置属性
    @Override
    public void resetAttributes() {
        super.resetAttributes();
        this.secondDamage = this.baseSecondDamage;
        this.isSecondDamageModified = false;
        if (this.isMultiSecondDamage) {
            this.multiSecondDamage = null;
        }
        this.secondBlock = this.baseSecondBlock;
        this.isSecondBlockModified = false;
        this.stanceCost = this.baseStanceCost;
        this.isStanceCostModified = false;

    }

    // 显示升级
    @Override
    public void displayUpgrades() {
        super.displayUpgrades();

        if (this.upgradedSecondDamage) {
            this.secondDamage = this.baseSecondDamage;
            this.isSecondDamageModified = true;
        }
        if (this.upgradedSecondBlock) {
            this.secondBlock = this.baseSecondBlock;
            this.isSecondBlockModified = true;
        }
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        AbstractCard card = super.makeStatEquivalentCopy();
        if (card instanceof AbstractHenryCard) {
            AbstractHenryCard henryCard = (AbstractHenryCard) card;
            // 复制所有姿态费用相关字段
            henryCard.stanceCost = this.stanceCost;
            henryCard.baseStanceCost = this.baseStanceCost;
            henryCard.isStanceCostModified = this.isStanceCostModified;
            henryCard.upgradedStanceCost = this.upgradedStanceCost;
            henryCard.showStanceCostAsModified = this.showStanceCostAsModified;

        }
        return card;
    }
}