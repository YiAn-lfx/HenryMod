package HenryTGZJMod.cards;

import HenryTGZJMod.helpers.ModHelper;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static HenryTGZJMod.characters.Henry.PlayerColorEnum.H_BROWN;

public abstract class AbstractHenryCard extends CustomCard {

    public int stanceCost;          // 当前架势消耗
    public int baseStanceCost;      // 基础架势消耗
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


    // useTmpArt表示是否使用测试卡图，当你卡图不够用时可以使用
    public AbstractHenryCard(String ID, boolean useTmpArt, CardStrings strings, int COST, CardType TYPE,
                             CardRarity RARITY, CardTarget TARGET) {
        super(ID, strings.NAME, useTmpArt ? getTmpImgPath(TYPE) : getImgPath(TYPE, ID), COST, strings.DESCRIPTION, TYPE,
                H_BROWN, RARITY, TARGET);

        this.baseSecondDamage = -1;
        this.secondDamage = -1;
        this.isSecondDamageModified = false;
        this.upgradedSecondDamage = false;
        this.isMultiSecondDamage = false;
        this.baseSecondBlock = -1;
        this.secondBlock = -1;
        this.isSecondBlockModified = false;
        this.upgradedSecondBlock = false;
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
        applyPowersToSecondDamage();
        applyPowersToSecondBlock();
    }
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        // 计算第一个伤害
        super.calculateCardDamage(mo);
        // 计算第二个伤害
        this.calculateSecondDamage(mo);
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

    public void applyPowersToSecondDamage() {
        // 单目标模式
        if (!this.isMultiSecondDamage) {
            float tmp = (float)this.baseSecondDamage;

            // 遗器加成
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                tmp = r.atDamageModify(tmp, this);
                if (this.baseSecondDamage != (int)tmp) {
                    this.isSecondDamageModified = true;
                }
            }

            // 力量加成（注意：可能需要使用不同的力量类型）
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

            // 遗器加成
            for(int i = 0; i < tmp.length; ++i) {
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    tmp[i] = r.atDamageModify(tmp[i], this);
                    if (this.baseSecondDamage != (int)tmp[i]) {
                        this.isSecondDamageModified = true;
                    }
                }
            }

            // 力量加成
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

            // 遗器加成
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                tmp = r.atDamageModify(tmp, this);
            }

            // 玩家力量加成
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

            // 遗器和玩家力量加成
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
     * 针对特定状态的格挡计算
     * 有时候需要知道当前玩家的状态来计算格挡（比如有无人工制品等）
     */
    public void calculateSecondBlock() {
        // 基本逻辑与applyPowersToSecondBlock相同
        // 但可以根据需要添加额外检查
        this.applyPowersToSecondBlock();
    }

    /**
     * 升级第二个格挡
     */
    protected void upgradeSecondBlock(int amount) {
        this.baseSecondBlock += amount;
        this.upgradedSecondBlock = true;
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
}