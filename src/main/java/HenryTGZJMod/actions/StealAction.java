package HenryTGZJMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StealAction extends AbstractGameAction {
    public enum StealMode {SPECIFIC_BUFF, ALL_BUFFS, RANDOM_BUFF, GOLD }

    private final AbstractCreature target;
    private final AbstractCreature stealer;
    private final StealMode mode;
    private final String powerId;
    private final int minAmount;
    private final int maxAmount;

    // 可偷取的通用buff列表
    private static final List<String> STEALABLE_POWERS = Arrays.asList(
            StrengthPower.POWER_ID,
            DexterityPower.POWER_ID,
            ArtifactPower.POWER_ID,
            PlatedArmorPower.POWER_ID,
            IntangiblePower.POWER_ID,
            IntangiblePlayerPower.POWER_ID,
            MetallicizePower.POWER_ID,
            RitualPower.POWER_ID,
            ThornsPower.POWER_ID,
            BarricadePower.POWER_ID,
            FlightPower.POWER_ID
    );

    // ========== 构造函数 ==========

    // 1. 偷取特定Power（必须指定Power ID）
    public StealAction(AbstractCreature target, AbstractCreature stealer,
                       String powerId, int minAmount, int maxAmount) {
        this(target, stealer, StealMode.SPECIFIC_BUFF, powerId, minAmount, maxAmount);
    }
    public static StealAction createSpecificBuff(AbstractCreature target, AbstractCreature stealer,
                                                 String powerId, int minAmount, int maxAmount) {
        return  new StealAction(target,stealer, StealMode.SPECIFIC_BUFF, powerId, minAmount, maxAmount);
    }

    // 2. 偷取所有buff（不需要Power ID）
    public static StealAction createAllBuffs(AbstractCreature target, AbstractCreature stealer,
                                             int minAmount, int maxAmount) {
        return new StealAction(target, stealer, StealMode.ALL_BUFFS, null, minAmount, maxAmount);
    }

    // 3. 随机偷取一个buff（不需要Power ID）
    public static StealAction createRandomBuff(AbstractCreature target, AbstractCreature stealer,
                                               int minAmount, int maxAmount) {
        return new StealAction(target, stealer, StealMode.RANDOM_BUFF, null, minAmount, maxAmount);
    }

    // 4. 偷取金币（不需要Power ID）
    public static StealAction createGold(AbstractCreature target, AbstractCreature stealer,
                                         int minGold, int maxGold) {
        return new StealAction(target, stealer, StealMode.GOLD, null, minGold, maxGold);
    }

    // 私有构造函数
    public StealAction(AbstractCreature target, AbstractCreature stealer,
                       StealMode mode, String powerId, int minAmount, int maxAmount) {
        this.target = target;
        this.stealer = stealer;
        this.mode = mode;
        this.powerId = powerId;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DEBUFF;
    }

    // ========== 主要逻辑 ==========

    @Override
    public void update() {
        if (this.duration == Settings.ACTION_DUR_FAST) {
            if (target == null || target.isDeadOrEscaped()) {
                this.isDone = true;
                return;
            }

            switch (mode) {
                case SPECIFIC_BUFF: stealSpecificPower(); break;
                case ALL_BUFFS: stealAllBuffs(); break;
                case RANDOM_BUFF: stealRandomBuff(); break;
                case GOLD: stealGold(); break;
            }
        }
        this.tickDuration();
    }

    private void stealSpecificPower() {
        AbstractPower powerT = stealer.getPower("HenryTGZJMod:TwoForOnePower");
        int loopCount = powerT != null ? 1 + powerT.amount : 1;
        for (int i = 0; i < loopCount; i++) {
            //江洋大盗判定
            AbstractPower powerM = stealer.getPower("HenryTGZJMod:MasterThiefPower");
            addToBot(new ApplyPowerAction(stealer, stealer, new DexterityPower(stealer, powerM.amount)));

            if (!STEALABLE_POWERS.contains(powerId)) return;

            String actualId = getActualPowerId(powerId);
            AbstractPower power = target.getPower(actualId);
            if (power != null && power.amount > 0) {
                int stealAmount = getRandomAmount(power.amount);
                if (stealAmount > 0) {
                    addToTop(new ReducePowerAction(target, stealer, actualId, stealAmount));
                    transferPower(stealer, powerId, stealAmount);
                }
            }
        }
    }

    private void stealAllBuffs() {
        AbstractPower powerT = stealer.getPower("HenryTGZJMod:TwoForOnePower");
        int loopCount = powerT != null ? 1 + powerT.amount : 1;
        for (int i = 0; i < loopCount; i++) {
            //江洋大盗判定
            AbstractPower powerM = stealer.getPower("HenryTGZJMod:MasterThiefPower");
            addToBot(new ApplyPowerAction(stealer, stealer, new DexterityPower(stealer, powerM.amount)));

            for (String pid : STEALABLE_POWERS) {
                String actualId = getActualPowerId(pid);
                AbstractPower power = target.getPower(actualId);
                if (power != null && power.amount > 0) {
                    int stealAmount = getRandomAmount(power.amount);
                    if (stealAmount > 0) {
                        addToTop(new ReducePowerAction(target, stealer, actualId, stealAmount));
                        transferPower(stealer, pid, stealAmount);
                    }
                }
            }
        }
    }

    private void stealRandomBuff() {
        AbstractPower powerT = stealer.getPower("HenryTGZJMod:TwoForOnePower");
        int loopCount = powerT != null ? 1 + powerT.amount : 1;
        for (int i = 0; i < loopCount; i++) {
            //江洋大盗判定
            AbstractPower powerM = stealer.getPower("HenryTGZJMod:MasterThiefPower");
            addToBot(new ApplyPowerAction(stealer, stealer, new DexterityPower(stealer, powerM.amount)));

            List<String> available = new ArrayList<>();
            for (String pid : STEALABLE_POWERS) {
                String actualId = getActualPowerId(pid);
                if (target.getPower(actualId) != null) {
                    available.add(pid);
                }
            }

            if (!available.isEmpty()) {
                String selectedId = available.get(AbstractDungeon.miscRng.random(available.size() - 1));
                String actualId = getActualPowerId(selectedId);
                AbstractPower power = target.getPower(actualId);
                int stealAmount = getRandomAmount(power.amount);
                if (stealAmount > 0) {
                    addToTop(new ReducePowerAction(target, stealer, actualId, stealAmount));
                    transferPower(stealer, selectedId, stealAmount);
                }
            }
        }
    }

    private void stealGold() {
        AbstractPower powerT = stealer.getPower("HenryTGZJMod:TwoForOnePower");
        int loopCount = powerT != null ? 1 + powerT.amount : 1;
            for (int i = 0; i < loopCount; i++) {
                //江洋大盗判定
                AbstractPower powerM = stealer.getPower("HenryTGZJMod:MasterThiefPower");
                if (powerM != null) {
                    addToBot(new ApplyPowerAction(stealer, stealer, new DexterityPower(stealer, powerM.amount)));
                }

                if (target instanceof AbstractMonster) {
                    int goldAmount = getRandomAmount();
                    AbstractPower powerA = stealer.getPower("HenryTGZJMod:ActOfIntegrityPower");
                    if (powerA != null && powerA.amount >0) {
                        addToBot(new DrawCardAction(stealer, powerA.amount));
                    } else {
                        if (goldAmount > 0) {
                            // 金币特效
                            for (int iG = 0; iG < goldAmount; iG++) {
                                AbstractDungeon.effectList.add(new GainPennyEffect(
                                        stealer, target.hb.cX, target.hb.cY,
                                        stealer.hb.cX, stealer.hb.cY, true
                                ));
                            }
                            // 音效和金币增加
                            CardCrawlGame.sound.play("GOLD_JINGLE");
                            AbstractDungeon.player.gainGold(goldAmount);
                        }
                    }
                }
            }
    }

    // ========== 辅助方法 ==========

    private String getActualPowerId(String baseId) {
        if (baseId.equals("Intangible") || baseId.equals("IntangiblePlayer")) {
            // 优先玩家版，再怪物版
            if (target.getPower("IntangiblePlayer") != null) return "IntangiblePlayer";
            if (target.getPower("Intangible") != null) return "Intangible";
            return "Intangible"; // 默认
        }
        return baseId;
    }

    private void transferPower(AbstractCreature to, String powerId, int amount) {
        AbstractPower power = createPowerForStealer(powerId, amount);
        if (power != null) {
            addToTop(new ApplyPowerAction(to, to, power, amount));
        }
    }

    private AbstractPower createPowerForStealer(String powerId, int amount) {
        switch (powerId) {
            case StrengthPower.POWER_ID:
                return new StrengthPower(stealer, amount);
            case DexterityPower.POWER_ID:
                return new DexterityPower(stealer, amount);
            case ArtifactPower.POWER_ID:
                return new ArtifactPower(stealer, amount);
            case PlatedArmorPower.POWER_ID:
                return new PlatedArmorPower(stealer, amount);
            case "Intangible":
            case "IntangiblePlayer":
                return createIntangiblePower(amount);
            case MetallicizePower.POWER_ID:
                return new MetallicizePower(stealer, amount);
            case "Ritual":
                return new RitualPower(stealer, amount, false);
            case ThornsPower.POWER_ID:
                return new ThornsPower(stealer, amount);
            case "Barricade":
                return createBarricadePower();
            default:
                return null;
        }
    }

    private AbstractPower createIntangiblePower(int amount) {
        try {
            // 尝试玩家版
            Class<?> cls = Class.forName("com.megacrit.cardcrawl.powers.IntangiblePlayerPower");
            return (AbstractPower) cls.getConstructor(AbstractCreature.class, int.class)
                    .newInstance(stealer, amount);
        } catch (Exception e1) {
            try {
                // 尝试怪物版
                Class<?> cls = Class.forName("com.megacrit.cardcrawl.powers.IntangiblePower");
                return (AbstractPower) cls.getConstructor(AbstractCreature.class, int.class)
                        .newInstance(stealer, amount);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    private AbstractPower createBarricadePower() {
        try {
            Class<?> cls = Class.forName("com.megacrit.cardcrawl.powers.BarricadePower");
            return (AbstractPower) cls.getConstructor(AbstractCreature.class).newInstance(stealer);
        } catch (Exception e) {
            return null;
        }
    }

    private int getRandomAmount(int targetCurrentAmount) {
        int effectiveMax = Math.min(maxAmount, targetCurrentAmount);
        int effectiveMin = Math.min(minAmount, effectiveMax);
        if (effectiveMin >= effectiveMax) return effectiveMin;
        return AbstractDungeon.cardRng.random(effectiveMin, effectiveMax);
    }

    private int getRandomAmount() {
        if (minAmount >= maxAmount) return minAmount;
        return AbstractDungeon.cardRng.random(minAmount, maxAmount);
    }
}