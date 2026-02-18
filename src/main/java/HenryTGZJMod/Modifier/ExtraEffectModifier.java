package HenryTGZJMod.Modifier;


import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.dynamicdynamic.DynamicDynamicVariable;
import com.evacipated.cardcrawl.mod.stslib.dynamicdynamic.DynamicProvider;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.IronWave;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.UUID;

/**
 * 额外效果修改器抽象类
 * 用于为卡牌添加额外效果和动态变量显示
 */
public abstract class ExtraEffectModifier extends AbstractCardModifier implements DynamicProvider {
    private static final String STRING_ID = "stslib:ExtraEffectModifier";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(STRING_ID);
    public final UUID uuid;                    // 唯一标识符
    public final VariableType type;           // 变量类型（伤害/格挡/魔法值）
    public Proxy proxy;                       // 代理对象，用于计算动态变量
    public int amount;                        // 效果触发次数（针对多实例效果）
    public int baseValue;                     // 基础数值

    // 构造函数：指定类型和数值，次数默认为1
    public ExtraEffectModifier(VariableType type, int value) {
        this(type, value, 1);
    }

    // 构造函数：指定类型、数值和次数
    public ExtraEffectModifier(VariableType type, int value, int times) {
        this.type = type;
        baseValue = value;
        amount = times;
        uuid = UUID.randomUUID();
    }

    /**
     * 执行额外效果
     * 类似于卡牌use方法中的效果。如果效果是多实例的，此方法会被调用多次，次数等于amount
     * @param card 附着的卡牌
     * @param p 玩家角色
     * @param m 目标（可为null）
     * @param useAction 使用卡牌的动作
     */
    public abstract void doExtraEffects(AbstractCard card, AbstractPlayer p, AbstractCreature m, UseCardAction useAction);

    /**
     * 获取额外文本
     * 文本中需要包含"%s"作为动态变量的占位符。
     * 如果priority<0，文本添加到卡牌描述开头；否则添加到末尾。
     * 如果是多实例效果且amount>1，文本会自动添加触发次数信息
     * @param card 附着的卡牌
     * @return 要添加的字符串，包含"%s"作为动态变量占位符
     */
    public abstract String getExtraText(AbstractCard card);

    /**
     * 获取效果标识符
     * 类似AbstractCardModifier的identifier方法，需要唯一标识每个效果
     * @param card 附着的卡牌
     * @return 表示此效果的唯一字符串
     */
    public abstract String getEffectId(AbstractCard card);

    /**
     * 控制效果是否可以堆叠
     * @param card 目标卡牌
     * @return true表示可以堆叠
     */
    protected boolean canStack(AbstractCard card) {
        return true;
    }

    /**
     * 控制是否可以与另一个修改器堆叠
     * @param card 目标卡牌
     * @param other 另一个修改器
     * @return true表示可以堆叠
     */
    protected boolean canStackWith(AbstractCard card, AbstractCardModifier other) {
        return true;
    }

    /**
     * 处理堆叠逻辑
     * @param card 目标卡牌
     * @param other 要堆叠的修改器
     * @return true表示堆叠成功，阻止当前修改器的应用
     */
    protected boolean stackEffects(AbstractCard card, AbstractCardModifier other) {
        if (other instanceof ExtraEffectModifier) {
            ExtraEffectModifier effect = (ExtraEffectModifier) other;
            if (isMultiInstanced(card)) {
                if (effect.baseValue == baseValue) {
                    effect.amount += amount;      // 多实例：增加次数
                    return true;
                }
            } else {
                effect.baseValue += baseValue;    // 单实例：增加数值
                return true;
            }
        }
        return false;
    }

    /**
     * 控制堆叠方式
     * @param card 目标卡牌
     * @return true表示多实例堆叠（增加次数），false表示数值堆叠（增加基础值）
     */
    protected boolean isMultiInstanced(AbstractCard card) {
        return true;
    }

