package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RKSword extends AbstractHenryRelic {
    public static final String ID = ModHelper.makePath(RKSword.class.getSimpleName()); // 遗物ID
    private static final String IMG_PATH = "HenryTGZJModResources/img/relics/" + RKSword.class.getSimpleName() + ".png"; // 图片路径
    //private static final String OUTLINE_PATH = "HenryTGZJModResources/img/relics/" + RKSword.class.getSimpleName() + "_Outline.png"; //遗物未解锁时的轮廓
    private static final RelicTier RELIC_TIER = RelicTier.STARTER; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public RKSword() {
        super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
        // super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new RKSword();
    }

//    public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
//        AbstractPower power = info.owner.getPower("HenryTGZJMod:StancePower");
//        if (info.owner != null && info.type != DamageType.HP_LOSS && info.type != DamageType.THORNS && power != null) {
//            this.flash();
//            damageAmount += 3; //power.amount ;
//        }
//        return damageAmount;
////    }
//    public float atDamageGive(float damage, DamageInfo.DamageType type) {
//        AbstractPower power = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
//        if (type != DamageType.HP_LOSS && type != DamageType.THORNS && power != null) {
//            this.flash();
//            return power.amount + damage;
//        }
//        return damage;
//    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        AbstractPower power = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
        if (power != null) {
            return power.amount + damage;
        }
        return damage;
    }
}






