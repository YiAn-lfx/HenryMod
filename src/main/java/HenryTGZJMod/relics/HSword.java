package HenryTGZJMod.relics;

import HenryTGZJMod.helpers.ModHelper;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class HSword extends AbstractHenryRelic {
    public static final String ID = ModHelper.makePath(HSword.class.getSimpleName()); // 遗物ID
//    private static final String IMG_PATH = "HenryTGZJModResources/img/relics/" + HSword.class.getSimpleName() + ".png"; // 图片路径
//    private static final String OUTLINE_PATH = "HenryTGZJModResources/img/relics/" + HSword.class.getSimpleName() + "_Outline.png"; //遗物未解锁时的轮廓
    private static final RelicTier RELIC_TIER = RelicTier.BOSS; // 遗物类型
    private static final LandingSound LANDING_SOUND = LandingSound.FLAT; // 点击音效

    public HSword() {
        //super(ID, ImageMaster.loadImage(IMG_PATH), RELIC_TIER, LANDING_SOUND);
        // 如果你需要轮廓图，取消注释下面一行并注释上面一行，不需要就删除
         super(ID, RELIC_TIER, LANDING_SOUND);
    }

    // 获取遗物描述，但原版游戏只在初始化和获取遗物时调用，故该方法等于初始描述
    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new HSword();
    }


    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        AbstractPower power = AbstractDungeon.player.getPower("HenryTGZJMod:StancePower");
        if (power != null) {
            return power.amount * 2 + damage;
        }
        return damage;
    }

    public boolean canSpawn() {
        return AbstractDungeon.player.hasRelic("HenryTGZJMod:RKSword");
    }

    public void bossObtainLogic() {
        this.isObtained = true;
    }
}