    /**
     * 从原卡牌继承属性到代理卡牌
     * 为了避免序列化问题，使用代理卡牌（IronWave）来计算数值
     * @param card 源卡牌
     * @param proxy 代理卡牌
     */
    protected void inheritValues(AbstractCard card, IronWave proxy) {
        // 复制卡牌基础属性
        proxy.isEthereal = card.isEthereal;
        proxy.exhaust = card.exhaust;
        proxy.color = card.color;
        proxy.type = card.type;
        proxy.target = card.target;
        proxy.rarity = card.rarity;
        proxy.timesUpgraded = card.timesUpgraded;
        proxy.upgraded = card.upgraded;
        proxy.cardID = card.cardID;
        proxy.cardsToPreview = card.cardsToPreview;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        // 计算无目标时的数值
        proxy = Proxy.of(card).setValueFor(type, baseValue).calculate(null, this);
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        // 计算有目标时的数值
        proxy = Proxy.of(card).setValueFor(type, baseValue).calculate(mo, this);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        // 使用卡牌时触发额外效果
        if (isMultiInstanced(card)) {
            for (int i = 0; i < amount; ++i) {
                doExtraEffects(card, AbstractDungeon.player, target, action);
            }
        } else {
            doExtraEffects(card, AbstractDungeon.player, target, action);
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        // 添加额外文本到卡牌描述
        String addition = String.format(getExtraText(card), "!" + DynamicProvider.generateKey(card, this) + "!");
        if (isMultiInstanced(card)) {
            addition = applyTimes(addition);  // 添加次数信息
        }
        if (priority < 0) {
            return addition + " NL " + rawDescription;  // 添加到开头
        } else {
            return rawDescription + " NL " + addition;  // 添加到末尾
        }
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        // 检查是否可以应用或堆叠
        if (!canStack(card)) return true;
        ArrayList<AbstractCardModifier> list = CardModifierManager.getModifiers(card, getEffectId(card));
        for (AbstractCardModifier other : list) {
            if (canStackWith(card, other)) {
                boolean changed = stackEffects(card, other);
                if (changed) {
                    // 堆叠成功后刷新卡牌
                    if (CardCrawlGame.isInARun() && AbstractDungeon.player != null) {
                        card.applyPowers();
                    }
                    card.initializeDescription();
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String identifier(AbstractCard card) {
        return getEffectId(card);
    }

    // DynamicProvider接口实现
    @Override
    public int baseValue(AbstractCard card) {
        return baseValue;
    }

    @Override
    public int value(AbstractCard card) {
        return proxy.getValueFor(type);
    }

    @Override
    public boolean isModified(AbstractCard card) {
        return proxy.getValueFor(type) != baseValue;
    }

    @Override
    public UUID getDynamicUUID() {
        return uuid;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        // 初始应用时注册动态变量
        DynamicDynamicVariable.registerVariable(card, this);
        proxy = Proxy.of(card).setValueFor(type, baseValue).calculate(null, this);
    }

    /**
     * 添加次数信息到文本
     * @param s 原始文本
     * @return 添加次数信息后的文本
     */
    protected String applyTimes(String s) {
        if (amount > 1) {
            if (s.endsWith(LocalizedStrings.PERIOD)) {
                s = s.substring(0, s.length() - 1);
            }
            s = String.format(cardStrings.DESCRIPTION, s, amount);
        }
        return s;
    }

    /**
     * 变量类型枚举
     */
    protected enum VariableType {
        DAMAGE,   // 伤害
        BLOCK,    // 格挡
        MAGIC     // 魔法值
    }

    /**
     * 代理类，用于计算动态变量的实际数值
     */
    protected static class Proxy {
        private final int damage;
        private final int block;
        private final int magicNumber;

        private Proxy(int damage, int block, int magicNumber) {
            this.damage = damage;
            this.block = block;
            this.magicNumber = magicNumber;
        }

        protected int getDamage() {
            return damage;
        }

        protected int getBlock() {
            return block;
        }

        protected int getMagicNumber() {
            return magicNumber;
        }

        // 根据类型获取对应数值
        protected int getValueFor(VariableType type) {
            switch (type) {
                default: return getDamage();
                case BLOCK: return getBlock();
                case MAGIC: return getMagicNumber();
            }
        }

        protected static Builder of(AbstractCard card) {
            return new Builder(card);
        }

        /**
         * 建造者类，用于创建和计算代理数值
         */
        private static class Builder {
            private static final IronWave DUMMY = new IronWave();  // 静态代理卡牌
            private final AbstractCard card;
            private int baseDamage;
            private int baseBlock;
            private int baseMagicNumber;

            private Builder(AbstractCard card) {
                this.card = card;
                baseDamage = card.baseDamage;
                baseBlock = card.baseBlock;
                baseMagicNumber = card.baseMagicNumber;
            }

            protected Builder baseDamage(int damage) {
                this.baseDamage = damage;
                return this;
            }

            protected Builder baseBlock(int block) {
                this.baseBlock = block;
                return this;
            }

            protected Builder baseMagicNumber(int magicNumber) {
                this.baseMagicNumber = magicNumber;
                return this;
            }

            // 根据类型设置对应数值
            protected Builder setValueFor(VariableType type, int value) {
                switch (type) {
                    default: return baseDamage(value);
                    case BLOCK: return baseBlock(value);
                    case MAGIC: return baseMagicNumber(value);
                }
            }

            /**
             * 计算代理数值
             * @param m 目标怪物（可为null）
             * @param effect 额外效果修改器
             * @return 计算后的代理对象
             */
            protected Proxy calculate(AbstractMonster m, ExtraEffectModifier effect) {
                IronWave proxy = DUMMY;
                // 设置代理卡牌的基础数值
                proxy.baseDamage = proxy.damage = baseDamage;
                proxy.baseBlock = proxy.block = baseBlock;
                proxy.baseMagicNumber = proxy.magicNumber = baseMagicNumber;

                // 继承原卡牌属性
                effect.inheritValues(card, proxy);

                // 复制非ExtraEffectModifier的修改器到代理卡牌
                CardModifierManager.removeAllModifiers(proxy, true);
                for (AbstractCardModifier mod : CardModifierManager.modifiers(card)) {
                    if (!(mod instanceof ExtraEffectModifier)) {
                        CardModifierManager.addModifier(card, mod.makeCopy());
                    }
                }

                // 计算最终数值
                if (AbstractDungeon.player != null) {
                    if (m == null) {
                        proxy.applyPowers();  // 无目标计算
                    } else {
                        proxy.calculateCardDamage(m);  // 有目标计算
                    }
                }
                return new Proxy(proxy.damage, proxy.block, proxy.magicNumber);
            }
        }
    }
}